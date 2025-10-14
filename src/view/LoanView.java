package view;

import domain.Loan;
import javax.swing.JOptionPane;
import java.util.List;

public class LoanView {

    // Method to ask user for book ID input
    public Integer askForBookId() {
        String input = JOptionPane.showInputDialog("Enter the book ID:");
        return input != null ? Integer.parseInt(input) : null;
    }

    // Method to ask user for member ID input
    public Integer askForMemberId() {
        String input = JOptionPane.showInputDialog("Enter the member ID:");
        return input != null ? Integer.parseInt(input) : null;
    }

    // Method to ask user for loan ID input
    public Integer askForLoanId() {
        String input = JOptionPane.showInputDialog("Enter the loan ID:");
        return input != null ? Integer.parseInt(input) : null;
    }

    // Method to display list of loans in a dialog
    public void displayLoans(List<Loan> loans) {
        // Check if loans list is empty
        if (loans.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No loans found.");
            return;
        }

        // Build formatted string with all loans information
        StringBuilder sb = new StringBuilder();
        sb.append("=== LOAN LIST ===\n\n");

        for (Loan loan : loans) {
            sb.append("Loan ID: ").append(loan.getId()).append("\n");
            sb.append("Book: ").append(loan.getBookTitle()).append(" (ISBN: ").append(loan.getBookIsbn()).append(")\n");
            sb.append("Member: ").append(loan.getMemberName()).append("\n");
            sb.append("Loan Date: ").append(loan.getLoanDate()).append("\n");
            sb.append("Due Date: ").append(loan.getDueDate()).append("\n");
            sb.append("Return Date: ").append(loan.getReturnDate() != null ? loan.getReturnDate() : "Pending").append("\n");
            sb.append("Status: ").append(loan.getStatus()).append("\n");
            sb.append("Fine: $").append(loan.getFineAmount()).append("\n");
            sb.append("------------------------\n");
        }

        // Display loans information in dialog
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    // Method to show success message dialog
    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to show error message dialog
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}