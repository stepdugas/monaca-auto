# KBB Integration Guide

Kelley Blue Book (KBB) value display and refresh guide for your dealership website.

---

## Table of Contents

1. [What is KBB?](#what-is-kbb)
2. [How KBB Values Work](#how-kbb-values-work)
3. [Setting Up KBB](#setting-up-kbb)
4. [Displaying KBB on Your Site](#displaying-kbb)
5. [Refreshing KBB Values](#refreshing-kbb)
6. [Troubleshooting](#troubleshooting)

---

## What is KBB?

**Kelley Blue Book (KBB)** provides fair market values for vehicles based on:
- Make, model, year
- Condition (new, used, certified)
- Mileage
- VIN (optional, for more accuracy)

Displaying KBB values on your site:
- ✅ Builds customer trust (shows fair pricing)
- ✅ Helps with SEO (price comparison keywords)
- ✅ Differentiates your dealership (transparency)
- ✅ Facilitates price negotiation

---

## How KBB Values Work

### Data Flow

```
Your Dealership
    ↓
[Vehicle Entry with VIN]
    ↓
KBB Database
    ↓
[Fair Market Value]
    ↓
Your Website
    ↓
[Customer sees: "KBB Value: $X,XXX"]
```

### Supported Information

The dealership template fetches KBB values using:

1. **VIN** (17-character vehicle ID number) — most accurate
2. **Make/Model/Year** — if VIN not available (less accurate)

---

## Setting Up KBB

### Step 1: Verify VIN Entry

For each vehicle in your inventory, ensure you have:

- ✅ **VIN** (required for accurate KBB lookup)
- ✅ **Make** (e.g., Toyota)
- ✅ **Model** (e.g., Camry)
- ✅ **Year** (e.g., 2023)
- ✅ **Mileage** (improves accuracy)
- ✅ **Condition** (New, Used, or Certified)

**Example:**
```
VIN: 4T1C1F1M4PU123456
Make: Toyota
Model: Camry
Year: 2023
Mileage: 45,000 miles
Condition: Used
```

### Step 2: Configure KBB Client

The template uses **VehicleDatabases.com** free API for KBB lookups.

**No API key required!** The integration works out-of-the-box.

If you want a different KBB provider (e.g., NADA Guides):
1. Edit `backend/src/main/java/com/dealership/api/service/KBBClient.java`
2. Update the API endpoint and response parsing
3. Test thoroughly

---

## Displaying KBB on Your Site

### Frontend Display

When you view a vehicle detail page, you'll see:

```
┌─────────────────────────────────────────┐
│ 2023 Toyota Camry                       │
│ Asking Price: $25,999                   │
├─────────────────────────────────────────┤
│ 📊 Kelley Blue Book Value: $26,500      │
│    ✓ Your Price is 2% Below KBB!        │
│    [🔄 Refresh KBB] (admin only)        │
└─────────────────────────────────────────┘
```

### What Customers See

- **KBB Value** — Official fair market value
- **Price Comparison** — How your asking price compares
- **Green badge** if below KBB (good deal!)
- **Last updated** — When the KBB value was fetched

### Admin Controls

If you're logged in as an admin:
- **"Refresh KBB"** button appears on every vehicle detail page
- Click to fetch the latest KBB value
- Shows loading spinner while refreshing
- Displays success/error message

---

## Refreshing KBB Values

### Manual Refresh (Admin Only)

**Via Web Interface:**

1. Log in to admin dashboard
2. Browse to any vehicle's detail page
3. Click the **"Refresh KBB"** button
4. Wait for the update (takes 1-5 seconds)
5. See updated value and timestamp

**Via API:**

```bash
# Requires admin JWT token
curl -X POST https://yourdealership.com/api/inventory/12345/refresh-kbb \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json"
```

**Response:**
```json
{
  "id": 12345,
  "year": 2023,
  "make": "Toyota",
  "model": "Camry",
  "kbbValue": 26500.00,
  "kbbLastUpdated": "2024-03-23T14:32:15Z",
  ...
}
```

### Batch Refresh (All Vehicles)

For advanced users, refresh all KBB values in batch:

```bash
# Run from backend server
java -cp "target/classes:target/dependency/*" \
  com.dealership.api.service.CarService \
  --refresh-all-kbb
```

Or via Spring REST endpoint:
```bash
curl -X POST https://yourdealership.com/api/admin/refresh-all-kbb \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Automatic Refresh (Optional)

For production deployments, you can schedule automatic KBB refreshes:

**Edit `application.properties`:**
```properties
# Refresh KBB values daily at 2 AM
app.kbb.auto-refresh.enabled=true
app.kbb.auto-refresh.cron=0 0 2 * * *
```

Then add a scheduled task to your backend:
```java
@Scheduled(cron = "${app.kbb.auto-refresh.cron}")
public void refreshAllKBBValues() {
    valuationService.refreshAllKBBValuations();
}
```

---

## Troubleshooting

### KBB Value Not Showing

**Problem:** "KBB Value" section is missing from vehicle detail page

**Cause:** Vehicle doesn't have a KBB value yet

**Solution:**
1. Ensure vehicle has a **valid VIN** (17 characters)
2. Verify VIN format is correct (no typos)
3. Click **"Refresh KBB"** button as admin
4. Wait 5-10 seconds
5. Refresh page

### Refresh Returns "VIN Invalid"

**Problem:** Error message: "Invalid VIN" or "VIN not supported"

**Cause:** 
- VIN is missing
- VIN is incorrect format (must be 17 chars)
- VIN not recognized by KBB database

**Solution:**
1. **Double-check VIN:** Count characters (must be exactly 17)
2. **Verify VIN is correct:** Check vehicle title/registration
3. **Try alternative:** If VIN is invalid, KBB lookup fails silently (no value shown)
4. **For new vehicles:** KBB may not have data yet; try again later

### Refresh Times Out

**Problem:** "Refresh KBB" button shows "Refreshing..." forever

**Cause:** 
- API server is slow/unresponsive
- Network issue
- KBB API is down

**Solution:**
1. Check network connection
2. Verify backend is running: `curl http://localhost:8080/api/inventory`
3. Wait a few minutes and try again
4. Check KBB API status (external service)

### Price Comparison Shows Wrong Values

**Problem:** KBB value or comparison is incorrect

**Cause:**
- Stale data (KBB updated, your site didn't refresh)
- Incorrect vehicle specs (wrong year/mileage)
- API returned outdated value

**Solution:**
1. **Force refresh:** Click "Refresh KBB" as admin
2. **Verify vehicle details:** Year, mileage, condition
3. **Check KBB directly:** Visit [kbb.com](https://www.kbb.com) and lookup the VIN
4. **Report issue:** If persistent, contact support

---

## Advanced Configuration

### Custom KBB Provider

To switch from VehicleDatabases.com to another provider:

1. **Identify new API:** (e.g., NADA Guides, Edmunds)
2. **Update KBBClient.java:**
   - Change API endpoint
   - Update request headers/auth
   - Parse new response format
3. **Test thoroughly:**
   - Sample VINs with known values
   - Error handling (invalid VINs)
   - Rate limits
4. **Deploy and monitor**

### Pricing Strategy

Use KBB values strategically:

- **Below KBB:** Highlight as "Great Deal!" 🟢
- **At KBB:** "Fair Price" 🟡
- **Above KBB:** "Premium Condition" (explain why) 🔵

Example in `CarDetailView.vue`:
```javascript
if (difference > 1000) {
  label = `✓ Excellent Value! ${percentage}% Below KBB`
} else if (difference > 0) {
  label = `✓ Below KBB by ${percentage}%`
} else {
  label = `Above KBB (Premium condition)`
}
```

### Hiding KBB (Optional)

If you prefer not to show KBB:

1. **Frontend:** Remove KBB display section from `CarDetailView.vue`
2. **Backend:** KBB values still fetch/store (optional to use)
3. **Still shown in:** Admin dashboard (for your reference)

---

## FAQ

**Q: Is KBB data real-time?**  
A: No. KBB updates periodically (daily/weekly). Our refresh gets the latest available value.

**Q: Do I need a KBB account?**  
A: No! The template uses a free third-party API that doesn't require authentication.

**Q: Can I guarantee a KBB value?**  
A: No. If VIN is invalid or vehicle is rare, KBB might not have a value. The field will be blank.

**Q: How often should I refresh?**  
A: At least weekly. More frequently if market prices fluctuate significantly.

**Q: Can customers trust the KBB value?**  
A: Yes! KBB is a trusted third-party valuation. Displaying it builds credibility.

**Q: What if my price is above KBB?**  
A: That's okay! Explain why (low mileage, excellent condition, rare model, etc.).

---

## Support

For technical issues:
1. Check vehicle VIN is entered correctly
2. Review error messages in browser console (`F12 → Console`)
3. Check backend logs: `tail -f backend.log`
4. See [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) for API details
5. Contact support with vehicle ID and error message

---

**Next:** Learn about [Facebook Integration](./FACEBOOK_INTEGRATION.md) or [Deployment](./DEPLOYMENT.md)
