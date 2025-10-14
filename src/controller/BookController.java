package controller;

import domain.Book;
import exceptions.BusinessException;
import service.BookService;
import util.CSVExportUtil;
import view.BookView;
import java.util.List;

public class BookController {
    // Handles business logic for book operations
    private BookService bookService;
    // Handles user interface for book-related screens
    private BookView bookView;

    // Constructor - creates new instances of service and view
    public BookController() {
        this.bookService = new BookService();
        this.bookView = new BookView();
    }

    // Handles the complete process of adding a new book
    public void addBook() {
        try {
            // Get book information from user input
            Book book = bookView.showAddBookForm();
            // Save book to database through service layer
            bookService.addBook(book);
            // Show confirmation message to user
            bookView.showSuccessMessage("Book added successfully");
        } catch (BusinessException e) {
            // Show specific error message for business rule violations
            bookView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Show generic error message for unexpected problems
            bookView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }

    // Retrieves and displays all books in the system
    public void showAllBooks() {
        // Get complete list of books from database
        List<Book> books = bookService.getAllBooks();
        // Display the list to the user
        bookView.displayBooks(books);
    }

    // Searches for books by title and displays results
    public void searchBooksByTitle() {
        // Ask user for search term
        String title = bookView.askForTitle();
        // Search database for matching books
        List<Book> books = bookService.searchBooksByTitle(title);
        // Show search results to user
        bookView.displayBooks(books);
    }

    // Searches for books by author and displays results
    public void searchBooksByAuthor() {
        // Ask user for author name to search
        String author = bookView.askForAuthor();
        // Search database for books by this author
        List<Book> books = bookService.searchBooksByAuthor(author);
        // Display matching books to user
        bookView.displayBooks(books);
    }

    // Handles updating an existing book's information
    public void updateBook() {
        try {
            // Get ISBN to identify which book to update
            String isbn = bookView.askForIsbn();
            // Find the book in database, throw error if not found
            Book book = bookService.getBookByIsbn(isbn)
                    .orElseThrow(() -> new BusinessException("Book not found"));

            // Get updated information from user
            Book updatedBook = bookView.showUpdateBookForm(book);
            // Save changes to database
            bookService.updateBook(updatedBook);
            // Confirm successful update to user
            bookView.showSuccessMessage("Book updated successfully");

        } catch (BusinessException e) {
            // Show specific error message
            bookView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Show generic error message
            bookView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }

    // Exports all books to a CSV file for reporting
    public void exportBooksToCSV() {
        try {
            // Get all books from database
            List<Book> books = bookService.getAllBooks();
            // Export to CSV file with automatic filename
            String filename = CSVExportUtil.exportBooksToCSV(books);
            // Confirm export success to user
            bookView.showSuccessMessage("Books exported successfully to: " + filename);
        } catch (Exception e) {
            // Show error if export fails
            bookView.showErrorMessage("Error exporting books: " + e.getMessage());
        }
    }

    // Handles deleting a book from the system
    public void deleteBook() {
        try {
            // Get ISBN to identify which book to delete
            String isbn = bookView.askForIsbn();
            // Find the book in database, throw error if not found
            Book book = bookService.getBookByIsbn(isbn)
                    .orElseThrow(() -> new BusinessException("Book not found"));

            // Ask user to confirm deletion
            if (bookView.confirmDelete(book)) {
                // Delete book from database
                bookService.deleteBook(book.getId());
                // Confirm successful deletion
                bookView.showSuccessMessage("Book deleted successfully");
            }

        } catch (BusinessException e) {
            // Show specific error message (e.g., book has active loans)
            bookView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Show generic error message
            bookView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }
}