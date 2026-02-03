package com.zaichko.digitalstore.repository;

import com.zaichko.digitalstore.exception.DatabaseOperationException;
import com.zaichko.digitalstore.model.Purchase;
import com.zaichko.digitalstore.repository.interfaces.PurchaseRepositoryInterface;
import com.zaichko.digitalstore.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseRepository implements PurchaseRepositoryInterface {

    @Override
    public void create(Purchase entity){
        String sql = "INSERT INTO purchases (user_id, content_id, price_paid, purchase_date) VALUES (?, ?, ?, ?)";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS)){

            ps.setInt(1, entity.getUserId());
            ps.setInt(2, entity.getContentId());
            ps.setDouble(3, entity.getPricePaid());
            ps.setDate(4, java.sql.Date.valueOf(entity.getPurchaseDate()));

            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    entity.setPurchaseId(rs.getInt("id"));
                }
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to create a new purchase", e);
        }

    }

    @Override
    public List<Purchase> getAll(){
        List<Purchase> purchases = new ArrayList<>();
        String sql = "SELECT * FROM purchases";

        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()){

            while(rs.next()){

                Purchase purchase = new Purchase(rs.getInt("user_id"),
                        rs.getInt("content_id"),
                        rs.getDouble("price_paid"),
                        rs.getDate("purchase_date").toLocalDate());
                purchase.setPurchaseId(rs.getInt("id"));

                purchases.add(purchase);

            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to get all purchases", e);
        }

        return purchases;
    }

    @Override
    public Purchase getById(Integer id){
        String sql = "SELECT * FROM purchases WHERE id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Purchase purchase = new Purchase(rs.getInt("user_id"),
                            rs.getInt("content_id"),
                            rs.getDouble("price_paid"),
                            rs.getDate("purchase_date").toLocalDate());
                    purchase.setPurchaseId(rs.getInt("id"));

                    return purchase;

                } else {
                    return null;
                }
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to get the purchase", e);
        }
    }

    @Override
    public boolean existsByUserAndContent(int userId, int contendId){
        String sql = "SELECT * FROM purchases WHERE user_id = ? AND content_id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, userId);
            ps.setInt(2, contendId);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return true;
                } else {
                    return false;
                }
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to find purchase by user and content", e);
        }

    }

}
