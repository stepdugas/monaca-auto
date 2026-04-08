# API Documentation

Complete reference for all REST API endpoints in the dealership template.

---

## Table of Contents

1. [Overview](#overview)
2. [Authentication](#authentication)
3. [Inventory Endpoints](#inventory-endpoints)
4. [Contact Endpoints](#contact-endpoints)
5. [Admin Endpoints](#admin-endpoints)
6. [Feed Endpoints](#feed-endpoints)
7. [Error Handling](#error-handling)

---

## Overview

**Base URL:** `https://yourdealership.com/api` (or `http://localhost:8080/api` locally)

**Authentication:** JWT Bearer token (required for admin endpoints)

**Response Format:** JSON (except XML feeds)

**Rate Limiting:** 100 requests/minute per IP

---

## Authentication

### Admin Login

**Endpoint:** `POST /api/admin/login`

**Description:** Authenticate as an admin and receive a JWT token.

**Request:**
```json
{
  "username": "admin",
  "password": "your_password"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400
}
```

**Error (401):**
```json
{
  "error": "Invalid credentials"
}
```

### Using JWT Token

Include in all admin requests:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json
```

---

## Inventory Endpoints

### List Vehicles (Public)

**Endpoint:** `GET /api/inventory`

**Description:** Fetch paginated list of available vehicles with optional filters.

**Query Parameters:**
```
page=0                  # Page number (0-indexed)
size=12                 # Items per page
search=toyota           # Search term (make, model, VIN)
make=Toyota             # Filter by make
model=Camry             # Filter by model
year=2023               # Filter by year
minPrice=10000          # Minimum price
maxPrice=50000          # Maximum price
maxMileage=100000       # Maximum mileage
condition=Used          # Filter by condition (New, Used, Certified)
sort=price,desc         # Sort field and direction
```

**Example Request:**
```bash
GET /api/inventory?make=Toyota&maxPrice=30000&page=0&size=12
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "year": 2023,
      "make": "Toyota",
      "model": "Camry",
      "trim": "SE",
      "price": 25999,
      "mileage": 45000,
      "vin": "4T1C1F1M4PU123456",
      "condition": "Used",
      "transmission": "Automatic",
      "engine": "2.5L 4-cyl",
      "driveTrain": "FWD",
      "exteriorColor": "Black",
      "interiorColor": "Tan",
      "status": "available",
      "description": "Well-maintained sedan with excellent service history.",
      "features": ["Backup Camera", "Apple CarPlay", "Sunroof"],
      "kbbValue": 26500,
      "kbbLastUpdated": "2024-03-23T14:32:15Z",
      "images": [
        {
          "id": 101,
          "imageUrl": "https://cdn.example.com/image1.jpg",
          "isPrimary": true,
          "sortOrder": 1
        }
      ],
      "createdAt": "2024-03-20T10:00:00Z",
      "updatedAt": "2024-03-23T14:32:15Z"
    }
  ],
  "totalElements": 42,
  "totalPages": 4,
  "currentPage": 0,
  "pageSize": 12
}
```

### Get Single Vehicle (Public)

**Endpoint:** `GET /api/inventory/{id}`

**Description:** Fetch detailed information about a specific vehicle.

**Path Parameter:**
- `id` — Vehicle ID (integer)

**Example Request:**
```bash
GET /api/inventory/1
```

**Response (200 OK):**
```json
{
  "id": 1,
  "year": 2023,
  "make": "Toyota",
  "model": "Camry",
  "trim": "SE",
  "price": 25999,
  "mileage": 45000,
  "vin": "4T1C1F1M4PU123456",
  "condition": "Used",
  "transmission": "Automatic",
  "engine": "2.5L 4-cyl",
  "driveTrain": "FWD",
  "exteriorColor": "Black",
  "interiorColor": "Tan",
  "status": "available",
  "description": "Well-maintained sedan with excellent service history.",
  "features": ["Backup Camera", "Apple CarPlay", "Sunroof"],
  "kbbValue": 26500,
  "kbbLastUpdated": "2024-03-23T14:32:15Z",
  "images": [
    {
      "id": 101,
      "imageUrl": "https://cdn.example.com/image1.jpg",
      "isPrimary": true,
      "sortOrder": 1
    }
  ],
  "createdAt": "2024-03-20T10:00:00Z",
  "updatedAt": "2024-03-23T14:32:15Z"
}
```

**Error (404):**
```json
{
  "error": "Vehicle not found"
}
```

### Create Vehicle (Admin)

**Endpoint:** `POST /api/inventory`

**Description:** Create a new vehicle (admin only).

**Request:**
```json
{
  "year": 2023,
  "make": "Toyota",
  "model": "Camry",
  "trim": "SE",
  "price": 25999,
  "mileage": 45000,
  "vin": "4T1C1F1M4PU123456",
  "condition": "Used",
  "transmission": "Automatic",
  "engine": "2.5L 4-cyl",
  "driveTrain": "FWD",
  "exteriorColor": "Black",
  "interiorColor": "Tan",
  "status": "available",
  "description": "Well-maintained sedan with excellent service history.",
  "features": ["Backup Camera", "Apple CarPlay", "Sunroof"]
}
```

**Response (201 Created):**
```json
{
  "id": 42,
  "year": 2023,
  "make": "Toyota",
  "model": "Camry",
  ...
  "createdAt": "2024-03-23T15:00:00Z"
}
```

### Update Vehicle (Admin)

**Endpoint:** `PUT /api/inventory/{id}`

**Description:** Update an existing vehicle.

**Request:**
```json
{
  "price": 24999,
  "mileage": 46000,
  "status": "pending"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "price": 24999,
  "mileage": 46000,
  "status": "pending",
  ...
  "updatedAt": "2024-03-23T15:15:00Z"
}
```

### Delete Vehicle (Admin)

**Endpoint:** `DELETE /api/inventory/{id}`

**Description:** Delete a vehicle.

**Response (204 No Content):**
```
(empty body)
```

### Refresh KBB Value (Admin)

**Endpoint:** `POST /api/inventory/{id}/refresh-kbb`

**Description:** Fetch and update KBB value for a specific vehicle.

**Response (200 OK):**
```json
{
  "id": 1,
  "year": 2023,
  "make": "Toyota",
  "model": "Camry",
  "kbbValue": 26500,
  "kbbLastUpdated": "2024-03-23T15:30:00Z",
  ...
}
```

**Error (400):**
```json
{
  "error": "Cannot refresh KBB: Vehicle has no VIN"
}
```

---

## Contact Endpoints

### Submit Contact Form (Public)

**Endpoint:** `POST /api/contact`

**Description:** Submit a contact/inquiry form (public access).

**Request:**
```json
{
  "name": "John Smith",
  "email": "john@example.com",
  "phone": "(555) 123-4567",
  "message": "I'm interested in the 2023 Camry",
  "carId": 1
}
```

**Response (201 Created):**
```json
{
  "id": 501,
  "name": "John Smith",
  "email": "john@example.com",
  "phone": "(555) 123-4567",
  "message": "I'm interested in the 2023 Camry",
  "carId": 1,
  "submittedAt": "2024-03-23T15:45:00Z"
}
```

**Error (400):**
```json
{
  "error": "Email is required"
}
```

### Get Contact Submissions (Admin)

**Endpoint:** `GET /api/admin/contacts`

**Description:** Fetch all contact form submissions (admin only).

**Response (200 OK):**
```json
[
  {
    "id": 501,
    "name": "John Smith",
    "email": "john@example.com",
    "phone": "(555) 123-4567",
    "message": "I'm interested in the 2023 Camry",
    "carId": 1,
    "submittedAt": "2024-03-23T15:45:00Z"
  },
  {
    "id": 502,
    "name": "Jane Doe",
    "email": "jane@example.com",
    "phone": "(555) 234-5678",
    "message": "What's the lowest price on the Ford F-150?",
    "carId": 5,
    "submittedAt": "2024-03-23T16:00:00Z"
  }
]
```

---

## Admin Endpoints

### Get Admin Dashboard Stats (Admin)

**Endpoint:** `GET /api/admin/stats`

**Description:** Get dashboard statistics (total vehicles, contacts, etc.).

**Response (200 OK):**
```json
{
  "totalVehicles": 42,
  "availableVehicles": 38,
  "soldVehicles": 4,
  "totalContacts": 156,
  "contactsThisWeek": 12,
  "averagePricePerCategory": {
    "SUV": 35000,
    "Sedan": 28000,
    "Truck": 32000
  }
}
```

---

## Feed Endpoints

### Facebook Marketplace Feed (Public)

**Endpoint:** `GET /api/inventory/feeds/facebook-marketplace.xml`

**Description:** Generate XML feed for Facebook Marketplace (public access).

**Content-Type:** `application/xml`

**Response (200 OK):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<rss version="2.0">
  <channel>
    <title>YourDealership Inventory</title>
    <link>https://yourdealership.com</link>
    <description>Available vehicles</description>
    <item>
      <id>1</id>
      <title>2023 Toyota Camry SE</title>
      <description>Well-maintained sedan. 45,000 miles. Backup Camera, Apple CarPlay.</description>
      <price currency="USD">25999</price>
      <link>https://yourdealership.com/inventory/1</link>
      <image_url>https://cdn.example.com/image1.jpg</image_url>
      <availability>in stock</availability>
      <condition>used</condition>
      <vin>4T1C1F1M4PU123456</vin>
      <mileage>45000</mileage>
    </item>
  </channel>
</rss>
```

**Caching:**
- XML is cached for 1 hour
- Add `?nocache=true` to force refresh
- Set `Cache-Control: max-age=3600` in response headers

---

## Error Handling

### Standard Error Response

All errors follow this format:

```json
{
  "error": "Error message",
  "status": 400,
  "timestamp": "2024-03-23T15:00:00Z"
}
```

### HTTP Status Codes

| Code | Meaning | Example |
|------|---------|---------|
| 200 | OK | Vehicle fetched successfully |
| 201 | Created | New vehicle created |
| 204 | No Content | Vehicle deleted successfully |
| 400 | Bad Request | Invalid parameters or validation error |
| 401 | Unauthorized | Missing or invalid JWT token |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Vehicle doesn't exist |
| 500 | Server Error | Internal server error |

### Common Errors

**Invalid Vehicle ID (404):**
```json
{
  "error": "Vehicle not found",
  "status": 404
}
```

**Unauthorized Access (401):**
```json
{
  "error": "Missing or invalid authorization token",
  "status": 401
}
```

**Validation Error (400):**
```json
{
  "error": "Validation failed",
  "details": {
    "price": "must be a positive number",
    "make": "is required"
  },
  "status": 400
}
```

---

## Examples

### cURL Examples

**List vehicles:**
```bash
curl -X GET "http://localhost:8080/api/inventory?make=Toyota&maxPrice=30000"
```

**Get single vehicle:**
```bash
curl -X GET "http://localhost:8080/api/inventory/1"
```

**Admin login:**
```bash
curl -X POST "http://localhost:8080/api/admin/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"your_password"}'
```

**Create vehicle (admin):**
```bash
curl -X POST "http://localhost:8080/api/inventory" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "year": 2023,
    "make": "Honda",
    "model": "Civic",
    "price": 22999,
    "status": "available"
  }'
```

**Refresh KBB (admin):**
```bash
curl -X POST "http://localhost:8080/api/inventory/1/refresh-kbb" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### JavaScript Examples

```javascript
// Fetch vehicles
const vehicles = await fetch('/api/inventory?make=Toyota')
  .then(r => r.json())

// Get single vehicle
const car = await fetch('/api/inventory/1')
  .then(r => r.json())

// Admin login
const login = await fetch('/api/admin/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ username: 'admin', password: 'pass' })
}).then(r => r.json())

// Refresh KBB (with token)
const updated = await fetch('/api/inventory/1/refresh-kbb', {
  method: 'POST',
  headers: { 'Authorization': `Bearer ${login.token}` }
}).then(r => r.json())
```

---

## Rate Limiting

- **Limit:** 100 requests per minute per IP
- **Headers:**
  ```
  X-RateLimit-Limit: 100
  X-RateLimit-Remaining: 95
  X-RateLimit-Reset: 1711270800
  ```
- **When exceeded:** Returns 429 (Too Many Requests)

---

## Support

For API issues:
1. Check request format matches examples
2. Verify authorization token is valid
3. Check HTTP status codes for details
4. Review backend logs: `tail -f backend.log`
5. Test endpoints with Swagger UI: `http://localhost:8080/swagger-ui.html`

---

**See Also:** [SETUP_GUIDE.md](./SETUP_GUIDE.md), [DEPLOYMENT.md](./DEPLOYMENT.md)
