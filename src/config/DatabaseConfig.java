package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConfig {
    // This is like having a notebook to write down everything that happens with the database
    // It helps us track when connections are made, when errors occur, etc.
    private static final Logger logger = Logger.getLogger(DatabaseConfig.class.getName());

    // These are like the "address" and "keys" to get into our database house
    // URL = the exact location of our database (like a house address)
    // USER = who we're pretending to be when we knock on the door
    // PASSWORD = the secret code to prove we're allowed to enter
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Qwe.123*";

    // This block runs automatically when the class is first used
    // Think of it as setting up our logging notebook before we start working
    static {
        setupLogger();
    }

    // This method prepares our logging notebook with the right format
    // It tells the notebook how to write down dates, times, and messages
    private static void setupLogger() {
        try {
            // This is like saying: "When you write in the notebook, use this format:
            // [Date Time] [TYPE] Message"
            System.setProperty("java.util.logging.SimpleFormatter.format",
                    "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        } catch (Exception e) {
            // If we can't set up the notebook properly, write the error somewhere else
            System.err.println("Error configuring logger: " + e.getMessage());
        }
    }

    // This is like calling a taxi to take us to the database house
    // It establishes a connection path between our program and the database
    public static Connection getConnection() throws SQLException {
        try {
            // First, we need to make sure we have the right "car" (MySQL driver)
            // This is like checking we have the right vehicle to get to MySQL database
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Write in our notebook that we're trying to connect
            logger.info("Attempting database connection to: " + URL);
            logger.info("Connection user: " + USER);

            // Actually knock on the database door with our credentials
            // If successful, we get a "Connection" object - our open door to the database
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Write in our notebook that we successfully entered
            logger.info("Database connection established successfully");
            return conn;

        } catch (ClassNotFoundException e) {
            // Oops! We don't have the MySQL driver installed
            // This is like trying to drive a car that doesn't exist
            logger.log(Level.SEVERE, "MySQL JDBC Driver not found in classpath", e);
            throw new SQLException("Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            // The database rejected us - wrong password, database doesn't exist, etc.
            logger.log(Level.SEVERE, "Failed to establish database connection", e);
            throw new SQLException("Connection failed: " + e.getMessage(), e);
        }
    }

    // This is like a secret, quiet connection for transactions
    // Transactions are groups of operations that must all succeed or all fail together
    public static Connection getTransactionConnection() throws SQLException {
        try {
            // Make sure we have the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Get a connection but don't make a big fuss about it
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // Just write a small note that we got a transaction connection
            logger.fine("Transaction connection established");
            return conn;

        } catch (ClassNotFoundException e) {
            // No MySQL driver found
            logger.log(Level.SEVERE, "MySQL Driver not found for transaction", e);
            throw new SQLException("Driver not found");
        } catch (SQLException e) {
            // Couldn't connect to the database
            logger.log(Level.SEVERE, "Failed to establish transaction connection", e);
            throw e;
        }
    }

    // This method builds the entire database structure from scratch
    // It's like building the shelves, tables, and organization system for a new library
    public static void initializeDatabase() {
        logger.info("Starting database initialization...");

        // First, create the database itself if it doesn't exist
        // Think of this as building the library building
        String createDatabase = "CREATE DATABASE IF NOT EXISTS library_db";

        // Tell MySQL we want to work with our library database
        // This is like walking into our specific library building
        String useDatabase = "USE library_db";

        // Build the "books" table - this is like building the bookshelves
        // Each column is like a different piece of information we track about each book
        String createBooksTable = """
            CREATE TABLE IF NOT EXISTS books (
                id INT AUTO_INCREMENT PRIMARY KEY,
                isbn VARCHAR(20) UNIQUE NOT NULL,
                title VARCHAR(255) NOT NULL,
                author VARCHAR(255) NOT NULL,
                year_published INT,
                genre VARCHAR(100),
                total_copies INT DEFAULT 0,
                available_copies INT DEFAULT 0,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

        // Build the "members" table - this is like creating membership cards
        String createMembersTable = """
            CREATE TABLE IF NOT EXISTS members (
                id INT AUTO_INCREMENT PRIMARY KEY,
                id_number VARCHAR(20) UNIQUE NOT NULL,
                first_name VARCHAR(100) NOT NULL,
                last_name VARCHAR(100) NOT NULL,
                email VARCHAR(255),
                phone VARCHAR(20),
                membership_date DATE NOT NULL,
                active BOOLEAN DEFAULT TRUE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

        // Build the "loans" table - this is like the checkout desk records
        // It connects books to members and tracks who borrowed what and when
        String createLoansTable = """
            CREATE TABLE IF NOT EXISTS loans (
                id INT AUTO_INCREMENT PRIMARY KEY,
                book_id INT NOT NULL,
                member_id INT NOT NULL,
                loan_date DATE NOT NULL,
                due_date DATE NOT NULL,
                return_date DATE NULL,
                status VARCHAR(20) DEFAULT 'ACTIVE',
                fine_amount DECIMAL(10,2) DEFAULT 0.00,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (book_id) REFERENCES books(id),
                FOREIGN KEY (member_id) REFERENCES members(id)
            )
            """;

        try (Connection conn = getConnection();  // Get our database "taxi"
             var stmt = conn.createStatement()) { // Create a "messenger" to send our commands

            logger.info("Creating database and tables...");

            // Send the command to create the database building
            stmt.execute(createDatabase);
            // Send the command to enter our specific library building
            stmt.execute(useDatabase);

            // Build the bookshelves (books table)
            stmt.execute(createBooksTable);
            logger.info("Books table created/verified");

            // Build the membership desk (members table)
            stmt.execute(createMembersTable);
            logger.info("Members table created/verified");

            // Build the checkout desk records (loans table)
            stmt.execute(createLoansTable);
            logger.info("Loans table created/verified");

            // Celebrate! Our library database is ready to use
            logger.info("Database initialization completed successfully");

        } catch (SQLException e) {
            // Oh no! Something went wrong while building our database
            logger.log(Level.SEVERE, "Database initialization failed", e);
            System.err.println("Error creating database: " + e.getMessage());
        }
    }

    // This method politely closes the database connection
    // Think of it as closing the door when we leave the database house
    public static void closeConnection(Connection conn) {
        if (conn != null) {  // Only try to close if we actually have an open connection
            try {
                conn.close();  // Say goodbye to the database
                logger.fine("Database connection closed successfully");
            } catch (SQLException e) {
                // The door got stuck! Couldn't close properly
                logger.log(Level.WARNING, "Error closing database connection", e);
            }
        }
    }

    // This is the "undo" button for database operations
    // If something goes wrong during a transaction, this reverts all changes
    public static void rollbackTransaction(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();  // Tell the database: "Forget everything I just did!"
                logger.info("Transaction rolled back successfully");
            } catch (SQLException e) {
                // Couldn't undo the operations - this is bad!
                logger.log(Level.SEVERE, "Error rolling back transaction", e);
            }
        }
    }

    // This is the "save" button for database operations
    // It makes all the changes in a transaction permanent
    public static void commitTransaction(Connection conn) {
        if (conn != null) {
            try {
                conn.commit();  // Tell the database: "Keep all the changes I made!"
                logger.info("Transaction committed successfully");
            } catch (SQLException e) {
                // Couldn't save the changes - this is problematic!
                logger.log(Level.SEVERE, "Error committing transaction", e);
            }
        }
    }

    // This lets other parts of our program use our logging notebook
    public static Logger getLogger() {
        return logger;
    }
}