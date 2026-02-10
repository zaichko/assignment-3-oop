package kz.aitu.digitalcontent.repository;

import kz.aitu.digitalcontent.exception.DatabaseOperationException;
import kz.aitu.digitalcontent.model.*;
import kz.aitu.digitalcontent.patterns.DatabaseConfig;
import kz.aitu.digitalcontent.patterns.DigitalContentFactory;
import kz.aitu.digitalcontent.patterns.LoggerService;
import kz.aitu.digitalcontent.repository.interfaces.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DigitalContentRepository implements CrudRepository<DigitalContent> {

    private final DatabaseConfig dbConfig = DatabaseConfig.getInstance();
    private final LoggerService logger = LoggerService.getInstance();

    @Override
    public DigitalContent create(DigitalContent content) {
        String sql = "INSERT INTO digital_content (name, release_year, available, content_type, " +
                "description, creator_country, creator_bio, rentable, duration_minutes, track_count) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, content.getName());
            stmt.setInt(2, content.getReleaseYear());
            stmt.setBoolean(3, content.isAvailable());
            stmt.setString(4, content.getEntityType());
            stmt.setString(5, content.getDescription());

            Creator creator = content.getCreator();
            stmt.setString(6, creator != null ? creator.getCountry() : null);
            stmt.setString(7, creator != null ? creator.getBio() : null);

            // Type-specific fields
            if (content instanceof Movie) {
                Movie movie = (Movie) content;
                stmt.setBoolean(8, movie.isRentable());
                stmt.setInt(9, movie.getDurationMinutes());
                stmt.setNull(10, Types.INTEGER);
            } else if (content instanceof MusicAlbum) {
                MusicAlbum album = (MusicAlbum) content;
                stmt.setNull(8, Types.BOOLEAN);
                stmt.setNull(9, Types.INTEGER);
                stmt.setInt(10, album.getCountTracks());
            } else {
                stmt.setNull(8, Types.BOOLEAN);
                stmt.setNull(9, Types.INTEGER);
                stmt.setNull(10, Types.INTEGER);
            }

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                content.setId(generatedKeys.getInt(1));
            }

            logger.info("Created " + content.getEntityType() + ": " + content.getName());
            return content;

        } catch (SQLException e) {
            logger.error("Failed to create content: " + e.getMessage());
            throw new DatabaseOperationException("create", e);
        }
    }

    @Override
    public List<DigitalContent> getAll() {
        List<DigitalContent> contents = new ArrayList<>();
        String sql = "SELECT * FROM digital_content";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                contents.add(mapResultSetToContent(rs));
            }

            logger.info("Retrieved " + contents.size() + " content items");
            return contents;

        } catch (SQLException e) {
            logger.error("Failed to retrieve all content: " + e.getMessage());
            throw new DatabaseOperationException("getAll", e);
        }
    }

    @Override
    public Optional<DigitalContent> getById(int id) {
        String sql = "SELECT * FROM digital_content WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                DigitalContent content = mapResultSetToContent(rs);
                logger.info("Found content: " + content.getName());
                return Optional.of(content);
            }

            logger.info("Content not found with ID: " + id);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error("Failed to get content by ID: " + e.getMessage());
            throw new DatabaseOperationException("getById", e);
        }
    }

    @Override
    public DigitalContent update(int id, DigitalContent content) {
        String sql = "UPDATE digital_content SET name = ?, release_year = ?, available = ?, " +
                "description = ?, creator_country = ?, creator_bio = ?, " +
                "rentable = ?, duration_minutes = ?, track_count = ? WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, content.getName());
            stmt.setInt(2, content.getReleaseYear());
            stmt.setBoolean(3, content.isAvailable());
            stmt.setString(4, content.getDescription());

            Creator creator = content.getCreator();
            stmt.setString(5, creator != null ? creator.getCountry() : null);
            stmt.setString(6, creator != null ? creator.getBio() : null);

            if (content instanceof Movie) {
                Movie movie = (Movie) content;
                stmt.setBoolean(7, movie.isRentable());
                stmt.setInt(8, movie.getDurationMinutes());
                stmt.setNull(9, Types.INTEGER);
            } else if (content instanceof MusicAlbum) {
                MusicAlbum album = (MusicAlbum) content;
                stmt.setNull(7, Types.BOOLEAN);
                stmt.setNull(8, Types.INTEGER);
                stmt.setInt(9, album.getCountTracks());
            } else {
                stmt.setNull(7, Types.BOOLEAN);
                stmt.setNull(8, Types.INTEGER);
                stmt.setNull(9, Types.INTEGER);
            }

            stmt.setInt(10, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseOperationException("No content found with ID: " + id);
            }

            content.setId(id);
            logger.info("Updated content: " + content.getName());
            return content;

        } catch (SQLException e) {
            logger.error("Failed to update content: " + e.getMessage());
            throw new DatabaseOperationException("update", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM digital_content WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Deleted content with ID: " + id);
                return true;
            }

            logger.info("No content found to delete with ID: " + id);
            return false;

        } catch (SQLException e) {
            logger.error("Failed to delete content: " + e.getMessage());
            throw new DatabaseOperationException("delete", e);
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM digital_content WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            throw new DatabaseOperationException("exists", e);
        }
    }

    private DigitalContent mapResultSetToContent(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int releaseYear = rs.getInt("release_year");
        boolean available = rs.getBoolean("available");
        String contentType = rs.getString("content_type");
        String description = rs.getString("description");

        Creator creator = new Creator(
                rs.getString("creator_country"),
                rs.getString("creator_bio")
        );

        DigitalContent content;

        switch (contentType) {
            case "MOVIE":
                boolean rentable = rs.getBoolean("rentable");
                int duration = rs.getInt("duration_minutes");
                content = DigitalContentFactory.createMovie(id, name, releaseYear,
                        available, creator, description, rentable, duration);
                break;

            case "MUSIC_ALBUM":
                int trackCount = rs.getInt("track_count");
                content = DigitalContentFactory.createMusicAlbum(id, name, releaseYear,
                        available, creator, description, trackCount);
                break;

            case "GAME":
            default:
                content = DigitalContentFactory.createContent("GAME", id, name,
                        releaseYear, available, creator, description);
                break;
        }

        return content;
    }
}