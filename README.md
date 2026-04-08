# Dealership Template 🚗

A modern, production-ready dealership website template with inventory management, KBB integration, and Facebook Marketplace feed support.

---

## Features ✨

### Frontend
- ✅ **Responsive Design** — Mobile-first, works on all devices
- ✅ **Inventory Listing** — Browse, search, and filter cars
- ✅ **Car Detail Pages** — Full specs, photos, features, KBB value
- ✅ **Contact Forms** — Inquiries automatically captured
- ✅ **Admin Dashboard** — Manage inventory, add/edit/delete cars
- ✅ **KBB Integration** — Displays Kelley Blue Book values and pricing comparison
- ✅ **Google Maps** — Embed dealership location
- ✅ **Social Integration** — Facebook, Instagram, Twitter links

### Backend
- ✅ **REST API** — Fully documented endpoints
- ✅ **Authentication** — JWT-based admin access
- ✅ **Database** — PostgreSQL with Flyway migrations
- ✅ **KBB Values** — Auto-fetch vehicle valuations
- ✅ **Facebook Feed** — Auto-generated XML for Marketplace
- ✅ **Contact Capture** — Form submissions stored in database
- ✅ **CORS** — Configurable for multiple domains
- ✅ **Security** — Password hashing, token expiration, rate limiting

### Infrastructure
- ✅ **One-Click Deploy** — Railway, Heroku, AWS supported
- ✅ **Environment Config** — .env template included
- ✅ **Database Migrations** — Flyway auto-migrations
- ✅ **Production Ready** — HTTPS, backups, monitoring

---

## Technology Stack

| Layer | Tech | Version |
|-------|------|---------|
| **Frontend** | Vue 3 + Vite | 4.x, 5.x |
| **Styling** | Tailwind CSS | 3.x |
| **Backend** | Spring Boot | 3.x |
| **Language** | Java | 21 |
| **Database** | PostgreSQL | 13+ |
| **Images** | Cloudinary | Free tier |
| **Animations** | AOS | Latest |

---

## Quick Start (5 minutes)

### Prerequisites

```bash
# Check Java
java --version        # Must be 21+

# Check Node
node --version        # Must be 18+
npm --version         # Must be 9+

# Check PostgreSQL
psql --version        # Must be 13+
```

### 1️⃣ Clone & Setup

```bash
git clone https://github.com/your-username/dealership-template.git
cd dealership-template

# Backend
cd backend
mvn clean install

# Frontend
cd ../frontend
npm install
```

### 2️⃣ Configure

Edit **`frontend/src/config.js`** — replace all `[PLACEHOLDER]` values:

```javascript
export const DEALERSHIP_NAME   = 'Smith Auto Group'
export const PHONE             = '(555) 867-5309'
export const EMAIL             = 'info@smithauto.com'
export const CLOUDINARY_CLOUD_NAME = 'your-cloud-name'
// ... etc
```

Edit **`backend/src/main/resources/application.properties`**:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/dealership_db
spring.datasource.username=dealership_user
spring.datasource.password=your_secure_password
app.jwt.secret=min-32-character-secure-key
app.admin.username=admin
app.admin.password=secure_password
```

### 3️⃣ Database

```bash
# Create database
createdb -U postgres dealership_db

# Create user
psql -U postgres -c "CREATE USER dealership_user WITH PASSWORD 'your_password';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE dealership_db TO dealership_user;"

# Migrations run automatically on first startup
# Or load sample data:
psql -U dealership_user -d dealership_db -f database/sample-data.sql
```

### 4️⃣ Run Locally

**Terminal 1 — Backend:**
```bash
cd backend
mvn spring-boot:run
# Runs on http://localhost:8080
```

**Terminal 2 — Frontend:**
```bash
cd frontend
npm run dev
# Runs on http://localhost:5173
```

Visit: **`http://localhost:5173`** 🎉

---

## Documentation 📚

Comprehensive guides are included:

