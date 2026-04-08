# Deployment Guide

Complete instructions for deploying to production on Railway (recommended), Heroku, or AWS.

---

## Table of Contents

1. [Railway (Recommended)](#railway-recommended)
2. [Heroku Alternative](#heroku-alternative)
3. [AWS EC2/RDS](#aws-ec2rds)
4. [Post-Deployment](#post-deployment)
5. [Troubleshooting](#troubleshooting)

---

## Railway (Recommended)

Railway is the easiest option for deploying this template. It handles both frontend and backend automatically.

### Prerequisites

- [ ] GitHub account (to connect repo)
- [ ] Railway account ([railway.app](https://railway.app))
- [ ] Credit card for Railway (free tier available, $5/month for small apps)

### Step 1: Prepare Repository

1. **Push code to GitHub:**
   ```bash
   git add .
   git commit -m "Ready for production"
   git push origin main
   ```

2. **Verify these files exist:**
   - [ ] `backend/pom.xml` (Maven build)
   - [ ] `frontend/package.json` (Node build)
   - [ ] `backend/src/main/resources/application.properties` (production config)
   - [ ] `.github/workflows/` (optional CI/CD)

### Step 2: Create Railway Project

1. Go to [Railway Dashboard](https://railway.app/dashboard)
2. Click **"New Project"**
3. Select **"Deploy from GitHub"**
4. Connect your GitHub account (authorize)
5. Select your `dealership-template` repository
6. Select the `main` branch

### Step 3: Configure Backend Service

Railway auto-detects the backend. Configure:

1. **Service Name:** `dealership-backend`

2. **Environment Variables** (match exactly — these map to `application.properties`):
   ```
   DB_USERNAME=postgres
   DB_PASSWORD=[your-db-password]
   JWT_SECRET=[min-32-char-random-string]
   ADMIN_USERNAME=admin
   ADMIN_PASSWORD=[strong-admin-password]
   MANAGER_USERNAME=manager
   MANAGER_PASSWORD=[strong-manager-password]
   CLOUDINARY_CLOUD_NAME=[your-cloudinary-name]
   CLOUDINARY_API_KEY=[your-cloudinary-key]
   CLOUDINARY_API_SECRET=[your-cloudinary-secret]
   MAIL_USERNAME=[your-gmail]
   MAIL_PASSWORD=[gmail-app-password]
   dealership.cors.allowed-origins=https://yourdomain.com,https://www.yourdomain.com
   ```
   Note: Railway PostgreSQL plugin sets `DATABASE_URL` automatically. Use it for
   `spring.datasource.url` by adding: `spring.datasource.url=${DATABASE_URL}`
   or manually set `DB_URL` to the Railway Postgres connection string.

3. **Root Directory:** `backend`

4. **Build Command:** `mvn clean package`

5. **Start Command:** `java -jar target/dealership-api-0.0.1-SNAPSHOT.jar`

### Step 4: Configure Database (PostgreSQL)

1. **Add PostgreSQL Plugin:**
   - In Railway dashboard, click **"Add Service"** (+ icon)
   - Search for **PostgreSQL**
   - Click **"Deploy"**

2. **Configure Database:**
   - Name: `dealership-db`
   - Set a strong password

3. **Link to Backend:**
   - Railway auto-generates connection string
   - Copy to `SPRING_DATASOURCE_URL` env var

4. **Run Migrations:**
   - Connect via Railway terminal or local `psql`
   - Run initial migrations
   - Load sample data (optional): `psql < database/sample-data.sql`

### Step 5: Configure Frontend Service

1. **Add Service** → **GitHub repository**
2. **Service Name:** `dealership-frontend`
3. **Root Directory:** `frontend`
4. **Environment Variables:**
   ```
   VITE_API_BASE_URL=https://your-backend-url.railway.app
   ```
   (Railway provides this URL after backend deploys)

5. **Build Command:** `npm run build`
6. **Start Command:** `npm run preview` or use static hosting
7. **Build Output Directory:** `dist`

### Step 6: Deploy

1. **Push to GitHub** (if you haven't already)
2. Railway auto-detects changes and deploys
3. Watch build logs in Railway dashboard
4. Wait for "✓ Deployed" message

### Step 7: Configure Domain

1. **Get Railway URLs:**
   - Backend: `https://dealership-backend-production.railway.app`
   - Frontend: `https://dealership-frontend-production.railway.app`

2. **Add Custom Domain (optional):**
   - In Railway → Service Settings → Domains
   - Add your custom domain (e.g., `yourdealership.com`)
   - Follow DNS configuration steps

3. **Update Frontend Config:**
   - In `frontend/src/config.js`:
   ```javascript
   export const API_BASE_URL = 'https://your-backend-url.railway.app'
   ```
   - Redeploy frontend

### Step 8: Test Production

```bash
# Test backend API
curl https://your-backend-url.railway.app/api/inventory

# Test frontend
Visit https://your-frontend-url.railway.app
```

---

## Heroku Alternative

Heroku is slightly more complex but still straightforward.

### Prerequisites

- [ ] Heroku account ([heroku.com](https://heroku.com))
- [ ] Heroku CLI installed
- [ ] Credit card (free tier removed, hobby tier $7/month)

### Step 1: Create Heroku Apps

```bash
# Login
heroku login

# Create backend app
heroku create dealership-api-prod

# Create frontend app
heroku create dealership-web-prod

# Add PostgreSQL to backend
heroku addons:create heroku-postgresql:standard-0 --app dealership-api-prod
```

### Step 2: Configure Backend

```bash
# Set environment variables
heroku config:set \
  APP_JWT_SECRET="[32-char-secret]" \
  APP_ADMIN_USERNAME="admin" \
  APP_ADMIN_PASSWORD="[password]" \
  --app dealership-api-prod

# View database URL
heroku config:get DATABASE_URL --app dealership-api-prod
```

### Step 3: Deploy Backend

Create `Procfile` in `backend/`:
```
web: java -jar target/dealership-api-0.0.1-SNAPSHOT.jar
```

```bash
cd backend

# Initialize git
git init
git add .
git commit -m "Initial commit"

# Deploy
git push heroku main

# Run migrations
heroku run "java -cp target/classes:target/dependency/* org.flywaydb.core.Flyway" --app dealership-api-prod

# Load sample data
heroku pg:psql --app dealership-api-prod < ../database/sample-data.sql
```

### Step 4: Deploy Frontend

Create `frontend/static.json`:
```json
{
  "root": "dist/",
  "routes": {
    "/**": "index.html"
  }
}
```

```bash
cd frontend

# Add Heroku buildpack for static sites
heroku buildpacks:set https://github.com/heroku/heroku-buildpack-static.git --app dealership-web-prod

# Set environment
heroku config:set VITE_API_BASE_URL="https://dealership-api-prod.herokuapp.com" --app dealership-web-prod

# Deploy
git push heroku main
```

---

## AWS EC2/RDS

More control but more setup required.

### Prerequisites

- [ ] AWS account
- [ ] EC2 key pair created
- [ ] Security groups configured
- [ ] Domain registered (Route53 or external)

### Step 1: Create RDS Database

1. **AWS Console** → **RDS** → **Create Database**
2. **Engine:** PostgreSQL (latest version)
3. **Multi-AZ:** No (for cost)
4. **DB Instance Identifier:** `dealership-db`
5. **Master Username:** `dealership_user`
6. **Password:** [Strong password]
7. **DB Name:** `dealership_db`
8. **Public Accessibility:** Yes (so your EC2 can connect)
9. **Create Database**

### Step 2: Create EC2 Instance

1. **AWS Console** → **EC2** → **Launch Instance**
2. **AMI:** Ubuntu Server 22.04 LTS (free tier eligible)
3. **Instance Type:** `t3.small` (or higher for production)
4. **Security Group:** Allow:
   - HTTP (80)
   - HTTPS (443)
   - SSH (22)
   - PostgreSQL (5432) — only from RDS subnet
5. **Key Pair:** Select your existing pair
6. **Launch**

### Step 3: Install Dependencies on EC2

```bash
# Connect to instance
ssh -i your-key.pem ubuntu@your-instance-ip

# Update system
sudo apt update && sudo apt upgrade -y

# Install Java
sudo apt install openjdk-17-jdk -y
java --version

# Install Node
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt install nodejs -y
node --version

# Install Nginx (reverse proxy)
sudo apt install nginx -y

# Install PostgreSQL client
sudo apt install postgresql-client -y
```

### Step 4: Deploy Backend

```bash
# Clone repo
git clone https://github.com/your-repo/dealership-template.git
cd dealership-template/backend

# Configure for RDS
# Edit src/main/resources/application.properties with RDS endpoint

# Build
mvn clean package

# Start backend
nohup java -jar target/dealership-api-0.0.1-SNAPSHOT.jar &
```

### Step 5: Deploy Frontend

```bash
cd ../frontend

# Build
npm run build

# Copy to web root
sudo cp -r dist/* /var/www/html/
```

### Step 6: Configure Nginx Reverse Proxy

Edit `/etc/nginx/sites-enabled/default`:

```nginx
server {
    listen 80 default_server;
    server_name yourdomain.com;

    # Frontend static files
    root /var/www/html;
    index index.html;

    # React routing fallback
    location / {
        try_files $uri $uri/ /index.html;
    }

    # Backend API proxy
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

Restart Nginx:
```bash
sudo systemctl restart nginx
```

### Step 7: Enable HTTPS (Let's Encrypt)

```bash
sudo apt install certbot python3-certbot-nginx -y
sudo certbot --nginx -d yourdomain.com -d www.yourdomain.com
```

---

## Post-Deployment

### Step 1: Verify Everything Works

- [ ] **API endpoint** responds: `curl https://yourdealership.com/api/inventory`
- [ ] **Frontend loads:** Visit `https://yourdealership.com`
- [ ] **Contact form** sends emails
- [ ] **Admin login** works
- [ ] **Database** has sample data

### Step 2: Monitor & Logs

**Railway:**
```bash
# View logs
railway logs
```

**Heroku:**
```bash
heroku logs --tail --app dealership-api-prod
```

**AWS EC2:**
```bash
# SSH to instance
ssh -i your-key.pem ubuntu@your-instance-ip

# View backend logs
tail -f nohup.out

# View nginx logs
sudo tail -f /var/log/nginx/access.log
```

### Step 3: Set Up Monitoring

- [ ] **Uptime monitoring** (UptimeRobot, StatusPage)
- [ ] **Error tracking** (Sentry, DataDog)
- [ ] **Database backups** (automated daily)
- [ ] **Log aggregation** (CloudWatch, LogRocket)
- [ ] **Performance monitoring** (New Relic, DataDog)

### Step 4: Configure CDN (Optional)

For better performance, use CloudFlare or AWS CloudFront:

1. **CloudFlare:**
   - Add domain to CloudFlare
   - Point nameservers to CloudFlare
   - Enable caching for static assets
   - Enable compression

2. **AWS CloudFront:**
   - Create distribution
   - Point to your origin (EC2 or ALB)
   - Cache invalidation on deploys

### Step 5: Email Notifications (Optional)

Configure contact form emails:

**Using SendGrid:**

1. Sign up at [sendgrid.com](https://sendgrid.com)
2. Get API key
3. Update `application.properties`:
   ```properties
   spring.mail.host=smtp.sendgrid.net
   spring.mail.port=587
   spring.mail.username=apikey
   spring.mail.password=YOUR_SENDGRID_API_KEY
   ```

**Using AWS SES:**

1. Verify domain in SES Console
2. Create SMTP credentials
3. Update `application.properties` with SES endpoint

---

## Troubleshooting

### Frontend Not Connecting to Backend

**Problem:** Frontend shows "Error loading inventory"

**Solution:**
1. Check `VITE_API_BASE_URL` in frontend config
2. Verify backend API is responding: `curl https://your-api.com/api/inventory`
3. Check CORS settings in `application.properties`
4. Clear browser cache and refresh

### Database Connection Failed

**Problem:** "Cannot connect to database" error

**Solution:**
1. Verify database credentials in env vars
2. Check security groups allow your app's IP
3. Verify database is running: `psql -h [host] -U [user] -d [db] -c "SELECT 1"`
4. Check VPC routing (AWS)

### Build Fails

**Problem:** "Build failed" message in Railway/Heroku

**Solution:**
1. Check build logs for specific error
2. Verify `pom.xml` or `package.json` exist
3. Run `mvn clean package` locally to test
4. Check for missing environment variables
5. Verify Java/Node versions match

### Pages Show Blank

**Problem:** Frontend loads but shows nothing

**Solution:**
1. Check browser console (F12) for errors
2. Verify API is responding
3. Clear browser cache
4. Check if JavaScript is enabled
5. Verify static files are being served

### Performance Issues

**Problem:** Site is slow

**Solution:**
1. Check server CPU/memory usage
2. Enable caching headers
3. Use CDN for static assets
4. Optimize database queries
5. Use connection pooling
6. Add indexing to frequently-searched columns

---

## Environment Variables Reference

### Production Environment Variables

**Backend:**
```
SPRING_DATASOURCE_URL=postgresql://[host]:[port]/[database]
SPRING_DATASOURCE_USERNAME=[user]
SPRING_DATASOURCE_PASSWORD=[password]
APP_JWT_SECRET=[32+ char secret]
APP_ADMIN_USERNAME=[username]
APP_ADMIN_PASSWORD=[password]
APP_CORS_ALLOWED_ORIGINS=https://yourdomain.com
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SERVER_PORT=8080
```

**Frontend:**
```
VITE_API_BASE_URL=https://your-api-url.com
```

### Recommended Sizes

- **EC2/VM:** t3.small (1 vCPU, 2GB RAM) minimum for 100+ requests/day
- **RDS:** db.t3.micro (1 vCPU, 1GB RAM) minimum
- **Storage:** 20GB for database, 20GB for app logs

---

## Maintenance

### Regular Tasks

- [ ] **Weekly:** Check logs for errors
- [ ] **Monthly:** Review database size and backups
- [ ] **Quarterly:** Update dependencies (`mvn versions:display-dependency-updates`)
- [ ] **Quarterly:** Security audit (check for vulnerabilities)
- [ ] **Annually:** Review and update SSL certificate

### Scaling

When traffic grows:
1. Upgrade instance size
2. Add database read replicas (if using managed database)
3. Implement caching (Redis)
4. Use load balancer (AWS ALB, Nginx)
5. Increase connection pool sizes

---

## Rollback

If something breaks after deployment:

**Railway:**
```bash
# Redeploy previous build
railway redeploy [previous-deployment-id]
```

**Heroku:**
```bash
# Rollback to previous release
heroku releases --app dealership-api-prod
heroku releases:rollback v42 --app dealership-api-prod
```

**AWS/EC2:**
```bash
# Restore from backup
# Use previous binary or container image
```

---

## See Also

- [SETUP_GUIDE.md](./SETUP_GUIDE.md) — Local development
- [TEST_CHECKLIST.md](./TEST_CHECKLIST.md) — Pre-deployment testing
- [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) — API reference
- [CUSTOMIZATION_CHECKLIST.md](./CUSTOMIZATION_CHECKLIST.md) — Client setup

---

**Congratulations!** Your dealership template is now live! 🚀
