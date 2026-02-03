package com.zaichko.digitalstore.repository;

import com.zaichko.digitalstore.exception.DatabaseOperationException;
import com.zaichko.digitalstore.model.Creator;
import com.zaichko.digitalstore.model.MusicAlbum;
import com.zaichko.digitalstore.repository.interfaces.CrudRepository;
import com.zaichko.digitalstore.repository.interfaces.MusicAlbumRepositoryInterface;
import com.zaichko.digitalstore.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicAlbumRepository implements MusicAlbumRepositoryInterface {

    @Override
    public void create(MusicAlbum entity){
        String sql = """
                        INSERT INTO content (name, creator_id, release_year, available, description, content_type, track_count) 
                        VALUES (?, ?, ?, ?, ?, ?, ?)""";

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
            ps.setInt(7, entity.getCountTracks());

            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    entity.setId(rs.getInt("id"));
                }
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to create a new music album", e);
        }
    }

    @Override
    public List<MusicAlbum> getAll(){
        List<MusicAlbum> musicAlbums = new ArrayList<>();
        String sql = """
                        SELECT 
                            c.id           AS creator_id,
                            c.name         AS creator_name,
                            c.country      AS creator_country,
                            c.bio          AS creator_bio,
                            ma.id           AS album_id,
                            ma.name         AS album_name,
                            ma.release_year,
                            ma.available,
                            ma.description,
                            ma.track_count
                        FROM content ma
                        JOIN creators c ON ma.creator_id = c.id
                        WHERE ma.content_type = 'MusicAlbum'""";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                Creator creator = new Creator(rs.getString("creator_name"),
                        rs.getString("creator_country"),
                        rs.getString("creator_bio"));
                creator.setId(rs.getInt("creator_id"));

                 MusicAlbum musicAlbum = new MusicAlbum(rs.getString("album_name"),
                        creator,
                        rs.getInt("release_year"),
                        rs.getBoolean("available"),
                        rs.getInt("track_count"));
                musicAlbum.setId(rs.getInt("album_id"));
                musicAlbum.setDescription(rs.getString("description"));

                musicAlbums.add(musicAlbum);
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to get all music albums", e);
        }

        return musicAlbums;
    }

    @Override
    public MusicAlbum getById(Integer id){
        String sql = """       
                SELECT 
                        c.id           AS creator_id,
                        c.name         AS creator_name,
                        c.country      AS creator_country,
                        c.bio          AS creator_bio,
                        ma.id           AS album_id,
                        ma.name         AS album_name,
                        ma.release_year,
                        ma.available,
                        ma.description,
                        ma.track_count
                FROM content ma
                JOIN creators c ON ma.creator_id = c.id
                WHERE ma.content_type = 'MusicAlbum' AND ma.id = ?""";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Creator creator = new Creator(rs.getString("creator_name"),
                            rs.getString("creator_country"),
                            rs.getString("creator_bio"));
                    creator.setId(rs.getInt("creator_id"));

                    MusicAlbum musicAlbum = new MusicAlbum(rs.getString("album_name"),
                            creator,
                            rs.getInt("release_year"),
                            rs.getBoolean("available"),
                            rs.getInt("track_count"));
                    musicAlbum.setId(rs.getInt("album_id"));
                    musicAlbum.setDescription(rs.getString("description"));

                    return musicAlbum;
                } else {
                    return null;
                }
            }

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to get a music album", e);
        }
    }

    @Override
    public void update(Integer id, MusicAlbum entity){
        String sql = """
                    UPDATE content 
                    SET name = ?, creator_id = ?, release_year = ?, available = ?, description = ?, track_count = ? 
                    WHERE id = ? AND content_type = 'MusicAlbum'""";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getCreator().getId());
            ps.setInt(3, entity.getReleaseYear());
            ps.setBoolean(4, entity.isAvailable());
            ps.setString(5, entity.getDescription());
            ps.setInt(6, entity.getCountTracks());
            ps.setInt(7, id);

            ps.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to update the music album", e);
        }
    }

    @Override
    public void delete(Integer id){
        String sql = "DELETE FROM content WHERE id = ? AND content_type = 'MusicAlbum'";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseOperationException("Failed to delete the music album", e);
        }
    }

}
