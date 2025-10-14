package app;

import config.DatabaseConfig;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("🧪 Probando conexión a MySQL...");

        try {
            Connection conn = DatabaseConfig.getConnection();
            System.out.println("🎉 ¡Conexión exitosa!");
            conn.close();
        } catch (Exception e) {
            System.err.println("💥 Error: " + e.getMessage());
            System.out.println("\n🔧 SOLUCIONES POSIBLES:");
            System.out.println("1. Verifica que MySQL esté ejecutándose");
            System.out.println("2. Prueba con contraseña vacía: password = \"\"");
            System.out.println("3. Prueba con contraseña 'root': password = \"root\"");
            System.out.println("4. Si usas XAMPP, asegúrate de que MySQL esté activo en el panel de control");
        }
    }
}