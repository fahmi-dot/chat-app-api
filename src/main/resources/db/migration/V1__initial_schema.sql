-- Users
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(255) NOT NULL,
    bio TEXT,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verification_code VARCHAR(10),
    code_expires_at TIMESTAMP
);

-- Rooms
CREATE TABLE IF NOT EXISTS rooms (
    id UUID PRIMARY KEY,
    room_key VARCHAR(255) UNIQUE NOT NULL
);

-- Room_users
CREATE TABLE IF NOT EXISTS room_users (
    room_id UUID NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (room_id, user_id)
);

-- Messages
CREATE TABLE IF NOT EXISTS messages (
    id UUID PRIMARY KEY,
    content TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    room_id UUID NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    sender_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE
);
