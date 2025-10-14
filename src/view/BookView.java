package view;

import domain.Book;
import javax.swing.JOptionPane;
import java.util.List;

public class BookView {

    // Method to display form for adding a new book
    public Book showAddBookForm() {
        // Show input dialogs to get book information from user
        String isbn = JOptionPane.showInputDialog("Enter the book ISBN:");
        String title = JOptionPane.showInputDialog("Enter the book title:");
        String author = JOptionPane.showInputDialog("Enter the book author:");
        String yearStr = JOptionPane.showInputDialog("Enter the publication year:");
        String genre = JOptionPane.showInputDialog("Enter the book genre:");
        String copiesStr = JOptionPane.showInputDialog("Enter the total number of copies:");

        // Parse year with null check for empty input
        Integer year = yearStr != null && !yearStr.trim().isEmpty() ? Integer.parseInt(yearStr) : null;
        // Parse copies with default value of 1 if null
        Integer copies = copiesStr != null ? Integer.parseInt(copiesStr) : 1;

        // Create and return new Book object with collected data
        return new Book(isbn, title, author, year, genre, copies);
    }

    // Method to display form for updating an existing book
    public Book showUpdateBookForm(Book book) {
        // Show input dialogs with current values as defaults
        String title = JOptionPane.showInputDialog("Enter the new title:", book.getTitle());
        String author = JOptionPane.showInputDialog("Enter the new author:", book.getAuthor());
        String yearStr = JOptionPane.showInputDialog("Enter the new year:", book.getYearPublished());
        String genre = JOptionPane.showInputDialog("Enter the new genre:", book.getGenre());
        String copiesStr = JOptionPane.showInputDialog("Enter the new total number of copies:", book.getTotalCopies());

        // Update book object with new values
        book.setTitle(title);
        book.setAuthor(author);
        book.setYearPublished(yearStr != null && !yearStr.trim().isEmpty() ? Integer.parseInt(yearStr) : null);
        book.setGenre(genre);
        book.setTotalCopies(Integer.parseInt(copiesStr));

        // Return updated book object
        return book;
    }

    // Method to ask user for ISBN input
    public String askForIsbn() {
        return JOptionPane.showInputDialog("Enter the book ISBN:");
    }

    // Method to ask user for title search input
    public String askForTitle() {
        return JOptionPane.showInputDialog("Enter the title to search:");
    }

    // Method to ask user for author search input
    public String askForAuthor() {
        return JOptionPane.showInputDialog("Enter the author to search:");
    }

    // Method to display list of books in a dialog
    public void displayBooks(List<Book> books) {
        // Check if books list is empty
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No books found.");
            return;
        }

        // Build formatted string with all books information
        StringBuilder sb = new StringBuilder();
        sb.append("=== BOOK LIST ===\n\n");

        for (Book book : books) {
            sb.append("ISBN: ").append(book.getIsbn()).append("\n");
            sb.append("Title: ").append(book.getTitle()).append("\n");
            sb.append("Author: ").append(book.getAuthor()).append("\n");
            sb.append("Genre: ").append(book.getGenre()).append("\n");
            sb.append("Year: ").append(book.getYearPublished()).append("\n");
            sb.append("Copies: ").append(book.getAvailableCopies()).append("/").append(book.getTotalCopies()).append("\n");
            sb.append("------------------------\n");
        }

        // Display books information in dialog
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    // Method to confirm book deletion with user
    public boolean confirmDelete(Book book) {
        int response = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete the book?\n" +
                        "Title: " + book.getTitle() + "\n" +
                        "ISBN: " + book.getIsbn(),
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        // Return true if user confirms deletion
        return response == JOptionPane.YES_OPTION;
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