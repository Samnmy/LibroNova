package app;

import config.DatabaseConfig;
import controller.BookController;
import controller.LoanController;
import controller.MemberController;
import view.MenuView;

public class Main {
    public static void main(String[] args) {
        // Initialize database connection
        DatabaseConfig.initializeDatabase();

        // Initialize controllers
        BookController bookController = new BookController();
        MemberController memberController = new MemberController();
        LoanController loanController = new LoanController();

        // Start main menu
        MenuView menuView = new MenuView(bookController, memberController, loanController);
        menuView.showMainMenu();
    }
}