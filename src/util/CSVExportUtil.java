package util;

import domain.Book;
import domain.Loan;
import domain.Member;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVExportUtil {
    private static final Logger logger = Logger.getLogger(CSVExportUtil.class.getName());

    // Format for timestamp in filenames: yearMonthDay_hourMinuteSecond
    private static final DateTimeFormatter FILE_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    // CSV file formatting constants
    private static final String CSV_DELIMITER = ",";
    private static final String CSV_QUOTE = "\"";
    private static final String LINE_SEPARATOR = "\n";

    //Export all books to CSV file with detailed information
    public static void exportBooksToCSV(List<Book> books, String filename) {
        if (books == null) {
            logger.warning("Cannot export null books list");
            return;
        }

        try (FileWriter writer = new FileWriter(filename)) {
            // Write the column headers at the top of the CSV file
            writer.write(createBooksHeader());

            // Write each book as a separate row in the CSV
            for (Book book : books) {
                writer.write(createBookRow(book));
            }

            logger.info("Successfully exported " + books.size() + " books to: " + filename);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error exporting books to CSV: " + filename, e);
            throw new RuntimeException("Failed to export books to CSV: " + e.getMessage(), e);
        }
    }

    //Export books with automatically generated filename containing timestamp
    public static String exportBooksToCSV(List<Book> books) {
        String filename = "books_export_" + LocalDateTime.now().format(FILE_DATE_FORMATTER) + ".csv";
        exportBooksToCSV(books, filename);
        return filename;
    }

    //Export overdue loans to CSV file with detailed information
    public static void exportOverdueLoansToCSV(List<Loan> loans, String filename) {
        if (loans == null) {
            logger.warning("Cannot export null loans list");
            return;
        }

        try (FileWriter writer = new FileWriter(filename)) {
            // Write column headers for overdue loans
            writer.write(createOverdueLoansHeader());

            // Write each overdue loan as a separate row
            for (Loan loan : loans) {
                writer.write(createOverdueLoanRow(loan));
            }

            logger.info("Successfully exported " + loans.size() + " overdue loans to: " + filename);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error exporting overdue loans to CSV: " + filename, e);
            throw new RuntimeException("Failed to export overdue loans to CSV: " + e.getMessage(), e);
        }
    }
    //Export overdue loans with automatically generated filename
    public static String exportOverdueLoansToCSV(List<Loan> loans) {
        String filename = "overdue_loans_" + LocalDateTime.now().format(FILE_DATE_FORMATTER) + ".csv";
        exportOverdueLoansToCSV(loans, filename);
        return filename;
    }

    //Export all members to CSV file
    public static void exportMembersToCSV(List<Member> members, String filename) {
        if (members == null) {
            logger.warning("Cannot export null members list");
            return;
        }

        try (FileWriter writer = new FileWriter(filename)) {
            // Write column headers for members
            writer.write(createMembersHeader());

            // Write each member as a separate row
            for (Member member : members) {
                writer.write(createMemberRow(member));
            }

            logger.info("Successfully exported " + members.size() + " members to: " + filename);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error exporting members to CSV: " + filename, e);
            throw new RuntimeException("Failed to export members to CSV: " + e.getMessage(), e);
        }
    }
    //Export members with automatically generated filename
    public static String exportMembersToCSV(List<Member> members) {
        String filename = "members_export_" + LocalDateTime.now().format(FILE_DATE_FORMATTER) + ".csv";
        exportMembersToCSV(members, filename);
        return filename;
    }
    //Export all loans to CSV file (complete loan history)
    public static void exportAllLoansToCSV(List<Loan> loans, String filename) {
        if (loans == null) {
            logger.warning("Cannot export null loans list");
            return;
        }

        try (FileWriter writer = new FileWriter(filename)) {
            // Write column headers for all loans
            writer.write(createAllLoansHeader());

            // Write each loan as a separate row
            for (Loan loan : loans) {
                writer.write(createAllLoanRow(loan));
            }

            logger.info("Successfully exported " + loans.size() + " loans to: " + filename);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error exporting all loans to CSV: " + filename, e);
            throw new RuntimeException("Failed to export loans to CSV: " + e.getMessage(), e);
        }
    }
    //Export all loans with automatically generated filename
    public static String exportAllLoansToCSV(List<Loan> loans) {
        String filename = "all_loans_" + LocalDateTime.now().format(FILE_DATE_FORMATTER) + ".csv";
        exportAllLoansToCSV(loans, filename);
        return filename;
    }

    //Private Helper Methods
    //Create CSV header row for books export
    private static String createBooksHeader() {
        return String.join(CSV_DELIMITER,
                "ISBN", "Title", "Author", "Year Published", "Genre",
                "Total Copies", "Available Copies", "Created At"
        ) + LINE_SEPARATOR;
    }

    //Create a CSV data row for a single book
    private static String createBookRow(Book book) {
        return String.join(CSV_DELIMITER,
                escapeCsvField(book.getIsbn()),
                escapeCsvField(book.getTitle()),
                escapeCsvField(book.getAuthor()),
                book.getYearPublished() != null ? book.getYearPublished().toString() : "",
                escapeCsvField(book.getGenre()),
                book.getTotalCopies().toString(),
                book.getAvailableCopies().toString(),
                book.getCreatedAt() != null ? book.getCreatedAt().toString() : ""
        ) + LINE_SEPARATOR;
    }

    //Create CSV header row for overdue loans export
    private static String createOverdueLoansHeader() {
        return String.join(CSV_DELIMITER,
                "Loan ID", "Book ISBN", "Book Title", "Member Name",
                "Loan Date", "Due Date", "Days Overdue", "Fine Amount", "Status"
        ) + LINE_SEPARATOR;
    }

    //Create a CSV data row for a single overdue loan
    private static String createOverdueLoanRow(Loan loan) {
        long daysOverdue = DateUtils.calculateDaysOverdue(loan.getDueDate());

        return String.join(CSV_DELIMITER,
                loan.getId().toString(),
                escapeCsvField(loan.getBookIsbn()),
                escapeCsvField(loan.getBookTitle()),
                escapeCsvField(loan.getMemberName()),
                loan.getLoanDate().toString(),
                loan.getDueDate().toString(),
                String.valueOf(daysOverdue),
                String.format("%.2f", loan.getFineAmount()),
                escapeCsvField(loan.getStatus())
        ) + LINE_SEPARATOR;
    }

    //Create CSV header row for members export
    private static String createMembersHeader() {
        return String.join(CSV_DELIMITER,
                "ID Number", "First Name", "Last Name", "Email", "Phone",
                "Membership Date", "Active Status", "Created At"
        ) + LINE_SEPARATOR;
    }

    //Create a CSV data row for a single member
    private static String createMemberRow(Member member) {
        return String.join(CSV_DELIMITER,
                escapeCsvField(member.getIdNumber()),
                escapeCsvField(member.getFirstName()),
                escapeCsvField(member.getLastName()),
                escapeCsvField(member.getEmail()),
                escapeCsvField(member.getPhone()),
                member.getMembershipDate().toString(),
                member.getActive() ? "Active" : "Inactive",
                member.getCreatedAt() != null ? member.getCreatedAt().toString() : ""
        ) + LINE_SEPARATOR;
    }

    //Create CSV header row for all loans export
    private static String createAllLoansHeader() {
        return String.join(CSV_DELIMITER,
                "Loan ID", "Book ISBN", "Book Title", "Member Name",
                "Loan Date", "Due Date", "Return Date", "Days Overdue",
                "Fine Amount", "Status", "Created At"
        ) + LINE_SEPARATOR;
    }

    //Create a CSV data row for any loan (active or returned)
    private static String createAllLoanRow(Loan loan) {
        long daysOverdue = DateUtils.calculateDaysOverdue(loan.getDueDate());

        return String.join(CSV_DELIMITER,
                loan.getId().toString(),
                escapeCsvField(loan.getBookIsbn()),
                escapeCsvField(loan.getBookTitle()),
                escapeCsvField(loan.getMemberName()),
                loan.getLoanDate().toString(),
                loan.getDueDate().toString(),
                loan.getReturnDate() != null ? loan.getReturnDate().toString() : "Not Returned",
                String.valueOf(daysOverdue),
                String.format("%.2f", loan.getFineAmount()),
                escapeCsvField(loan.getStatus()),
                loan.getCreatedAt() != null ? loan.getCreatedAt().toString() : ""
        ) + LINE_SEPARATOR;
    }

    //Escape special characters in CSV fields to prevent formatting issues
    private static String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }

        // If field contains comma, quotes, or newlines, wrap in quotes and escape existing quotes
        if (field.contains(CSV_DELIMITER) || field.contains(CSV_QUOTE) || field.contains("\n")) {
            return CSV_QUOTE + field.replace(CSV_QUOTE, CSV_QUOTE + CSV_QUOTE) + CSV_QUOTE;
        }

        return field;
    }
}