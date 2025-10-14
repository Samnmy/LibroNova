package service;

import dao.BookDAO;
import dao.BookDAOJDBC;
import domain.Book;
import exceptions.BusinessException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class BookService {
    private BookDAO bookDAO;
    private static final Logger logger = Logger.getLogger(BookService.class.getName());

    // Constructor - creates a new BookService with database access
    public BookService() {
        this.bookDAO = new BookDAOJDBC();
        logger.info("BookService initialized");
    }

    // Adds a new book to the system after validation
    public void addBook(Book book) throws BusinessException {
        logger.info("Attempting to add new book: " + book.getIsbn());

        validateBook(book);

        // Check if ISBN already exists in the system
        if (!bookDAO.isIsbnUnique(book.getIsbn())) {
            logger.warning("ISBN already exists: " + book.getIsbn());
            throw new BusinessException("ISBN already exists in the system: " + book.getIsbn());
        }

        // Validate that total copies is not negative
        if (book.getTotalCopies() < 0) {
            logger.warning("Invalid total copies: " + book.getTotalCopies());
            throw new BusinessException("Number of copies cannot be negative");
        }

        bookDAO.save(book);
        logger.info("Book added successfully: " + book.getIsbn());
    }

    // Retrieves all books from the database
    public List<Book> getAllBooks() {
        logger.info("Retrieving all books");
        List<Book> books = bookDAO.findAll();
        logger.info("Retrieved " + books.size() + " books");
        return books;
    }

    // Finds a book by its unique ISBN number
    public Optional<Book> getBookByIsbn(String isbn) {
        logger.info("Searching for book by ISBN: " + isbn);
        return bookDAO.findByIsbn(isbn);
    }

    // Searches for books by title (partial matches)
    public List<Book> searchBooksByTitle(String title) {
        logger.info("Searching books by title: " + title);
        List<Book> books = bookDAO.findByTitle(title);
        logger.info("Found " + books.size() + " books matching title: " + title);
        return books;
    }

    // Searches for books by author name
    public List<Book> searchBooksByAuthor(String author) {
        logger.info("Searching books by author: " + author);
        List<Book> books = bookDAO.findByAuthor(author);
        logger.info("Found " + books.size() + " books by author: " + author);
        return books;
    }

    // Updates an existing book's information
    public void updateBook(Book book) throws BusinessException {
        logger.info("Attempting to update book ID: " + book.getId());

        validateBook(book);

        // Check for ISBN conflicts with other books
        Optional<Book> existingBook = bookDAO.findByIsbn(book.getIsbn());
        if (existingBook.isPresent() && !existingBook.get().getId().equals(book.getId())) {
            logger.warning("ISBN conflict during update: " + book.getIsbn());
            throw new BusinessException("ISBN already exists in the system: " + book.getIsbn());
        }

        // Validate that available copies don't exceed total copies
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            logger.warning("Available copies exceed total copies for book ID: " + book.getId());
            throw new BusinessException("Available copies cannot be greater than total copies");
        }

        bookDAO.update(book);
        logger.info("Book updated successfully: " + book.getIsbn());
    }

    // Deletes a book from the system
    public void deleteBook(Integer id) throws BusinessException {
        logger.info("Attempting to delete book ID: " + id);

        // Prevent deletion if books are currently borrowed
        Optional<Book> book = bookDAO.findById(id);
        if (book.isPresent() && book.get().getAvailableCopies() < book.get().getTotalCopies()) {
            logger.warning("Cannot delete book with borrowed copies: " + id);
            throw new BusinessException("Cannot delete the book because it has borrowed copies");
        }

        bookDAO.delete(id);
        logger.info("Book deleted successfully: " + id);
    }

    // Updates the available copies when books are borrowed or returned
    public void updateBookStock(Integer bookId, Integer change) {
        logger.info("Updating book stock - Book ID: " + bookId + ", Change: " + change);
        bookDAO.updateAvailableCopies(bookId, change);
        logger.fine("Book stock updated successfully");
    }

    // Checks if a book is available for loan (has available copies)
    public boolean isBookAvailableForLoan(Integer bookId) {
        logger.fine("Checking book availability for loan - Book ID: " + bookId);
        Optional<Book> book = bookDAO.findById(bookId);
        boolean available = book.isPresent() && book.get().getAvailableCopies() > 0;
        logger.fine("Book ID " + bookId + " available: " + available);
        return available;
    }

    // Validates book data meets all business rules
    private void validateBook(Book book) throws BusinessException {
        logger.fine("Validating book data");

        // Check required fields are not empty
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            logger.warning("Book validation failed: ISBN is required");
            throw new BusinessException("ISBN is required");
        }

        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            logger.warning("Book validation failed: Title is required");
            throw new BusinessException("Title is required");
        }

        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            logger.warning("Book validation failed: Author is required");
            throw new BusinessException("Author is required");
        }

        // Validate total copies is not negative
        if (book.getTotalCopies() == null || book.getTotalCopies() < 0) {
            logger.warning("Book validation failed: Invalid total copies");
            throw new BusinessException("Total number of copies must be greater than or equal to 0");
        }

        logger.fine("Book validation passed");
    }
}