| Guide | Purpose |
|-------|---------|
| **[SETUP_GUIDE.md](./SETUP_GUIDE.md)** | Complete local dev setup instructions |
| **[CUSTOMIZATION_CHECKLIST.md](./CUSTOMIZATION_CHECKLIST.md)** | Step-by-step client customization |
| **[API_DOCUMENTATION.md](./API_DOCUMENTATION.md)** | Full API reference with examples |
| **[KBB_INTEGRATION.md](./KBB_INTEGRATION.md)** | How KBB values work and how to refresh them |
| **[FACEBOOK_INTEGRATION.md](./FACEBOOK_INTEGRATION.md)** | Setting up Facebook Marketplace feed |
| **[TEST_CHECKLIST.md](./TEST_CHECKLIST.md)** | Pre-launch testing guide |
| **[DEPLOYMENT.md](./DEPLOYMENT.md)** | Production deployment (Railway, Heroku, AWS) |
| **[.env.example](./.env.example)** | Environment variables template |

---

## Project Structure

```
dealership-template/
├── frontend/                    # Vue 3 + Vite + Tailwind
│   ├── src/
│   │   ├── config.js           # ⭐ Client-specific config
│   │   ├── api/index.js        # API service layer
│   │   ├── components/
│   │   │   ├── EditCarModal.vue    # New: Edit car modal
│   │   │   ├── inventory/CarCard.vue
│   │   │   └── inventory/FilterBar.vue
│   │   └── views/
│   │       ├── CarDetailView.vue   # Enhanced: KBB display
│   │       └── InventoryView.vue   # Enhanced: Search & filters
│   ├── tailwind.config.js       # Brand color config
│   └── package.json
│
├── backend/                     # Spring Boot 3
│   ├── src/main/java/com/dealership/api/
│   │   ├── controller/          # REST endpoints
│   │   ├── service/             # KBB, Facebook feed, etc.
│   │   ├── model/               # JPA entities
│   │   ├── repository/          # Data access
│   │   └── security/            # JWT auth
│   ├── src/main/resources/
│   │   ├── application.properties     # ⭐ Backend config
│   │   └── db/migration/        # Flyway migrations
│   └── pom.xml
│
├── database/
│   └── sample-data.sql          # 15 realistic sample cars
│
├── docs/
│   ├── SETUP_GUIDE.md
│   ├── CUSTOMIZATION_CHECKLIST.md
│   ├── API_DOCUMENTATION.md
│   ├── KBB_INTEGRATION.md
│   ├── FACEBOOK_INTEGRATION.md
│   ├── TEST_CHECKLIST.md
│   ├── DEPLOYMENT.md
│   └── .env.example
│
└── README.md                    # This file
```

---

## Key Endpoints

### Public API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/inventory` | List vehicles (filterable, paginated) |
| GET | `/api/inventory/{id}` | Get single vehicle |
| POST | `/api/contact` | Submit contact form |
| GET | `/api/inventory/feeds/facebook-marketplace.xml` | Facebook feed |

### Admin API (JWT Required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/admin/login` | Get JWT token |
| POST | `/api/inventory` | Create vehicle |
| PUT | `/api/inventory/{id}` | Update vehicle |
| DELETE | `/api/inventory/{id}` | Delete vehicle |
| POST | `/api/inventory/{id}/refresh-kbb` | Refresh KBB value |
| GET | `/api/admin/contacts` | List contact submissions |

**Example:**
```bash
curl -X GET "http://localhost:8080/api/inventory?make=Toyota&maxPrice=30000&page=0&size=12"
```

See [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) for complete reference.

---

## Admin Dashboard

### Access
1. Navigate to `/admin`
2. Login with credentials from `application.properties`
3. Manage inventory, view contacts

### Features
- ✅ Add/edit/delete vehicles
- ✅ Upload photos to Cloudinary
- ✅ Refresh KBB values
- ✅ View contact submissions
- ✅ View sales dashboard

**Default credentials:**
- Username: `admin`
- Password: [Set in `application.properties`]

⚠️ **Change password before going to production!**

---

## New Features Added

### 1. Car Detail Page Enhancements
- ✨ **KBB Value Display** — Shows fair market value prominently
- ✨ **Price Comparison** — "X% Below KBB" badge for great deals
- ✨ **Refresh KBB Button** — Admin can manually update valuations
- ✨ **Loading States** — Spinner while refreshing

