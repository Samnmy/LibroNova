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
        String sql = "INSERT INTO loans (book_id, member_id, loan_date, due_date, return_date, status, fine_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, loan.getBookId());
            stmt.setInt(2, loan.getMemberId());
            stmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            stmt.setDate(4, Date.valueOf(loan.getDueDate()));
            stmt.setObject(5, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null, Types.DATE);
            stmt.setString(6, loan.getStatus());
            stmt.setDouble(7, loan.getFineAmount());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    loan.setId(generatedKeys.getInt(1));
                }
            }

            DatabaseConfig.getLogger().info("Loan saved for book ID: " + loan.getBookId() + ", member ID: " + loan.getMemberId());

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error saving loan", e);
            throw new RuntimeException("Error saving loan", e);
        }
    }

    @Override
    public Optional<Loan> findById(Integer id) {
        String sql = "SELECT l.*, b.title as book_title, b.isbn as book_isbn, m.first_name, m.last_name " +
                "FROM loans l " +
                "JOIN books b ON l.book_id = b.id " +
                "JOIN members m ON l.member_id = m.id " +
                "WHERE l.id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToLoan(rs));
            }

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding loan by id: " + id, e);
        }

        return Optional.empty();
    }

    @Override
    public List<Loan> findAll() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, b.title as book_title, b.isbn as book_isbn, m.first_name, m.last_name " +
                "FROM loans l " +
                "JOIN books b ON l.book_id = b.id " +
                "JOIN members m ON l.member_id = m.id " +
                "ORDER BY l.due_date";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding all loans", e);
        }

        return loans;
    }

    @Override
    public List<Loan> findByMemberId(Integer memberId) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, b.title as book_title, b.isbn as book_isbn, m.first_name, m.last_name " +
                "FROM loans l " +
                "JOIN books b ON l.book_id = b.id " +
                "JOIN members m ON l.member_id = m.id " +
                "WHERE l.member_id = ? " +
                "ORDER BY l.due_date";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding loans by member: " + memberId, e);
        }

        return loans;
    }

    @Override
    public List<Loan> findActiveLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, b.title as book_title, b.isbn as book_isbn, m.first_name, m.last_name " +
                "FROM loans l " +
                "JOIN books b ON l.book_id = b.id " +
                "JOIN members m ON l.member_id = m.id " +
                "WHERE l.status = 'ACTIVE' " +
                "ORDER BY l.due_date";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding active loans", e);
        }

        return loans;
    }

    @Override
    public List<Loan> findOverdueLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, b.title as book_title, b.isbn as book_isbn, m.first_name, m.last_name " +
                "FROM loans l " +
                "JOIN books b ON l.book_id = b.id " +
                "JOIN members m ON l.member_id = m.id " +
                "WHERE l.status = 'ACTIVE' AND l.due_date < CURDATE() " +
                "ORDER BY l.due_date";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error finding overdue loans", e);
        }

        return loans;
    }

    @Override
    public void update(Loan loan) {
        String sql = "UPDATE loans SET return_date = ?, status = ?, fine_amount = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null, Types.DATE);
            stmt.setString(2, loan.getStatus());
            stmt.setDouble(3, loan.getFineAmount());
            stmt.setInt(4, loan.getId());

            stmt.executeUpdate();
            DatabaseConfig.getLogger().info("Loan updated: " + loan.getId());

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error updating loan", e);
            throw new RuntimeException("Error updating loan", e);
        }
    }

    @Override
    public int countActiveLoansByMember(Integer memberId) {
        String sql = "SELECT COUNT(*) FROM loans WHERE member_id = ? AND status = 'ACTIVE'";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            DatabaseConfig.getLogger().log(Level.SEVERE, "Error counting active loans for member: " + memberId, e);
        }

        return 0;
    }

    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setId(rs.getInt("id"));
        loan.setBookId(rs.getInt("book_id"));
        loan.setMemberId(rs.getInt("member_id"));
        loan.setLoanDate(rs.getDate("loan_date").toLocalDate());
        loan.setDueDate(rs.getDate("due_date").toLocalDate());

        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            loan.setReturnDate(returnDate.toLocalDate());
        }

        loan.setStatus(rs.getString("status"));
        loan.setFineAmount(rs.getDouble("fine_amount"));
        loan.setCreatedAt(rs.getDate("created_at").toLocalDate());

        // Additional fields from joins
        loan.setBookTitle(rs.getString("book_title"));
        loan.setBookIsbn(rs.getString("book_isbn"));
        loan.setMemberName(rs.getString("first_name") + " " + rs.getString("last_name"));

        return loan;
    }
}