import java.util.Comparator;
import java.lang.Math;

public class RadiusComparator implements Comparator<Planet> {

    public RadiusComparator() {
    }

    /** Returns the difference in mass as an int.
     *  Round after calculating the difference. */
    public int compare(Planet planet1, Planet planet2) {
    	double r1 = planet1.getRadius();
    	double r2 = planet2.getRadius();
    	if (r1 > r2) {
    		return 1;
    	} else if (r1 < r2) {
    		return -1;
    	}
    	return 0;
    }
}