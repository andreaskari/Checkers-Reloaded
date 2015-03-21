package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {

	private static final double DELTA = 0.005;
    
    @Test
    public void testEnqueue() {
    	double[] vals = {1.1, 2.2, 3.3, 4.4};
    	ArrayRingBuffer arb = new ArrayRingBuffer(4);

    	for (double v: vals) {
    		arb.enqueue(v);
    		assertEquals(v, arb.peek(), DELTA);
    		assertEquals(v, arb.dequeue(), DELTA);
    	}
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 