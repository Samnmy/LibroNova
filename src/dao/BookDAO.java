package dao;

import domain.Book;
import java.util.List;
import java.util.Optional;

public interface BookDAO {
    void save(Book book);
    Optional<Book> findById(Integer id);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAll();
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    void update(Book book);
    void delete(Integer id);
    boolean isIsbnUnique(String isbn);
    void updateAvailableCopies(Integer bookId, Integer change);
}