-- ============================================================
-- Dealership Template — PostgreSQL Schema
-- ============================================================
-- Run against a fresh database:
--   psql -U postgres -d dealership_db -f schema.sql
--
-- Create the database first if it doesn't exist:
--   createdb -U postgres dealership_db
-- ============================================================

-- ── Extensions ────────────────────────────────────────────────────────
-- pgcrypto is optional but useful for hashing if you add user accounts
-- CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ── cars ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS cars (
    id              BIGSERIAL PRIMARY KEY,

    -- Identity
    make            VARCHAR(100)   NOT NULL,
    model           VARCHAR(100)   NOT NULL,
    year            INT            NOT NULL CHECK (year >= 1900 AND year <= 2100),
    trim            VARCHAR(100),

    -- Pricing & usage
    price           NUMERIC(12, 2),
    mileage         INT            CHECK (mileage >= 0),
    vin             VARCHAR(17)    UNIQUE,

    -- Descriptions
    description     TEXT,
    features        JSONB          DEFAULT '[]'::jsonb,   -- array of feature strings

    -- Classification
    condition       VARCHAR(20)    DEFAULT 'Used',         -- New | Used | Certified
    status          VARCHAR(20)    NOT NULL DEFAULT 'available',  -- available | pending | sold

    -- Specs
    transmission    VARCHAR(50),   -- Automatic | Manual | CVT | DCT
    engine          VARCHAR(100),  -- e.g. "2.5L 4-Cylinder"
    drive_train     VARCHAR(10),   -- FWD | RWD | AWD | 4WD
    exterior_color  VARCHAR(50),
    interior_color  VARCHAR(50),

    -- Audit
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

-- Trigger to auto-update updated_at
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER LANGUAGE plpgsql AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS cars_updated_at ON cars;
CREATE TRIGGER cars_updated_at
    BEFORE UPDATE ON cars
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- Indexes for common filter queries
CREATE INDEX IF NOT EXISTS idx_cars_status    ON cars(status);
CREATE INDEX IF NOT EXISTS idx_cars_make      ON cars(make);
CREATE INDEX IF NOT EXISTS idx_cars_year      ON cars(year);
CREATE INDEX IF NOT EXISTS idx_cars_price     ON cars(price);
CREATE INDEX IF NOT EXISTS idx_cars_created   ON cars(created_at DESC);

-- Full-text search index on make + model + description
CREATE INDEX IF NOT EXISTS idx_cars_fts ON cars
    USING gin(to_tsvector('english', coalesce(make,'') || ' ' || coalesce(model,'') || ' ' || coalesce(description,'')));


-- ── car_images ────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS car_images (
    id          BIGSERIAL PRIMARY KEY,
    car_id      BIGINT        NOT NULL REFERENCES cars(id) ON DELETE CASCADE,
    image_url   TEXT          NOT NULL,
    is_primary  BOOLEAN       NOT NULL DEFAULT FALSE,
    sort_order  INT           NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_car_images_car_id ON car_images(car_id);


-- ── contact_submissions ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS contact_submissions (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(200)  NOT NULL,
    email       VARCHAR(320)  NOT NULL,
    phone       VARCHAR(30),
    message     TEXT          NOT NULL,
    car_id      BIGINT        REFERENCES cars(id) ON DELETE SET NULL,
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_contact_created ON contact_submissions(created_at DESC);


-- ── dealership_config ─────────────────────────────────────────────────
-- Key/value store for runtime configuration (e.g., business hours override,
-- announcement banners, featured car IDs).
CREATE TABLE IF NOT EXISTS dealership_config (
    id      BIGSERIAL PRIMARY KEY,
    key     VARCHAR(100) NOT NULL UNIQUE,
    value   TEXT
);

-- Seed example config rows (all placeholder values)
INSERT INTO dealership_config (key, value) VALUES
    ('dealership_name',   '[DEALERSHIP_NAME]'),
    ('phone',             '[PHONE]'),
    ('email',             '[EMAIL]'),
    ('address',           '[ADDRESS]'),
    ('city_state_zip',    '[CITY_STATE_ZIP]'),
    ('hours_mon_fri',     '[MON_FRI_HOURS]'),
    ('hours_saturday',    '[SAT_HOURS]'),
    ('hours_sunday',      '[SUN_HOURS]'),
    ('google_maps_url',   '[GOOGLE_MAPS_EMBED_URL]'),
    ('facebook_url',      '[FACEBOOK_URL]'),
    ('instagram_url',     '[INSTAGRAM_URL]'),
    ('announcement',      NULL)       -- set to a string to show a site-wide banner
ON CONFLICT (key) DO NOTHING;


-- ── Sample data (development only — delete before production) ─────────
-- Uncomment to seed a few demo cars for local development.
/*
INSERT INTO cars (make, model, year, trim, price, mileage, condition, status, vin, description, features, transmission, exterior_color)
VALUES
    ('Toyota',  'Camry',   2022, 'XLE',      28995, 18400, 'Used',      'available', '4T1B11HK5NU123456', 'One owner, excellent condition.', '["Backup Camera","Heated Seats","Apple CarPlay"]', 'Automatic', 'Midnight Black'),
    ('Honda',   'Civic',   2023, 'Sport',    24500, 6200,  'Certified', 'available', '2HGFE2F52PH123456', 'Honda Certified Pre-Owned, full warranty.', '["Honda Sensing","Sunroof","Lane Keeping Assist"]', 'CVT', 'Lunar Silver'),
    ('Ford',    'F-150',   2021, 'XLT',      38750, 32100, 'Used',      'available', '1FTEW1E52MKD12345', 'Clean CarFax, tow package.', '["Tow Package","Bed Liner","Sync 3"]', 'Automatic', 'Oxford White'),
    ('BMW',     '3 Series',2022, '330i',     41900, 15800, 'Used',      'available', 'WBA5R7C52NF123456', 'Premium package, low miles.', '["Premium Package","Navigation","Heated Steering"]', 'Automatic', 'Alpine White'),
    ('Tesla',   'Model 3', 2023, 'Long Range',47500, 8900,  'Used',      'available', '5YJ3E1EA5PF123456', 'FSD capable, 358 mile range.', '["Autopilot","FSD","Glass Roof","Premium Audio"]', 'Automatic', 'Deep Blue Metallic'),
    ('Chevrolet','Tahoe',  2021, 'LT',       49900, 41200, 'Used',      'available', '1GNSKCKD5MR123456', 'Third row seating, loaded.', '["3rd Row Seating","Bose Audio","Captain Chairs"]', 'Automatic', 'Summit White');

INSERT INTO car_images (car_id, image_url, is_primary, sort_order)
SELECT id, 'https://images.unsplash.com/photo-1583121274602-3e2820c69888?auto=format&fit=crop&w=800&q=70', TRUE, 0
FROM cars;
*/
