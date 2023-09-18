package Demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class DemoTest {
    @Test
    public void testDivide() {
        int x = 4;
        int y = 2;

        double result = demo.divide(x, y);

        assertEquals(2.0, result);
    }

    @Test
    public void testDivide2() {
        int x = 5;
        int y = 0;

        double result = demo.divide(x, y);

        assertEquals(0.0, result);
    }

}
