# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

`monaca-auto` is a live dealership website for **Monaca Auto Sales** (monacaautosales.com). It is a full-stack app, not an iOS app — the global CLAUDE.md's SwiftUI/iOS defaults do not apply here.

- **Frontend:** Vue 3 + Vite + Tailwind, deployed to **Netlify** (site id in `.netlify/state.json`)
- **Backend:** Spring Boot 3.2.4 on Java 21, deployed to **Render** at `https://monaca-auto-sales.onrender.com`. NOTE: `railway.toml` and `nixpacks.toml` are stale leftovers from an earlier plan — the actual deploy is Render, not Railway. Confirmed via `netlify/functions/keep-alive.js` (which pings the Render URL) and Stephanie's Railway dashboard (only shows LearnClaudeAI + ReplyIQ, no monaca-auto).
- **Database:** PostgreSQL on Render (managed by Render), schema managed by Flyway migrations in `backend/src/main/resources/db/migration/`
- **Images:** Cloudinary (unsigned uploads directly from the browser)
- A `netlify/functions/keep-alive.js` scheduled function pings `/actuator/health` every 10 min to keep the free-tier Render service from spinning down.

## Commands

### Toolchain gotcha
Homebrew may have JDK 23 or 25 as default. The build target is Java 21 (`<java.version>21</java.version>` in `backend/pom.xml`) and Spring Boot 3.2.4 does not officially support 25. Always run Maven with JDK 21:

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
```

### Backend (from `backend/`)
```bash
mvn clean package -DskipTests       # produces target/dealership-api-0.0.1-SNAPSHOT.jar
mvn spring-boot:run                  # dev server on :8080
mvn test                             # run tests
java -jar target/dealership-api-0.0.1-SNAPSHOT.jar   # run built JAR (what Railway does)
```
Backend needs a reachable Postgres — set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` (or Railway-style `DATABASE_URL`) in the environment or `.env`. Flyway auto-runs migrations on startup.

### Frontend (from `frontend/`)
```bash
npm install
npm run dev        # Vite dev server on :5173
npm run build      # production build to frontend/dist/
npm run preview    # preview built output
```
Frontend reads `VITE_API_BASE_URL` (defaults to `http://localhost:8080`), `VITE_CLOUDINARY_CLOUD_NAME`, `VITE_CLOUDINARY_UPLOAD_PRESET`, and `VITE_SITE_URL`.

## Architecture

### Two auth flows on the backend — this is the footgun

`config/SecurityConfig.java` defines the standard Spring Security JWT filter chain (via `JwtAuthFilter` + `JwtTokenProvider`). Most protected endpoints follow that pattern: send `Authorization: Bearer <token>`, filter validates, `@RequestMapping` handlers see an authenticated principal.

**BUT** the admin write endpoints on `/api/cars` (`AdminCarsController`) intentionally bypass Spring Security. They are declared `.permitAll()` in `SecurityConfig` and validate the JWT manually inside the controller via a `?token=` query parameter. This was done because some proxy layer between the client and Railway was stripping the `Authorization` header, causing 403s. Do not "fix" this by moving those endpoints back under the standard filter chain — the query-param workaround is deliberate.

- Public reads live at `/api/inventory/**` (GET), served by `InventoryController`
- Admin writes live at `/api/cars` and `/api/cars/{id}` (POST/PUT/DELETE), served by `AdminCarsController` with manual `?token=` auth
- Manager writes go through `/api/manager/*` and re-use standard Bearer-token auth
- `/api/admin/login` and `/api/manager/login` are public and return the JWT

### Two axios instances on the frontend

`frontend/src/api/index.js` defines **two** axios clients:
- `api` — reads `localStorage.admin_token`, used by admin views
- `managerApi` — reads `localStorage.manager_token`, used by manager views

They share a base URL but have separate interceptors. When adding a new authenticated endpoint, use whichever client matches the caller's role. Both wipe their token on 401 so the router guard boots the user back to login.

For calls hitting the `/api/cars` admin-write endpoints, use `fetch` with the token in the query string (see recent commits) — axios has had trouble with those paths in production.

### Frontend structure
- `src/config.js` — single source of truth for client-specific values (dealership name, phone, hours, colors, Cloudinary, API base). Uses `[BRACKET]` placeholders that can be grepped.
- `src/router/index.js` — defines public routes, `/admin/*` (guarded by `admin_token`), `/manager/*` (guarded by `manager_token`)
- `src/views/admin/` — 8 admin views (dashboard, cars CRUD, contacts, settings, staff)
- `src/views/manager/` — reduced-scope manager panel
- `src/components/{home,inventory,layout}/` — grouped Vue components

### Backend structure
Standard Spring Boot layout under `com.dealership.api`:
- `controller/` — one controller per resource (Inventory, AdminCars, Contact, Manager, Auth, Feed, Email, Staff, Review)
- `service/` — business logic including `KBBClient`, `ValuationService`, `FacebookFeedGenerator`, `EmailService`
- `model/` — JPA entities (`Car`, `CarImage`, `ContactSubmission`, `DealershipConfig`, `Review`, `StaffMember`)
- `security/` — `JwtAuthFilter`, `JwtTokenProvider`
- `config/` — `SecurityConfig`, `WebConfig`

### Database migrations
Flyway migrations in `backend/src/main/resources/db/migration/` (`V0_1__…` through `V5__…`). `spring.flyway.baseline-on-migrate=true` is set so migrations work against a DB that predates Flyway. Add new migrations with the next version prefix — do not edit existing ones.

## Deployment

- **Backend (Render):** deployed at `https://monaca-auto-sales.onrender.com`. Free tier — spins down after inactivity, warmed by the Netlify keep-alive function. `Dockerfile` in the repo root is what Render builds from. Health check at `/actuator/health`. All secrets (DB, JWT, Cloudinary, admin creds) injected as Render env vars.
- **Frontend (Netlify):** `netlify.toml` builds with `cd frontend && npm install && npm run build`, publishes `frontend/dist`. SPA rewrite sends all routes to `index.html`. `netlify/functions/keep-alive.js` runs every 10 min to ping the Render backend.
- CORS allow-list is in `application.properties` under `dealership.cors.allowed-origins` — must include the Netlify domain plus `monacaautosales.com`.
- **Stale files to ignore:** `railway.toml`, `nixpacks.toml`, `Procfile`, `deploy-railway.ps1` — all from an abandoned Railway plan. Don't act on them.

## Client-specific notes

- `frontend/src/config.js` is already filled in with real Monaca Auto Sales info — don't wipe it back to placeholders
- Default admin creds via env vars `ADMIN_USERNAME` / `ADMIN_PASSWORD`; manager via `MANAGER_USERNAME` / `MANAGER_PASSWORD`. Fallbacks in `application.properties` are only for local dev.
- KBB integration relies on VIN being present on the `Car`; admins refresh valuations from the car detail page.
- Facebook Marketplace feed served at `/api/feeds/…` (public), configured via `facebook.feed.base-url`.
