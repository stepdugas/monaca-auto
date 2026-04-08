# Testing Checklist

Complete testing guide before showing to clients.

---

## Pre-Testing Setup

### Start Services

```bash
# Terminal 1 — Backend
cd backend
mvn spring-boot:run

# Terminal 2 — Frontend
cd frontend
npm run dev

# Terminal 3 — PostgreSQL (if not already running)
# macOS: brew services start postgresql
# Linux: sudo systemctl start postgresql
# Windows: Start PostgreSQL service
```

### Load Sample Data (Optional)

```bash
psql -U dealership_user -d dealership_db -f database/sample-data.sql
```

---

## 1. Frontend Functionality

### Home Page

- [ ] **Page loads** without console errors
- [ ] **Hero section** displays correctly with image
- [ ] **Featured inventory** section shows cars (if sample data loaded)
- [ ] **Hours section** displays correct business hours
- [ ] **Contact CTA** buttons are clickable
- [ ] **Testimonials section** loads and scrolls
- [ ] **Logo appears** in header
- [ ] **Navigation menu** works and highlights current page
- [ ] **Footer** displays correctly with business info, hours, social links

### Inventory Page

- [ ] **Page loads** without errors
- [ ] **All vehicles display** in grid layout
- [ ] **Car count message** shows correct number (e.g., "15 vehicles found")
- [ ] **Search bar** appears and accepts input
- [ ] **Filter panel** expands/collapses
- [ ] **Filter controls work:**
  - [ ] Search by make/model (type "Toyota")
  - [ ] Filter by year
  - [ ] Filter by min/max price
  - [ ] Filter by max mileage
  - [ ] Filter by condition
- [ ] **Filters update results** in real-time
- [ ] **"X cars found" message** updates when filtering
- [ ] **Clear filters button** resets all filters
- [ ] **Pagination works** (prev/next buttons, page indicator)
- [ ] **Car cards display:**
  - [ ] Image loads
  - [ ] Year/make/model displays
  - [ ] Price displays (formatted with $)
  - [ ] Mileage displays
  - [ ] Condition badge shows
  - [ ] Status badge shows (if pending/sold)

### Car Detail Page

- [ ] **Page loads** with correct vehicle info
- [ ] **Breadcrumb navigation** shows correct path
- [ ] **Main image gallery:**
  - [ ] Large image displays
  - [ ] Thumbnail images show below
  - [ ] Can click thumbnails to switch image
  - [ ] Images load from correct CDN
- [ ] **Vehicle title** displays (year, make, model, trim)
- [ ] **Asking price** displays and is formatted correctly
- [ ] **KBB section appears** (if KBB value exists):
  - [ ] KBB value displays
  - [ ] Price comparison shows correctly
  - [ ] "Below KBB" badge appears if applicable
  - [ ] Last updated timestamp shows
  - [ ] **"Refresh KBB" button** appears for admin users
- [ ] **Vehicle specs table** displays:
  - [ ] Year, make, model, trim
  - [ ] Mileage (with comma formatting)
  - [ ] Condition, transmission, engine
  - [ ] Drive train, colors, VIN
- [ ] **Description** displays (if filled in)
- [ ] **Features list** displays correctly
- [ ] **Contact form** appears on right side with fields:
  - [ ] Name field
  - [ ] Email field
  - [ ] Phone field
  - [ ] Message field
  - [ ] Submit button
- [ ] **Phone CTA** shows dealership number (clickable on mobile)
- [ ] **Responsive design** works on mobile (text stacks, images scale)

### Contact Page

- [ ] **Page loads** without errors
- [ ] **Contact form** displays with fields
- [ ] **Google Maps embed** shows dealership location (if configured)
- [ ] **Business hours** display
- [ ] **Phone number** displays and is clickable
- [ ] **Email address** displays
- [ ] **Social links** work (if configured)

---

## 2. Forms & Input

### Car Detail Contact Form

