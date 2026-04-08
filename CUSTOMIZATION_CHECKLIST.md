# Customization Checklist

Step-by-step checklist for customizing the dealership template for your clients.

---

## Overview

This checklist ensures every client configuration is complete before deploying. Work through each section systematically.

---

## 1. Basic Business Information

### Frontend (`frontend/src/config.js`)

- [ ] **Dealership Name**
  - Find: `DEALERSHIP_NAME = '[DEALERSHIP_NAME]'`
  - Replace with: Client's actual business name
  - Example: `'Smith Auto Group'`

- [ ] **Tagline**
  - Find: `TAGLINE = '[TAGLINE]'`
  - Replace with: Business tagline/slogan
  - Example: `'Your Trusted Local Dealer Since 1995'`

- [ ] **Phone Number**
  - Find: `PHONE = '[PHONE]'`
  - Replace with: Business phone number
  - Format: `'(555) 867-5309'` (with parentheses/dashes)

- [ ] **Email Address**
  - Find: `EMAIL = '[EMAIL]'`
  - Replace with: Business email
  - Example: `'info@smithauto.com'`

- [ ] **Physical Address**
  - Find: `ADDRESS = '[ADDRESS]'`
  - Replace with: Street address only
  - Example: `'123 Main Street'`

- [ ] **City, State, ZIP**
  - Find: `CITY_STATE_ZIP = '[CITY_STATE_ZIP]'`
  - Replace with: Full location
  - Example: `'Springfield, IL 62701'`

- [ ] **Logo URL**
  - Find: `LOGO_URL = '[LOGO_URL]'`
  - Option A: Store logo in `frontend/public/logo.png`, set to `'/logo.png'`
  - Option B: Use CDN URL (e.g., Cloudinary, AWS S3)
  - Upload logo file if needed

### Backend (`backend/src/main/resources/application.properties`)

- [ ] **Database URL**
  - Find: `spring.datasource.url=jdbc:postgresql://localhost:5432/dealership_db`
  - Update: Database URL and name (production uses cloud database)

- [ ] **Database Credentials**
  - Find: `spring.datasource.username` and `spring.datasource.password`
  - Replace with: Secure credentials from hosting provider

- [ ] **JWT Secret**
  - Find: `app.jwt.secret=[JWT_SECRET_KEY_MIN_32_CHARS]`
  - Generate: Strong random key (min 32 characters)
  - Example: `'aB3xY9nQpL7wM2kJ8vC4rH6tZ0fG5sD1'`

- [ ] **Admin Credentials**
  - Find: `app.admin.username=admin`
  - Replace with: Unique username (e.g., `'smith_admin'`)
  - Find: `app.admin.password=[SECURE_PASSWORD]`
  - Replace with: Strong password

- [ ] **CORS Origins**
  - Find: `app.cors.allowed-origins=...`
  - Add production domain: `'https://yourdealership.com'`
  - Keep local dev URLs for testing

---

## 2. Business Hours

### Frontend (`frontend/src/config.js`)

Display on website header, footer, and contact page.

```javascript
export const HOURS = {
  monday:    '9:00 AM – 7:00 PM',
  tuesday:   '9:00 AM – 7:00 PM',
  wednesday: '9:00 AM – 7:00 PM',
  thursday:  '9:00 AM – 7:00 PM',
  friday:    '9:00 AM – 7:00 PM',
  saturday:  '9:00 AM – 5:00 PM',
  sunday:    'Closed',
}
```

- [ ] Monday hours
- [ ] Tuesday hours
- [ ] Wednesday hours
- [ ] Thursday hours
- [ ] Friday hours
- [ ] Saturday hours
- [ ] Sunday hours

**Verify with client:** Ask for exact hours; common format is `'9:00 AM – 5:00 PM'`

---

## 3. Branding & Colors

### Primary Color

- [ ] **Tailwind Config** (`frontend/tailwind.config.js`)
  - Find: `colors: { primary: '#6172f0' }`
  - Replace with: Client's brand color (hex code)
  - Example: `'#D1344E'` (red)

- [ ] **Config.js** (`frontend/src/config.js`)
  - Find: `PRIMARY_COLOR = '#6172f0'`
  - Replace with: Same color as above

### Logo & Images

- [ ] **Logo**
  - Add: `frontend/public/logo.png` (PNG or SVG)
  - Size: 200-400px wide
  - Set in config: `LOGO_URL = '/logo.png'`

