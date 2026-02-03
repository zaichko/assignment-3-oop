package com.zaichko.digitalstore.repository;

import com.zaichko.digitalstore.exception.DatabaseOperationException;
import com.zaichko.digitalstore.model.Creator;
import com.zaichko.digitalstore.model.Movie;
import com.zaichko.digitalstore.repository.interfaces.CrudRepository;
import com.zaichko.digitalstore.repository.interfaces.MovieRepositoryInterface;
import com.zaichko.digitalstore.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository implements MovieRepositoryInterface {

    @Override
    public void create(Movie entity){
        String sql = """
                        INSERT INTO content (name, creator_id, release_year, available, description, content_type, duration_minutes, rentable) 
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)""";

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
            ps.setInt(7, entity.getDurationMinutes());
            ps.setBoolean(8, entity.getRentability());

            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    entity.setId(rs.getInt("id"));
                }
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to create a new movie", e);
        }
    }

    @Override
    public List<Movie> getAll(){
        List<Movie> movies = new ArrayList<>();
        String sql = """
                        SELECT 
                            c.id           AS creator_id,
                            c.name         AS creator_name,
                            c.country      AS creator_country,
                            c.bio          AS creator_bio,
                            m.id           AS movie_id,
                            m.name         AS movie_name,
                            m.release_year,
                            m.available,
                            m.description,
                            m.duration_minutes,
                            m.rentable
                        FROM content m
                        JOIN creators c ON m.creator_id = c.id
                        WHERE m.content_type = 'Movie'""";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                Creator creator = new Creator(rs.getString("creator_name"),
                        rs.getString("creator_country"),
                        rs.getString("creator_bio"));
                creator.setId(rs.getInt("creator_id"));

                Movie movie = new Movie(rs.getString("movie_name"),
                        creator,
                        rs.getInt("release_year"),
                        rs.getBoolean("available"),
                        rs.getBoolean("rentable"),
                        rs.getInt("duration_minutes"));
                movie.setId(rs.getInt("movie_id"));
                movie.setDescription(rs.getString("description"));

                movies.add(movie);
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to get all movies", e);
        }

        return movies;
    }

    @Override
    public Movie getById(Integer id){
        String sql = """       
                SELECT 
                        c.id           AS creator_id,
                        c.name         AS creator_name,
                        c.country      AS creator_country,
                        c.bio          AS creator_bio,
                        m.id           AS movie_id,
                        m.name         AS movie_name,
                        m.release_year,
                        m.available,
                        m.description,
                        m.duration_minutes,
                        m.rentable
                FROM content m
                JOIN creators c ON m.creator_id = c.id
                WHERE m.content_type = 'Movie' AND m.id = ?""";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Creator creator = new Creator(rs.getString("creator_name"),
                            rs.getString("creator_country"),
                            rs.getString("creator_bio"));
                    creator.setId(rs.getInt("creator_id"));

                    Movie movie = new Movie(rs.getString("movie_name"),
                            creator,
                            rs.getInt("release_year"),
                            rs.getBoolean("available"),
                            rs.getBoolean("rentable"),
                            rs.getInt("duration_minutes"));
                    movie.setId(rs.getInt("movie_id"));
                    movie.setDescription(rs.getString("description"));

                    return movie;
                } else {
                    return null;
                }
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to get movie",e);
        }
    }

    @Override
    public void update(Integer id, Movie entity){
        String sql = """
                    UPDATE content 
                    SET name = ?, creator_id = ?, release_year = ?, available = ?, description = ?, duration_minutes = ?, rentable = ? 
                    WHERE id = ? AND content_type = 'Movie'""";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getCreator().getId());
            ps.setInt(3, entity.getReleaseYear());
            ps.setBoolean(4, entity.isAvailable());
            ps.setString(5, entity.getDescription());
            ps.setInt(6, entity.getDurationMinutes());
            ps.setBoolean(7, entity.getRentability());
            ps.setInt(8, id);

            ps.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to update movie",e);
        }
    }

    @Override
    public void delete(Integer id){
        String sql = "DELETE FROM content WHERE id = ? AND content_type = 'Movie'";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to delete movie", e);
        }
    }

}
