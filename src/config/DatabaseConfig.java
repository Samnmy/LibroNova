package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConfig {
    // Logger instance for logging database operations and errors
    private static final Logger logger = Logger.getLogger(DatabaseConfig.class.getName());

    // Method to establish and return a database connection
    public static Connection getConnection() throws SQLException {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/library_db";
        String user = "root";
        String password = "Qwe.123*";

        try {
            // Load MySQL JDBC driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Display connection attempt information
            System.out.println("Attempting connection to: " + url);
            System.out.println("User: " + user);

            // Establish connection to MySQL database
            Connection conn = DriverManager.getConnection(url, user, password);
            // Success message
            System.out.println("Connection to MySQL successful!");
            return conn;

        } catch (ClassNotFoundException e) {
            // Log error if MySQL driver is not found
            logger.log(Level.SEVERE, "MySQL Driver not found", e);
            throw new SQLException("Driver not found");
        } catch (SQLException e) {
            // Handle SQL connection errors and display troubleshooting tips
            System.err.println("Connection error: " + e.getMessage());
            System.err.println("Solution: Verify MySQL is running and credentials are correct");
            throw e;
        }
    }

    // Method to initialize database schema and tables
    public static void initializeDatabase() {
        System.out.println("Initializing database...");

        // SQL statement to create database if it doesn't exist
        String createDatabase = "CREATE DATABASE IF NOT EXISTS library_db";
        // SQL statement to switch to the library database
        String useDatabase = "USE library_db";

        // SQL statement to create books table with all necessary columns
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

        // SQL statement to create members table with member information
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

        // SQL statement to create loans table with foreign key relationships
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

        try (Connection conn = getConnection();
             var stmt = conn.createStatement()) {

            // Execute database creation and selection
            stmt.execute(createDatabase);
            stmt.execute(useDatabase);

            // Execute table creation statements
            stmt.execute(createBooksTable);
            stmt.execute(createMembersTable);
            stmt.execute(createLoansTable);

            // Success message after database initialization
            System.out.println("Database and tables created successfully");

        } catch (SQLException e) {
            // Error handling for database creation failures
            System.err.println("Error creating database: " + e.getMessage());
        }
    }

    // Getter method for logger instance
    public static Logger getLogger() {
        return logger;
    }
}