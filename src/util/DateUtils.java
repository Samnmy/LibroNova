package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    // Date formatter for consistent date formatting across the application
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Method to parse a string into LocalDate object
    public static LocalDate parseDate(String dateStr) {
        try {
            // Parse the date string using the defined formatter
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            // Throw exception with user-friendly message for invalid date format
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD");
        }
    }

    // Method to format a LocalDate object into string
    public static String formatDate(LocalDate date) {
        // Return formatted date or empty string if date is null
        return date != null ? date.format(DATE_FORMATTER) : "";
    }

    // Method to check if a due date is overdue
    public static boolean isOverdue(LocalDate dueDate) {
        // Compare due date with current date
        return LocalDate.now().isAfter(dueDate);
    }

    // Method to calculate the number of days a loan is overdue
    public static long calculateDaysOverdue(LocalDate dueDate) {
        // Calculate difference in days between current date and due date
        return LocalDate.now().toEpochDay() - dueDate.toEpochDay();
    }
}