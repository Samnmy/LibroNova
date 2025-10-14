package app;

import config.DatabaseConfig;
import controller.BookController;
import controller.LoanController;
import controller.MemberController;
import view.MenuView;

public class Main {
    public static void main(String[] args) {
        // Initialize database connection and create necessary tables
        DatabaseConfig.initializeDatabase();

        // Initialize controllers for handling business operations
        BookController bookController = new BookController();
        MemberController memberController = new MemberController();
        LoanController loanController = new LoanController();

        // Create main menu view and pass controllers as dependencies
        MenuView menuView = new MenuView(bookController, memberController, loanController);

        // Display the main menu to start user interaction
        menuView.showMainMenu();
    }
}