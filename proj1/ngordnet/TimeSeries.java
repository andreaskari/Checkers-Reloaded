package ngordnet;

import java.util.*;

public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {    
    /** Constructs a new empty TimeSeries. */
    public TimeSeries() {
      super();
    }

    /** Creates a copy of TS, but only between STARTYEAR and ENDYEAR. 
     * inclusive of both end points. */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {
        for (Number year: ts.keySet()) {
            if (year.intValue() >= startYear && year.intValue() <= endYear) {
                put((Integer) year, ts.get((Integer) year));
            }
        }
    }

    /** Creates a copy of TS. */
    public TimeSeries(TimeSeries<T> ts) {
        Collection<Number> yearNumbers = ts.years();
        for (int i = 0; i < yearNumbers.size(); i++) {
            for (Number year: yearNumbers) {
                put((Integer) year, ts.get((Integer) year));
            }
        }
    }

    /** Returns the quotient of this time series divided by the relevant value in ts.
      * If ts is missing a key in this time series, return an IllegalArgumentException. */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts) {
        if (this.years().size() != ts.years().size()) {
            throw new IllegalArgumentException();
        }
        TimeSeries<Double> tsQuotient = new TimeSeries<Double>();
        for (Number year: ts.keySet()) {
            if (years().contains(year)) {
                double quotient = this.get((Integer) year).doubleValue() / ts.get((Integer) year).doubleValue();
                tsQuotient.put((Integer) year, quotient);
            } else {
                throw new IllegalArgumentException();
            }
        }
        return tsQuotient;
    }

    /** Returns the sum of this time series with the given ts. The result is a 
      * a Double time series (for simplicity). */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> tsSum = new TimeSeries<Double>();
        for (Number year: ts.keySet()) {
            if (years().contains(year)) {
                double sum = ts.get((Integer) year).doubleValue() + this.get((Integer) year).doubleValue();
                tsSum.put((Integer) year, sum);
            } else {
                tsSum.put((Integer) year, ts.get((Integer) year).doubleValue());
            }
        }
        for (Number year: keySet()) {
            if (!ts.years().contains(year)) {
                tsSum.put((Integer) year, this.get((Integer) year).doubleValue());
            }
        }
        return tsSum;
    }

    /** Returns all years for this time series (in any order). */
    public Collection<Number> years() {
        Collection<Number> yearNumbers = new TreeSet<Number>();
        for (Number year: keySet()) {
            yearNumbers.add(year);
        }
        return yearNumbers;
    }

    /** Returns all data for this time series (in any order). */
    public Collection<Number> data() {
        Collection<Number> dataNumbers = new TreeSet<Number>();
        for (Number year: values()) {
            dataNumbers.add(year);
        }
        return dataNumbers;
    }
}