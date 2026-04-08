-- ============================================================
-- Sample Dealership Data
-- Run with: psql -U postgres -d dealership_db -f sample-data.sql
-- ============================================================

-- Clear existing data
DELETE FROM car_images;
DELETE FROM cars;
ALTER SEQUENCE cars_id_seq RESTART WITH 1;
ALTER SEQUENCE car_images_id_seq RESTART WITH 1;

-- ============================================================
-- Sample Vehicles (15 cars)
-- ============================================================

INSERT INTO cars (year, make, model, trim, price, mileage, vin, description, features, condition, status, transmission, engine, drive_train, exterior_color, interior_color, kbb_value, kbb_last_updated, created_at, updated_at)
VALUES
  (2023, 'Toyota', 'Camry', 'SE', 25999, 15000, 'TOYOTA2023001', 'Well-maintained sedan with excellent service history. Fresh oil change. One owner, no accidents.', '["Backup Camera","Apple CarPlay","Android Auto","Sunroof","Bluetooth"]', 'Used', 'available', 'Automatic', '2.5L 4-cyl', 'FWD', 'Black Pearl', 'Tan Leather', 26500.00, NOW(), NOW(), NOW()),
  (2022, 'Toyota', 'Camry', 'LE', 23500, 32000, 'TOYOTA2022001', 'Reliable daily driver. Perfect for commuting. Clean interior, well-kept exterior. Regular maintenance records included.', '["Backup Camera","Cruise Control","Bluetooth"]', 'Used', 'available', 'Automatic', '2.5L 4-cyl', 'FWD', 'Silver Metallic', 'Black Cloth', 24000.00, NOW(), NOW(), NOW()),
  (2021, 'Toyota', 'Camry', 'XLE', 27999, 28000, 'TOYOTA2021001', 'Premium condition with all the bells and whistles. Heated seats, sunroof, navigation system. No accidents.', '["Backup Camera","Sunroof","Heated Seats","Apple CarPlay","Android Auto","Bluetooth","Cruise Control"]', 'Certified', 'available', 'Automatic', '2.5L 4-cyl', 'FWD', 'Pearl White', 'Leather', 26000.00, NOW(), NOW(), NOW()),
  (2023, 'Honda', 'Civic', 'Sport', 22999, 8000, 'HONDA2023001', 'Brand new model year. Pristine condition. All safety features. Great warranty remaining.', '["Backup Camera","Apple CarPlay","Bluetooth","ABS","Stability Control"]', 'Used', 'available', 'Manual', '2.0L 4-cyl', 'FWD', 'Red Metallic', 'Black Sport', 23000.00, NOW(), NOW(), NOW()),
  (2022, 'Honda', 'Civic', 'EX', 21500, 24000, 'HONDA2022001', 'Fuel-efficient and reliable. Good tires. Recent transmission service.', '["Backup Camera","Cruise Control","Bluetooth"]', 'Used', 'available', 'Automatic', '2.0L 4-cyl', 'FWD', 'Silver', 'Gray Cloth', 20500.00, NOW(), NOW(), NOW()),
  (2023, 'Ford', 'F-150', 'XLT', 34999, 22000, 'FORD2023001', 'Powerful pickup with towing package. Crew cab, 4-door. Excellent for work or weekend adventures.', '["Backup Camera","Towing Package","Apple CarPlay","Bluetooth","Cruise Control"]', 'Used', 'available', 'Automatic', '5.0L V8', '4WD', 'Oxford White', 'Black Cloth', 35000.00, NOW(), NOW(), NOW()),
  (2021, 'Ford', 'F-150', 'STX', 31500, 45000, 'FORD2021001', 'Super Duty with all-terrain tires. Regular maintenance. Great for heavy hauling.', '["Backup Camera","Towing Package","Cruise Control"]', 'Used', 'available', 'Automatic', '5.0L V8', '4WD', 'Stone Gray', 'Gray Cloth', 30000.00, NOW(), NOW(), NOW()),
  (2023, 'Chevrolet', 'Equinox', 'LT', 26999, 18000, 'CHEVY2023001', 'Modern SUV perfect for families. Great safety ratings. All-season tires.', '["Backup Camera","Apple CarPlay","Bluetooth","Cruise Control","Stability Control"]', 'Used', 'available', 'Automatic', '1.5L Turbo 4-cyl', 'FWD', 'Midnight Black', 'Jet Black', 27000.00, NOW(), NOW(), NOW()),
  (2022, 'Chevrolet', 'Equinox', 'LS', 24500, 35000, 'CHEVY2022001', 'Spacious interior for groceries, kids, and adventures. Recent brake service.', '["Backup Camera","Bluetooth"]', 'Used', 'available', 'Automatic', '1.5L Turbo 4-cyl', 'FWD', 'Silver Ice', 'Jet Black', 23500.00, NOW(), NOW(), NOW()),
  (2024, 'Hyundai', 'Elantra', 'SEL', 18999, 5000, 'HYUNDAI2024001', 'Practically brand new! Full factory warranty. Amazing gas mileage. Modern tech at affordable price.', '["Backup Camera","Apple CarPlay","Android Auto","Bluetooth","ABS"]', 'New', 'available', 'Automatic', '2.0L 4-cyl', 'FWD', 'Calypso Red', 'Black', 19500.00, NOW(), NOW(), NOW()),
  (2023, 'Hyundai', 'Elantra', 'Value', 17500, 12000, 'HYUNDAI2023001', 'Economy sedan with great features. Perfect first car. No issues, runs like new.', '["Backup Camera","Bluetooth"]', 'Used', 'available', 'Automatic', '2.0L 4-cyl', 'FWD', 'Cloud White', 'Beige', 17000.00, NOW(), NOW(), NOW()),
  (2023, 'Mazda', 'CX-5', 'Preferred Plus', 28999, 19000, 'MAZDA2023001', 'Premium compact SUV. Leather interior. All-wheel drive. Tech-forward features.', '["Backup Camera","Leather Seats","Heated Seats","Apple CarPlay","Android Auto","AWD","Cruise Control"]', 'Used', 'available', 'Automatic', '2.5L 4-cyl', 'AWD', 'Soul Red Crystal', 'Leather Tan', 29000.00, NOW(), NOW(), NOW()),
  (2022, 'GMC', 'Sierra', '1500 SLE', 32500, 38000, 'GMC2022001', 'Truck with style. Premium cabin. Comfortable for long drives. Integrated towing.', '["Backup Camera","Towing Package","Bluetooth","Cruise Control"]', 'Used', 'available', 'Automatic', '5.3L V8', '4WD', 'Graphite Gray', 'Jet Black', 31500.00, NOW(), NOW(), NOW()),
  (2023, 'Subaru', 'Outback', 'Premium', 29999, 16000, 'SUBARU2023001', 'Adventure-ready wagon with all-wheel drive. Perfect for outdoor enthusiasts. Excellent ground clearance.', '["Backup Camera","AWD","Apple CarPlay","Bluetooth","Heated Seats","Cruise Control"]', 'Used', 'available', 'Automatic', '2.5L 4-cyl', 'AWD', 'Autumn Green', 'Brown Leather', 30000.00, NOW(), NOW(), NOW()),
  (2024, 'Kia', 'Sportage', 'EX', 24999, 3000, 'KIA2024001', 'Latest model year! Gorgeous styling. Excellent warranty. Amazing fuel economy.', '["Backup Camera","Apple CarPlay","Bluetooth","Cruise Control","Stability Control"]', 'New', 'available', 'Automatic', '2.0L Turbo 4-cyl', 'FWD', 'Cherry Black', 'Black Leather', 25500.00, NOW(), NOW(), NOW());

