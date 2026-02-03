package com.zaichko.digitalstore.repository;

import com.zaichko.digitalstore.exception.DatabaseOperationException;
import com.zaichko.digitalstore.model.User;
import com.zaichko.digitalstore.repository.interfaces.CrudRepository;
import com.zaichko.digitalstore.repository.interfaces.UserRepositoryInterface;
import com.zaichko.digitalstore.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements UserRepositoryInterface {

    @Override
    public void create(User entity){
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());

            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    entity.setId(rs.getInt("id"));
                }
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to create a new user", e);
        }
    }

    @Override
    public List<User> getAll(){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                User user = new User(rs.getString("name"), rs.getString("email"));
                user.setId(rs.getInt("id"));
                users.add(user);
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to get all users", e);
        }

        return users;
    }

    @Override
    public User getById(Integer id){
        String sql = "SELECT * FROM users WHERE id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    User user = new User(rs.getString("name"), rs.getString("email"));
                    user.setId(rs.getInt("id"));
                    return user;
                } else {
                    return null;
                }
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to get user",e);
        }
    }

    @Override
    public User getByEmail(String email){
        String sql = "SELECT * FROM users WHERE email = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, email);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    User user = new User(rs.getString("name"), rs.getString("email"));
                    user.setId(rs.getInt("id"));
                    return user;
                } else {
                    return null;
                }
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to get user",e);
        }

    }

    @Override
    public void update(Integer id, User entity){
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());
            ps.setInt(3, id);

            ps.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to update user",e);
        }
    }

    @Override
    public void delete(Integer id){
        String sql = "DELETE FROM users WHERE id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to delete user", e);
        }
    }

}
