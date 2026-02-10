package kz.aitu.digitalcontent.repository;

import kz.aitu.digitalcontent.exception.DatabaseOperationException;
import kz.aitu.digitalcontent.model.Purchase;
import kz.aitu.digitalcontent.patterns.DatabaseConfig;
import kz.aitu.digitalcontent.patterns.LoggerService;
import kz.aitu.digitalcontent.repository.interfaces.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PurchaseRepository implements CrudRepository<Purchase> {

    private final DatabaseConfig dbConfig = DatabaseConfig.getInstance();
    private final LoggerService logger = LoggerService.getInstance();

    @Override
    public Purchase create(Purchase purchase) {
        String sql = "INSERT INTO purchases (user_id, content_id, purchase_date, price_paid) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, purchase.getUserId());
            stmt.setInt(2, purchase.getContentId());
            stmt.setDate(3, Date.valueOf(purchase.getPurchaseDate()));
            stmt.setDouble(4, purchase.getPricePaid());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                purchase.setPurchaseId(generatedKeys.getInt(1));
            }

            logger.info("Created purchase #" + purchase.getPurchaseId());
            return purchase;

        } catch (SQLException e) {
            logger.error("Failed to create purchase: " + e.getMessage());
            throw new DatabaseOperationException("create purchase", e);
        }
    }

    @Override
    public List<Purchase> getAll() {
        List<Purchase> purchases = new ArrayList<>();
        String sql = "SELECT * FROM purchases";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                purchases.add(mapResultSetToPurchase(rs));
            }

            logger.info("Retrieved " + purchases.size() + " purchases");
            return purchases;

        } catch (SQLException e) {
            throw new DatabaseOperationException("getAll purchases", e);
        }
    }

    @Override
    public Optional<Purchase> getById(int id) {
        String sql = "SELECT * FROM purchases WHERE purchase_id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToPurchase(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseOperationException("getById purchase", e);
        }
    }

    @Override
    public Purchase update(int id, Purchase purchase) {
        String sql = "UPDATE purchases SET user_id = ?, content_id = ?, " +
                "purchase_date = ?, price_paid = ? WHERE purchase_id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, purchase.getUserId());
            stmt.setInt(2, purchase.getContentId());
            stmt.setDate(3, Date.valueOf(purchase.getPurchaseDate()));
            stmt.setDouble(4, purchase.getPricePaid());
            stmt.setInt(5, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseOperationException("No purchase found with ID: " + id);
            }

            purchase.setPurchaseId(id);
            logger.info("Updated purchase #" + id);
            return purchase;

        } catch (SQLException e) {
            throw new DatabaseOperationException("update purchase", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM purchases WHERE purchase_id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            logger.info("Deleted purchase #" + id);
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DatabaseOperationException("delete purchase", e);
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM purchases WHERE purchase_id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            throw new DatabaseOperationException("exists purchase", e);
        }
    }

    public List<Purchase> findByUserId(int userId) {
        List<Purchase> purchases = new ArrayList<>();
        String sql = "SELECT * FROM purchases WHERE user_id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                purchases.add(mapResultSetToPurchase(rs));
            }

            return purchases;

        } catch (SQLException e) {
            throw new DatabaseOperationException("findByUserId", e);
        }
    }

    private Purchase mapResultSetToPurchase(ResultSet rs) throws SQLException {
        return new Purchase(
                rs.getInt("purchase_id"),
                rs.getInt("user_id"),
                rs.getInt("content_id"),
                rs.getDate("purchase_date").toLocalDate(),
                rs.getDouble("price_paid")
        );
    }
}