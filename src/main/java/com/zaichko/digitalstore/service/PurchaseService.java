package com.zaichko.digitalstore.service;

import com.zaichko.digitalstore.exception.DuplicateResourceException;
import com.zaichko.digitalstore.exception.InvalidInputException;
import com.zaichko.digitalstore.exception.ResourceNotFoundException;
import com.zaichko.digitalstore.model.*;
import com.zaichko.digitalstore.repository.*;

import java.util.List;

public class PurchaseService {

    private final PurchaseRepository purchaseRepo;
    private final UserRepository userRepo;
    private final GameRepository gameRepo;
    private final MovieRepository movieRepo;
    private final MusicAlbumRepository albumRepo;

    public PurchaseService(PurchaseRepository purchaseRepo,
                           UserRepository userRepo,
                           GameRepository gameRepo,
                           MovieRepository movieRepo,
                           MusicAlbumRepository albumRepo) {
        this.purchaseRepo = purchaseRepo;
        this.userRepo = userRepo;
        this.gameRepo = gameRepo;
        this.movieRepo = movieRepo;
        this.albumRepo = albumRepo;
    }

    public void purchaseContent(Purchase purchase){

        purchase.validate();

        if(userRepo.getById(purchase.getUserId()) == null){
            throw new ResourceNotFoundException("User not found");
        }

        DigitalContent content = findContentById(purchase.getContentId());

        if(!content.isAvailable()){
            throw new InvalidInputException("Content is not available");
        }

        if(purchaseRepo.existsByUserAndContent(purchase.getUserId(), purchase.getContentId())){
            throw new DuplicateResourceException("Content already purchased");
        }

        purchaseRepo.create(purchase);

    }

    private DigitalContent findContentById(int contentId){

        Game game = gameRepo.getById(contentId);
        if(game != null) return game;

        Movie movie = movieRepo.getById(contentId);
        if(movie != null) return movie;

        MusicAlbum album = albumRepo.getById(contentId);
        if(album != null) return album;

        throw new ResourceNotFoundException("Content not found with ID " + contentId);

    }

    public List<Purchase> getAllPurchases(){
        return purchaseRepo.getAll();
    }

    public Purchase getPurchaseById(int id){

        Purchase purchase = purchaseRepo.getById(id);

        if(purchase == null){
            throw new ResourceNotFoundException("Purchase not found with ID " + id);
        }

        return purchase;

    }

}
