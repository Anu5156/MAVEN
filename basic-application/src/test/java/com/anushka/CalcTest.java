// package com.anushka;
// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.assertEquals;


// public class CalcTest {
//     @Test
//     void testcalc(){
//         Calc calculator=new Calc();
//         int res=calculator.divide(10,5);
//         int expectedResult=2;

//         assertEquals(expectedResult,res);
//     }
// }

package com.anushka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CalcTest {

    private final Calc calculator = new Calc();

    @Test
    @DisplayName("10 / 5 should be 2")
    void dividesTwoNumbers() {
        int result = calculator.divide(10, 5);
        int expectedResult = 2;
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Dividing by zero throws ArithmeticException")
    void divideByZeroThrows() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(10, 0));
    }
}