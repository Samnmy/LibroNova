package view;

import controller.BookController;
import controller.LoanController;
import controller.MemberController;
import javax.swing.JOptionPane;

public class MenuView {
    // Controller objects to handle business logic for each module
    private BookController bookController;
    private MemberController memberController;
    private LoanController loanController;

    // Constructor receives all controllers needed for the application
    public MenuView(BookController bookController, MemberController memberController, LoanController loanController) {
        this.bookController = bookController;
        this.memberController = memberController;
        this.loanController = loanController;
    }

    // Main application loop - displays the primary menu and handles navigation
    public void showMainMenu() {
        while (true) {
            // Define the main menu options available to users
            String[] options = {
                    "Book Management",
                    "Member Management",
                    "Loan Management",
                    "Reports",
                    "Exit"
            };

            // Display the main menu dialog to the user
            int choice = JOptionPane.showOptionDialog(null,
                    "=== LIBRARY MANAGEMENT SYSTEM ===\nSelect an option:",
                    "Main Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            // Process the user's menu selection
            switch (choice) {
                case 0:
                    showBookMenu(); // Navigate to book management functions
                    break;
                case 1:
                    showMemberMenu(); // Navigate to member management functions
                    break;
                case 2:
                    showLoanMenu(); // Navigate to loan management functions
                    break;
                case 3:
                    showReportsMenu(); // Navigate to reports and exports
                    break;
                case 4:
                case -1: // User selected Exit or closed the window
                    JOptionPane.showMessageDialog(null, "Thank you for using the system!");
                    return; // Exit the application completely
                default:
                    break; // No action for invalid selections
            }
        }
    }

    // Book management submenu - handles all book-related operations
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

        // Display book management menu
        int choice = JOptionPane.showOptionDialog(null,
                "=== BOOK MANAGEMENT ===",
                "Book Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        // Route to the appropriate book controller method
        switch (choice) {
            case 0:
                bookController.addBook(); // Add a new book to the system
                break;
            case 1:
                bookController.showAllBooks(); // Display all books in the catalog
                break;
            case 2:
                bookController.searchBooksByTitle(); // Search books by title keyword
                break;
            case 3:
                bookController.searchBooksByAuthor(); // Search books by author name
                break;
            case 4:
                bookController.updateBook(); // Update existing book information
                break;
            case 5:
                bookController.deleteBook(); // Remove a book from the system
                break;
            default:
                break; // Return to main menu
        }
    }

    // Member management submenu - handles all member-related operations
    private void showMemberMenu() {
        String[] options = {
                "Add Member",
                "List All Members",
                "List Active Members",
                "Update Member",
                "Deactivate Member",
                "Back"
        };

        // Display member management menu
        int choice = JOptionPane.showOptionDialog(null,
                "=== MEMBER MANAGEMENT ===",
                "Member Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        // Route to the appropriate member controller method
        switch (choice) {
            case 0:
                memberController.addMember(); // Register a new library member
                break;
            case 1:
                memberController.showAllMembers(); // Display all members (active and inactive)
                break;
            case 2:
                memberController.showActiveMembers(); // Display only active members
                break;
            case 3:
                memberController.updateMember(); // Update member information
                break;
            case 4:
                memberController.deactivateMember(); // Deactivate a member account
                break;
            default:
                break; // Return to main menu
        }
    }

    // Loan management submenu - handles all loan-related operations
    private void showLoanMenu() {
        String[] options = {
                "Create Loan",
                "Return Book",
                "List All Loans",
                "List Active Loans",
                "List Loans by Member",
                "Back"
        };

        // Display loan management menu
        int choice = JOptionPane.showOptionDialog(null,
                "=== LOAN MANAGEMENT ===",
                "Loan Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        // Route to the appropriate loan controller method
        switch (choice) {
            case 0:
                loanController.createLoan(); // Create a new book loan
                break;
            case 1:
                loanController.returnLoan(); // Process a book return
                break;
            case 2:
                loanController.showAllLoans(); // Display all loans (active and returned)
                break;
            case 3:
                loanController.showActiveLoans(); // Display only active/outstanding loans
                break;
            case 4:
                loanController.showMemberLoans(); // Display loans for a specific member
                break;
            default:
                break; // Return to main menu
        }
    }

    // Reports submenu - handles data export and reporting functions
    private void showReportsMenu() {
        // Menu options in Spanish as requested (no translation of JOptionPane text)
        String[] options = {
                "Préstamos Vencidos",
                "Exportar Catálogo de Libros (CSV)",
                "Exportar Lista de Socios (CSV)",
                "Exportar Todos los Préstamos (CSV)",
                "Volver"
        };

        // Display reports menu in Spanish
        int choice = JOptionPane.showOptionDialog(null,
                "=== REPORTES Y EXPORTACIÓN ===",
                "Menú de Reportes",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        // Route to the appropriate reporting function
        switch (choice) {
            case 0:
                loanController.showOverdueLoans(); // Display overdue loans
                break;
            case 1:
                bookController.exportBooksToCSV(); // Export book catalog to CSV
                break;
            case 2:
                memberController.exportMembersToCSV(); // Export member list to CSV
                break;
            case 3:
                loanController.exportAllLoansToCSV(); // Export all loans to CSV
                break;
            default:
                break; // Return to main menu
        }
    }
}