package service;

import domain.Book;
import exceptions.BusinessException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Very simple tests to get started
public class BookServiceTest {

    @Test
    void testBookCreation() {
        // Create a new Book object with test data
        Book book = new Book("1234567890", "My Book", "Test Author", 2024, "Fiction", 5);

        // Verify that book properties are set correctly
        assertEquals("1234567890", book.getIsbn());
        assertEquals("My Book", book.getTitle());
        assertEquals(5, book.getTotalCopies());
    }

    @Test
    void testBookValidation() {
        // Create a book with empty ISBN to test validation
        Book book = new Book("", "Title", "Author", 2024, "Genre", 1);

        // Create BookService instance
        BookService service = new BookService();

        // This should fail because ISBN is empty
        assertThrows(BusinessException.class, () -> {
            service.addBook(book);
        });
    }
}