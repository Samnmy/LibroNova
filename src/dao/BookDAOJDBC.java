package dao;

import config.DatabaseConfig;
import domain.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class BookDAOJDBC implements BookDAO {

    @Override
    public void save(Book book) {
        // SQL query to insert a new book record
        String sql = "INSERT INTO books (isbn, title, author, year_published, genre, total_copies, available_copies) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters for the prepared statement
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setObject(4, book.getYearPublished(), Types.INTEGER);
            stmt.setString(5, book.getGenre());
            stmt.setInt(6, book.getTotalCopies());
            stmt.setInt(7, book.getAvailableCopies());

            // Execute the insert operation
            stmt.executeUpdate();

            // Retrieve the auto-generated book ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getInt(1));
                }
            }

            // Log successful book creation
            DatabaseConfig.getLogger().info("Book saved: " + book.getIsbn());

        } catch (SQLException e) {
            // Log and handle database errors
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error saving book", e);
            throw new RuntimeException("Error saving book", e);
        }
    }

    @Override
    public Optional<Book> findById(Integer id) {
        // SQL query to find book by primary key
        String sql = "SELECT * FROM books WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the ID parameter
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            // If book found, map result set to Book object
            if (rs.next()) {
                return Optional.of(mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            // Log database error
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding book by id: " + id, e);
        }

        // Return empty Optional if no book found
        return Optional.empty();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        // SQL query to find book by unique ISBN
        String sql = "SELECT * FROM books WHERE isbn = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the ISBN parameter
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();

            // If book found, map result set to Book object
            if (rs.next()) {
                return Optional.of(mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            // Log database error
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding book by ISBN: " + isbn, e);
        }

        // Return empty Optional if no book found
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        // Initialize list to store all books
        List<Book> books = new ArrayList<>();
        // SQL query to retrieve all books ordered by title
        String sql = "SELECT * FROM books ORDER BY title";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Iterate through all results and map to Book objects
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            // Log database error
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding all books", e);
        }

        // Return list of books (empty list if none found)
        return books;
    }

    @Override
    public List<Book> findByTitle(String title) {
        // Initialize list to store search results
        List<Book> books = new ArrayList<>();
        // SQL query with LIKE for partial title matching
        String sql = "SELECT * FROM books WHERE title LIKE ? ORDER BY title";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set title parameter with wildcards for partial matching
            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();

            // Iterate through results and map to Book objects
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            // Log database error
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding books by title: " + title, e);
        }

        // Return search results
        return books;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        // Initialize list to store search results
        List<Book> books = new ArrayList<>();
        // SQL query with LIKE for partial author matching
        String sql = "SELECT * FROM books WHERE author LIKE ? ORDER BY title";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set author parameter with wildcards for partial matching
            stmt.setString(1, "%" + author + "%");
            ResultSet rs = stmt.executeQuery();

            // Iterate through results and map to Book objects
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            // Log database error
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding books by author: " + author, e);
        }

        // Return search results
        return books;
    }

    @Override
    public void update(Book book) {
        // SQL query to update existing book record
        String sql = "UPDATE books SET isbn = ?, title = ?, author = ?, year_published = ?, genre = ?, total_copies = ?, available_copies = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set all update parameters
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setObject(4, book.getYearPublished(), Types.INTEGER);
            stmt.setString(5, book.getGenre());
            stmt.setInt(6, book.getTotalCopies());
            stmt.setInt(7, book.getAvailableCopies());
            stmt.setInt(8, book.getId());

            // Execute the update operation
            stmt.executeUpdate();
            // Log successful update
            DatabaseConfig.getLogger().info("Book updated: " + book.getIsbn());

        } catch (SQLException e) {
            // Log and handle database errors
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error updating book", e);
            throw new RuntimeException("Error updating book", e);
        }
    }

    @Override
    public void delete(Integer id) {
        // SQL query to delete book by ID
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the ID parameter
            stmt.setInt(1, id);
            // Execute the delete operation
            stmt.executeUpdate();
            // Log successful deletion
            DatabaseConfig.getLogger().info("Book deleted: " + id);

        } catch (SQLException e) {
            // Log and handle database errors
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error deleting book: " + id, e);
            throw new RuntimeException("Error deleting book", e);
        }
    }

    @Override
    public boolean isIsbnUnique(String isbn) {
        // SQL query to check if ISBN already exists
        String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the ISBN parameter
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();

            // Return true if count is 0 (ISBN is unique)
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (SQLException e) {
            // Log database error
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error checking ISBN uniqueness: " + isbn, e);
        }

        // Return false in case of error or non-unique ISBN
        return false;
    }

    @Override
    public void updateAvailableCopies(Integer bookId, Integer change) {
        // SQL query to atomically update available copies count
        String sql = "UPDATE books SET available_copies = available_copies + ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the change amount and book ID
            stmt.setInt(1, change);
            stmt.setInt(2, bookId);
            // Execute the update
            stmt.executeUpdate();

        } catch (SQLException e) {
            // Log and handle database errors
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error updating available copies for book: " + bookId, e);
            throw new RuntimeException("Error updating available copies", e);
        }
    }

    // Helper method to map ResultSet row to Book object
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setIsbn(rs.getString("isbn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setYearPublished(rs.getInt("year_published"));
        book.setGenre(rs.getString("genre"));
        book.setTotalCopies(rs.getInt("total_copies"));
        book.setAvailableCopies(rs.getInt("available_copies"));
        book.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return book;
    }
}