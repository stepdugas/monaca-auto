# Testing Guide — All 4 Features

## Quick Start

### Prerequisites
- Java 21 installed
- Node.js 18+ installed
- PostgreSQL running (or use Docker)
- Gmail app password set (for email testing)

### 1. Start the Backend

```bash
cd backend
mvn clean spring-boot:run
```

The API will be available at `http://localhost:8080`

Check the startup logs for:
```
Started DealershipApiApplication in X.XXX seconds
```

### 2. Start the Frontend

```bash
cd frontend
npm install        # if needed
npm run dev
```

The app will be available at `http://localhost:5173`

---

## Test 1: Email Service

### Test the Email Endpoint

**Using cURL:**
```bash
curl -X POST http://localhost:8080/api/email/test \
  -H "Content-Type: application/json" \
  -d '{
    "to": "your.email@gmail.com",
    "subject": "Dealership Email Test",
    "body": "This is a test email from the dealership API."
  }'
```

**Expected response (200 OK):**
```json
{
  "status": "success",
  "message": "Test email sent to your.email@gmail.com"
}
```

**Check your email:**
- Wait 10-30 seconds
- Look for email from `Dugas.openclaw@gmail.com`
- Subject: "Dealership Email Test"
- Body contains your message

### Error Scenarios

**Invalid email format:**
```bash
curl -X POST http://localhost:8080/api/email/test \
  -H "Content-Type: application/json" \
  -d '{
    "to": "not-an-email",
    "subject": "Test",
    "body": "Test"
  }'
```
Response: `400 Bad Request` with validation error

**Missing required field:**
```bash
curl -X POST http://localhost:8080/api/email/test \
  -H "Content-Type: application/json" \
  -d '{
    "to": "test@example.com"
  }'
```
Response: `400 Bad Request` — subject and body required

---

## Test 2: Facebook Marketplace Feed

### View the Feed

**In browser:**
```
http://localhost:8080/api/feeds/facebook-marketplace
```
or
```
http://localhost:8080/api/feeds/facebook-marketplace.xml
```

**You should see:**
- XML output with vehicles
- First line: `<?xml version="1.0" encoding="UTF-8"?>`
- Root element: `<rss version="2.0">`
- Each car as an `<item>` element

**Using cURL:**
```bash
curl http://localhost:8080/api/feeds/facebook-marketplace
```

### Validate XML Format

**Using xmllint (if installed):**
```bash
curl http://localhost:8080/api/feeds/facebook-marketplace | xmllint --format -
```

Should output prettified XML with no errors.

### Test with Sample Data

First, add some test cars:

1. Login to admin: `http://localhost:5173/admin/login`
   - Username: `admin`
   - Password: `changeme123`

2. Go to "Manage Inventory"

3. Add a car:
   - Make: Toyota
   - Model: Camry
   - Year: 2022
   - Price: $28,500
   - Status: **available** (important!)
   - Add an image (optional)

4. Refresh the feed: `curl http://localhost:8080/api/feeds/facebook-marketplace`

5. Verify your car appears:
   - Look for `<item>` with your car details
   - Check `<title>`, `<price>`, `<image_link>`

### Add Image to Feed

If you want to test image inclusion:
1. Edit the car you created
2. Upload an image
3. Refresh the feed
4. Look for `<g:image_link>` in the XML

---

## Test 3: Manager Login & Dashboard

### Access Manager Login

1. Visit: `http://localhost:5173/manager/login`

2. You should see:
   - Manager Portal heading
   - Username field
   - Password field
   - Sign In button

### Login

**Credentials:**
- Username: `manager`
- Password: `ChangeMeInProduction123!`

Enter these and click "Sign In"

You should be redirected to the Manager Dashboard.

### Manager Dashboard Tabs

#### Tab 1: Inventory

**Test Adding a Car:**
1. Click "+ Add Vehicle" button
2. Fill in:
   - Make: Honda
   - Model: Civic
   - Year: 2021
   - Price: 22500
   - Other optional fields
3. Click "Add Vehicle"
4. Car should appear in the table

**Test Editing a Car:**
1. Click "Edit" on any car
2. Change a field (e.g., price)
3. Click "Save Changes"
4. Change should appear immediately

**Test Deleting a Car:**
1. Click "Delete" on any car
2. Confirm deletion
3. Car should disappear from table

#### Tab 2: Contacts

Shows all form submissions from the public contact form.

**To test:**
1. Open a new tab to `http://localhost:5173/contact`
2. Fill in and submit the contact form
3. Go back to manager dashboard
4. Switch to Contacts tab
5. Your submission should appear

#### Tab 3: Settings

Update store hours, phone, email, address.

**To test:**
1. Scroll through the form
2. Edit a field (e.g., phone number)
3. Click "Save Settings"
4. Should see "Settings saved successfully"
5. Refresh page
6. Settings should persist

### Test Logout

1. Click "Sign Out" button (top right)
2. You should be redirected to `/manager/login`
3. Token should be cleared from localStorage
4. Trying to access `/manager` should redirect to login

### Test Permission Errors