-- ============================================================
-- Sample Car Images
-- ============================================================

INSERT INTO car_images (car_id, image_url, is_primary, sort_order)
VALUES
  (1, 'https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?auto=format&fit=crop&w=800&q=80', true, 1),
  (1, 'https://images.unsplash.com/photo-1552820728-8ac41f1ce891?auto=format&fit=crop&w=800&q=80', false, 2),
  (1, 'https://images.unsplash.com/photo-1564818735033-13ee77255f41?auto=format&fit=crop&w=800&q=80', false, 3),
  (2, 'https://images.unsplash.com/photo-1557622985-8e86cb6e6a2e?auto=format&fit=crop&w=800&q=80', true, 1),
  (3, 'https://images.unsplash.com/photo-1583121274602-3e2820c69888?auto=format&fit=crop&w=800&q=80', true, 1),
  (4, 'https://images.unsplash.com/photo-1617469677492-7f8f5e4d6f4d?auto=format&fit=crop&w=800&q=80', true, 1),
  (5, 'https://images.unsplash.com/photo-1618469449619-91667eee5d11?auto=format&fit=crop&w=800&q=80', true, 1),
  (6, 'https://images.unsplash.com/photo-1533473359331-35dca7a01e1f?auto=format&fit=crop&w=800&q=80', true, 1),
  (7, 'https://images.unsplash.com/photo-1605611214413-3da0e8ed6a2e?auto=format&fit=crop&w=800&q=80', true, 1),
  (8, 'https://images.unsplash.com/photo-1594274690992-23097a33a8c5?auto=format&fit=crop&w=800&q=80', true, 1),
  (9, 'https://images.unsplash.com/photo-1593625211151-c7e1ba5e8fbf?auto=format&fit=crop&w=800&q=80', true, 1),
  (10, 'https://images.unsplash.com/photo-1540454549336-331cc28fb4d4?auto=format&fit=crop&w=800&q=80', true, 1),
  (11, 'https://images.unsplash.com/photo-1581270995818-a0fa4ba73265?auto=format&fit=crop&w=800&q=80', true, 1),
  (12, 'https://images.unsplash.com/photo-1567166282014-98f3237ef881?auto=format&fit=crop&w=800&q=80', true, 1),
  (13, 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?auto=format&fit=crop&w=800&q=80', true, 1),
  (14, 'https://images.unsplash.com/photo-1502519923640-ec2f18dcc95d?auto=format&fit=crop&w=800&q=80', true, 1),
  (15, 'https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?auto=format&fit=crop&w=800&q=80', true, 1);

-- ============================================================
-- Verify data was inserted
-- ============================================================

SELECT COUNT(*) as total_vehicles FROM cars;
SELECT COUNT(*) as total_images FROM car_images;
SELECT id, year, make, model, trim, price, mileage, status FROM cars ORDER BY id;
