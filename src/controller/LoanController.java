package controller;

import domain.Loan;
import exceptions.BusinessException;
import service.LoanService;
import util.CSVExportUtil;
import view.LoanView;
import java.util.List;

public class LoanController {
    // Handles business logic for loan operations
    private LoanService loanService;
    // Handles user interface for loan-related screens
    private LoanView loanView;

    // Constructor - creates new instances of service and view
    public LoanController() {
        this.loanService = new LoanService();
        this.loanView = new LoanView();
    }

    // Handles the process of creating a new book loan
    public void createLoan() {
        try {
            // Get book ID from user input
            Integer bookId = loanView.askForBookId();
            // Get member ID from user input
            Integer memberId = loanView.askForMemberId();

            // Create the loan in the database through service layer
            loanService.createLoan(bookId, memberId);
            // Show confirmation message to user
            loanView.showSuccessMessage("Loan created successfully");

        } catch (BusinessException e) {
            // Show specific error message for business rule violations
            loanView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Show generic error message for unexpected problems
            loanView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }

    // Handles the process of returning a borrowed book
    public void returnLoan() {
        try {
            // Get loan ID from user input to identify which loan to return
            Integer loanId = loanView.askForLoanId();
            // Process the book return through service layer
            loanService.returnLoan(loanId);
            // Show confirmation message to user
            loanView.showSuccessMessage("Return processed successfully");

        } catch (BusinessException e) {
            // Show specific error message for business rule violations
            loanView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Show generic error message for unexpected problems
            loanView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }

    // Retrieves and displays all loans in the system
    public void showAllLoans() {
        // Get complete list of all loans from database
        List<Loan> loans = loanService.getAllLoans();
        // Display the list to the user
        loanView.displayLoans(loans);
    }

    // Retrieves and displays only active loans (books not yet returned)
    public void showActiveLoans() {
        // Get list of currently active loans from database
        List<Loan> loans = loanService.getActiveLoans();
        // Display active loans to user
        loanView.displayLoans(loans);
    }

    // Exports overdue loans to a CSV file for reporting
    public void exportOverdueLoansToCSV() {
        try {
            // Get all overdue loans from database
            List<Loan> loans = loanService.getOverdueLoans();
            // Export to CSV file with automatic filename
            String filename = CSVExportUtil.exportOverdueLoansToCSV(loans);
            // Confirm export success to user
            loanView.showSuccessMessage("Overdue loans exported successfully to: " + filename);
        } catch (Exception e) {
            // Show error if export fails
            loanView.showErrorMessage("Error exporting overdue loans: " + e.getMessage());
        }
    }

    // Exports all loans to a CSV file for reporting
    public void exportAllLoansToCSV() {
        try {
            // Get all loans from database
            List<Loan> loans = loanService.getAllLoans();
            // Export to CSV file with automatic filename
            String filename = CSVExportUtil.exportAllLoansToCSV(loans);
            // Confirm export success to user
            loanView.showSuccessMessage("All loans exported successfully to: " + filename);
        } catch (Exception e) {
            // Show error if export fails
            loanView.showErrorMessage("Error exporting loans: " + e.getMessage());
        }
    }

    // Retrieves and displays only overdue loans (books past due date)
    public void showOverdueLoans() {
        // Get list of overdue loans from database
        List<Loan> loans = loanService.getOverdueLoans();
        // Display overdue loans to user
        loanView.displayLoans(loans);
    }

    // Retrieves and displays loans for a specific member
    public void showMemberLoans() {
        try {
            // Get member ID from user input
            Integer memberId = loanView.askForMemberId();
            // Get all loans for this member from database
            List<Loan> loans = loanService.getLoansByMember(memberId);
            // Display member's loans to user
            loanView.displayLoans(loans);

        } catch (Exception e) {
            // Show generic error message for unexpected problems
            loanView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }
}