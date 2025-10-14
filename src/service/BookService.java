package service;

import dao.BookDAO;
import dao.BookDAOJDBC;
import domain.Book;
import exceptions.BusinessException;
import java.util.List;
import java.util.Optional;

public class BookService {
    // Data Access Object for book operations
    private BookDAO bookDAO;

    // Constructor initializes the BookDAO implementation
    public BookService() {
        this.bookDAO = new BookDAOJDBC();
    }

    // Method to add a new book with validation
    public void addBook(Book book) throws BusinessException {
        validateBook(book); // Validate book data

        // Check if ISBN is unique
        if (!bookDAO.isIsbnUnique(book.getIsbn())) {
            throw new BusinessException("ISBN already exists in the system: " + book.getIsbn());
        }

        // Validate total copies is not negative
        if (book.getTotalCopies() < 0) {
            throw new BusinessException("Number of copies cannot be negative");
        }

        // Save the book to database
        bookDAO.save(book);
    }

    // Method to retrieve all books
    public List<Book> getAllBooks() {
        return bookDAO.findAll();
    }

    // Method to find a book by ISBN
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookDAO.findByIsbn(isbn);
    }

    // Method to search books by title (partial match)
    public List<Book> searchBooksByTitle(String title) {
        return bookDAO.findByTitle(title);
    }

    // Method to search books by author (partial match)
    public List<Book> searchBooksByAuthor(String author) {
        return bookDAO.findByAuthor(author);
    }

    // Method to update an existing book with validation
    public void updateBook(Book book) throws BusinessException {
        validateBook(book); // Validate book data

        // Check ISBN uniqueness excluding current book
        Optional<Book> existingBook = bookDAO.findByIsbn(book.getIsbn());
        if (existingBook.isPresent() && !existingBook.get().getId().equals(book.getId())) {
            throw new BusinessException("ISBN already exists in the system: " + book.getIsbn());
        }

        // Validate available copies don't exceed total copies
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new BusinessException("Available copies cannot be greater than total copies");
        }

        // Update the book in database
        bookDAO.update(book);
    }

    // Method to delete a book with validation
    public void deleteBook(Integer id) throws BusinessException {
        Optional<Book> book = bookDAO.findById(id);
        // Check if book has borrowed copies before deletion
        if (book.isPresent() && book.get().getAvailableCopies() < book.get().getTotalCopies()) {
            throw new BusinessException("Cannot delete the book because it has borrowed copies");
        }

        // Delete the book from database
        bookDAO.delete(id);
    }

    // Method to update book stock (available copies)
    public void updateBookStock(Integer bookId, Integer change) {
        bookDAO.updateAvailableCopies(bookId, change);
    }

    // Method to check if a book is available for loan
    public boolean isBookAvailableForLoan(Integer bookId) {
        Optional<Book> book = bookDAO.findById(bookId);
        return book.isPresent() && book.get().getAvailableCopies() > 0;
    }

    // Private method to validate book data
    private void validateBook(Book book) throws BusinessException {
        // Validate ISBN is not null or empty
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new BusinessException("ISBN is required");
        }

        // Validate title is not null or empty
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new BusinessException("Title is required");
        }

        // Validate author is not null or empty
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new BusinessException("Author is required");
        }

        // Validate total copies is not null and non-negative
        if (book.getTotalCopies() == null || book.getTotalCopies() < 0) {
            throw new BusinessException("Total number of copies must be greater than or equal to 0");
        }
    }
}