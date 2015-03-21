import java.util.Comparator;
import java.lang.Math;

/**
 * MassComparator.java
 */

public class MassComparator implements Comparator<Planet> {

    public MassComparator() {
    }

    /** Returns the difference in mass as an int.
     *  Round after calculating the difference. */
    public int compare(Planet planet1, Planet planet2) {
    	double m1 = planet1.getMass();
    	double m2 = planet2.getMass();
    	if (m1 > m2) {
    		return 1;
    	} else if (m1 < m2) {
    		return -1;
    	}
    	return 0;
    }
}