- [ ] **Favicon**
  - Replace: `frontend/public/favicon.svg` (optional)

---

## 4. Social Media & Links

### Frontend (`frontend/src/config.js`)

- [ ] **Facebook URL**
  - Find: `FACEBOOK_URL = '[FACEBOOK_URL]'`
  - Replace: `'https://facebook.com/yourdealership'`
  - Set to `null` if no Facebook

- [ ] **Instagram URL**
  - Find: `INSTAGRAM_URL = '[INSTAGRAM_URL]'`
  - Replace: `'https://instagram.com/yourdealership'`
  - Set to `null` if not active

- [ ] **Twitter URL**
  - Currently: `null`
  - If active: `'https://twitter.com/yourdealership'`

### Google Maps

- [ ] **Google Maps Embed**
  - Find: `GOOGLE_MAPS_EMBED_URL = '[GOOGLE_MAPS_EMBED_URL]'`
  - Get embed code from [Google Maps](https://www.google.com/maps)
  - Example: `'https://www.google.com/maps/embed?pb=!1m18!...'`
  - Paste only the `src` URL value

---

## 5. Cloud Services & Credentials

### Cloudinary (for image uploads)

Used for vehicle photo uploads.

- [ ] **Sign up:** [Cloudinary.com](https://cloudinary.com) (free plan available)

- [ ] **Get Credentials:**
  - Cloud Name: `frontend/src/config.js`
  - Upload Preset: `frontend/src/config.js`

- [ ] **Update config.js:**
  ```javascript
  export const CLOUDINARY_CLOUD_NAME = 'your-cloud-name'
  export const CLOUDINARY_UPLOAD_PRESET = 'your-upload-preset'
  ```

### Database Hosting (for production)

Options:
- [ ] **Railway** (recommended, easiest)
- [ ] **AWS RDS**
- [ ] **Heroku Postgres**
- [ ] **DigitalOcean Managed PostgreSQL**
- [ ] **Self-hosted VPS**

See [DEPLOYMENT.md](./DEPLOYMENT.md) for detailed setup.

---

## 6. Search & Replace All Placeholders

Run a global find-and-replace to catch any missed placeholders:

```bash
# From project root
grep -r "\[PLACEHOLDER\]" .
grep -r "\[DEALERSHIP_NAME\]" .
grep -r "\[TAGLINE\]" .
grep -r "\[PHONE\]" .
grep -r "\[EMAIL\]" .
# etc.
```

- [ ] **Frontend config.js** — All placeholders replaced
- [ ] **Backend application.properties** — All placeholders replaced
- [ ] **README.md** — Updated with client details (optional)
- [ ] **No instances of `[...]` remain in config files**

---

## 7. Testing & Validation

### Build Tests

- [ ] **Frontend builds successfully**
  ```bash
  cd frontend
  npm run build
  # Verify: dist/ folder created, no errors
  ```

- [ ] **Backend builds successfully**
  ```bash
  cd backend
  mvn clean package
  # Verify: target/dealership-api-0.0.1-SNAPSHOT.jar created
  ```

### Local Testing

- [ ] **Backend starts without errors**
  ```bash
  mvn spring-boot:run
  # Verify: Listening on http://localhost:8080
  ```

- [ ] **Frontend starts without errors**
  ```bash
  npm run dev
  # Verify: Running on http://localhost:5173
  ```

- [ ] **Database connection works**
  - Can see vehicles listed
  - No connection errors in logs

- [ ] **All images display correctly**
  - Check homepage featured cars
  - Check inventory page
  - Check car detail pages

- [ ] **Links work**
  - Navigation links
  - Social media links
  - Contact form
  - Phone number (clickable on mobile)

- [ ] **Business hours display**
  - Check footer
  - Check contact page
  - Check header (if shown)

- [ ] **Logo displays**
  - Check header
  - Check footer

- [ ] **Colors are correct**
  - Buttons match brand color
  - Links match brand color
  - Badges match brand color

### See [TEST_CHECKLIST.md](./TEST_CHECKLIST.md) for comprehensive testing

---

## 8. Database Setup

### Sample Data

- [ ] **Load sample cars** (if provided)
  ```bash
  psql -U dealership_user -d dealership_db -f database/sample-data.sql
  ```
  See [sample-data.sql](./database/sample-data.sql)

- [ ] **Verify cars appear** on `/inventory` page

### Admin User

- [ ] **Login works**
  - Visit `/admin/login`
  - Username: value from `application.properties`
  - Password: value from `application.properties`
  - Should see admin dashboard

- [ ] **Can add/edit/delete cars**
  - Test admin car form
  - Upload test image
  - Edit car details
  - Delete test car

---

## 9. Optional: Advanced Customization

### Custom Styles

- [ ] **Update colors in Tailwind** (already done above)
- [ ] **Custom fonts** — Edit `frontend/src/App.vue` to import Google Fonts
- [ ] **Custom backgrounds** — Add to `frontend/src/assets/` and reference in Vue templates

### Additional Pages

- [ ] **Blog/News** (if enabled)
  - Edit `frontend/src/views/BlogView.vue`
  - Add actual blog posts

- [ ] **Staff/Team** (if enabled)
  - Edit `frontend/src/views/StaffView.vue`
  - Add team member info

- [ ] **Financing** (if enabled)
  - Edit `frontend/src/views/FinancingView.vue`
  - Add financing options/lenders

### Email Notifications

- [ ] **Contact form emails**
  - Configure email service (SendGrid, AWS SES, SMTP)
  - Update backend to send emails on contact submission
  - Test: Submit contact form, verify email received

---

## 10. Pre-Deployment Final Checks

Before deploying to production:

- [ ] **All config values finalized**
- [ ] **Database credentials are secure**
- [ ] **JWT secret is strong (min 32 chars)**
- [ ] **HTTPS/SSL will be enabled**
- [ ] **CORS origins include production domain**
- [ ] **Environment variables documented** (see .env.example)
- [ ] **Backups planned** (database, media, code)
- [ ] **Monitoring set up** (logs, uptime, performance)
- [ ] **Admin password is secure and documented** (in secure location, not in code)

---

## 11. Post-Deployment

After going live:

- [ ] **Test production domain** — Full site works
- [ ] **Forms work** — Contact form, car inquiries
- [ ] **Images load** — From Cloudinary
- [ ] **No console errors** — Check browser dev tools
- [ ] **Mobile responsive** — Test on iPhone, Android
- [ ] **Google Analytics** (optional) — Set up tracking
- [ ] **SSL certificate** — Ensure HTTPS works
- [ ] **Monitor logs** — Check for errors

---

## Checklist Summary

**Total Items:** 80+

**Typical Time:** 2-3 hours per client

**Critical Items** (must do):
- Business info (name, phone, email, address)
- Database credentials
- JWT secret
- Admin password
- Brand color
- Logo
- Cloudinary credentials
- Build & test frontend/backend
- Load initial inventory

**Nice-to-Have** (if time permits):
- Custom fonts
- Additional pages
- Email notifications
- Analytics setup

---

## Quick Reference

### Files to Edit

| File | What | Frequency |
|------|------|-----------|
| `frontend/src/config.js` | Business info, colors, hours, social | Every client |
| `backend/src/main/resources/application.properties` | DB, JWT, auth, CORS | Every client |
| `frontend/public/logo.png` | Logo image | Most clients |
| `frontend/tailwind.config.js` | Brand color | Most clients |
| `frontend/public/favicon.svg` | Browser tab icon | Optional |

### Commands to Run

```bash
# Check for placeholders
grep -r "\[" frontend/src/config.js backend/src/main/resources/

# Build frontend
cd frontend && npm run build && cd ..

# Build backend
cd backend && mvn clean package && cd ..

# Run tests
cd frontend && npm run test && cd ..
cd backend && mvn test && cd ..

# Start local dev
# Terminal 1: cd backend && mvn spring-boot:run
# Terminal 2: cd frontend && npm run dev
```

---

## Support

**Common Issues:**

- **Placeholder not replaced:** Use find-and-replace tool (Ctrl+H in VS Code)
- **Build fails:** Check Node/Java versions, run `npm install` / `mvn clean install`
- **Config not loading:** Restart dev servers and clear browser cache
- **Images not uploading:** Verify Cloudinary credentials are correct
- **Login fails:** Check admin username/password match `application.properties`

**For help:** See [README.md](./README.md) or [SETUP_GUIDE.md](./SETUP_GUIDE.md)

---

**Ready to deploy? See [DEPLOYMENT.md](./DEPLOYMENT.md) next!**
