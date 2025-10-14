package domain;

import java.time.LocalDateTime;

public class Book {
    // Private fields representing book attributes
    private Integer id;
    private String isbn;
    private String title;
    private String author;
    private Integer yearPublished;
    private String genre;
    private Integer totalCopies;
    private Integer availableCopies;
    private LocalDateTime createdAt;

    // Default constructor
    public Book() {}

    // Parameterized constructor for creating new books without ID
    public Book(String isbn, String title, String author, Integer yearPublished,
                String genre, Integer totalCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies; // Initially all copies are available
    }

    // Getter for book ID
    public Integer getId() { return id; }
    // Setter for book ID
    public void setId(Integer id) { this.id = id; }

    // Getter for ISBN
    public String getIsbn() { return isbn; }
    // Setter for ISBN
    public void setIsbn(String isbn) { this.isbn = isbn; }

    // Getter for book title
    public String getTitle() { return title; }
    // Setter for book title
    public void setTitle(String title) { this.title = title; }

    // Getter for author name
    public String getAuthor() { return author; }
    // Setter for author name
    public void setAuthor(String author) { this.author = author; }

    // Getter for publication year
    public Integer getYearPublished() { return yearPublished; }
    // Setter for publication year
    public void setYearPublished(Integer yearPublished) { this.yearPublished = yearPublished; }

    // Getter for book genre
    public String getGenre() { return genre; }
    // Setter for book genre
    public void setGenre(String genre) { this.genre = genre; }

    // Getter for total number of copies
    public Integer getTotalCopies() { return totalCopies; }
    // Setter for total number of copies
    public void setTotalCopies(Integer totalCopies) { this.totalCopies = totalCopies; }

    // Getter for available copies count
    public Integer getAvailableCopies() { return availableCopies; }
    // Setter for available copies count
    public void setAvailableCopies(Integer availableCopies) { this.availableCopies = availableCopies; }

    // Getter for creation timestamp
    public LocalDateTime getCreatedAt() { return createdAt; }
    // Setter for creation timestamp
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Overridden toString method for displaying book information
    @Override
    public String toString() {
        return String.format("ISBN: %s, Title: %s, Author: %s, Available: %d/%d",
                isbn, title, author, availableCopies, totalCopies);
    }
}