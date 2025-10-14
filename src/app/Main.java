package app;

import config.DatabaseConfig;
import controller.BookController;
import controller.LoanController;
import controller.MemberController;
import view.MenuView;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    // Main method - the starting point of the application
    public static void main(String[] args) {
        try {
            logger.info("=== LibroNova Library Management System Starting ===");

            // Step 1: Set up the database with all required tables
            logger.info("Initializing database...");
            DatabaseConfig.initializeDatabase();

            // Step 2: Create controllers that handle business logic
            logger.info("Initializing controllers...");
            BookController bookController = new BookController();
            MemberController memberController = new MemberController();
            LoanController loanController = new LoanController();

            // Step 3: Launch the main user interface menu
            logger.info("Starting main menu...");
            MenuView menuView = new MenuView(bookController, memberController, loanController);
            menuView.showMainMenu();

            logger.info("=== LibroNova Application Shutting Down ===");

        } catch (Exception e) {
            // Handle any critical errors during application startup
            logger.severe("Fatal error starting application: " + e.getMessage());
            System.err.println("Critical error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}