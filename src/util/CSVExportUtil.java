package util;

import domain.Book;
import domain.Loan;
import domain.Member;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVExportUtil {
    // Logger for tracking export operations and errors
    private static final Logger logger = Logger.getLogger(CSVExportUtil.class.getName());

    // Method to export book data to CSV file
    public static void exportBooksToCSV(List<Book> books, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            // Write CSV header row
            writer.write("ISBN,Title,Author,Year,Genre,Total Copies,Available Copies\n");

            // Write each book as a row in the CSV
            for (Book book : books) {
                writer.write(String.format("\"%s\",\"%s\",\"%s\",%s,\"%s\",%d,%d\n",
                        book.getIsbn(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getYearPublished() != null ? book.getYearPublished().toString() : "",
                        book.getGenre() != null ? book.getGenre() : "",
                        book.getTotalCopies(),
                        book.getAvailableCopies()));
            }

            // Log successful export
            logger.info("Books exported to CSV: " + filename);

        } catch (IOException e) {
            // Log export error
            logger.log(Level.SEVERE, "Error exporting books to CSV", e);
        }
    }

    // Method to export overdue loans data to CSV file
    public static void exportOverdueLoansToCSV(List<Loan> loans, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            // Write CSV header row for overdue loans
            writer.write("Loan ID,Book ISBN,Book Title,Member Name,Due Date,Days Overdue,Fine Amount\n");

            // Write each overdue loan as a row in the CSV
            for (Loan loan : loans) {
                // Calculate days overdue from current date
                long daysOverdue = java.time.LocalDate.now().toEpochDay() - loan.getDueDate().toEpochDay();

                // Format and write loan data
                writer.write(String.format("%d,\"%s\",\"%s\",\"%s\",%s,%d,%.2f\n",
                        loan.getId(),
                        loan.getBookIsbn(),
                        loan.getBookTitle(),
                        loan.getMemberName(),
                        loan.getDueDate().toString(),
                        daysOverdue,
                        loan.getFineAmount()));
            }

            // Log successful export
            logger.info("Overdue loans exported to CSV: " + filename);

        } catch (IOException e) {
            // Log export error
            logger.log(Level.SEVERE, "Error exporting overdue loans to CSV", e);
        }
    }
}