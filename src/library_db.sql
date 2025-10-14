-- Database: library_db
-- Create database if it doesn't exist and switch to it
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- Books table definition
CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Unique identifier for each book
    isbn VARCHAR(20) UNIQUE NOT NULL, -- International Standard Book Number (unique)
    title VARCHAR(255) NOT NULL, -- Book title
    author VARCHAR(255) NOT NULL, -- Book author
    year_published INT, -- Publication year (nullable)
    genre VARCHAR(100), -- Book genre/category (nullable)
    total_copies INT DEFAULT 0, -- Total number of copies owned
    available_copies INT DEFAULT 0, -- Number of copies currently available
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Record creation timestamp
);

-- Members table definition
CREATE TABLE members (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Unique identifier for each member
    id_number VARCHAR(20) UNIQUE NOT NULL, -- Member identification number (unique)
    first_name VARCHAR(100) NOT NULL, -- Member's first name
    last_name VARCHAR(100) NOT NULL, -- Member's last name
    email VARCHAR(255), -- Member's email address (nullable)
    phone VARCHAR(20), -- Member's phone number (nullable)
    membership_date DATE NOT NULL, -- Date when member joined
    active BOOLEAN DEFAULT TRUE, -- Member status (active/inactive)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Record creation timestamp
);

-- Loans table definition
CREATE TABLE loans (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Unique identifier for each loan
    book_id INT NOT NULL, -- Foreign key referencing books table
    member_id INT NOT NULL, -- Foreign key referencing members table
    loan_date DATE NOT NULL, -- Date when book was borrowed
    due_date DATE NOT NULL, -- Date when book should be returned
    return_date DATE NULL, -- Actual return date (null if not returned yet)
    status ENUM('ACTIVE', 'RETURNED', 'OVERDUE') DEFAULT 'ACTIVE', -- Loan status
    fine_amount DECIMAL(10,2) DEFAULT 0.00, -- Fine amount for overdue returns
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Record creation timestamp
    FOREIGN KEY (book_id) REFERENCES books(id), -- Foreign key constraint to books table
    FOREIGN KEY (member_id) REFERENCES members(id) -- Foreign key constraint to members table
);