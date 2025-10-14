package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConfig {
    private static final Logger logger = Logger.getLogger(DatabaseConfig.class.getName());

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/library_db";
        String user = "root";
        String password = "Qwe.123*";

        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("🔌 Intentando conectar a: " + url);
            System.out.println("👤 Usuario: " + user);

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Conexión exitosa a MySQL!");
            return conn;

        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "MySQL Driver no encontrado", e);
            throw new SQLException("Driver no encontrado");
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
            System.err.println("💡 Solución: Verifica que MySQL esté ejecutándose y las credenciales sean correctas");
            throw e;
        }
    }

    public static void initializeDatabase() {
        System.out.println("🔄 Inicializando base de datos...");

        // Primero crear la base de datos si no existe
        String createDatabase = "CREATE DATABASE IF NOT EXISTS library_db";
        String useDatabase = "USE library_db";

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

            // Crear base de datos
            stmt.execute(createDatabase);
            stmt.execute(useDatabase);

            // Crear tablas
            stmt.execute(createBooksTable);
            stmt.execute(createMembersTable);
            stmt.execute(createLoansTable);

            System.out.println("✅ Base de datos y tablas creadas exitosamente");

        } catch (SQLException e) {
            System.err.println("❌ Error creando base de datos: " + e.getMessage());
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}