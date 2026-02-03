package com.zaichko.digitalstore;

import com.zaichko.digitalstore.exception.DuplicateResourceException;
import com.zaichko.digitalstore.exception.InvalidInputException;
import com.zaichko.digitalstore.exception.ResourceNotFoundException;
import com.zaichko.digitalstore.model.*;
import com.zaichko.digitalstore.repository.*;
import com.zaichko.digitalstore.service.*;
import com.zaichko.digitalstore.utils.ReflectionUtil;
import com.zaichko.digitalstore.utils.SortingUtil;

import java.util.ArrayList;
import java.util.Comparator;
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

            System.out.println("--- ADDITIONAL METHOD: TOP CREATOR ---");
            topCreator();

            System.out.println("--- LAMBDA EXPRESSION ---");
            lambdaDemo();

            System.out.println("--- DEMONSTRATING RUNTIME INSPECTION ---");
            reflectionDemo();


        } catch (RuntimeException e){
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
        gameService = new GameService(creatorService, gameRepo);
        movieService = new MovieService(creatorService, movieRepo);
        musicAlbumService = new MusicAlbumService(creatorService, musicAlbumRepo);
        purchaseService = new PurchaseService(
                purchaseRepo, userRepo, gameRepo, movieRepo, musicAlbumRepo);
    }

    private static void createDemo(){

        try {
            Creator creator = new Creator("Eric Barone", "USA", "Indie game developer");
            creatorService.createCreator(creator);
            System.out.println("Created content creator:\n" + creator.describe() + "\n");

            Game game = new Game("Stardew Valley", creator, 2016, true);
            gameService.createGame(game);
            System.out.println("Created game:\n" + game.describe() + "\n");

            User user = new User("Yelena", "yelena@mail.com");
            userService.createUser(user);
            System.out.println("Created user:\n" + user.describe() + "\n");

            Purchase purchase = new Purchase(user.getId(), game.getId(), game.getPrice());
            purchaseService.purchaseContent(purchase);
            System.out.println("Purchased content:\n" + purchase.toString() + "\n");

        } catch (ResourceNotFoundException e){
            System.out.println("Something went wrong: " + e.getMessage());
        } finally {
            System.out.println("Entities created successfully \n");
        }

    }

    private static void readDemo(){
        System.out.println("Get all games: ");
        gameService.getAllGames().forEach(game -> System.out.println(game.describe()));

        System.out.println("Get user by ID 1:");
        System.out.println(userService.getUserById(1).describe());

    }

    private static void updateDemo(){
        System.out.println("Change game availability:");
        Game game = gameService.getAllGames().getFirst();
        System.out.println(game.describe() + "\n Changed:");
        game.changeAvailability();
        gameService.updateGame(game.getId(), game);
        System.out.println(game.describe());
        System.out.println("Updated game entity successfully");
    }

    private static void deleteDemo(){
        System.out.println("Delete user with ID 1:");
        try {
            userService.deleteUser(1);
        } catch (ResourceNotFoundException e){
            System.out.println("Deletion validation triggered: " + e.getMessage());
        }

        System.out.println("Showing that user no longer exists:");
        try {
            userService.getUserById(1);
        } catch (ResourceNotFoundException e){
            System.out.println("Validation triggered: " + e.getMessage());
        }


        System.out.println("Delete music album that doesn't exist (id = 999): ");
        try {
            musicAlbumService.deleteMusicAlbum(999); // doesn't exist
        } catch (ResourceNotFoundException e){
            System.out.println("Deletion validation triggered: " + e.getMessage());
        }
    }

    private static void validationDemo(){
        System.out.println("Entity name validation:");
        try{
            Movie invalidMovie = new Movie("", null, 2000, false, false, 1);
            movieService.createMovie(invalidMovie);
        } catch (InvalidInputException e){
            System.out.println("Validation triggered: " + e + "\n");
        }

        System.out.println("User email validation: ");
        try{
            User invalidUser = new User("Christopher", "email");
            userService.createUser(invalidUser);
        } catch (InvalidInputException e){
            System.out.println("Validation triggered: " + e + "\n");
        }

        System.out.println("Numeric validation - invalid track number in music album: ");
        try{
            Creator creator = new Creator("Queen", "The UK", "British rock band");
            MusicAlbum invalidAlbum = new MusicAlbum("Sheer Heart Attack", creator, 1974, true, -1);
            musicAlbumService.createMusicAlbum(invalidAlbum);
        } catch (InvalidInputException e){
            System.out.println("Validation triggered: " + e + "\n");
        }

        System.out.println("Business rule - creator with existing content cannot be deleted:");
        try{
            creatorService.deleteCreator(1);
        } catch (InvalidInputException e){
            System.out.println("Business rule triggered: " + e + "\n");
        }

        System.out.println("Business rule - a user can't purchase same content twice:");
        try{
            Purchase duplicate = new Purchase(4, 5, 10);
            purchaseService.purchaseContent(duplicate);
        } catch (DuplicateResourceException e){
            System.out.println("Business rule triggered: " + e + "\n");
        }
    }

    private static void polymorphismDemo(){
        System.out.println("Polymorphism is shown by DigitalContent's describe() method, where each content entity describes itself uniquely: ");
        List<DigitalContent> content = new ArrayList<>();
        content.addAll(gameService.getAllGames());
        content.addAll(movieService.getAllMovies());
        content.addAll(musicAlbumService.getAllMusicAlbums());

        for(DigitalContent dc : content){
            System.out.println(dc.describe());
        }
    }

    private static void topCreator(){
        Creator top = creatorService.getTopEarningCreator();
        System.out.println("Creator with most revenue:\n" + top.describe() + "\t|\tTotal revenue: " + creatorService.getTopEarnings());
    }

    private static void lambdaDemo(){
        List<Game> sortedGames =
                SortingUtil.sortedCopy(
                        gameService.getAllGames(),
                        Comparator.comparingInt(Game::getReleaseYear)
                );
    }

    private static void reflectionDemo() {
        DigitalContent content = gameService.getAllGames().getFirst();
        ReflectionUtil.inspect(content);
    }
}