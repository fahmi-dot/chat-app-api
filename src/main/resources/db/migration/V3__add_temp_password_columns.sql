ALTER TABLE users
    ADD COLUMN temp_password VARCHAR(255);

ALTER TABLE users
    ADD COLUMN password_expires_at TIMESTAMP;