package service;

import domain.Book;
import exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {
    private BookService bookService;

    // This runs before each test method to set up a fresh BookService instance
    @BeforeEach
    void setUp() {
        bookService = new BookService();
    }

    // Test that a valid book can be added without throwing any exceptions
    @Test
    void testAddBook_ValidBook_Success() {
        // Arrange - create a book with all valid data
        Book book = new Book("1234567890", "Test Book", "Test Author", 2024, "Fiction", 5);

        // Act & Assert - verify no exception is thrown when adding the book
        assertDoesNotThrow(() -> bookService.addBook(book));
    }

    // Test that adding a book with empty ISBN throws the correct exception
    @Test
    void testAddBook_EmptyISBN_ThrowsException() {
        // Arrange - create a book with empty ISBN
        Book book = new Book("", "Test Book", "Test Author", 2024, "Fiction", 1);

        // Act & Assert - verify the correct exception with correct message is thrown
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            bookService.addBook(book);
        });

        assertEquals("ISBN is required", exception.getMessage());
    }

    // Test that adding a book with null title throws the correct exception
    @Test
    void testAddBook_NullTitle_ThrowsException() {
        // Arrange - create a book with null title
        Book book = new Book("1234567890", null, "Test Author", 2024, "Fiction", 1);

        // Act & Assert - verify the correct exception is thrown
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            bookService.addBook(book);
        });

        assertEquals("Title is required", exception.getMessage());
    }

    // Test that adding a book with negative copies throws the correct exception
    @Test
    void testAddBook_NegativeCopies_ThrowsException() {
        // Arrange - create a book with negative number of copies
        Book book = new Book("1234567890", "Test Book", "Test Author", 2024, "Fiction", -1);

        // Act & Assert - verify the correct exception is thrown
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            bookService.addBook(book);
        });

        assertEquals("Number of copies cannot be negative", exception.getMessage());
    }

    // Test that getAllBooks method returns a list (even if empty)
    @Test
    void testGetAllBooks_ReturnsList() {
        // Act - call the method to get all books
        var books = bookService.getAllBooks();

        // Assert - verify the result is not null
        assertNotNull(books);
        // Note: The list might be empty if no books in database, but should not be null
    }

    // Test searching books by title returns a valid list
    @Test
    void testSearchBooksByTitle_ValidTitle_ReturnsList() {
        // Act - search for books with "Test" in the title
        var books = bookService.searchBooksByTitle("Test");

        // Assert - verify the result is not null
        assertNotNull(books);
        // Could be empty if no matching books, but should not throw exception
    }

    // Test searching books by author returns a valid list
    @Test
    void testSearchBooksByAuthor_ValidAuthor_ReturnsList() {
        // Act - search for books by author name
        var books = bookService.searchBooksByAuthor("Author");

        // Assert - verify the result is not null
        assertNotNull(books);
    }

    // Test that checking availability for non-existent book returns false
    @Test
    void testIsBookAvailableForLoan_NonExistentBook_ReturnsFalse() {
        // Act - check availability for a book that doesn't exist
        boolean available = bookService.isBookAvailableForLoan(9999); // Non-existent ID

        // Assert - verify it returns false
        assertFalse(available);
    }

    // Test that book object correctly stores all its properties
    @Test
    void testBookCreation_ValidData_SetsPropertiesCorrectly() {
        // Arrange & Act - create a book with specific data
        Book book = new Book("1234567890", "My Book", "Test Author", 2024, "Fiction", 5);

        // Assert - verify all properties are set correctly
        assertEquals("1234567890", book.getIsbn());
        assertEquals("My Book", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals(2024, book.getYearPublished());
        assertEquals("Fiction", book.getGenre());
        assertEquals(5, book.getTotalCopies());
        assertEquals(5, book.getAvailableCopies()); // Should match total copies initially
    }

    // Test that the book's toString method returns a properly formatted string
    @Test
    void testBookToString_ReturnsFormattedString() {
        // Arrange - create a book with known data
        Book book = new Book("1234567890", "Test Book", "Test Author", 2024, "Fiction", 3);

        // Act - call the toString method
        String result = book.toString();

        // Assert - verify the string contains all the important information
        assertTrue(result.contains("ISBN: 1234567890"));
        assertTrue(result.contains("Title: Test Book"));
        assertTrue(result.contains("Author: Test Author"));
        assertTrue(result.contains("Available: 3/3"));
    }
}