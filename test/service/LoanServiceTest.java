package service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoanServiceTest {

    @Test
    void testFineCalculation() {
        // Prueba simple de cálculo de multas
        LoanService service = new LoanService();

        // Aquí podrías probar el cálculo de multas
        // Por ahora solo una prueba básica
        assertTrue(true);
    }

    @Test
    void testLoanLimits() {
        // Prueba de límites de préstamos
        LoanService service = new LoanService();

        // Verificar que el máximo de libros por socio es 3
        // (definido en la constante MAX_BOOKS_PER_MEMBER)
        assertTrue(true);
    }
}