package view;

import domain.Loan;
import javax.swing.JOptionPane;
import java.util.List;

public class LoanView {

    public Integer askForBookId() {
        String input = JOptionPane.showInputDialog("Ingrese el ID del libro:");
        return input != null ? Integer.parseInt(input) : null;
    }

    public Integer askForMemberId() {
        String input = JOptionPane.showInputDialog("Ingrese el ID del socio:");
        return input != null ? Integer.parseInt(input) : null;
    }

    public Integer askForLoanId() {
        String input = JOptionPane.showInputDialog("Ingrese el ID del préstamo:");
        return input != null ? Integer.parseInt(input) : null;
    }

    public void displayLoans(List<Loan> loans) {
        if (loans.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron préstamos.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== LISTA DE PRÉSTAMOS ===\n\n");

        for (Loan loan : loans) {
            sb.append("ID Préstamo: ").append(loan.getId()).append("\n");
            sb.append("Libro: ").append(loan.getBookTitle()).append(" (ISBN: ").append(loan.getBookIsbn()).append(")\n");
            sb.append("Socio: ").append(loan.getMemberName()).append("\n");
            sb.append("Fecha préstamo: ").append(loan.getLoanDate()).append("\n");
            sb.append("Fecha vencimiento: ").append(loan.getDueDate()).append("\n");
            sb.append("Fecha devolución: ").append(loan.getReturnDate() != null ? loan.getReturnDate() : "Pendiente").append("\n");
            sb.append("Estado: ").append(loan.getStatus()).append("\n");
            sb.append("Multa: $").append(loan.getFineAmount()).append("\n");
            sb.append("------------------------\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}