package view;

import controller.BookController;
import controller.LoanController;
import controller.MemberController;
import javax.swing.JOptionPane;

public class MenuView {
    // Controller dependencies for handling user actions
    private BookController bookController;
    private MemberController memberController;
    private LoanController loanController;

    // Constructor initializes all controllers
    public MenuView(BookController bookController, MemberController memberController, LoanController loanController) {
        this.bookController = bookController;
        this.memberController = memberController;
        this.loanController = loanController;
    }

    // Main menu method that runs the application loop
    public void showMainMenu() {
        while (true) {
            // Define main menu options
            String[] options = {
                    "Book Management",
                    "Member Management",
                    "Loan Management",
                    "Reports",
                    "Exit"
            };

            // Display main menu dialog
            int choice = JOptionPane.showOptionDialog(null,
                    "=== LIBRARY MANAGEMENT SYSTEM ===\nSelect an option:",
                    "Main Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            // Handle user selection
            switch (choice) {
                case 0:
                    showBookMenu(); // Navigate to book management
                    break;
                case 1:
                    showMemberMenu(); // Navigate to member management
                    break;
                case 2:
                    showLoanMenu(); // Navigate to loan management
                    break;
                case 3:
                    showReportsMenu(); // Navigate to reports
                    break;
                case 4:
                case -1: // Close button or exit option
                    JOptionPane.showMessageDialog(null, "Thank you for using the system!");
                    return; // Exit application
                default:
                    break;
            }
        }
    }

    // Book management submenu
    private void showBookMenu() {
        String[] options = {
                "Add Book",
                "List All Books",
                "Search by Title",
                "Search by Author",
                "Update Book",
                "Delete Book",
                "Back"
        };

        // Display book menu dialog
        int choice = JOptionPane.showOptionDialog(null,
                "=== BOOK MANAGEMENT ===",
                "Book Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        // Handle book menu selection
        switch (choice) {
            case 0:
                bookController.addBook(); // Add new book
                break;
            case 1:
                bookController.showAllBooks(); // Display all books
                break;
            case 2:
                bookController.searchBooksByTitle(); // Search books by title
                break;
            case 3:
                bookController.searchBooksByAuthor(); // Search books by author
                break;
            case 4:
                bookController.updateBook(); // Update existing book
                break;
            case 5:
                bookController.deleteBook(); // Delete book
                break;
            default:
                break; // Return to main menu
        }
    }

    // Member management submenu
    private void showMemberMenu() {
        String[] options = {
                "Add Member",
                "List All Members",
                "List Active Members",
                "Update Member",
                "Deactivate Member",
                "Back"
        };

        // Display member menu dialog
        int choice = JOptionPane.showOptionDialog(null,
                "=== MEMBER MANAGEMENT ===",
                "Member Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        // Handle member menu selection
        switch (choice) {
            case 0:
                memberController.addMember(); // Add new member
                break;
            case 1:
                memberController.showAllMembers(); // Display all members
                break;
            case 2:
                memberController.showActiveMembers(); // Display active members only
                break;
            case 3:
                memberController.updateMember(); // Update member information
                break;
            case 4:
                memberController.deactivateMember(); // Deactivate member
                break;
            default:
                break; // Return to main menu
        }
    }

    // Loan management submenu
    private void showLoanMenu() {
        String[] options = {
                "Create Loan",
                "Return Book",
                "List All Loans",
                "List Active Loans",
                "List Loans by Member",
                "Back"
        };

        // Display loan menu dialog
        int choice = JOptionPane.showOptionDialog(null,
                "=== LOAN MANAGEMENT ===",
                "Loan Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        // Handle loan menu selection
        switch (choice) {
            case 0:
                loanController.createLoan(); // Create new loan
                break;
            case 1:
                loanController.returnLoan(); // Process book return
                break;
            case 2:
                loanController.showAllLoans(); // Display all loans
                break;
            case 3:
                loanController.showActiveLoans(); // Display active loans only
                break;
            case 4:
                loanController.showMemberLoans(); // Display loans by specific member
                break;
            default:
                break; // Return to main menu
        }
    }

    // Reports submenu
    private void showReportsMenu() {
        String[] options = {
                "Overdue Loans",
                "Back"
        };

        // Display reports menu dialog
        int choice = JOptionPane.showOptionDialog(null,
                "=== REPORTS ===",
                "Reports Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        // Handle reports menu selection
        switch (choice) {
            case 0:
                loanController.showOverdueLoans(); // Display overdue loans report
                break;
            default:
                break; // Return to main menu
        }
    }
}