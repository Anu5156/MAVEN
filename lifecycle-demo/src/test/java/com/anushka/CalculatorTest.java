package com.anushka;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private final Calculator calc = new Calculator();

    @Test void addsNumbers()      { assertEquals(5, calc.add(2, 3)); }
    @Test void subtractsNumbers() { assertEquals(1, calc.subtract(3, 2)); }
    @Test void multipliesNumbers(){ assertEquals(6, calc.multiply(2, 3)); }
    @Test void dividesNumbers()   { assertEquals(2, calc.divide(6, 3)); }

    @Test void divideByZeroThrows() {
        assertThrows(ArithmeticException.class, () -> calc.divide(1, 0));
    }
}