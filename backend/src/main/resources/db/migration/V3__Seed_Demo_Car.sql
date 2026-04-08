-- Migration: Insert a demo car so new sites don't look empty
-- Admin can delete or update this via the admin panel whenever they're ready.

INSERT INTO cars (make, model, year, trim, price, mileage, vin, description, features,
                  condition, status, transmission, engine, drive_train,
                  exterior_color, interior_color, created_at, updated_at)
SELECT
  'Ford',
  'F-150',
  2022,
  'XLT SuperCrew',
  38995,
  24500,
  '1FTFW1ET5NFA00001',
  'This 2022 Ford F-150 XLT is loaded with features and ready to work or play. One owner, clean Carfax, and fully inspected by our service team. This is a demo listing — feel free to update or replace it.',
  '["Backup Camera","Bluetooth","Apple CarPlay / Android Auto","Tow Package","Bed Liner","Running Boards","Remote Start","Heated Front Seats","Lane Keep Assist","Blind Spot Monitoring"]'::jsonb,
  'Used',
  'available',
  'Automatic',
  '3.5L EcoBoost V6',
  '4WD',
  'Agate Black',
  'Medium Dark Slate'
  ,NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM cars LIMIT 1);

-- Add a primary image using a free Unsplash truck photo
INSERT INTO car_images (car_id, image_url, is_primary, sort_order)
SELECT id,
       'https://images.unsplash.com/photo-1588515724527-074a7a56616c?auto=format&fit=crop&w=1200&q=80',
       true,
       0
FROM cars
WHERE vin = '1FTFW1ET5NFA00001'
  AND NOT EXISTS (SELECT 1 FROM car_images WHERE car_id = cars.id);
