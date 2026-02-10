package kz.aitu.digitalcontent;

import kz.aitu.digitalcontent.model.*;
import kz.aitu.digitalcontent.patterns.*;
import kz.aitu.digitalcontent.utils.ReflectionUtils;
import kz.aitu.digitalcontent.utils.SortingUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OOPFeaturesDemo {

    public static void main(String[] args) {
        demonstrateFactoryPattern();
        demonstrateBuilderPattern();
        demonstrateSingletonPattern();
        demonstratePolymorphism();
        demonstrateLambdas();
        demonstrateReflection();
        demonstrateInterfaces();
    }

    private static void demonstrateFactoryPattern() {
        System.out.println("\n========== FACTORY PATTERN DEMO ==========");

        DigitalContent game = DigitalContentFactory.createContent("GAME", "Cyberpunk 2077");
        DigitalContent movie = DigitalContentFactory.createContent("MOVIE", "Interstellar");
        DigitalContent album = DigitalContentFactory.createContent("ALBUM", "Abbey Road");

        System.out.println("Created using Factory:");
        System.out.println(game.displayInfo());
        System.out.println(movie.displayInfo());
        System.out.println(album.displayInfo());
    }

    private static void demonstrateBuilderPattern() {
        System.out.println("\n========== BUILDER PATTERN DEMO ==========");

        Purchase purchase = PurchaseBuilder.newPurchase()
                .purchaseId(1)
                .userId(101)
                .contentId(202)
                .pricePaid(29.99)
                .purchaseDate(LocalDate.now())
                .build();

        System.out.println("Built Purchase:");
        System.out.println(purchase.displayInfo());
    }

    private static void demonstrateSingletonPattern() {
        System.out.println("\n========== SINGLETON PATTERN DEMO ==========");

        DatabaseConfig db1 = DatabaseConfig.getInstance();
        DatabaseConfig db2 = DatabaseConfig.getInstance();

        System.out.println("DatabaseConfig instances are same: " + (db1 == db2));
        System.out.println("Database URL: " + db1.getUrl());

        LoggerService logger1 = LoggerService.getInstance();
        LoggerService logger2 = LoggerService.getInstance();

        System.out.println("LoggerService instances are same: " + (logger1 == logger2));
        logger1.info("This is a singleton logger message");
    }

    private static void demonstratePolymorphism() {
        System.out.println("\n========== POLYMORPHISM DEMO ==========");

        Creator creator1 = new Creator("USA", "Famous director");
        Creator creator2 = new Creator("UK", "Music legend");

        List<DigitalContent> contents = new ArrayList<>();
        contents.add(new Game(1, "Elden Ring", 2022, true, creator1, "Epic RPG"));
        contents.add(new Movie(2, "Dune", 2021, true, creator1, "Sci-fi epic", true, 155));
        contents.add(new MusicAlbum(3, "1989", 2014, true, creator2, "Pop album", 13));

        System.out.println("Polymorphic method calls:");
        for (DigitalContent content : contents) {
            System.out.println(content.describe());
            System.out.println(content.availabilityString());
            System.out.println();
        }
    }

    private static void demonstrateLambdas() {
        System.out.println("\n========== LAMBDA EXPRESSIONS DEMO ==========");

        Creator creator = new Creator("Japan", "Game developer");
        List<DigitalContent> contents = new ArrayList<>();
        contents.add(new Game(5, "Zelda", 2023, true, creator, "Adventure"));
        contents.add(new Game(3, "Mario", 2021, true, creator, "Platform"));
        contents.add(new Game(1, "Pokemon", 2022, true, creator, "RPG"));

        System.out.println("Before sorting:");
        contents.forEach(c -> System.out.println(c.getId() + ": " + c.getName()));

        SortingUtils.sortById(contents);

        System.out.println("\nAfter sorting by ID (using lambda):");
        contents.forEach(c -> System.out.println(c.getId() + ": " + c.getName()));

        System.out.println("\nFiltering with lambda:");
        long availableCount = SortingUtils.countMatching(contents,
                c -> c.isAvailable());
        System.out.println("Available content count: " + availableCount);
    }

    private static void demonstrateReflection() {
        System.out.println("\n========== REFLECTION (RTTI) DEMO ==========");

        Creator creator = new Creator("USA", "Director");
        Movie movie = new Movie(1, "Inception", 2010, true, creator,
                "Mind-bending", true, 148);

        String inspection = ReflectionUtils.inspectObject(movie);
        System.out.println(inspection);

        boolean isContent = ReflectionUtils.isInstanceOf(movie, DigitalContent.class);
        boolean isMovie = ReflectionUtils.isInstanceOf(movie, Movie.class);

        System.out.println("Is DigitalContent? " + isContent);
        System.out.println("Is Movie? " + isMovie);
    }

    private static void demonstrateInterfaces() {
        System.out.println("\n========== INTERFACE METHODS DEMO ==========");

        User user = new User(1, "Alice", "alice@example.com");

        if (user instanceof Validatable) {
            Validatable validatable = (Validatable) user;
            String message = validatable.getValidationMessage("Email", "Invalid format");
            System.out.println(message);
        }

        boolean isValid = Validatable.isValidString("Hello");
        boolean isEmpty = Validatable.isValidString("");

        System.out.println("'Hello' is valid string: " + isValid);
        System.out.println("Empty is valid string: " + isEmpty);

        double discounted = PricedItem.calculateDiscount(100.0, 20.0);
        System.out.println("Price after 20% discount on $100: $" + discounted);
    }
}