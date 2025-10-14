package service;

import dao.LoanDAO;
import dao.LoanDAOJDBC;
import domain.Loan;
import exceptions.BusinessException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LoanService {
    // Data Access Object for loan operations
    private LoanDAO loanDAO;
    // Service dependencies for book and member operations
    private BookService bookService;
    private MemberService memberService;

    // Constants for loan configuration (instead of properties file)
    private static final int LOAN_DAYS = 14; // Default loan period in days
    private static final double FINE_PER_DAY = 5.00; // Fine amount per overdue day
    private static final int MAX_BOOKS_PER_MEMBER = 3; // Maximum books a member can borrow

    // Constructor initializes the service dependencies
    public LoanService() {
        this.loanDAO = new LoanDAOJDBC();
        this.bookService = new BookService();
        this.memberService = new MemberService();
    }

    // Method to create a new loan with validation
    public void createLoan(Integer bookId, Integer memberId) throws BusinessException {
        // Validate book availability
        if (!bookService.isBookAvailableForLoan(bookId)) {
            throw new BusinessException("The book is not available for loan");
        }

        // Validate member is active
        if (!memberService.isMemberActive(memberId)) {
            throw new BusinessException("The member is not active");
        }

        // Validate member hasn't reached loan limit
        int activeLoans = loanDAO.countActiveLoansByMember(memberId);
        if (activeLoans >= MAX_BOOKS_PER_MEMBER) {
            throw new BusinessException("The member already has the maximum of " + MAX_BOOKS_PER_MEMBER + " active loans");
        }

        // Create loan with current date and calculated due date
        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(LOAN_DAYS);

        // Create and save loan object
        Loan loan = new Loan(bookId, memberId, loanDate, dueDate);
        loanDAO.save(loan);

        // Update book stock (decrease available copies by 1)
        bookService.updateBookStock(bookId, -1);
    }

    // Method to process book return
    public void returnLoan(Integer loanId) throws BusinessException {
        Optional<Loan> loanOpt = loanDAO.findById(loanId);

        // Validate loan exists
        if (loanOpt.isEmpty()) {
            throw new BusinessException("Loan not found");
        }

        Loan loan = loanOpt.get();

        // Validate loan is still active
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

        // Update book stock (increase available copies by 1)
        bookService.updateBookStock(loan.getBookId(), 1);
    }

    // Method to retrieve all loans
    public List<Loan> getAllLoans() {
        return loanDAO.findAll();
    }

    // Method to retrieve active loans only
    public List<Loan> getActiveLoans() {
        return loanDAO.findActiveLoans();
    }

    // Method to retrieve overdue loans
    public List<Loan> getOverdueLoans() {
        return loanDAO.findOverdueLoans();
    }

    // Method to retrieve loans for a specific member
    public List<Loan> getLoansByMember(Integer memberId) {
        return loanDAO.findByMemberId(memberId);
    }

    // Method to count active loans for a member
    public int countActiveLoansByMember(Integer memberId) {
        return loanDAO.countActiveLoansByMember(memberId);
    }

    // Private method to calculate fine for overdue loans
    private double calculateFine(Loan loan) {
        // Check if loan is overdue
        if (LocalDate.now().isAfter(loan.getDueDate())) {
            // Calculate days overdue
            long daysOverdue = LocalDate.now().toEpochDay() - loan.getDueDate().toEpochDay();
            // Calculate fine amount
            return daysOverdue * FINE_PER_DAY;
        }
        // Return zero fine if not overdue
        return 0.0;
    }
}