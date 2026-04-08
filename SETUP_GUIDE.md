# Dealership Template — Setup Guide

Complete step-by-step instructions to set up and deploy your dealership website.

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Local Development Setup](#local-development-setup)
3. [Database Configuration](#database-configuration)
4. [Backend Setup](#backend-setup)
5. [Frontend Setup](#frontend-setup)
6. [Running Locally](#running-locally)
7. [Customization](#customization)
8. [Deployment Checklist](#deployment-checklist)

---

## Prerequisites

Before you begin, ensure you have the following installed on your machine:

### Required Software

- **Node.js** (v16+) — [Download](https://nodejs.org/en/)
  - Verify: `node --version` and `npm --version`

- **Java Development Kit (JDK)** 17+ — [Download](https://www.oracle.com/java/technologies/downloads/)
  - Verify: `java --version`
  - Verify Maven: `mvn --version`

- **PostgreSQL** (v13+) — [Download](https://www.postgresql.org/download/)
  - Verify: `psql --version`
  - Ensure PostgreSQL is running on your machine

- **Git** — [Download](https://git-scm.com/)

### Accounts & Credentials

- PostgreSQL local user (default: `postgres` with password)
- Admin user credentials (for dealership backend)

---

## Local Development Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/your-username/dealership-template.git
cd dealership-template
```

### Step 2: Install Dependencies

#### Backend
```bash
cd backend
mvn clean install
```

#### Frontend
```bash
cd ../frontend
npm install
```

---

## Database Configuration

### Step 1: Create PostgreSQL Database

Open your PostgreSQL client (`psql` or pgAdmin) and run:

```sql
CREATE DATABASE dealership_db;
CREATE USER dealership_user WITH PASSWORD 'secure_password_here';
GRANT ALL PRIVILEGES ON DATABASE dealership_db TO dealership_user;
```

### Step 2: Update Backend Properties

Edit `backend/src/main/resources/application.properties`:

```properties
# ============ DATABASE ============
spring.datasource.url=jdbc:postgresql://localhost:5432/dealership_db
spring.datasource.username=dealership_user
spring.datasource.password=secure_password_here

# ============ JPA / HIBERNATE ============
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL10Dialect

# ============ JWT / SECURITY ============
app.jwt.secret=[JWT_SECRET_KEY_MIN_32_CHARS]
app.jwt.expiration=86400000

# ============ SERVER ============
server.port=8080
server.servlet.context-path=/

# ============ CORS ============
app.cors.allowed-origins=http://localhost:5173,http://localhost:3000,https://yourdomain.com

# ============ ADMIN CREDENTIALS ============
app.admin.username=admin
app.admin.password=[SECURE_PASSWORD]
```

### Step 3: Run Flyway Migrations

Flyway will automatically create tables on first startup. Migrations are in `backend/src/main/resources/db/migration/`.

**Optional:** Load sample data (see [Sample Data](#sample-data))

---

## Backend Setup

### Build the JAR

```bash
cd backend
mvn clean package
```

This creates: `backend/target/dealership-api-0.0.1-SNAPSHOT.jar`

### Run Locally (Development)

```bash
mvn spring-boot:run
```

Or run the JAR directly:

```bash
java -jar target/dealership-api-0.0.1-SNAPSHOT.jar
```

**Backend runs on:** `http://localhost:8080`

---

## Frontend Setup

### Update Configuration

Edit `frontend/src/config.js` and replace all `[PLACEHOLDER]` values:

```javascript
export const DEALERSHIP_NAME   = 'Smith Auto Group'
export const TAGLINE           = 'Your Trusted Local Dealer Since 1995'
export const PHONE             = '(555) 867-5309'
export const EMAIL             = 'info@smithauto.com'
export const ADDRESS           = '123 Main Street'
export const CITY_STATE_ZIP    = 'Springfield, IL 62701'
export const LOGO_URL          = '/logo.png'

export const HOURS = {
  monday:    '9:00 AM – 7:00 PM',
  tuesday:   '9:00 AM – 7:00 PM',
  wednesday: '9:00 AM – 7:00 PM',
  thursday:  '9:00 AM – 7:00 PM',
  friday:    '9:00 AM – 7:00 PM',
  saturday:  '9:00 AM – 5:00 PM',
  sunday:    'Closed',
}

export const GOOGLE_MAPS_EMBED_URL = 'https://www.google.com/maps/embed?pb=...'

export const FACEBOOK_URL  = 'https://facebook.com/smithauto'
export const INSTAGRAM_URL = 'https://instagram.com/smithauto'

export const API_BASE_URL = 'http://localhost:8080'
```

### Update Cloudinary Credentials

For image uploads:

1. Sign up at [Cloudinary](https://cloudinary.com/)
2. Get your **Cloud Name** and **Upload Preset**
3. Update in `frontend/src/config.js`:

```javascript
export const CLOUDINARY_CLOUD_NAME = 'your-cloud-name'
export const CLOUDINARY_UPLOAD_PRESET = 'your-upload-preset'
```

### Run Frontend Dev Server

```bash
cd frontend
npm run dev
```

**Frontend runs on:** `http://localhost:5173`

---

## Running Locally

### Quick Start

**Terminal 1 — Backend:**
```bash
cd backend
mvn spring-boot:run
```

**Terminal 2 — Frontend:**
```bash
cd frontend
npm run dev
```

**Terminal 3 — PostgreSQL (if not already running):**
```bash
# macOS:
brew services start postgresql

# Linux:
sudo systemctl start postgresql

# Windows:
# Start PostgreSQL service from Services app or pgAdmin
```

Visit: **`http://localhost:5173`**

### API Documentation

- View Swagger/OpenAPI docs at: `http://localhost:8080/swagger-ui.html`
- Or see [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)

---

## Customization

### Colors & Branding

1. Update `frontend/src/config.js` with your primary color
2. Update Tailwind config in `frontend/tailwind.config.js`:

```javascript
theme: {
  extend: {
    colors: {
      primary: '#6172f0', // Change this to your brand color
    },
  },
}
```

3. Add your logo in `frontend/public/logo.png`
4. Update all social media links

### Text & Copy

Search for `[PLACEHOLDER]` in the codebase to find all places needing updates:

```bash
grep -r "\[PLACEHOLDER\]" .
```

### Database

See [DATABASE.md](./DATABASE.md) for schema details and sample queries.

---

## Deployment Checklist

Before deploying to production, verify:

- [ ] All `[PLACEHOLDER]` values in `config.js` are replaced
- [ ] Database credentials in `application.properties` are correct
- [ ] JWT secret key is set (min 32 characters)
- [ ] Admin username/password are secure
- [ ] Cloudinary credentials are configured
- [ ] CORS allowed origins include your domain
- [ ] Frontend env vars set correctly (`VITE_API_BASE_URL`)
- [ ] All tests pass: `mvn test` and `npm run test`
- [ ] Frontend builds without errors: `npm run build`
- [ ] Backend JAR builds: `mvn clean package`
- [ ] SSL/HTTPS configured on production domain
- [ ] Database backups configured
- [ ] Email notifications configured (if applicable)

---

## Troubleshooting

### Port Already in Use

**Backend (8080):**
```bash
# macOS/Linux:
lsof -i :8080

# Windows:
netstat -ano | findstr :8080

# Kill process:
kill -9 <PID>  # macOS/Linux
taskkill /PID <PID> /F  # Windows
```

**Frontend (5173):**
```bash
npm run dev -- --port 3000
```

### Database Connection Error

1. Verify PostgreSQL is running
2. Check credentials in `application.properties`
3. Ensure database `dealership_db` exists
4. Check firewall/network settings

### CORS Errors

Update `app.cors.allowed-origins` in `application.properties` to include your frontend URL.

### Images Not Loading

1. Verify Cloudinary credentials in `config.js`
2. Check upload preset permissions
3. Test upload via `POST /api/upload`

---

## Next Steps

1. Read [CUSTOMIZATION_CHECKLIST.md](./CUSTOMIZATION_CHECKLIST.md) for client-specific changes
2. See [DEPLOYMENT.md](./DEPLOYMENT.md) for production hosting options
3. Check [TEST_CHECKLIST.md](./TEST_CHECKLIST.md) before going live

---

**Need help?** Contact support or refer to [README.md](./README.md)
