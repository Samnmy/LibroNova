package view;

import domain.Book;
import javax.swing.JOptionPane;
import java.util.List;

public class BookView {

    public Book showAddBookForm() {
        String isbn = JOptionPane.showInputDialog("Ingrese el ISBN del libro:");
        String title = JOptionPane.showInputDialog("Ingrese el título del libro:");
        String author = JOptionPane.showInputDialog("Ingrese el autor del libro:");
        String yearStr = JOptionPane.showInputDialog("Ingrese el año de publicación:");
        String genre = JOptionPane.showInputDialog("Ingrese el género del libro:");
        String copiesStr = JOptionPane.showInputDialog("Ingrese el número de copias totales:");

        Integer year = yearStr != null && !yearStr.trim().isEmpty() ? Integer.parseInt(yearStr) : null;
        Integer copies = copiesStr != null ? Integer.parseInt(copiesStr) : 1;

        return new Book(isbn, title, author, year, genre, copies);
    }

    public Book showUpdateBookForm(Book book) {
        String title = JOptionPane.showInputDialog("Ingrese el nuevo título:", book.getTitle());
        String author = JOptionPane.showInputDialog("Ingrese el nuevo autor:", book.getAuthor());
        String yearStr = JOptionPane.showInputDialog("Ingrese el nuevo año:", book.getYearPublished());
        String genre = JOptionPane.showInputDialog("Ingrese el nuevo género:", book.getGenre());
        String copiesStr = JOptionPane.showInputDialog("Ingrese el nuevo número de copias totales:", book.getTotalCopies());

        book.setTitle(title);
        book.setAuthor(author);
        book.setYearPublished(yearStr != null && !yearStr.trim().isEmpty() ? Integer.parseInt(yearStr) : null);
        book.setGenre(genre);
        book.setTotalCopies(Integer.parseInt(copiesStr));

        return book;
    }

    public String askForIsbn() {
        return JOptionPane.showInputDialog("Ingrese el ISBN del libro:");
    }

    public String askForTitle() {
        return JOptionPane.showInputDialog("Ingrese el título a buscar:");
    }

    public String askForAuthor() {
        return JOptionPane.showInputDialog("Ingrese el autor a buscar:");
    }

    public void displayBooks(List<Book> books) {
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron libros.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== LISTA DE LIBROS ===\n\n");

        for (Book book : books) {
            sb.append("ISBN: ").append(book.getIsbn()).append("\n");
            sb.append("Título: ").append(book.getTitle()).append("\n");
            sb.append("Autor: ").append(book.getAuthor()).append("\n");
            sb.append("Género: ").append(book.getGenre()).append("\n");
            sb.append("Año: ").append(book.getYearPublished()).append("\n");
            sb.append("Copias: ").append(book.getAvailableCopies()).append("/").append(book.getTotalCopies()).append("\n");
            sb.append("------------------------\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public boolean confirmDelete(Book book) {
        int response = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar el libro?\n" +
                        "Título: " + book.getTitle() + "\n" +
                        "ISBN: " + book.getIsbn(),
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

        return response == JOptionPane.YES_OPTION;
    }

    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}