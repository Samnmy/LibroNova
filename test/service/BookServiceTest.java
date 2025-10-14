package service;

import domain.Book;
import exceptions.BusinessException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Pruebas MUY simples para empezar
public class BookServiceTest {

    @Test
    void testBookCreation() {
        Book book = new Book("1234567890", "Mi Libro", "Autor Test", 2024, "Ficción", 5);

        assertEquals("1234567890", book.getIsbn());
        assertEquals("Mi Libro", book.getTitle());
        assertEquals(5, book.getTotalCopies());
    }

    @Test
    void testBookValidation() {
        Book book = new Book("", "Título", "Autor", 2024, "Género", 1);

        BookService service = new BookService();

        // Esto debería fallar porque el ISBN está vacío
        assertThrows(BusinessException.class, () -> {
            service.addBook(book);
        });
    }
}