### 2. Inventory Search & Filters
- 🔍 **Search Bar** — Search by make, model, VIN
- 🎯 **Advanced Filters** — By make, model, year, price range, condition
- 📊 **Results Counter** — Shows "X cars found"
- 🔄 **Real-time Updates** — Results update as you type

### 3. Edit Car Modal
- ✏️ **Modal Form** — Edit all vehicle details
- 📸 **Feature List** — Edit features as comma-separated text
- 💾 **Auto-Save** — Changes immediately visible
- 📱 **Mobile Responsive** — Works on all screen sizes

### 4. Frontend Polish
- ⚡ **Loading Spinners** — During KBB refresh, form submission
- 🎉 **Toast Notifications** — Success/error messages
- 🎨 **Improved Styling** — Better spacing, typography, colors
- 📱 **Mobile Responsiveness** — All new features mobile-optimized

### 5. Documentation
- 📖 **7 Comprehensive Guides** — Setup, customization, deployment, testing
- 📋 **Test Checklist** — Pre-launch validation
- 🚀 **Deployment Guide** — Railway, Heroku, AWS instructions
- 🔧 **API Documentation** — Complete endpoint reference
- 📊 **Sample Data** — 15 realistic cars ready to demo

---

## Customization for Clients

### 1. Basic Setup (30 minutes)

Follow **[CUSTOMIZATION_CHECKLIST.md](./CUSTOMIZATION_CHECKLIST.md):**

- [ ] Update business name, phone, email, address
- [ ] Set business hours
- [ ] Upload logo
- [ ] Choose brand color
- [ ] Add social media links
- [ ] Update Cloudinary credentials
- [ ] Update database credentials

### 2. Sample Data (5 minutes)

```bash
psql -U dealership_user -d dealership_db -f database/sample-data.sql
```

Loads 15 realistic cars:
- Toyota Camry, Civic, F-150
- Chevy Equinox, GMC Sierra
- Mazda CX-5, Subaru Outback, Kia Sportage
- Hyundai Elantra

### 3. Testing (15 minutes)

Use **[TEST_CHECKLIST.md](./TEST_CHECKLIST.md)** to verify everything works:
- Forms submit correctly
- Filters work
- KBB values display
- Mobile responsive
- No console errors

### 4. Deploy (30 minutes)

Follow **[DEPLOYMENT.md](./DEPLOYMENT.md)** for:
- Railway (recommended, easiest)
- Heroku
- AWS EC2 + RDS
- Custom VPS

---

## KBB Integration

The template automatically displays Kelley Blue Book values for each vehicle.

### How It Works
1. Enter vehicle **VIN** when creating a car
2. Admin clicks **"Refresh KBB"** on car detail page
3. Backend fetches current market value
4. Frontend displays KBB and price comparison

### Features
- ✅ Shows fair market value
- ✅ Displays price comparison ("2% Below KBB")
- ✅ Last updated timestamp
- ✅ Refresh button with loading state
- ✅ Success/error messages

See [KBB_INTEGRATION.md](./KBB_INTEGRATION.md) for details.

---

## Facebook Marketplace

Automatically generate an XML feed for Facebook Marketplace.

### Setup (10 minutes)
1. Create Facebook Business account
2. Create Vehicle Catalog in Catalog Manager
3. Add feed URL: `https://yourdealership.com/api/inventory/feeds/facebook-marketplace.xml`
4. Facebook auto-syncs daily

### Features
- ✅ Auto-generated XML feed
- ✅ All vehicle details included
- ✅ Updated daily automatically
- ✅ Reach millions of Facebook/Instagram users
- ✅ No manual data entry

See [FACEBOOK_INTEGRATION.md](./FACEBOOK_INTEGRATION.md) for step-by-step.

---

## Build & Deploy

### Development Build

```bash
# Frontend
cd frontend
npm run dev          # Hot-reload server
npm run build        # Production build
npm run lint         # Code quality check

# Backend
cd ../backend
mvn spring-boot:run  # Dev server
mvn clean package    # Production JAR
mvn test             # Run tests
```

