package dao;

import domain.Loan;
import java.util.List;
import java.util.Optional;

public interface LoanDAO {
    void save(Loan loan);
    Optional<Loan> findById(Integer id);
    List<Loan> findAll();
    List<Loan> findByMemberId(Integer memberId);
    List<Loan> findActiveLoans();
    List<Loan> findOverdueLoans();
    void update(Loan loan);
    int countActiveLoansByMember(Integer memberId);
}