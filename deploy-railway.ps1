# ============================================================
# Railway Deployment Script — Dealership Template Backend
# Double-click this file or run it in PowerShell
# ============================================================

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  Dealership Template — Railway Deployment" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Login
Write-Host "STEP 1: Logging into Railway..." -ForegroundColor Yellow
Write-Host "(A browser window will open — click Authorize)" -ForegroundColor Gray
railway login
if ($LASTEXITCODE -ne 0) {
    Write-Host "Login failed. Please try again." -ForegroundColor Red
    pause
    exit 1
}
Write-Host "Logged in!" -ForegroundColor Green
Write-Host ""

# Step 2: Create project
Write-Host "STEP 2: Creating Railway project..." -ForegroundColor Yellow
Set-Location "C:\Users\Dugas\Desktop\dealership-template\backend"
railway init --name "dealership-template"
if ($LASTEXITCODE -ne 0) {
    Write-Host "Project init failed." -ForegroundColor Red
    pause
    exit 1
}
Write-Host "Project created!" -ForegroundColor Green
Write-Host ""

# Step 3: Add PostgreSQL
Write-Host "STEP 3: Adding PostgreSQL database..." -ForegroundColor Yellow
railway add --database postgres
Write-Host "PostgreSQL added!" -ForegroundColor Green
Write-Host ""

# Step 4: Set environment variables
Write-Host "STEP 4: Setting environment variables..." -ForegroundColor Yellow

# Prompt for sensitive values
Write-Host ""
Write-Host "Enter your Gmail App Password (for email notifications):" -ForegroundColor Cyan
Write-Host "  (Get one at myaccount.google.com/apppasswords — needs 2FA enabled)" -ForegroundColor Gray
$gmailPassword = Read-Host "Gmail App Password"

Write-Host ""
Write-Host "Enter a secure Admin Password for the dealership admin panel:" -ForegroundColor Cyan
$adminPassword = Read-Host "Admin Password"

Write-Host ""
Write-Host "Enter your Cloudinary Cloud Name (from cloudinary.com dashboard):" -ForegroundColor Cyan
$cloudinaryName = Read-Host "Cloudinary Cloud Name"

Write-Host ""
Write-Host "Enter your Cloudinary API Key:" -ForegroundColor Cyan
$cloudinaryKey = Read-Host "Cloudinary API Key"

Write-Host ""
Write-Host "Enter your Cloudinary API Secret:" -ForegroundColor Cyan
$cloudinarySecret = Read-Host "Cloudinary API Secret"

# Set all env vars
railway variables set `
    ADMIN_USERNAME=admin `
    ADMIN_PASSWORD="$adminPassword" `
    MANAGER_USERNAME=manager `
    MANAGER_PASSWORD=manager123 `
    JWT_SECRET=CHANGE_ME_USE_A_LONG_RANDOM_SECRET_STRING_256_BIT `
    MAIL_HOST=smtp.gmail.com `
    MAIL_PORT=587 `
    MAIL_USERNAME=dugas.openclaw@gmail.com `
    MAIL_PASSWORD="$gmailPassword" `
    CLOUDINARY_CLOUD_NAME="$cloudinaryName" `
    CLOUDINARY_API_KEY="$cloudinaryKey" `
    CLOUDINARY_API_SECRET="$cloudinarySecret" `
    SPRING_JPA_HIBERNATE_DDL_AUTO=update

Write-Host "Environment variables set!" -ForegroundColor Green
Write-Host ""

# Step 5: Deploy
Write-Host "STEP 5: Deploying backend (this takes 3-5 minutes)..." -ForegroundColor Yellow
railway up --detach
if ($LASTEXITCODE -ne 0) {
    Write-Host "Deployment failed. Check logs with: railway logs" -ForegroundColor Red
    pause
    exit 1
}
Write-Host "Deployment started!" -ForegroundColor Green
Write-Host ""

# Step 6: Get the URL
Write-Host "STEP 6: Getting your backend URL..." -ForegroundColor Yellow
Start-Sleep -Seconds 5
railway domain
Write-Host ""

Write-Host "============================================" -ForegroundColor Green
Write-Host "  All done! Check Railway dashboard for    " -ForegroundColor Green
Write-Host "  your live backend URL.                   " -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
Write-Host ""
Write-Host "Next step: Copy your Railway backend URL and" -ForegroundColor Cyan
Write-Host "paste it back to Claude to finish the setup." -ForegroundColor Cyan
Write-Host ""
pause
