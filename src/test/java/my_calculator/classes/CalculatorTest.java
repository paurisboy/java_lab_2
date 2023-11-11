package my_calculator.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    @Test
    public void testBasicOperations() {
        assertEquals(10.0, Calculator.calculate("5 + 5"), 0.001);
        assertEquals(5.0, Calculator.calculate("8 - 3"), 0.001);
        assertEquals(30.0, Calculator.calculate("6 * 5"), 0.001);
        assertEquals(8.0, Calculator.calculate("16 / 2"), 0.001);
        assertEquals(125.0, Calculator.calculate("5 ^ 3"), 0.001);
    }

    @Test
    public void testUnaryOperators() {
        assertEquals(-7.0, Calculator.calculate("-7"), 0.001);
        assertEquals(7.0, Calculator.calculate("-(-7)"), 0.001);
    }

    @Test
    public void testMathFunctions() {
        assertEquals(0.0, Calculator.calculate("sin(0)"), 0.001);
        assertEquals(1.0, Calculator.calculate("cos(0)"), 0.001);
        assertEquals(0.0, Calculator.calculate("tan(0)"), 0.001);
        assertEquals(2.0, Calculator.calculate("log(100)"), 0.001);
        assertEquals(0, Calculator.calculate("ln(1)"), 0.001);
        assertEquals(4.0, Calculator.calculate("sqrt(16)"), 0.001);
    }

    @Test
    public void testComplexExpression() {
        assertEquals(20.0, Calculator.calculate("5 + 3 * (7 - 2)"), 0.001);
        assertEquals(22.5, Calculator.calculate("(10 + 5) * 3 / 2"), 0.001);
        assertEquals(15, Calculator.calculate("sqrt(25) * 3"), 0.001);
    }
}