1. **Fill form:**
   ```
   Name: John Test
   Email: john@test.com
   Phone: (555) 123-4567
   Message: I'm interested in this car
   ```

2. **Submit:**
   - [ ] Form submits without page refresh
   - [ ] Loading spinner appears while submitting
   - [ ] Success message appears ("Message sent!")
   - [ ] Form clears after submission
   - [ ] No console errors

3. **Validation:**
   - [ ] Submit button disabled while submitting
   - [ ] Name field required (show error if empty)
   - [ ] Email field required (validate email format)
   - [ ] Submit again after success works

### Inventory Page Filters

1. **Search:**
   - [ ] Type "Toyota" → shows Toyota cars only
   - [ ] Type "Camry" → shows Camry models
   - [ ] Type "invalid" → shows "No vehicles matched"
   - [ ] Clear search → shows all cars again

2. **Price Filter:**
   - [ ] Set min=$20,000 → filters correctly
   - [ ] Set max=$30,000 → filters correctly
   - [ ] Set both → shows cars in range
   - [ ] Zero results shows message

3. **Multiple Filters:**
   - [ ] Make: Toyota + Max Price: $25,000 → shows filtered results
   - [ ] Reset filters → shows all again

---

## 3. KBB Integration (Admin Only)

### Login

1. **Admin login:**
   - [ ] Visit `/admin/login`
   - [ ] Enter username and password from `application.properties`
   - [ ] Click login
   - [ ] Redirected to admin dashboard
   - [ ] JWT token stored in localStorage

2. **Admin dashboard:**
   - [ ] Shows list of cars
   - [ ] No console errors
   - [ ] Logout button works

### KBB Refresh (on Car Detail Page)

1. **As admin user:**
   - [ ] Visit car detail page
   - [ ] KBB section visible
   - [ ] "Refresh KBB" button appears

2. **Click Refresh:**
   - [ ] Button shows loading spinner
   - [ ] After 2-5 seconds, button returns to normal
   - [ ] Success message appears
   - [ ] KBB value updates (if changed)
   - [ ] Last updated timestamp updates
   - [ ] No console errors

3. **Error handling:**
   - [ ] If VIN is invalid, error message appears
   - [ ] Error auto-hides after 3 seconds
   - [ ] Button remains clickable

---

## 4. Mobile Responsiveness

### Test on Mobile Device (or Dev Tools)

**Open DevTools:** F12 → Toggle device toolbar (Ctrl+Shift+M)

### Home Page

- [ ] **Logo and navigation** visible and clickable
- [ ] **Hero section** image scales properly
- [ ] **Text is readable** (not too small)
- [ ] **Buttons are large enough** to click (min 44px)
- [ ] **No horizontal scroll** needed

### Inventory Page

- [ ] **Search bar** accessible
- [ ] **Filter panel** collapses on mobile (toggle button works)
- [ ] **Car cards** display in single column
- [ ] **Images scale** to screen width
- [ ] **Pagination** buttons are large enough

### Car Detail Page

- [ ] **Image gallery** works on touch (tap to change images)
- [ ] **Contact form** fields stack properly
- [ ] **Text is readable** without pinching/zooming
- [ ] **No content cut off** on right edge

### Test Devices/Sizes

- [ ] **iPhone 12 (390x844)**
- [ ] **iPhone SE (375x667)**
- [ ] **iPad (768x1024)**
- [ ] **Android (360x800)**

---

## 5. API Integration

### Backend API Tests

**Use curl or Postman**

1. **List vehicles:**
   ```bash
   curl http://localhost:8080/api/inventory
   ```
   - [ ] Returns JSON array
   - [ ] Contains expected cars
   - [ ] Pagination fields present

2. **Get single car:**
   ```bash
   curl http://localhost:8080/api/inventory/1
   ```
   - [ ] Returns car with ID 1
   - [ ] All fields populated
   - [ ] Images array included

