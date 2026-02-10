package kz.aitu.digitalcontent;

import kz.aitu.digitalcontent.utils.DatabaseInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DigitalcontentApplication {

    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();

        SpringApplication.run(DigitalcontentApplication.class, args);

        System.out.println("\n========================================");
        System.out.println("   Digital Content Platform API");
        System.out.println("   Server running on: http://localhost:8080");
        System.out.println("========================================\n");
    }
}