package app;

import config.DatabaseConfig;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        // Display message indicating connection test is starting
        System.out.println("🧪 Testing MySQL connection...");

        try {
            // Attempt to get database connection from DatabaseConfig
            Connection conn = DatabaseConfig.getConnection();

            // Success message if connection is established
            System.out.println("🎉 Connection successful!");

            // Close the connection to release resources
            conn.close();
        } catch (Exception e) {
            // Error handling if connection fails
            System.err.println("💥 Error: " + e.getMessage());

            // Display possible solutions for connection issues
            System.out.println("\n🔧 POSSIBLE SOLUTIONS:");
            System.out.println("1. Verify that MySQL is running");
            System.out.println("2. Try with empty password: password = \"\"");
            System.out.println("3. Try with password 'root': password = \"root\"");
        }
    }
}