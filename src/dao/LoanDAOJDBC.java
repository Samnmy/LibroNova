package dao;

import config.DatabaseConfig;
import domain.Loan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class LoanDAOJDBC implements LoanDAO {

    @Override
    public void save(Loan loan) {
        // SQL query to insert a new loan record
        String sql = "INSERT INTO loans (book_id, member_id, loan_date, due_date, return_date, status, fine_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters for the prepared statement
            stmt.setInt(1, loan.getBookId());
            stmt.setInt(2, loan.getMemberId());
            stmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            stmt.setDate(4, Date.valueOf(loan.getDueDate()));
            stmt.setObject(5, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null, Types.DATE);
            stmt.setString(6, loan.getStatus());
            stmt.setDouble(7, loan.getFineAmount());

            // Execute the insert operation
            stmt.executeUpdate();

            // Retrieve the auto-generated loan ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    loan.setId(generatedKeys.getInt(1));
                }
            }

            // Log successful loan creation
            DatabaseConfig.getLogger().info("Loan saved for book ID: " + loan.getBookId() + ", member ID: " + loan.getMemberId());

        } catch (SQLException e) {
            // Log and handle database errors
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error saving loan", e);
            throw new RuntimeException("Error saving loan", e);
        }
    }

    @Override
    public Optional<Loan> findById(Integer id) {
        // SQL query with JOINs to get loan details including book and member information
        String sql = "SELECT l.*, b.title as book_title, b.isbn as book_isbn, m.first_name, m.last_name " +
                "FROM loans l " +
                "JOIN books b ON l.book_id = b.id " +
                "JOIN members m ON l.member_id = m.id " +
                "WHERE l.id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the ID parameter
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            // If loan found, map result set to Loan object
            if (rs.next()) {
                return Optional.of(mapResultSetToLoan(rs));
            }

        } catch (SQLException e) {
            // Log database error
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding loan by id: " + id, e);
        }

        // Return empty Optional if no loan found
        return Optional.empty();
    }

    @Override
    public List<Loan> findAll() {
        // Initialize list to store all loans
        List<Loan> loans = new ArrayList<>();
        // SQL query with JOINs to get all loans with book and member details
        String sql = "SELECT l.*, b.title as book_title, b.isbn as book_isbn, m.first_name, m.last_name " +
                "FROM loans l " +
                "JOIN books b ON l.book_id = b.id " +
                "JOIN members m ON l.member_id = m.id " +
                "ORDER BY l.due_date";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Iterate through all results and map to Loan objects
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }

        } catch (SQLException e) {
            // Log database error
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding all loans", e);
        }

        // Return list of loans (empty list if none found)
        return loans;
    }

    @Override
    public List<Loan> findByMemberId(Integer memberId) {
        // Initialize list to store member's loans
        List<Loan> loans = new ArrayList<>();
        // SQL query with JOINs to get loans for specific member
        String sql = "SELECT l.*, b.title as book_title, b.isbn as book_isbn, m.first_name, m.last_name " +
                "FROM loans l " +
                "JOIN books b ON l.book_id = b.id " +
                "JOIN members m ON l.member_id = m.id " +
                "WHERE l.member_id = ? " +
                "ORDER BY l.due_date";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the member ID parameter
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            // Iterate through results and map to Loan objects
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }

        } catch (SQLException e) {
            // Log database error
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding loans by member: " + memberId, e);
        }

        // Return member's loans
        return loans;
    }

    @Override
    public List<Loan> findActiveLoans() {
        // Initialize list to store active loans
        List<Loan> loans = new ArrayList<>();
        // SQL query to find all active (non-returned) loans
        String sql = "SELECT l.*, b.title as book_title, b.isbn as book_isbn, m.first_name, m.last_name " +
                "FROM loans l " +
                "JOIN books b ON l.book_id = b.id " +
                "JOIN members m ON l.member_id = m.id " +
                "WHERE l.status = 'ACTIVE' " +
                "ORDER BY l.due_date";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Iterate through results and map to Loan objects
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }

        } catch (SQLException e) {
            // Log database error
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding active loans", e);
        }

        // Return active loans
        return loans;
    }

    @Override
    public List<Loan> findOverdueLoans() {
        // Initialize list to store overdue loans
        List<Loan> loans = new ArrayList<>();
        // SQL query to find active loans past their due date
        String sql = "SELECT l.*, b.title as book_title, b.isbn as book_isbn, m.first_name, m.last_name " +
                "FROM loans l " +
                "JOIN books b ON l.book_id = b.id " +
                "JOIN members m ON l.member_id = m.id " +
                "WHERE l.status = 'ACTIVE' AND l.due_date < CURDATE() " +
                "ORDER BY l.due_date";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Iterate through results and map to Loan objects
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }

        } catch (SQLException e) {
            // Log database error
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding overdue loans", e);
        }

        // Return overdue loans
        return loans;
    }

    @Override
    public void update(Loan loan) {
        // SQL query to update loan information (primarily for returns)
        String sql = "UPDATE loans SET return_date = ?, status = ?, fine_amount = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set update parameters
            stmt.setObject(1, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null, Types.DATE);
            stmt.setString(2, loan.getStatus());
            stmt.setDouble(3, loan.getFineAmount());
            stmt.setInt(4, loan.getId());

            // Execute the update operation
            stmt.executeUpdate();
            // Log successful update
            DatabaseConfig.getLogger().info("Loan updated: " + loan.getId());

        } catch (SQLException e) {
            // Log and handle database errors
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error updating loan", e);
            throw new RuntimeException("Error updating loan", e);
        }
    }

    @Override
    public int countActiveLoansByMember(Integer memberId) {
        // SQL query to count active loans for a member (for validation)
        String sql = "SELECT COUNT(*) FROM loans WHERE member_id = ? AND status = 'ACTIVE'";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the member ID parameter
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            // Return the count of active loans
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            // Log database error
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error counting active loans for member: " + memberId, e);
        }

        // Return 0 in case of error or no active loans
        return 0;
    }

    // Helper method to map ResultSet row to Loan object with joined data
    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setId(rs.getInt("id"));
        loan.setBookId(rs.getInt("book_id"));
        loan.setMemberId(rs.getInt("member_id"));
        loan.setLoanDate(rs.getDate("loan_date").toLocalDate());
        loan.setDueDate(rs.getDate("due_date").toLocalDate());

        // Handle nullable return date
        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            loan.setReturnDate(returnDate.toLocalDate());
        }

        loan.setStatus(rs.getString("status"));
        loan.setFineAmount(rs.getDouble("fine_amount"));
        loan.setCreatedAt(rs.getDate("created_at").toLocalDate());

        // Set additional fields from JOINed tables for display purposes
        loan.setBookTitle(rs.getString("book_title"));
        loan.setBookIsbn(rs.getString("book_isbn"));
        loan.setMemberName(rs.getString("first_name") + " " + rs.getString("last_name"));

        return loan;
    }
}