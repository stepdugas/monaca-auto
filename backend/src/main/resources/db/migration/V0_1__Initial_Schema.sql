-- Initial schema — creates all base tables on a fresh database.
-- Uses IF NOT EXISTS throughout so it is safe to run against an existing DB.

CREATE TABLE IF NOT EXISTS cars (
    id               BIGSERIAL PRIMARY KEY,
    make             VARCHAR(255)   NOT NULL,
    model            VARCHAR(255)   NOT NULL,
    year             INTEGER        NOT NULL,
    trim             VARCHAR(255),
    price            NUMERIC(12, 2),
    mileage          INTEGER,
    vin              VARCHAR(17)    UNIQUE,
    description      TEXT,
    features         JSONB          NOT NULL DEFAULT '[]',
    condition        VARCHAR(50),
    status           VARCHAR(50)    NOT NULL DEFAULT 'available',
    transmission     VARCHAR(100),
    engine           VARCHAR(100),
    drive_train      VARCHAR(50),
    exterior_color   VARCHAR(100),
    interior_color   VARCHAR(100),
    kbb_value        NUMERIC(12, 2),
    kbb_last_updated TIMESTAMP,
    created_at       TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP
);

CREATE TABLE IF NOT EXISTS car_images (
    id         BIGSERIAL PRIMARY KEY,
    car_id     BIGINT        NOT NULL REFERENCES cars(id) ON DELETE CASCADE,
    image_url  VARCHAR(1000) NOT NULL,
    is_primary BOOLEAN       NOT NULL DEFAULT FALSE,
    sort_order INTEGER       NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS contact_submissions (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    phone      VARCHAR(50),
    message    TEXT,
    car_id     BIGINT,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS dealership_config (
    id    BIGSERIAL PRIMARY KEY,
    key   VARCHAR(255) UNIQUE NOT NULL,
    value TEXT
);

CREATE INDEX IF NOT EXISTS idx_cars_status             ON cars(status);
CREATE INDEX IF NOT EXISTS idx_cars_kbb_last_updated   ON cars(kbb_last_updated);
CREATE INDEX IF NOT EXISTS idx_car_images_car_id       ON car_images(car_id);
