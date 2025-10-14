package service;

import dao.LoanDAO;
import dao.LoanDAOJDBC;
import domain.Loan;
import exceptions.BusinessException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LoanService {
    private LoanDAO loanDAO;
    private BookService bookService;
    private MemberService memberService;

    // Constantes simples en vez de archivo properties
    private static final int LOAN_DAYS = 14;
    private static final double FINE_PER_DAY = 5.00;
    private static final int MAX_BOOKS_PER_MEMBER = 3;

    public LoanService() {
        this.loanDAO = new LoanDAOJDBC();
        this.bookService = new BookService();
        this.memberService = new MemberService();
    }

    public void createLoan(Integer bookId, Integer memberId) throws BusinessException {
        // Validaciones simples
        if (!bookService.isBookAvailableForLoan(bookId)) {
            throw new BusinessException("El libro no está disponible para préstamo");
        }

        if (!memberService.isMemberActive(memberId)) {
            throw new BusinessException("El socio no está activo");
        }

        int activeLoans = loanDAO.countActiveLoansByMember(memberId);
        if (activeLoans >= MAX_BOOKS_PER_MEMBER) {
            throw new BusinessException("El socio ya tiene el máximo de " + MAX_BOOKS_PER_MEMBER + " préstamos activos");
        }

        // Crear préstamo
        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(LOAN_DAYS);

        Loan loan = new Loan(bookId, memberId, loanDate, dueDate);
        loanDAO.save(loan);

        // Actualizar stock del libro
        bookService.updateBookStock(bookId, -1);
    }

    public void returnLoan(Integer loanId) throws BusinessException {
        Optional<Loan> loanOpt = loanDAO.findById(loanId);

        if (loanOpt.isEmpty()) {
            throw new BusinessException("Préstamo no encontrado");
        }

        Loan loan = loanOpt.get();

        if (!"ACTIVE".equals(loan.getStatus())) {
            throw new BusinessException("El préstamo ya fue devuelto");
        }

        // Calcular multa si está vencido
        double fine = calculateFine(loan);

        // Actualizar préstamo
        loan.setReturnDate(LocalDate.now());
        loan.setStatus("RETURNED");
        loan.setFineAmount(fine);
        loanDAO.update(loan);

        // Actualizar stock del libro
        bookService.updateBookStock(loan.getBookId(), 1);
    }

    public List<Loan> getAllLoans() {
        return loanDAO.findAll();
    }

    public List<Loan> getActiveLoans() {
        return loanDAO.findActiveLoans();
    }

    public List<Loan> getOverdueLoans() {
        return loanDAO.findOverdueLoans();
    }

    public List<Loan> getLoansByMember(Integer memberId) {
        return loanDAO.findByMemberId(memberId);
    }

    public int countActiveLoansByMember(Integer memberId) {
        return loanDAO.countActiveLoansByMember(memberId);
    }

    private double calculateFine(Loan loan) {
        if (LocalDate.now().isAfter(loan.getDueDate())) {
            long daysOverdue = LocalDate.now().toEpochDay() - loan.getDueDate().toEpochDay();
            return daysOverdue * FINE_PER_DAY;
        }
        return 0.0;
    }
}