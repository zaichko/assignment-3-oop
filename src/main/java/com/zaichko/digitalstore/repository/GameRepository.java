package com.zaichko.digitalstore.repository;

import com.zaichko.digitalstore.exception.DatabaseOperationException;
import com.zaichko.digitalstore.model.Creator;
import com.zaichko.digitalstore.model.Game;
import com.zaichko.digitalstore.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameRepository implements CrudRepository<Game>{

    @Override
    public void create(Game entity){
        String sql = """
                        INSERT INTO content (name, creator_id, release_year, available, description, content_type) 
                        VALUES (?, ?, ?, ?, ?, ?)""";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getCreator().getId());
            ps.setInt(3, entity.getReleaseYear());
            ps.setBoolean(4,entity.isAvailable());
            ps.setString(5,entity.getDescription());
            ps.setString(6, entity.getContentType());

            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    entity.setId(rs.getInt("id"));
                }
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to create a new game", e);
        }
    }

    @Override
    public List<Game> getAll(){
        List<Game> games = new ArrayList<>();
        String sql = """
                        SELECT 
                            c.id           AS creator_id,
                            c.name         AS creator_name,
                            c.country      AS creator_country,
                            c.bio          AS creator_bio,
                            g.id           AS game_id,
                            g.name         AS game_name,
                            g.release_year,
                            g.available,
                            g.description 
                        FROM content g
                        JOIN creators c ON g.creator_id = c.id
                        WHERE g.content_type = 'Game'""";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                Creator creator = new Creator(rs.getString("creator_name"),
                        rs.getString("creator_country"),
                        rs.getString("creator_bio"));
                creator.setId(rs.getInt("creator_id"));

                Game game = new Game(rs.getString("game_name"),
                        creator,
                        rs.getInt("release_year"),
                        rs.getBoolean("available"));
                game.setId(rs.getInt("game_id"));
                game.setDescription(rs.getString("description"));

                games.add(game);
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to get all games", e);
        }

        return games;
    }

    @Override
    public Game getById(Integer id){
        String sql = """       
                SELECT
                        c.id           AS creator_id,
                        c.name         AS creator_name,
                        c.country      AS creator_country,
                        c.bio          AS creator_bio,
                        g.id           AS game_id,
                        g.name         AS game_name,
                        g.release_year,
                        g.available,
                        g.description
                FROM content g
                JOIN creators c ON g.creator_id = c.id
                WHERE g.id = ? AND content_type = 'Game'""";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Creator creator = new Creator(rs.getString("creator_name"),
                            rs.getString("creator_country"),
                            rs.getString("creator_bio"));
                    creator.setId(rs.getInt("creator_id"));

                    Game game = new Game(rs.getString("game_name"),
                            creator,
                            rs.getInt("release_year"),
                            rs.getBoolean("available"));
                    game.setId(rs.getInt("game_id"));
                    game.setDescription(rs.getString("description"));

                    return game;
                } else {
                    return null;
                }
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to get the game",e);
        }
    }

    @Override
    public void update(Integer id, Game entity){
        String sql = """
                    UPDATE content 
                    SET name = ?, creator_id = ?, release_year = ?, available = ?, description = ? 
                    WHERE id = ? AND content_type = 'Game'""";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getCreator().getId());
            ps.setInt(3, entity.getReleaseYear());
            ps.setBoolean(4, entity.isAvailable());
            ps.setString(5, entity.getDescription());
            ps.setInt(6, id);

            ps.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to update game",e);
        }
    }

    @Override
    public void delete(Integer id){
        String sql = "DELETE FROM content WHERE id = ? AND content_type = 'Game'";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to delete game", e);
        }
    }

}
