package domain;

import java.time.LocalDate;

public class Loan {
    // Private fields representing loan attributes
    private Integer id;
    private Integer bookId;
    private Integer memberId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
    private Double fineAmount;
    private LocalDate createdAt;

    // Additional fields for display purposes (populated from JOIN queries)
    private String bookTitle;
    private String memberName;
    private String bookIsbn;

    // Default constructor
    public Loan() {}

    // Parameterized constructor for creating new loans
    public Loan(Integer bookId, Integer memberId, LocalDate loanDate, LocalDate dueDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.status = "ACTIVE"; // Default status for new loans
        this.fineAmount = 0.0; // Initial fine amount is zero
    }

    // Getter for loan ID
    public Integer getId() { return id; }
    // Setter for loan ID
    public void setId(Integer id) { this.id = id; }

    // Getter for book ID
    public Integer getBookId() { return bookId; }
    // Setter for book ID
    public void setBookId(Integer bookId) { this.bookId = bookId; }

    // Getter for member ID
    public Integer getMemberId() { return memberId; }
    // Setter for member ID
    public void setMemberId(Integer memberId) { this.memberId = memberId; }

    // Getter for loan date
    public LocalDate getLoanDate() { return loanDate; }
    // Setter for loan date
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    // Getter for due date
    public LocalDate getDueDate() { return dueDate; }
    // Setter for due date
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    // Getter for return date (nullable - null if not returned yet)
    public LocalDate getReturnDate() { return returnDate; }
    // Setter for return date
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    // Getter for loan status (ACTIVE, RETURNED, OVERDUE, etc.)
    public String getStatus() { return status; }
    // Setter for loan status
    public void setStatus(String status) { this.status = status; }

    // Getter for fine amount
    public Double getFineAmount() { return fineAmount; }
    // Setter for fine amount
    public void setFineAmount(Double fineAmount) { this.fineAmount = fineAmount; }

    // Getter for creation date
    public LocalDate getCreatedAt() { return createdAt; }
    // Setter for creation date
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    // Getter for book title (from JOIN query)
    public String getBookTitle() { return bookTitle; }
    // Setter for book title
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    // Getter for member name (from JOIN query)
    public String getMemberName() { return memberName; }
    // Setter for member name
    public void setMemberName(String memberName) { this.memberName = memberName; }

    // Getter for book ISBN (from JOIN query)
    public String getBookIsbn() { return bookIsbn; }
    // Setter for book ISBN
    public void setBookIsbn(String bookIsbn) { this.bookIsbn = bookIsbn; }

    // Overridden toString method for displaying loan information
    @Override
    public String toString() {
        return String.format("Loan ID: %d, Book: %s, Member: %s, Due: %s, Status: %s",
                id, bookTitle, memberName, dueDate, status);
    }
}