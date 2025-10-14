package controller;

import domain.Book;
import exceptions.BusinessException;
import service.BookService;
import view.BookView;
import java.util.List;

public class BookController {
    // Service layer dependency for business logic operations
    private BookService bookService;
    // View layer dependency for user interface interactions
    private BookView bookView;

    // Constructor initializes service and view dependencies
    public BookController() {
        this.bookService = new BookService();
        this.bookView = new BookView();
    }

    // Method to handle adding a new book
    public void addBook() {
        try {
            // Get book data from view layer
            Book book = bookView.showAddBookForm();
            // Call service to add book to database
            bookService.addBook(book);
            // Show success message to user
            bookView.showSuccessMessage("Book added successfully");
        } catch (BusinessException e) {
            // Handle business rule violations
            bookView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            bookView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }

    // Method to retrieve and display all books
    public void showAllBooks() {
        // Get all books from service layer
        List<Book> books = bookService.getAllBooks();
        // Display books through view layer
        bookView.displayBooks(books);
    }

    // Method to search and display books by title
    public void searchBooksByTitle() {
        // Get search title from view layer
        String title = bookView.askForTitle();
        // Search books by title through service layer
        List<Book> books = bookService.searchBooksByTitle(title);
        // Display search results through view layer
        bookView.displayBooks(books);
    }

    // Method to search and display books by author
    public void searchBooksByAuthor() {
        // Get search author from view layer
        String author = bookView.askForAuthor();
        // Search books by author through service layer
        List<Book> books = bookService.searchBooksByAuthor(author);
        // Display search results through view layer
        bookView.displayBooks(books);
    }

    // Method to handle updating an existing book
    public void updateBook() {
        try {
            // Get ISBN from view layer to identify book
            String isbn = bookView.askForIsbn();
            // Retrieve book from service layer, throw exception if not found
            Book book = bookService.getBookByIsbn(isbn)
                    .orElseThrow(() -> new BusinessException("Book not found"));

            // Get updated book data from view layer
            Book updatedBook = bookView.showUpdateBookForm(book);
            // Call service to update book in database
            bookService.updateBook(updatedBook);
            // Show success message to user
            bookView.showSuccessMessage("Book updated successfully");

        } catch (BusinessException e) {
            // Handle business rule violations
            bookView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            bookView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }

    // Method to handle deleting a book
    public void deleteBook() {
        try {
            // Get ISBN from view layer to identify book
            String isbn = bookView.askForIsbn();
            // Retrieve book from service layer, throw exception if not found
            Book book = bookService.getBookByIsbn(isbn)
                    .orElseThrow(() -> new BusinessException("Book not found"));

            // Confirm deletion with user through view layer
            if (bookView.confirmDelete(book)) {
                // Call service to delete book from database
                bookService.deleteBook(book.getId());
                // Show success message to user
                bookView.showSuccessMessage("Book deleted successfully");
            }

        } catch (BusinessException e) {
            // Handle business rule violations
            bookView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            bookView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }
}