package view;

import controller.BookController;
import controller.LoanController;
import controller.MemberController;
import javax.swing.JOptionPane;

public class MenuView {
    private BookController bookController;
    private MemberController memberController;
    private LoanController loanController;

    public MenuView(BookController bookController, MemberController memberController, LoanController loanController) {
        this.bookController = bookController;
        this.memberController = memberController;
        this.loanController = loanController;
    }

    public void showMainMenu() {
        while (true) {
            String[] options = {
                    "Gestión de Libros",
                    "Gestión de Socios",
                    "Gestión de Préstamos",
                    "Reportes",
                    "Salir"
            };

            int choice = JOptionPane.showOptionDialog(null,
                    "=== SISTEMA DE GESTIÓN DE BIBLIOTECA ===\nSeleccione una opción:",
                    "Menú Principal",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (choice) {
                case 0:
                    showBookMenu();
                    break;
                case 1:
                    showMemberMenu();
                    break;
                case 2:
                    showLoanMenu();
                    break;
                case 3:
                    showReportsMenu();
                    break;
                case 4:
                case -1: // Close button
                    JOptionPane.showMessageDialog(null, "¡Gracias por usar el sistema!");
                    return;
                default:
                    break;
            }
        }
    }

    private void showBookMenu() {
        String[] options = {
                "Agregar Libro",
                "Listar Todos los Libros",
                "Buscar por Título",
                "Buscar por Autor",
                "Actualizar Libro",
                "Eliminar Libro",
                "Volver"
        };

        int choice = JOptionPane.showOptionDialog(null,
                "=== GESTIÓN DE LIBROS ===",
                "Menú de Libros",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0:
                bookController.addBook();
                break;
            case 1:
                bookController.showAllBooks();
                break;
            case 2:
                bookController.searchBooksByTitle();
                break;
            case 3:
                bookController.searchBooksByAuthor();
                break;
            case 4:
                bookController.updateBook();
                break;
            case 5:
                bookController.deleteBook();
                break;
            default:
                break;
        }
    }

    private void showMemberMenu() {
        String[] options = {
                "Agregar Socio",
                "Listar Todos los Socios",
                "Listar Socios Activos",
                "Actualizar Socio",
                "Desactivar Socio",
                "Volver"
        };

        int choice = JOptionPane.showOptionDialog(null,
                "=== GESTIÓN DE SOCIOS ===",
                "Menú de Socios",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0:
                memberController.addMember();
                break;
            case 1:
                memberController.showAllMembers();
                break;
            case 2:
                memberController.showActiveMembers();
                break;
            case 3:
                memberController.updateMember();
                break;
            case 4:
                memberController.deactivateMember();
                break;
            default:
                break;
        }
    }

    private void showLoanMenu() {
        String[] options = {
                "Crear Préstamo",
                "Devolver Libro",
                "Listar Todos los Préstamos",
                "Listar Préstamos Activos",
                "Listar Préstamos por Socio",
                "Volver"
        };

        int choice = JOptionPane.showOptionDialog(null,
                "=== GESTIÓN DE PRÉSTAMOS ===",
                "Menú de Préstamos",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0:
                loanController.createLoan();
                break;
            case 1:
                loanController.returnLoan();
                break;
            case 2:
                loanController.showAllLoans();
                break;
            case 3:
                loanController.showActiveLoans();
                break;
            case 4:
                loanController.showMemberLoans();
                break;
            default:
                break;
        }
    }

    private void showReportsMenu() {
        String[] options = {
                "Préstamos Vencidos",
                "Volver"
        };

        int choice = JOptionPane.showOptionDialog(null,
                "=== REPORTES ===",
                "Menú de Reportes",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0:
                loanController.showOverdueLoans();
                break;
            default:
                break;
        }
    }
}