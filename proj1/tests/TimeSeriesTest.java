package tests;

import ngordnet.TimeSeries;
import static org.junit.Assert.*;
import org.junit.Test;

public class TimeSeriesTest {

	@Test
	public void timeSeriesBasic() {
		TimeSeries<Double> ts = new TimeSeries<Double>();

        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);

        assertTrue(ts.years().contains(new Integer(1992)));
        assertTrue(ts.years().contains(new Integer(1993)));
        assertTrue(ts.years().contains(new Integer(1994)));
        assertTrue(ts.years().contains(new Integer(1995)));
        assertTrue(ts.years().contains(new Integer(1996)));
        assertFalse(ts.years().contains(new Integer(1990)));

        assertTrue(ts.data().contains(new Double(3.6)));
        assertTrue(ts.data().contains(new Double(9.2)));
        assertTrue(ts.data().contains(new Double(15.2)));
        assertTrue(ts.data().contains(new Double(16.1)));
        assertTrue(ts.data().contains(new Double(-15.7)));
        assertFalse(ts.data().contains(new Double(0)));
	}

	@Test
	public void timeSeriesConstructors() {
		TimeSeries<Double> ts = new TimeSeries<Double>();

        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);

        TimeSeries<Double> ts2 = new TimeSeries<Double>(ts);

        assertFalse(ts == ts2);

        assertTrue(ts2.years().contains(new Integer(1992)));
        assertTrue(ts2.years().contains(new Integer(1993)));
        assertTrue(ts2.years().contains(new Integer(1994)));
        assertTrue(ts2.years().contains(new Integer(1995)));
        assertTrue(ts2.years().contains(new Integer(1996)));
        assertFalse(ts2.years().contains(new Integer(1990)));

        assertTrue(ts2.data().contains(new Double(3.6)));
        assertTrue(ts2.data().contains(new Double(9.2)));
        assertTrue(ts2.data().contains(new Double(15.2)));
        assertTrue(ts2.data().contains(new Double(16.1)));
        assertTrue(ts2.data().contains(new Double(-15.7)));
        assertFalse(ts2.data().contains(new Double(0)));

        TimeSeries<Double> ts3 = new TimeSeries<Double>(ts, 1993, 1995);

        assertFalse(ts3.years().contains(new Integer(1992)));
        assertTrue(ts3.years().contains(new Integer(1993)));
        assertTrue(ts3.years().contains(new Integer(1994)));
        assertTrue(ts3.years().contains(new Integer(1995)));
        assertFalse(ts3.years().contains(new Integer(1996)));
        assertFalse(ts3.years().contains(new Integer(1990)));

        assertFalse(ts3.data().contains(new Double(3.6)));
        assertTrue(ts3.data().contains(new Double(9.2)));
        assertTrue(ts3.data().contains(new Double(15.2)));
        assertTrue(ts3.data().contains(new Double(16.1)));
        assertFalse(ts3.data().contains(new Double(-15.7)));
        assertFalse(ts3.data().contains(new Double(0)));
	}

	@Test
	public void testPlus() {
		TimeSeries<Double> ts = new TimeSeries<Double>();

        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);

        TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
        ts2.put(1991, 10);
        ts2.put(1992, -5);
        ts2.put(1993, 1);

        TimeSeries<Double> tSum = ts.plus(ts2);
        
        assertTrue(tSum.years().contains(new Integer(1991)));
        assertTrue(tSum.years().contains(new Integer(1992)));
        assertTrue(tSum.years().contains(new Integer(1993)));
        assertTrue(tSum.years().contains(new Integer(1994)));
        assertTrue(tSum.years().contains(new Integer(1995)));
        assertTrue(tSum.years().contains(new Integer(1996)));
        assertFalse(tSum.years().contains(new Integer(1990)));

        assertEquals(10, tSum.get(new Integer(1991)).doubleValue(), 0.0005);
        assertEquals(-1.4, tSum.get(new Integer(1992)).doubleValue(), 0.0005);
        assertEquals(10.2, tSum.get(new Integer(1993)).doubleValue(), 0.0005);
	}

	@Test
	public void testDividedBy() {
        TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
        ts2.put(1991, 10);
        ts2.put(1992, -5);
        ts2.put(1993, 1);

        TimeSeries<Double> ts3 = new TimeSeries<Double>();
        ts3.put(1991, 5.0);
        ts3.put(1992, 1.0);
        ts3.put(1993, 100.0);

        TimeSeries<Double> tQuotient = ts2.dividedBy(ts3);

		assertEquals(2.0, tQuotient.get(new Integer(1991)).doubleValue(), 0.0005);
        assertEquals(-5.0, tQuotient.get(new Integer(1992)).doubleValue(), 0.0005);
        assertEquals(0.01, tQuotient.get(new Integer(1993)).doubleValue(), 0.0005);
	}

	@Test
	public void testExceptions() {
		TimeSeries<Double> ts = new TimeSeries<Double>();

        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);

        TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
        ts2.put(1991, 10);
        ts2.put(1992, -5);
        ts2.put(1993, 1);

		boolean error = false;
		try {
			TimeSeries<Double> quotient = ts.dividedBy(ts2);
		} catch (Exception e) {
			error = true;
		}
		assertTrue(error);
	}


	public static void main(String... args) {
        jh61b.junit.textui.runClasses(TimeSeriesTest.class);
    }
}

