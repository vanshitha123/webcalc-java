package mypackage;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CalculatorTest {
    @Test
    public void twoAndThreeIsFive() throws Exception {
        final long result = new Calculator().addFunc(2, 3);
        assertThat(result, is(5L));
        // Uncomment the following line to intentionally fail the test
        // assertThat(result, is(6L));
    }

    @Test
    public void threeMinusTwoIsOne() throws Exception {
        final long result = new Calculator().subFunc(2, 3);
        assertThat(result, is(1L));
        // Uncomment the following line to intentionally fail the test
        // assertThat(result, is(2L));
    }

    @Test
    public void threeXThreeIsNine() throws Exception {
        final long result = new Calculator().mulFunc(3, 3);
        assertThat(result, is(9L));
        // Uncomment the following line to intentionally fail the test
        // assertThat(result, is(8L));
    }
}