3. **Search/filter:**
   ```bash
   curl "http://localhost:8080/api/inventory?make=Toyota&maxPrice=30000"
   ```
   - [ ] Returns filtered results
   - [ ] Results match criteria

4. **Contact submission:**
   ```bash
   curl -X POST http://localhost:8080/api/contact \
     -H "Content-Type: application/json" \
     -d '{
       "name": "John Test",
       "email": "john@test.com",
       "phone": "(555) 123-4567",
       "message": "Test message",
       "carId": 1
     }'
   ```
   - [ ] Returns 201 (created)
   - [ ] Submission stored in database

5. **Admin login:**
   ```bash
   curl -X POST http://localhost:8080/api/admin/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"password"}'
   ```
   - [ ] Returns JWT token
   - [ ] Token is valid string

---

## 6. Facebook Feed

### XML Feed Validation

1. **Test feed URL:**
   ```bash
   curl http://localhost:8080/api/inventory/feeds/facebook-marketplace.xml
   ```
   - [ ] Returns valid XML (no parse errors)
   - [ ] Contains all vehicles
   - [ ] Each vehicle has required fields:
     - [ ] id
     - [ ] title
     - [ ] price
     - [ ] link
     - [ ] image_url
     - [ ] availability
     - [ ] condition

2. **Validate XML:**
   - [ ] Use online tool: [XMLLint](https://www.xmlvalidation.com/)
   - [ ] Feed passes validation
   - [ ] No warnings

---

## 7. Database

### Verify Data

1. **Connect to database:**
   ```bash
   psql -U dealership_user -d dealership_db
   ```

2. **Check vehicles:**
   ```sql
   SELECT COUNT(*) FROM cars;
   -- Should show 15 (if sample data loaded)
   
   SELECT * FROM cars LIMIT 1;
   -- Should show vehicle with all fields
   ```

3. **Check images:**
   ```sql
   SELECT COUNT(*) FROM car_images;
   -- Should show > 0
   
   SELECT * FROM car_images LIMIT 1;
   -- Should show image URL and primary flag
   ```

4. **Check contacts:**
   ```sql
   SELECT COUNT(*) FROM contact_submissions;
   -- Should increase after form submission
   ```

---

## 8. Browser Compatibility

### Test on Multiple Browsers

- [ ] **Chrome** (latest)
- [ ] **Firefox** (latest)
- [ ] **Safari** (latest)
- [ ] **Edge** (latest)

### For Each Browser

- [ ] **No console errors** (F12 → Console tab)
- [ ] **All images load**
- [ ] **Forms work**
- [ ] **Navigation works**
- [ ] **Colors display correctly**
- [ ] **Responsive design works**

---

## 9. Performance

### Frontend Performance

1. **Load time:**
   - [ ] Home page loads in < 3 seconds
   - [ ] Inventory page loads in < 3 seconds
   - [ ] Car detail page loads in < 2 seconds

2. **Lighthouse audit:**
   ```bash
   # In Chrome DevTools
   Ctrl+Shift+P → "Lighthouse" → Generate report
   ```
   - [ ] Performance > 80
   - [ ] Accessibility > 80
   - [ ] Best Practices > 80
   - [ ] SEO > 80

### Backend Performance

1. **API response times:**
   - [ ] List vehicles: < 500ms
   - [ ] Get single car: < 200ms
   - [ ] Contact submission: < 1000ms

2. **Database queries:**
   - [ ] Enable logging in `application.properties`
   - [ ] Verify no slow queries (> 1000ms)

---

## 10. Security

- [ ] **HTTPS enabled** on production (or test with self-signed cert)
- [ ] **No sensitive data** in console logs
- [ ] **JWT token** not exposed in URLs
- [ ] **Database password** not in frontend code
- [ ] **Admin credentials** not hardcoded
- [ ] **CORS** properly configured (only allowed origins)

---

## 11. Accessibility

- [ ] **Keyboard navigation:** Tab through page, all buttons reachable
- [ ] **Alt text:** All images have descriptive alt text
- [ ] **Color contrast:** Text readable (use [WCAG Checker](https://www.tota11y.org/))
- [ ] **Form labels:** All inputs have labels
- [ ] **Error messages:** Clear and accessible
- [ ] **Focus indicators:** Visible when tabbing through

---

## 12. Content & Customization

- [ ] **Business name** displays everywhere (header, footer, title)
- [ ] **Phone number** displays and is clickable
- [ ] **Email** displays correctly
- [ ] **Logo** displays in header and footer
- [ ] **Hours** are correct for all days
- [ ] **Address** displays correctly
- [ ] **Social media links** point to correct profiles
- [ ] **Brand colors** applied to buttons, links, badges
- [ ] **No placeholder text** visible (`[PLACEHOLDER]` not found)
- [ ] **No hardcoded company names** (all replaced with client's name)

---

## 13. Before Going Live

### Final Checks

- [ ] **All cars have images** (no broken image icons)
- [ ] **All cars have prices** (not "Call for Price" unless intended)
- [ ] **VINs entered** for KBB lookup
- [ ] **Descriptions filled in** (not generic)
- [ ] **Features listed** for at least some cars
- [ ] **Admin password changed** from default
- [ ] **Database backup** created
- [ ] **SSL certificate** installed (if production)
- [ ] **Email notifications** configured (if needed)
- [ ] **Analytics** set up (Google Analytics, etc.)
- [ ] **Domain** pointing to production server

---

## Testing Report Template

**Project:** [Client Name] Dealership  
**Date:** [Date]  
**Tester:** [Name]  
**Environment:** Local / Staging / Production  
**Outcome:** ✅ Pass / ⚠️ Pass with Issues / ❌ Fail

### Results Summary

| Category | Status | Notes |
|----------|--------|-------|
| Frontend Functionality | ✅ | All pages load, no console errors |
| Forms & Input | ✅ | Contact form works, validation works |
| KBB Integration | ✅ | KBB values display, refresh button works |
| Mobile Responsiveness | ✅ | All pages responsive, touch-friendly |
| API Integration | ✅ | All endpoints respond correctly |
| Database | ✅ | Sample data loaded, queries work |
| Browser Compatibility | ✅ | Works on Chrome, Firefox, Safari, Edge |
| Security | ✅ | HTTPS, auth tokens, no exposed credentials |

### Issues Found

None.

### Approval

- [ ] Tester approves site is ready for launch
- [ ] Client approves functionality
- [ ] Client approves design/branding

---

## Commands for Quick Testing

```bash
# Build and test everything
cd frontend && npm run build && npm run lint
cd ../backend && mvn clean package && mvn test

# Run locally
# Terminal 1:
cd backend && mvn spring-boot:run

# Terminal 2:
cd frontend && npm run dev

# Check API endpoints
curl http://localhost:8080/api/inventory | jq
curl http://localhost:8080/api/inventory/1 | jq
curl http://localhost:8080/api/inventory/feeds/facebook-marketplace.xml

# Test contact form
curl -X POST http://localhost:8080/api/contact \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@test.com","phone":"555-1234","message":"Test"}'

# Check database
psql -U dealership_user -d dealership_db -c "SELECT COUNT(*) FROM cars;"
psql -U dealership_user -d dealership_db -c "SELECT COUNT(*) FROM contact_submissions;"
```

---

## When to Stop Testing

✅ **Ready to launch when:**
- All checkboxes checked
- No critical issues
- Client approves
- Performance acceptable
- Security verified
- Mobile responsive
- No console errors

❌ **Not ready when:**
- Any critical functionality broken
- Data not displaying correctly
- Forms not submitting
- Performance poor (> 5 seconds load)
- Security issues found
- Client not approved

---

**Next:** See [DEPLOYMENT.md](./DEPLOYMENT.md) for going live!
