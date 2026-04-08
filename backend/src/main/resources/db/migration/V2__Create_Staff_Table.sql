-- Migration: Create staff_members table for Meet the Staff page

CREATE TABLE IF NOT EXISTS staff_members (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    title       VARCHAR(255),
    bio         TEXT,
    email       VARCHAR(255),
    phone       VARCHAR(50),
    photo_url   VARCHAR(1000),
    sort_order  INTEGER NOT NULL DEFAULT 0,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);
