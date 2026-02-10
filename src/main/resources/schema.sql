-- Digital Content Platform Database Schema
-- PostgreSQL Database

-- Drop existing tables
DROP TABLE IF EXISTS purchases CASCADE;
DROP TABLE IF EXISTS digital_content CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ==============================================
-- Users Table
-- ==============================================
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL
);

-- ==============================================
-- Digital Content Table
-- Supports polymorphic content (Game, Movie, MusicAlbum)
-- ==============================================
CREATE TABLE digital_content (
                                 id SERIAL PRIMARY KEY,
                                 name VARCHAR(255) NOT NULL,
                                 release_year INTEGER NOT NULL,
                                 available BOOLEAN NOT NULL,
                                 content_type VARCHAR(50) NOT NULL CHECK(content_type IN ('GAME', 'MOVIE', 'MUSIC_ALBUM')),
                                 description TEXT,
                                 creator_country VARCHAR(100),
                                 creator_bio TEXT,

    -- Movie-specific fields
                                 rentable BOOLEAN,
                                 duration_minutes INTEGER,

    -- MusicAlbum-specific fields
                                 track_count INTEGER
);

-- ==============================================
-- Purchases Table
-- ==============================================
CREATE TABLE purchases (
                           purchase_id SERIAL PRIMARY KEY,
                           user_id INTEGER NOT NULL,
                           content_id INTEGER NOT NULL,
                           purchase_date DATE NOT NULL,
                           price_paid DECIMAL(10,2) NOT NULL,

                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                           FOREIGN KEY (content_id) REFERENCES digital_content(id) ON DELETE CASCADE
);

-- ==============================================
-- Indexes for better query performance
-- ==============================================
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_content_type ON digital_content(content_type);
CREATE INDEX idx_content_available ON digital_content(available);
CREATE INDEX idx_purchases_user ON purchases(user_id);
CREATE INDEX idx_purchases_content ON purchases(content_id);

-- ==============================================
-- Sample Data Inserts
-- ==============================================

-- Insert sample users
INSERT INTO users (name, email) VALUES
                                    ('John Doe', 'john@example.com'),
                                    ('Jane Smith', 'jane@example.com'),
                                    ('Bob Johnson', 'bob@example.com'),
                                    ('Alice Wonder', 'alice@example.com');

-- Insert sample movies
INSERT INTO digital_content (
    name, release_year, available, content_type,
    description, creator_country, creator_bio,
    rentable, duration_minutes
) VALUES
      ('The Matrix', 1999, true, 'MOVIE',
       'A mind-bending sci-fi thriller about reality and simulation',
       'USA', 'Wachowski Sisters', true, 136),

      ('Inception', 2010, true, 'MOVIE',
       'Dreams within dreams - a heist in the subconscious',
       'USA', 'Christopher Nolan', true, 148),

      ('The Shawshank Redemption', 1994, true, 'MOVIE',
       'Hope and friendship in prison',
       'USA', 'Frank Darabont', true, 142),

      ('Pulp Fiction', 1994, true, 'MOVIE',
       'Interconnected crime stories',
       'USA', 'Quentin Tarantino', true, 154);

-- Insert sample music albums
INSERT INTO digital_content (
    name, release_year, available, content_type,
    description, creator_country, creator_bio,
    track_count
) VALUES
      ('Dark Side of the Moon', 1973, true, 'MUSIC_ALBUM',
       'Progressive rock masterpiece exploring mental health and society',
       'UK', 'Pink Floyd', 10),

      ('Thriller', 1982, true, 'MUSIC_ALBUM',
       'Best-selling album of all time',
       'USA', 'Michael Jackson', 9),

      ('Abbey Road', 1969, true, 'MUSIC_ALBUM',
       'Iconic Beatles album with medley suite',
       'UK', 'The Beatles', 17),

      ('Back in Black', 1980, true, 'MUSIC_ALBUM',
       'Hard rock classic tribute album',
       'Australia', 'AC/DC', 10);

-- Insert sample games
INSERT INTO digital_content (
    name, release_year, available, content_type,
    description, creator_country, creator_bio
) VALUES
      ('Minecraft', 2011, true, 'GAME',
       'Sandbox building and survival game',
       'Sweden', 'Mojang Studios'),

      ('The Witcher 3: Wild Hunt', 2015, true, 'GAME',
       'Epic open-world RPG based on books',
       'Poland', 'CD Projekt Red'),

      ('Red Dead Redemption 2', 2018, true, 'GAME',
       'Western action-adventure epic',
       'USA', 'Rockstar Games'),

      ('Elden Ring', 2022, true, 'GAME',
       'Souls-like action RPG collaboration',
       'Japan', 'FromSoftware');

-- Insert sample purchases
INSERT INTO purchases (user_id, content_id, purchase_date, price_paid) VALUES
                                                                           (1, 1, '2024-01-15', 14.99),  -- John bought The Matrix
                                                                           (1, 5, '2024-01-20', 9.99),   -- John bought Dark Side of the Moon
                                                                           (2, 2, '2024-02-01', 12.99),  -- Jane bought Inception
                                                                           (2, 9, '2024-02-03', 29.99),  -- Jane bought Minecraft
                                                                           (3, 10, '2024-02-05', 59.99), -- Bob bought The Witcher 3
                                                                           (4, 3, '2024-02-07', 11.99),  -- Alice bought Shawshank
                                                                           (4, 6, '2024-02-08', 8.99);   -- Alice bought Thriller

-- ==============================================
-- Verification Queries
-- ==============================================

-- Count content by type
-- SELECT content_type, COUNT(*) as count
-- FROM digital_content
-- GROUP BY content_type;

-- List all purchases with user and content details
-- SELECT
--     u.name as user_name,
--     dc.name as content_name,
--     dc.content_type,
--     p.purchase_date,
--     p.price_paid
-- FROM purchases p
-- JOIN users u ON p.user_id = u.id
-- JOIN digital_content dc ON p.content_id = dc.id
-- ORDER BY p.purchase_date DESC;