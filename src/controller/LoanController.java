package controller;

import domain.Loan;
import exceptions.BusinessException;
import service.LoanService;
import view.LoanView;
import java.util.List;

public class LoanController {
    private LoanService loanService;
    private LoanView loanView;

    public LoanController() {
        this.loanService = new LoanService();
        this.loanView = new LoanView();
    }

    public void createLoan() {
        try {
            Integer bookId = loanView.askForBookId();
            Integer memberId = loanView.askForMemberId();

            loanService.createLoan(bookId, memberId);
            loanView.showSuccessMessage("Préstamo creado exitosamente");

        } catch (BusinessException e) {
            loanView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            loanView.showErrorMessage("Error inesperado: " + e.getMessage());
        }
    }

    public void returnLoan() {
        try {
            Integer loanId = loanView.askForLoanId();
            loanService.returnLoan(loanId);
            loanView.showSuccessMessage("Devolución procesada exitosamente");

        } catch (BusinessException e) {
            loanView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            loanView.showErrorMessage("Error inesperado: " + e.getMessage());
        }
    }

    public void showAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        loanView.displayLoans(loans);
    }

    public void showActiveLoans() {
        List<Loan> loans = loanService.getActiveLoans();
        loanView.displayLoans(loans);
    }

    public void showOverdueLoans() {
        List<Loan> loans = loanService.getOverdueLoans();
        loanView.displayLoans(loans);
    }

    public void showMemberLoans() {
        try {
            Integer memberId = loanView.askForMemberId();
            List<Loan> loans = loanService.getLoansByMember(memberId);
            loanView.displayLoans(loans);

        } catch (Exception e) {
            loanView.showErrorMessage("Error inesperado: " + e.getMessage());
        }
    }
}