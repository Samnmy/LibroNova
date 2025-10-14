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
    private static final Logger logger = Logger.getLogger(CSVExportUtil.class.getName());

    public static void exportBooksToCSV(List<Book> books, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("ISBN,Title,Author,Year,Genre,Total Copies,Available Copies\n");

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

            logger.info("Books exported to CSV: " + filename);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error exporting books to CSV", e);
        }
    }

    public static void exportOverdueLoansToCSV(List<Loan> loans, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Loan ID,Book ISBN,Book Title,Member Name,Due Date,Days Overdue,Fine Amount\n");

            for (Loan loan : loans) {
                long daysOverdue = java.time.LocalDate.now().toEpochDay() - loan.getDueDate().toEpochDay();

                writer.write(String.format("%d,\"%s\",\"%s\",\"%s\",%s,%d,%.2f\n",
                        loan.getId(),
                        loan.getBookIsbn(),
                        loan.getBookTitle(),
                        loan.getMemberName(),
                        loan.getDueDate().toString(),
                        daysOverdue,
                        loan.getFineAmount()));
            }

            logger.info("Overdue loans exported to CSV: " + filename);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error exporting overdue loans to CSV", e);
        }
    }
}