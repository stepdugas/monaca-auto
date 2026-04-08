# Facebook Marketplace Integration Guide

Step-by-step instructions to integrate your dealership with Facebook Marketplace using the automated vehicle catalog feed.

---

## Table of Contents

1. [What is Facebook Marketplace for Vehicles?](#what-is-facebook-marketplace)
2. [Prerequisites](#prerequisites)
3. [Facebook Business Setup](#facebook-business-setup)
4. [Creating a Vehicle Catalog](#creating-vehicle-catalog)
5. [Connecting the Feed](#connecting-the-feed)
6. [Monitoring & Troubleshooting](#troubleshooting)

---

## What is Facebook Marketplace for Vehicles?

Facebook Marketplace allows you to list vehicles directly on Facebook and Instagram. The dealership template **automatically generates** an XML feed of your vehicles that Facebook can ingest, keeping your listings synchronized.

**Benefits:**
- ✅ Reach millions of Facebook & Instagram users
- ✅ Automatic syncing as you add/update cars
- ✅ No manual data entry on Facebook
- ✅ Higher visibility in local searches

---

## Prerequisites

- [ ] Facebook Business Account (create at [business.facebook.com](https://business.facebook.com))
- [ ] Facebook Page for your dealership
- [ ] Admin access to your dealership template (backend running)
- [ ] Dealership website deployed or accessible via URL

---

## Facebook Business Setup

### Step 1: Create/Verify Business Account

1. Go to [business.facebook.com](https://business.facebook.com)
2. Click **"Create Account"** (or log in if you already have one)
3. Enter your business details:
   - **Business Name:** [Your Dealership Name]
   - **Business Email:** [Your Business Email]
   - **Business Address:** [Your Dealership Address]

### Step 2: Create a Facebook Page (if you don't have one)

1. From Business Manager, go to **Pages** → **Create Page**
2. Select **Business** as the category
3. Fill in:
   - **Page Name:** [Your Dealership Name]
   - **Category:** Automotive > Auto Dealer
   - **About:** Brief description of your dealership
4. Add your dealership logo as the page image
5. Click **Create Page**

### Step 3: Claim/Link Your Website

1. Go to your Facebook Page
2. Click **Settings** → **Instagram Linked Accounts** (or **Business Settings**)
3. Add your website URL (e.g., `https://yourdealership.com`)
4. Verify ownership (Facebook will provide verification methods)

---

## Creating a Vehicle Catalog

### Step 1: Access Catalog Manager

1. From Business Manager, navigate to **Catalogs**
2. Click **Create Catalog**
3. Select **Vehicles** as the catalog type

**Name your catalog:** "YourDealership Inventory"

### Step 2: Configure Catalog Settings

1. Fill in your dealership info:
   - **Business Name:** [Your Dealership Name]
   - **Business Phone:** [Your Phone]
   - **Website:** [Your Website URL]
   - **Address:** [Your Address]

2. Set **Data Source** to:
   - **External Data Feed** (we'll provide the URL)
   - **Feed Type:** XML

### Step 3: Add Your Feed URL

1. In Catalog Manager, go to **Data Sources** → **External Data Feed**
2. Enter your feed URL:

```
https://yourdealership.com/api/inventory/feeds/facebook-marketplace.xml
```

⚠️ **Replace `yourdealership.com` with your actual domain**

3. Set **Schedule:**
   - **Frequency:** Daily
   - **Time:** Off-peak hours (e.g., 2:00 AM)

4. Click **Add Data Source**

### Step 4: Map Feed Columns (if needed)

Facebook may ask you to map XML elements to their vehicle fields. The feed includes:

```
<product>
  <id>12345</id>
  <title>2023 Toyota Camry</title>
  <description>...</description>
  <price>$25,999</price>
  <link>https://yourdealership.com/inventory/12345</link>
  <image_url>...</image_url>
  <availability>in stock</availability>
  <condition>used</condition>
  <!-- Additional fields: mileage, vin, engine, etc. -->
</product>
```

If prompted, map each field to Facebook's vehicle attributes (most should auto-map).

---

## Connecting the Feed

### Step 1: Create a Vehicle Ad

1. From your Facebook Page, click **Create** → **Vehicle Ad**
2. Select your Vehicle Catalog (created above)
3. Choose **Automatic product selection** or hand-pick vehicles

### Step 2: Set Up Ad Details

- **Title:** "Browse [Your Dealership] Inventory"
- **Description:** "View all available vehicles. Click to see details and contact us."
- **Call-to-Action:** "Shop Now" or "Learn More"
- **Landing Page:** Link to your dealership's inventory page

### Step 3: Configure Targeting

- **Audience:** Local (e.g., 25+ miles from your location)
- **Age:** 18+
- **Interests:** Auto enthusiasts, car shoppers, etc.

### Step 4: Set Budget & Schedule

- **Daily Budget:** $10-50 (adjust based on goals)
- **Schedule:** Run continuously or specific hours
- **Start/End Dates:** Leave open-ended or set campaign duration

### Step 5: Review & Publish

Review all settings and click **Publish** to go live.

---

## Testing the Feed

### Step 1: Verify Feed URL

Before adding to Facebook, test that the feed is accessible:

```bash
curl https://yourdealership.com/api/inventory/feeds/facebook-marketplace.xml
```

You should see XML output with your vehicles listed.

### Step 2: Validate XML

Use an online XML validator to ensure the feed is well-formed:
- [XMLLint](https://www.xmlvalidation.com/)
- [W3C XML Validator](https://www.w3schools.com/xml/xml_validator.asp)

### Step 3: Check Facebook Catalog Diagnostics

1. Go to **Catalog Manager** → **Diagnostics**
2. Look for:
   - ✅ **Feed Status:** "Healthy"
   - ✅ **Last Update:** Recent timestamp
   - ⚠️ **Warnings/Errors:** Review any issues
   - 📊 **Product Count:** Should match your inventory

---

## Monitoring & Troubleshooting

### Common Issues

#### Feed Not Updating

**Problem:** Facebook shows old vehicle data

**Solution:**
1. Verify feed URL is correct and accessible
2. Check feed frequency (should update daily)
3. Manually trigger a refresh in Catalog Manager:
   - **Data Sources** → **Your Feed** → **Manual Refresh**
4. Wait 5-10 minutes for update to appear

#### Products Not Appearing

**Problem:** Vehicles not showing in Marketplace

**Solution:**
1. Ensure vehicles have status = "available"
2. Check that price is set (required field)
3. Verify at least one image is attached
4. Review diagnostics for warnings:
   - Missing fields
   - Invalid data (e.g., non-numeric mileage)

#### Slow Uploads

**Problem:** Feed takes too long to update

**Solution:**
1. Reduce image file sizes (optimize before upload)
2. Check network/server performance
3. Consider scheduled updates during low-traffic hours

### Dashboard Tips

**Monitor Performance:**
1. Go to **Catalogs** → **Your Catalog**
2. Check **Insights** tab for:
   - Impressions
   - Clicks
   - Click-through rate
   - Conversion actions

**Optimize Listings:**
- Add high-quality images (5+ per vehicle recommended)
- Write detailed descriptions (mileage, features, condition)
- Set competitive prices
- Update status to "sold" when vehicles are sold
- Add fresh inventory regularly

---

## Advanced Setup (Optional)

### Dynamic Pricing

To automatically adjust prices based on KBB values:

1. Ensure KBB values are set in your backend
2. Feed template uses asking price (not KBB)
3. Customize feed to use `kbbValue` instead:
   - Edit `FacebookFeedGenerator.java`
   - Change `<price>` element to use `car.getKbbValue()`

### Multi-Location Dealerships

If you have multiple locations:

1. Create separate catalogs per location
2. Create separate feeds (if backend supports filtering by location)
3. Set up separate ad campaigns

### Lead Integration

To capture leads from Facebook:

1. Add **Lead Form** to your ad
2. Collect: Name, Email, Phone, Vehicle Interest
3. Export leads from Facebook to CRM (or integrate with backend)

---

## Deactivating/Updating

### To Pause Listings

1. **Catalog Manager** → Your Catalog
2. Click **Pause** (vehicles remain indexed)
3. Resume anytime by clicking **Activate**

### To Remove a Vehicle

1. In your backend, set car status to **"sold"**
2. Feed automatically marks as unavailable on next refresh
3. Facebook removes from Marketplace within hours

### To Delete Entire Catalog

1. **Catalog Manager** → Your Catalog
2. Click **Delete Catalog**
3. Facebook removes all listings within 24 hours

---

## FAQ

**Q: Will my feed update in real-time?**  
A: No, Facebook checks your feed daily on a schedule. Updates appear within 24 hours.

**Q: Can I edit vehicle details in Facebook?**  
A: No—Facebook pulls from your feed. Edit in your dealership backend; changes sync on next feed update.

**Q: What if a vehicle sells?**  
A: Set status to "sold" in your backend. It will be marked unavailable on next feed update and removed from Marketplace.

**Q: Do I need to pay for this?**  
A: The feed is free. You only pay for Facebook ads if you run paid campaigns to promote listings.

**Q: Can I use the feed with Instagram?**  
A: Yes! Facebook Marketplace listings automatically appear on Instagram as well.

---

## Support

For issues:
1. Check **Catalog Manager → Diagnostics** for error details
2. Review **[README.md](./README.md)** troubleshooting section
3. Contact Facebook Business Support: [facebook.com/business/help](https://www.facebook.com/business/help/)
4. Review our [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) for feed endpoint details

---

**Next:** See [DEPLOYMENT.md](./DEPLOYMENT.md) to go live!
