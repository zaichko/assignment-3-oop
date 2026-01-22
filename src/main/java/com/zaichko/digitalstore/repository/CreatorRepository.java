package com.zaichko.digitalstore.repository;

import com.zaichko.digitalstore.exception.DatabaseOperationException;
import com.zaichko.digitalstore.model.Creator;
import com.zaichko.digitalstore.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CreatorRepository implements CrudRepository<Creator> {

    @Override
    public void create(Creator entity){
        String sql = "INSERT INTO creators (name, country, bio) VALUES (?, ?, ?)";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, entity.getName());
            ps.setString(2, entity.getCountry());
            ps.setString(3, entity.getBio());

            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    entity.setId(rs.getInt("id"));
                }
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to create a new creator", e);
        }
    }

    @Override
    public List<Creator> getAll(){
        List<Creator> creators = new ArrayList<>();
        String sql = "SELECT * FROM creators";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                Creator creator = new Creator(rs.getString("name"),
                        rs.getString("country"),
                        rs.getString("bio"));
                creator.setId(rs.getInt("id"));
                creators.add(creator);
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to get all creators", e);
        }

        return creators;
    }

    @Override
    public Creator getById(Integer id){
        String sql = "SELECT * FROM creators WHERE id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Creator creator = new Creator(rs.getString("name"),
                            rs.getString("country"),
                            rs.getString("bio"));
                    creator.setId(rs.getInt("id"));
                    return creator;
                } else {
                    return null;
                }
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to get creator",e);
        }
    }

    @Override
    public void update(Integer id, Creator entity){
        String sql = "UPDATE creators SET name = ?, country = ?, bio = ? WHERE id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, entity.getName());
            ps.setString(2, entity.getCountry());
            ps.setString(3, entity.getBio());
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to update creator",e);
        }
    }

    @Override
    public void delete(Integer id){
        String sql = "DELETE FROM creators WHERE id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to delete creator", e);
        }
    }

}
