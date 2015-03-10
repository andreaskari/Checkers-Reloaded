package tests;

import ngordnet.NGramMap;
import ngordnet.YearlyRecord;
import ngordnet.TimeSeries;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

public class NGramMapTest {

	@Test
	public void nGramMapBasic() {
        NGramMap ngm = new NGramMap("./ngrams/all_words.csv", 
                                    "./ngrams/total_counts.csv");


        assertEquals(139, ngm.countInYear("quantity", 1736));
        assertEquals(0, ngm.countInYear("aansdlfkjasda", 1505));

        YearlyRecord yr = ngm.getRecord(1736);
        assertEquals(139, yr.count("quantity"));
	}

    @Test
    public void nGramMapCountHistory() {
        NGramMap ngm = new NGramMap("./ngrams/words_that_start_with_q.csv", 
                                    "./ngrams/total_counts.csv");

        TimeSeries<Integer> countHistory = ngm.countHistory("quantity");
        assertEquals(new Integer(139), countHistory.get(1736));

        TimeSeries<Long> totalCountHistory = ngm.totalCountHistory();
        assertEquals(new Long(8049773), totalCountHistory.get(1736));

        System.out.println((double) countHistory.get(1736) 
                           / (double) totalCountHistory.get(1736)); 
    }

    @Test
    public void nGramMapWeightHistory() {
        NGramMap ngm = new NGramMap("./ngrams/words_that_start_with_q.csv", 
                                    "./ngrams/total_counts.csv");

        TimeSeries<Double> weightHistory = ngm.weightHistory("quantity");
        assertEquals(1.7267E-5, weightHistory.get(1736).doubleValue(), 1E-6);

        ArrayList<String> words = new ArrayList<String>();
        words.add("quantity");
        words.add("quality");        

        TimeSeries<Double> sum = ngm.summedWeightHistory(words);
        assertEquals(3.875E-5, sum.get(1736).doubleValue(), 1E-6);
    }

	public static void main(String... args) {
        jh61b.junit.textui.runClasses(NGramMapTest.class);
    }
}

