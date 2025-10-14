package dao;

import domain.Loan;
import java.util.List;
import java.util.Optional;

public interface LoanDAO {
    // Saves a new loan to the database
    void save(Loan loan);
    // Finds a loan by its unique identifier, returns Optional to handle null cases
    Optional<Loan> findById(Integer id);
    // Retrieves all loans from the database
    List<Loan> findAll();
    // Finds all loans associated with a specific member ID
    List<Loan> findByMemberId(Integer memberId);
    // Retrieves all active loans (loans that haven't been returned yet)
    List<Loan> findActiveLoans();
    // Retrieves all overdue loans (loans past their due date that haven't been returned)
    List<Loan> findOverdueLoans();
    // Updates an existing loan in the database
    void update(Loan loan);
    // Counts the number of active loans for a specific member (for loan limit validation)
    int countActiveLoansByMember(Integer memberId);
}