In browser console:
1. Go to Manager Dashboard (logged in)
2. Press F12 to open Dev Tools
3. Go to Application tab → Local Storage
4. Copy the `manager_token` value
5. Note that it contains `"role":"MANAGER"`

The JWT prevents manager access to:
- Admin panel (`/admin`)
- System configuration
- User management

---

## Test 4: Financing Page

### Access the Page

Visit: `http://localhost:5173/financing`

### Visual Inspection

Verify all sections load:
- ✅ Hero banner with headline and buttons
- ✅ "Why Finance With Us" section (3 benefits)
- ✅ Payment Estimator calculator
- ✅ Financing Partners section
- ✅ Loan Terms FAQ (expandable)
- ✅ Call-to-Action section

### Test Payment Calculator

1. In the "Payment Estimator" section:
   - Vehicle Price: 30000
   - Down Payment: 5000
   - Interest Rate: 5.9
   - Loan Term: 60 (months)

2. Check the output:
   - Loan amount: $25,000 (30000 - 5000)
   - Est. Monthly Payment: $472.27 (approx)
   - Total paid: $28,336.40 (approx)

3. Change loan term to 36 months
   - Monthly payment should increase (~$736)
   - Total paid should decrease (~$26,500)

4. Change interest rate to 7.5
   - Monthly payment should increase

### Test Expandable Sections

1. Click on "APR (Annual Percentage Rate)"
   - Should expand to show explanation
2. Click again
   - Should collapse
3. Test other terms (Down Payment, Credit Score Impact, etc.)

### Test Buttons

- Click "Call to Apply" button
  - Should open phone app (or show tel: protocol)
  - Number should be dealership phone
  
- Click "Send Us a Message"
  - Should navigate to `/contact` page

- Click "Apply Online"
  - Should navigate to `/contact` page

### Mobile Responsiveness

1. Right-click → "Inspect" (or F12)
2. Click device toolbar (responsive mode)
3. Test on:
   - iPhone 12 (375px)
   - iPad (768px)
   - Desktop (1920px)

Verify:
- Text is readable on all sizes
- Buttons don't overlap
- Calculator inputs are accessible
- Sections stack properly on mobile

---

## Automated Testing (Optional)

### Run Backend Tests

```bash
cd backend
mvn test
```

### Run Frontend Tests

```bash
cd frontend
npm test
```

---

## Troubleshooting

### Email not sending?

**Check:**
1. Is `MAIL_PASSWORD` environment variable set?
   ```bash
   echo $MAIL_PASSWORD  # on Linux/Mac
   echo %MAIL_PASSWORD%  # on Windows
   ```

2. Is Gmail 2FA enabled? Must use app password, not regular password.

3. Check backend logs:
   ```
   Failed to send test email: Could not connect to SMTP server...
   ```

4. Test with telnet (to verify SMTP access):
   ```bash
   telnet smtp.gmail.com 587
   ```

### Feed returns no cars?

**Check:**
1. Are there cars in the database?
   - Admin panel → Manage Inventory
   - Should show at least one car

2. Is the car status "available"?
   - Only available cars show in feed
   - Edit car → change status to "available"

3. Refresh feed:
   ```bash
   curl http://localhost:8080/api/feeds/facebook-marketplace
   ```

### Manager login fails?

**Check:**
1. Correct credentials?
   - Username: `manager`
   - Password: `ChangeMeInProduction123!`

2. Backend running?
   - Should see startup message
   - Port 8080 in use?

3. Check backend logs for errors:
   ```
   401 Unauthorized: Invalid credentials
   ```

### Financing page doesn't load?

**Check:**
1. Frontend running?
   - `npm run dev` in frontend directory
   - Should see "ready in XXXms"

2. Is Vue Router configured?
   - Check `/frontend/src/router/index.js`
   - Financing route should be there

3. Check browser console (F12) for errors:
   - Network tab
   - Console tab
   - Should show page loaded

---

## Performance Checklist

After all tests pass:

- [ ] Backend starts in < 10 seconds
- [ ] Frontend loads in < 5 seconds  
- [ ] Email sent within 30 seconds
- [ ] Feed generates in < 2 seconds
- [ ] Manager login redirects in < 2 seconds
- [ ] Car CRUD operations complete in < 2 seconds
- [ ] Financing page calculator responds immediately (no lag)

---

## Success Criteria

All 4 features are working when:

1. **Email:** Test email arrives in your inbox
2. **Facebook Feed:** Valid XML returns with all available cars
3. **Manager System:** Can login, add/edit/delete cars, view contacts
4. **Financing Page:** Loads completely, calculator works, all links function

---

## Next Steps

After successful testing:

1. **Customize for client:**
   - Edit FinancingView.vue for client branding
   - Update manager credentials
   - Configure email settings

2. **Deploy to production:**
   - Use HTTPS
   - Set environment variables securely
   - Enable database backups
   - Register Facebook feed

3. **Monitor:**
   - Check email delivery logs
   - Monitor Facebook feed updates
   - Review contact submissions
   - Track manager activity logs (if needed)
