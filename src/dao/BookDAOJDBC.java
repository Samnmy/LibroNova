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
        String sql = "INSERT INTO books (isbn, title, author, year_published, genre, total_copies, available_copies) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setObject(4, book.getYearPublished(), Types.INTEGER);
            stmt.setString(5, book.getGenre());
            stmt.setInt(6, book.getTotalCopies());
            stmt.setInt(7, book.getAvailableCopies());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getInt(1));
                }
            }

            DatabaseConfig.getLogger().info("Book saved: " + book.getIsbn());

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error saving book", e);
            throw new RuntimeException("Error saving book", e);
        }
    }

    @Override
    public Optional<Book> findById(Integer id) {
        String sql = "SELECT * FROM books WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding book by id: " + id, e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding book by ISBN: " + isbn, e);
        }

        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY title";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding all books", e);
        }

        return books;
    }

    @Override
    public List<Book> findByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? ORDER BY title";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding books by title: " + title, e);
        }

        return books;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE author LIKE ? ORDER BY title";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + author + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding books by author: " + author, e);
        }

        return books;
    }

    @Override
    public void update(Book book) {
        String sql = "UPDATE books SET isbn = ?, title = ?, author = ?, year_published = ?, genre = ?, total_copies = ?, available_copies = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setObject(4, book.getYearPublished(), Types.INTEGER);
            stmt.setString(5, book.getGenre());
            stmt.setInt(6, book.getTotalCopies());
            stmt.setInt(7, book.getAvailableCopies());
            stmt.setInt(8, book.getId());

            stmt.executeUpdate();
            DatabaseConfig.getLogger().info("Book updated: " + book.getIsbn());

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error updating book", e);
            throw new RuntimeException("Error updating book", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            DatabaseConfig.getLogger().info("Book deleted: " + id);

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error deleting book: " + id, e);
            throw new RuntimeException("Error deleting book", e);
        }
    }

    @Override
    public boolean isIsbnUnique(String isbn) {
        String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error checking ISBN uniqueness: " + isbn, e);
        }

        return false;
    }

    @Override
    public void updateAvailableCopies(Integer bookId, Integer change) {
        String sql = "UPDATE books SET available_copies = available_copies + ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, change);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error updating available copies for book: " + bookId, e);
            throw new RuntimeException("Error updating available copies", e);
        }
    }

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