package kz.aitu.digitalcontent.repository;

import kz.aitu.digitalcontent.exception.DatabaseOperationException;
import kz.aitu.digitalcontent.model.User;
import kz.aitu.digitalcontent.patterns.DatabaseConfig;
import kz.aitu.digitalcontent.patterns.LoggerService;
import kz.aitu.digitalcontent.repository.interfaces.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository implements CrudRepository<User> {

    private final DatabaseConfig dbConfig = DatabaseConfig.getInstance();
    private final LoggerService logger = LoggerService.getInstance();

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }

            logger.info("Created user: " + user.getName());
            return user;

        } catch (SQLException e) {
            logger.error("Failed to create user: " + e.getMessage());
            throw new DatabaseOperationException("create user", e);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

            logger.info("Retrieved " + users.size() + " users");
            return users;

        } catch (SQLException e) {
            throw new DatabaseOperationException("getAll users", e);
        }
    }

    @Override
    public Optional<User> getById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseOperationException("getById user", e);
        }
    }

    @Override
    public User update(int id, User user) {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setInt(3, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseOperationException("No user found with ID: " + id);
            }

            user.setId(id);
            logger.info("Updated user: " + user.getName());
            return user;

        } catch (SQLException e) {
            throw new DatabaseOperationException("update user", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            logger.info("Deleted user with ID: " + id);
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DatabaseOperationException("delete user", e);
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            throw new DatabaseOperationException("exists user", e);
        }
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseOperationException("findByEmail", e);
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email")
        );
    }
}