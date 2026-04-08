-- Migration: Add KBB pricing columns to cars table
-- Purpose: Support Kelley Blue Book (KBB) valuation integration

ALTER TABLE cars
ADD COLUMN IF NOT EXISTS kbb_value NUMERIC(12, 2),
ADD COLUMN IF NOT EXISTS kbb_last_updated TIMESTAMP;

-- Optional: create index on kbb_last_updated for queries filtering by update time
CREATE INDEX IF NOT EXISTS idx_cars_kbb_last_updated ON cars(kbb_last_updated);
