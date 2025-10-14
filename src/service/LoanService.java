package service;

import config.DatabaseConfig;
import dao.LoanDAO;
import dao.LoanDAOJDBC;
import domain.Loan;
import exceptions.BusinessException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LoanService {
    private LoanDAO loanDAO;
    private BookService bookService;
    private MemberService memberService;

    // Constants for business rules
    private static final int LOAN_DAYS = 14;
    private static final double FINE_PER_DAY = 5.00;
    private static final int MAX_BOOKS_PER_MEMBER = 3;

    public LoanService() {
        // Initialize DAO and service dependencies
        this.loanDAO = new LoanDAOJDBC();
        this.bookService = new BookService();
        this.memberService = new MemberService();
    }

    public void createLoan(Integer bookId, Integer memberId) throws BusinessException {
        Connection conn = null;
        try {
            // Get connection and start transaction
            conn = DatabaseConfig.getTransactionConnection();
            conn.setAutoCommit(false); // Start transaction

            // Business validations
            if (!bookService.isBookAvailableForLoan(bookId)) {
                throw new BusinessException("The book is not available for loan");
            }

            if (!memberService.isMemberActive(memberId)) {
                throw new BusinessException("The member is not active");
            }

            int activeLoans = loanDAO.countActiveLoansByMember(memberId);
            if (activeLoans >= MAX_BOOKS_PER_MEMBER) {
                throw new BusinessException("The member already has the maximum of " + MAX_BOOKS_PER_MEMBER + " active loans");
            }

            // Create loan
            LocalDate loanDate = LocalDate.now();
            LocalDate dueDate = loanDate.plusDays(LOAN_DAYS);

            Loan loan = new Loan(bookId, memberId, loanDate, dueDate);

            // Save loan to database
            loanDAO.save(loan);

            // Update book stock (decrease available copies)
            bookService.updateBookStock(bookId, -1);

            // Commit transaction if everything is successful
            conn.commit();
            DatabaseConfig.getLogger().info("Loan created successfully: Book ID " + bookId + ", Member ID " + memberId);

        } catch (SQLException e) {
            // Rollback transaction in case of error
            DatabaseConfig.rollbackTransaction(conn);
            DatabaseConfig.getLogger().severe("Transaction rolled back due to error: " + e.getMessage());
            throw new BusinessException("Error creating loan: " + e.getMessage());
        } catch (BusinessException e) {
            // Rollback for business rule violations
            DatabaseConfig.rollbackTransaction(conn);
            DatabaseConfig.getLogger().warning("Transaction rolled back due to business rule: " + e.getMessage());
            throw e; // Re-throw the original exception
        } finally {
            // Clean up resources
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restore auto-commit
                    DatabaseConfig.closeConnection(conn);
                } catch (SQLException e) {
                    DatabaseConfig.getLogger().warning("Error cleaning up connection: " + e.getMessage());
                }
            }
        }
    }

    public void returnLoan(Integer loanId) throws BusinessException {
        Connection conn = null;
        try {
            // Get connection and start transaction
            conn = DatabaseConfig.getTransactionConnection();
            conn.setAutoCommit(false); // Start transaction

            // Find loan by ID
            Optional<Loan> loanOpt = loanDAO.findById(loanId);

            if (loanOpt.isEmpty()) {
                throw new BusinessException("Loan not found");
            }

            Loan loan = loanOpt.get();

            // Check if loan is already returned
            if (!"ACTIVE".equals(loan.getStatus())) {
                throw new BusinessException("The loan has already been returned");
            }

            // Calculate fine if overdue
            double fine = calculateFine(loan);

            // Update loan with return information
            loan.setReturnDate(LocalDate.now());
            loan.setStatus("RETURNED");
            loan.setFineAmount(fine);
            loanDAO.update(loan);

            // Update book stock (increase available copies)
            bookService.updateBookStock(loan.getBookId(), 1);

            // Commit transaction
            conn.commit();
            DatabaseConfig.getLogger().info("Return processed successfully: Loan ID " + loanId);

        } catch (SQLException e) {
            // Rollback in case of error
            DatabaseConfig.rollbackTransaction(conn);
            DatabaseConfig.getLogger().severe("Transaction rolled back due to error: " + e.getMessage());
            throw new BusinessException("Error processing return: " + e.getMessage());
        } catch (BusinessException e) {
            // Rollback for business rule violations
            DatabaseConfig.rollbackTransaction(conn);
            DatabaseConfig.getLogger().warning("Transaction rolled back due to business rule: " + e.getMessage());
            throw e;
        } finally {
            // Clean up resources
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restore auto-commit
                    DatabaseConfig.closeConnection(conn);
                } catch (SQLException e) {
                    DatabaseConfig.getLogger().warning("Error cleaning up connection: " + e.getMessage());
                }
            }
        }
    }

    // Get all loans from the database
    public List<Loan> getAllLoans() {
        return loanDAO.findAll();
    }

    // Get only active loans
    public List<Loan> getActiveLoans() {
        return loanDAO.findActiveLoans();
    }

    // Get overdue loans
    public List<Loan> getOverdueLoans() {
        return loanDAO.findOverdueLoans();
    }

    // Get loans for a specific member
    public List<Loan> getLoansByMember(Integer memberId) {
        return loanDAO.findByMemberId(memberId);
    }

    // Count active loans for a specific member
    public int countActiveLoansByMember(Integer memberId) {
        return loanDAO.countActiveLoansByMember(memberId);
    }

    // Calculate fine amount for overdue loan
    private double calculateFine(Loan loan) {
        // Check if loan is overdue
        if (LocalDate.now().isAfter(loan.getDueDate())) {
            // Calculate days overdue
            long daysOverdue = LocalDate.now().toEpochDay() - loan.getDueDate().toEpochDay();
            return daysOverdue * FINE_PER_DAY;
        }
        return 0.0; // No fine if not overdue
    }
}