package dao;

import domain.Book;
import java.util.List;
import java.util.Optional;

public interface BookDAO {
    // Saves a new book to the database
    void save(Book book);
    // Finds a book by its unique identifier, returns Optional to handle null cases
    Optional<Book> findById(Integer id);
    // Finds a book by ISBN number, returns Optional to handle null cases
    Optional<Book> findByIsbn(String isbn);
    // Retrieves all books from the database
    List<Book> findAll();
    // Searches for books by title (typically partial match/search)
    List<Book> findByTitle(String title);
    // Searches for books by author (typically partial match/search)
    List<Book> findByAuthor(String author);
    // Updates an existing book in the database
    void update(Book book);
    // Deletes a book by its unique identifier
    void delete(Integer id);
    // Checks if an ISBN number is unique in the database (for validation)
    boolean isIsbnUnique(String isbn);
    // Updates the available copies count for a book (increment/decrement)
    void updateAvailableCopies(Integer bookId, Integer change);
}