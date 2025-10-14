package controller;

import domain.Loan;
import exceptions.BusinessException;
import service.LoanService;
import view.LoanView;
import java.util.List;

public class LoanController {
    // Service layer dependency for loan business logic operations
    private LoanService loanService;
    // View layer dependency for loan-related user interface interactions
    private LoanView loanView;

    // Constructor initializes loan service and view dependencies
    public LoanController() {
        this.loanService = new LoanService();
        this.loanView = new LoanView();
    }

    // Method to handle creating a new loan
    public void createLoan() {
        try {
            // Get book ID from view layer
            Integer bookId = loanView.askForBookId();
            // Get member ID from view layer
            Integer memberId = loanView.askForMemberId();

            // Call service to create loan in database
            loanService.createLoan(bookId, memberId);
            // Show success message to user
            loanView.showSuccessMessage("Loan created successfully");

        } catch (BusinessException e) {
            // Handle business rule violations (e.g., book not available, member has too many loans)
            loanView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            loanView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }

    // Method to handle returning a loan (book return process)
    public void returnLoan() {
        try {
            // Get loan ID from view layer to identify which loan to return
            Integer loanId = loanView.askForLoanId();
            // Call service to process book return and update loan status
            loanService.returnLoan(loanId);
            // Show success message to user
            loanView.showSuccessMessage("Return processed successfully");

        } catch (BusinessException e) {
            // Handle business rule violations (e.g., loan not found, already returned)
            loanView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            loanView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }

    // Method to retrieve and display all loans
    public void showAllLoans() {
        // Get all loans from service layer (both active and returned)
        List<Loan> loans = loanService.getAllLoans();
        // Display loans through view layer
        loanView.displayLoans(loans);
    }

    // Method to retrieve and display only active loans
    public void showActiveLoans() {
        // Get active loans from service layer (loans that haven't been returned yet)
        List<Loan> loans = loanService.getActiveLoans();
        // Display active loans through view layer
        loanView.displayLoans(loans);
    }

    // Method to retrieve and display overdue loans
    public void showOverdueLoans() {
        // Get overdue loans from service layer (loans past their due date)
        List<Loan> loans = loanService.getOverdueLoans();
        // Display overdue loans through view layer
        loanView.displayLoans(loans);
    }

    // Method to retrieve and display loans for a specific member
    public void showMemberLoans() {
        try {
            // Get member ID from view layer
            Integer memberId = loanView.askForMemberId();
            // Get all loans for the specified member from service layer
            List<Loan> loans = loanService.getLoansByMember(memberId);
            // Display member's loans through view layer
            loanView.displayLoans(loans);

        } catch (Exception e) {
            // Handle unexpected errors
            loanView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }
}