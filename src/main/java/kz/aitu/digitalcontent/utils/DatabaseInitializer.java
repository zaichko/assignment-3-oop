package kz.aitu.digitalcontent.utils;

import kz.aitu.digitalcontent.patterns.DatabaseConfig;
import kz.aitu.digitalcontent.patterns.LoggerService;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final LoggerService logger = LoggerService.getInstance();
    private static final DatabaseConfig dbConfig = DatabaseConfig.getInstance();

    public static void initializeDatabase() {
        try {
            createTables();
            insertSampleData();
            logger.info("Database initialized successfully");
        } catch (SQLException e) {
            logger.error("Database initialization failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void createTables() throws SQLException {
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("DROP TABLE IF EXISTS purchases CASCADE");
            stmt.execute("DROP TABLE IF EXISTS digital_content CASCADE");
            stmt.execute("DROP TABLE IF EXISTS users CASCADE");

            stmt.execute(
                    "CREATE TABLE users (" +
                            "id SERIAL PRIMARY KEY, " +
                            "name VARCHAR(255) NOT NULL, " +
                            "email VARCHAR(255) UNIQUE NOT NULL)"
            );

            stmt.execute(
                    "CREATE TABLE digital_content (" +
                            "id SERIAL PRIMARY KEY, " +
                            "name VARCHAR(255) NOT NULL, " +
                            "release_year INTEGER NOT NULL, " +
                            "available BOOLEAN NOT NULL, " +
                            "content_type VARCHAR(50) NOT NULL CHECK(content_type IN ('GAME', 'MOVIE', 'MUSIC_ALBUM')), " +
                            "description TEXT, " +
                            "creator_country VARCHAR(100), " +
                            "creator_bio TEXT, " +
                            "rentable BOOLEAN, " +
                            "duration_minutes INTEGER, " +
                            "track_count INTEGER)"
            );

            stmt.execute(
                    "CREATE TABLE purchases (" +
                            "purchase_id SERIAL PRIMARY KEY, " +
                            "user_id INTEGER NOT NULL, " +
                            "content_id INTEGER NOT NULL, " +
                            "purchase_date DATE NOT NULL, " +
                            "price_paid DECIMAL(10,2) NOT NULL, " +
                            "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                            "FOREIGN KEY (content_id) REFERENCES digital_content(id) ON DELETE CASCADE)"
            );

            logger.info("Tables created successfully");
        }
    }

    private static void insertSampleData() throws SQLException {
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(
                    "INSERT INTO users (name, email) VALUES " +
                            "('John Doe', 'john@example.com'), " +
                            "('Jane Smith', 'jane@example.com'), " +
                            "('Bob Johnson', 'bob@example.com')"
            );

            stmt.execute(
                    "INSERT INTO digital_content (name, release_year, available, content_type, " +
                            "description, creator_country, creator_bio, rentable, duration_minutes) VALUES " +
                            "('The Matrix', 1999, true, 'MOVIE', 'A mind-bending sci-fi thriller', " +
                            "'USA', 'Wachowski Sisters', true, 136), " +
                            "('Inception', 2010, true, 'MOVIE', 'Dreams within dreams', " +
                            "'USA', 'Christopher Nolan', true, 148)"
            );

            stmt.execute(
                    "INSERT INTO digital_content (name, release_year, available, content_type, " +
                            "description, creator_country, creator_bio, track_count) VALUES " +
                            "('Dark Side of the Moon', 1973, true, 'MUSIC_ALBUM', " +
                            "'Progressive rock masterpiece', 'UK', 'Pink Floyd', 10), " +
                            "('Thriller', 1982, true, 'MUSIC_ALBUM', 'Best-selling album', " +
                            "'USA', 'Michael Jackson', 9)"
            );

            stmt.execute(
                    "INSERT INTO digital_content (name, release_year, available, content_type, " +
                            "description, creator_country, creator_bio) VALUES " +
                            "('Minecraft', 2011, true, 'GAME', 'Sandbox building game', " +
                            "'Sweden', 'Mojang Studios'), " +
                            "('The Witcher 3', 2015, true, 'GAME', 'Epic RPG adventure', " +
                            "'Poland', 'CD Projekt Red')"
            );

            stmt.execute(
                    "INSERT INTO purchases (user_id, content_id, purchase_date, price_paid) VALUES " +
                            "(1, 1, '2024-01-15', 14.99), " +
                            "(1, 3, '2024-01-20', 9.99), " +
                            "(2, 2, '2024-02-01', 12.99), " +
                            "(3, 5, '2024-02-05', 29.99)"
            );

            logger.info("Sample data inserted successfully");
        }
    }
}