### Production Build

```bash
# Frontend
npm run build        # Creates dist/ folder

# Backend
mvn clean package    # Creates JAR file

# Both ready for deployment
```

### One-Click Deploy

Use **[DEPLOYMENT.md](./DEPLOYMENT.md)** for automated deployment to:
- **Railway** (recommended) — Click `Deploy` button
- **Heroku** — `git push heroku main`
- **AWS** — CloudFormation or manual EC2/RDS setup

---

## Environment Variables

Create `.env` file in each directory (or set system env vars):

### Backend (root)
```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/dealership_db
SPRING_DATASOURCE_USERNAME=dealership_user
SPRING_DATASOURCE_PASSWORD=your_password
APP_JWT_SECRET=min-32-character-key
APP_ADMIN_USERNAME=admin
APP_ADMIN_PASSWORD=password
APP_CORS_ALLOWED_ORIGINS=http://localhost:5173,https://yourdomain.com
```

### Frontend (frontend/)
```
VITE_API_BASE_URL=http://localhost:8080
```

See [.env.example](./.env.example) for complete template.

---

## Testing

Comprehensive testing guide included: **[TEST_CHECKLIST.md](./TEST_CHECKLIST.md)**

**Pre-launch verification:**
- [ ] All pages load without errors
- [ ] Forms submit and validate
- [ ] Filters work correctly
- [ ] KBB values display
- [ ] Mobile responsive
- [ ] No console errors
- [ ] Database queries work
- [ ] API endpoints respond
- [ ] Security configured

---

## Troubleshooting

### "Cannot connect to database"
→ See [SETUP_GUIDE.md](./SETUP_GUIDE.md#troubleshooting)

### "Build failed: pom.xml not found"
→ Verify you're in the `backend/` directory when running Maven

### "API returns CORS error"
→ Update `APP_CORS_ALLOWED_ORIGINS` in `application.properties`

### "Images not uploading"
→ Verify Cloudinary credentials in `config.js` and `application.properties`

### "KBB value not showing"
→ Ensure vehicle has valid VIN (17 characters), click "Refresh KBB"

See individual guide docs for detailed troubleshooting sections.

---

## Performance Tips

- 🚀 **Images** — Use Cloudinary's auto-optimization
- 📦 **Frontend** — Built with Vite (fast build, small bundle)
- 🗄️ **Database** — Use indexes on make, model, year
- 🔍 **Caching** — Enable HTTP caching for static assets
- ⚡ **CDN** — Use CloudFlare or AWS CloudFront for images

---

## Security

- ✅ JWT authentication for admin endpoints
- ✅ Password hashing (bcrypt)
- ✅ CORS configurable per domain
- ✅ SQL injection protection (JPA)
- ✅ XSS protection (Vue templating)
- ✅ Rate limiting on API

See [DEPLOYMENT.md](./DEPLOYMENT.md#security) for production hardening.

---

## Support & Community

- 📖 Read the guides: `SETUP_GUIDE.md`, `API_DOCUMENTATION.md`, etc.
- 🐛 Check [TEST_CHECKLIST.md](./TEST_CHECKLIST.md) if something's broken
- 🚀 Deploy with [DEPLOYMENT.md](./DEPLOYMENT.md)
- ❓ FAQ in individual guide documents

---

## License

[MIT License](./LICENSE) — Use freely for clients, sell the service, modify as needed.

---

## What's Next?

1. **Review** — Read [SETUP_GUIDE.md](./SETUP_GUIDE.md)
2. **Customize** — Follow [CUSTOMIZATION_CHECKLIST.md](./CUSTOMIZATION_CHECKLIST.md)
3. **Test** — Use [TEST_CHECKLIST.md](./TEST_CHECKLIST.md)
4. **Deploy** — Follow [DEPLOYMENT.md](./DEPLOYMENT.md)
5. **Celebrate** — Your dealership site is live! 🎉

---

**Built with ❤️ for dealership owners who want a modern, professional online presence.**
