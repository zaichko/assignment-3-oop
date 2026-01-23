package com.zaichko.digitalstore.service;

import com.zaichko.digitalstore.exception.ResourceNotFoundException;
import com.zaichko.digitalstore.model.Creator;
import com.zaichko.digitalstore.model.MusicAlbum;
import com.zaichko.digitalstore.repository.CreatorRepository;
import com.zaichko.digitalstore.repository.MusicAlbumRepository;

import java.util.List;

public class MusicAlbumService {

    private final CreatorService creatorService;
    private final MusicAlbumRepository albumRepo;

    public MusicAlbumService(CreatorService creatorService,
                        MusicAlbumRepository albumRepo){
        this.creatorService = creatorService;
        this.albumRepo = albumRepo;
    }

    public void createMusicAlbum(MusicAlbum album) {
        album.validate();

        Creator creator = creatorService.getCreatorById(album.getCreator().getId());

        if (creator == null) {
            throw new ResourceNotFoundException("Creator with id " + album.getCreator().getId() + " does not exist");
        }

        albumRepo.create(album);
    }

    public List<MusicAlbum> getAllMusicAlbums(){
        return albumRepo.getAll();
    }

    public MusicAlbum getMusicAlbumById(int id){

        MusicAlbum album = albumRepo.getById(id);

        if(album == null){
            throw new ResourceNotFoundException("MusicAlbum not found with ID " + id);
        }

        return album;

    }

    public void updateMusicAlbum(int id, MusicAlbum album){

        album.validate();

        if(albumRepo.getById(id) == null){
            throw new ResourceNotFoundException("MusicAlbum not found");
        }

        albumRepo.update(id, album);

    }

    public void deleteMusicAlbum(int id){

        if(albumRepo.getById(id) == null){
            throw new ResourceNotFoundException("MusicAlbum not found");
        }

        albumRepo.delete(id);

    }
    
}
