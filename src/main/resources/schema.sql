DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS creators CASCADE;
DROP TABLE IF EXISTS content CASCADE;
DROP TABLE IF EXISTS purchases CASCADE;


CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name TEXT NOT NULL,
                       email TEXT NOT NULL UNIQUE
);

CREATE TABLE creators (
                          id SERIAL PRIMARY KEY,
                          name TEXT NOT NULL,
                          country TEXT,
                          bio TEXT
);

CREATE TABLE content (
                         id SERIAL PRIMARY KEY,
                         name TEXT NOT NULL,
                         creator_id INT NOT NULL,
                         release_year INT NOT NULL,
                         available BOOLEAN NOT NULL,
                         description TEXT,
                         content_type TEXT NOT NULL,
                         duration_minutes INT,
                         rentable BOOLEAN,
                         track_count INT,

                         CONSTRAINT fk_creator
                             FOREIGN KEY (creator_id)
                                 REFERENCES creators(id)
);

CREATE TABLE purchases (
                           id SERIAL PRIMARY KEY,
                           user_id INT NOT NULL,
                           content_id INT NOT NULL,
                           price_paid DECIMAL(6,2) NOT NULL,
                           purchase_date DATE NOT NULL,

                           CONSTRAINT fk_user
                               FOREIGN KEY (user_id)
                                   REFERENCES users(id),

                           CONSTRAINT fk_content
                               FOREIGN KEY (content_id)
                                   REFERENCES content(id),

                           CONSTRAINT unique_purchase
                               UNIQUE (user_id, content_id)
);


INSERT INTO users (name, email)
VALUES ('Alice', 'alice@mail.com'),
       ('Bob', 'bob@mail.com'),
       ('Peter', 'peter@mail.com');

INSERT INTO creators (name, country, bio)
VALUES ('Rammstein', 'Germany', NULL),
       ('Hideo Kojima', 'Japan', NULL),
       ('David Fincher', 'USA', NULL);

INSERT INTO content (name, creator_id, release_year, available, description, content_type, duration_minutes, rentable, track_count)
VALUES ('Fight Club', 3, 1999, true, NULL, 'Movie', 140, true, NULL),
       ('Sehnsucht', 1, 1997, true, NULL, 'MusicAlbum', NULL, NULL, 11),
       ('Metal Gear', 2, 1987, false, NULL, 'Game', NULL, NULL, NULL),
       ('Zodiac', 3, 2007, true, NULL, 'Movie', 157, false, NULL);

INSERT INTO purchases (user_id, content_id, price_paid, purchase_date) VALUES
                    (1, 1, 4.99, '2025-12-21'),
                    (1, 2, 14.55, '2025-12-26'),
                    (2, 3, 15.98, '2026-01-01');