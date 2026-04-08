-- Reset admin password so it falls back to the ADMIN_PASSWORD env var on next login
DELETE FROM dealership_config WHERE key = 'admin_password';
