package service;

import dao.BookDAO;
import dao.BookDAOJDBC;
import domain.Book;
import exceptions.BusinessException;
import java.util.List;
import java.util.Optional;

public class BookService {
    private BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAOJDBC();
    }

    public void addBook(Book book) throws BusinessException {
        validateBook(book);

        if (!bookDAO.isIsbnUnique(book.getIsbn())) {
            throw new BusinessException("El ISBN ya existe en el sistema: " + book.getIsbn());
        }

        if (book.getTotalCopies() < 0) {
            throw new BusinessException("El número de copias no puede ser negativo");
        }

        bookDAO.save(book);
    }

    public List<Book> getAllBooks() {
        return bookDAO.findAll();
    }

    public Optional<Book> getBookByIsbn(String isbn) {
        return bookDAO.findByIsbn(isbn);
    }

    public List<Book> searchBooksByTitle(String title) {
        return bookDAO.findByTitle(title);
    }

    public List<Book> searchBooksByAuthor(String author) {
        return bookDAO.findByAuthor(author);
    }

    public void updateBook(Book book) throws BusinessException {
        validateBook(book);

        Optional<Book> existingBook = bookDAO.findByIsbn(book.getIsbn());
        if (existingBook.isPresent() && !existingBook.get().getId().equals(book.getId())) {
            throw new BusinessException("El ISBN ya existe en el sistema: " + book.getIsbn());
        }

        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new BusinessException("Las copias disponibles no pueden ser mayores que las copias totales");
        }

        bookDAO.update(book);
    }

    public void deleteBook(Integer id) throws BusinessException {
        Optional<Book> book = bookDAO.findById(id);
        if (book.isPresent() && book.get().getAvailableCopies() < book.get().getTotalCopies()) {
            throw new BusinessException("No se puede eliminar el libro porque tiene copias prestadas");
        }

        bookDAO.delete(id);
    }

    public void updateBookStock(Integer bookId, Integer change) {
        bookDAO.updateAvailableCopies(bookId, change);
    }

    public boolean isBookAvailableForLoan(Integer bookId) {
        Optional<Book> book = bookDAO.findById(bookId);
        return book.isPresent() && book.get().getAvailableCopies() > 0;
    }

    private void validateBook(Book book) throws BusinessException {
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new BusinessException("El ISBN es obligatorio");
        }

        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new BusinessException("El título es obligatorio");
        }

        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new BusinessException("El autor es obligatorio");
        }

        if (book.getTotalCopies() == null || book.getTotalCopies() < 0) {
            throw new BusinessException("El número de copias totales debe ser mayor o igual a 0");
        }
    }
}