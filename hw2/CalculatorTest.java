import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {
    /* Do not change this to be private. For silly testing reasons it is public. */
    public Calculator tester;

    /**
     * setUp() performs setup work for your Calculator.  In short, we 
     * initialize the appropriate Calculator for you to work with.
     * By default, we have set up the Staff Calculator for you to test your 
     * tests.  To use your unit tests for your own implementation, comment 
     * out the StaffCalculator() line and uncomment the Calculator() line.
     **/
    @Before
    public void setUp() {
        tester = new StaffCalculator(); // Comment me out to test your Calculator
        // tester = new Calculator();   // Un-comment me to test your Calculator
    }

    // TASK 1: WRITE JUNIT TESTS
    // YOUR CODE HERE -- DONE

    @Test
    public void testAdd1() {
        assertEquals(6, tester.add(0, 6));
    }

    @Test
    public void testAdd2() {
        assertEquals(11, tester.add(5, 6));
    }

    @Test
    public void testAdd3() {
        assertEquals(-4, tester.add(-10, 6));
    }

    @Test
    public void testAdd4() {
        assertEquals(53, tester.add(54, -1));
    }

    @Test
    public void testAdd5() {
        assertEquals(-26, tester.add(-20, -6));
    }

    @Test
    public void testMultiply1() {
        assertEquals(0, tester.multiply(0, 6));
    }

    @Test
    public void testMultiply2() {
        assertEquals(30, tester.multiply(5, 6));
    }

    @Test
    public void testMultiply3() {
        assertEquals(-60, tester.multiply(10, -6));
    }

    @Test
    public void testMultiply4() {
        assertEquals(-54, tester.multiply(54, -1));
    }

    @Test
    public void testMultiply5() {
        assertEquals(120, tester.multiply(-20, -6));
    }

    /* Run the unit tests in this file. */
    public static void main(String... args) {
        jh61b.junit.textui.runClasses(CalculatorTest.class);
    }       
}