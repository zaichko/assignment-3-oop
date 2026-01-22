package com.zaichko.digitalstore;

import com.zaichko.digitalstore.exception.DuplicateResourceException;
import com.zaichko.digitalstore.exception.InvalidInputException;
import com.zaichko.digitalstore.exception.ResourceNotFoundException;
import com.zaichko.digitalstore.model.*;
import com.zaichko.digitalstore.repository.*;
import com.zaichko.digitalstore.service.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static UserService userService;
    private static CreatorService creatorService;
    private static GameService gameService;
    private static MovieService movieService;
    private static MusicAlbumService musicAlbumService;
    private static PurchaseService purchaseService;

    public static void main(String[] args) {

        initServices();

        try{

            System.out.println("--- CREATE ---");
            createDemo();

            System.out.println("--- READ ---");
            readDemo();

            System.out.println("--- UPDATE ---");
            updateDemo();

            System.out.println("--- DELETE WITH VALIDATION ---");
            deleteDemo();

            System.out.println("--- VALIDATION AND BUSINESS RULES ---");
            validationDemo();

            System.out.println("--- POLYMORPHISM DEMONSTRATION ---");
            polymorphismDemo();


        } catch (Exception e){
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private static void initServices(){
        UserRepository userRepo = new UserRepository();
        CreatorRepository creatorRepo = new CreatorRepository();
        GameRepository gameRepo = new GameRepository();
        MovieRepository movieRepo = new MovieRepository();
        MusicAlbumRepository musicAlbumRepo = new MusicAlbumRepository();
        PurchaseRepository purchaseRepo = new PurchaseRepository();

        userService = new UserService(userRepo);
        creatorService = new CreatorService(creatorRepo);
        gameService = new GameService(creatorRepo, gameRepo);
        movieService = new MovieService(creatorRepo, movieRepo);
        musicAlbumService = new MusicAlbumService(creatorRepo, musicAlbumRepo);
        purchaseService = new PurchaseService(
                purchaseRepo, userRepo, gameRepo, movieRepo, musicAlbumRepo);
    }

    private static void createDemo(){

        Creator creator = new Creator("Eric Barone", "USA", "");

        creatorService.createCreator(creator);

        Game game = new Game("Stardew Valley", creator, 2016, true);

        gameService.createGame(game);

        User user = new User("Yelena", "yelena@mail.com");
        userService.createUser(user);

        Purchase purchase = new Purchase(user.getId(), game.getId(), game.getPrice());
        purchaseService.purchaseContent(purchase);

        System.out.println("Entities created successfully");

    }

    private static void readDemo(){
        gameService.getAllGames().forEach(game -> System.out.println(game.describe()));
        purchaseService.getAllPurchases().forEach(purchase -> System.out.println(purchase.toString()));

    }

    private static void updateDemo(){
        Game game = gameService.getAllGames().getFirst();
        game.changeAvailability();
        gameService.updateGame(game.getId(), game);
        System.out.println("Updated game availability");
    }

    private static void deleteDemo(){
        try {
            musicAlbumService.deleteMusicAlbum(999); // doesn't exist
        } catch (ResourceNotFoundException e){
            System.out.println("Deletion validation triggered: " + e);
        }
    }

    private static void validationDemo(){
        try{
            User invalidUser = new User("", "no-email");
            userService.createUser(invalidUser);
        } catch (InvalidInputException e){
            System.out.println("Validation triggered: " + e);
        }
        try{
            Purchase duplicate = new Purchase(1, 1, 10);
            purchaseService.purchaseContent(duplicate);
        } catch (DuplicateResourceException e){
            System.out.println("Business rule triggered: " + e);
        }
    }

    private static void polymorphismDemo(){
        List<DigitalContent> content = new ArrayList<>();
        content.addAll(gameService.getAllGames());
        content.addAll(movieService.getAllMovies());

        for(DigitalContent dc : content){
            System.out.println(dc.describe());
        }
    }
}