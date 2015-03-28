package tests;

import ngordnet.YearlyRecord;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class YearlyRecordTest {

	@Test
	public void yearlyRecordBasic() {
        YearlyRecord yr = new YearlyRecord();
        yr.put("quayside", 95);        
        yr.put("surrogate", 340);
        yr.put("merchantman", 181);      

        assertEquals(95, yr.count("quayside"));
        assertEquals(340, yr.count("surrogate"));
        assertEquals(181, yr.count("merchantman"));

        Collection<Number> counts = yr.counts();
        int[] orderedInts = {95, 181, 340};
        int i = 0;
        for (Number count: counts) {
            assertEquals(orderedInts[i], count);
            i += 1;
        }

        i = 0;

        Collection<String> words = yr.words();
        String[] orderedStrings = {"quayside", "merchantman", "surrogate"};
        for (String word : words) {
            assertEquals(orderedStrings[i], word);
            i += 1;
        }

        assertEquals(1, yr.rank("surrogate"));
        assertEquals(3, yr.rank("quayside"));
        assertEquals(3, yr.size());
	}

	@Test
	public void yearlyRecordConstructors() {
        HashMap<String, Integer> rawData = new HashMap<String, Integer>();
        rawData.put("berry", 1290);
        rawData.put("auscultating", 6);
        rawData.put("temporariness", 20);
        rawData.put("puppetry", 191);
        YearlyRecord yr2 = new YearlyRecord(rawData);

        assertEquals(4, yr2.rank("auscultating"));
        assertEquals(1290, yr2.count("berry"));
        assertEquals(6, yr2.count("auscultating"));
        assertEquals(191, yr2.count("puppetry"));

        yr2.put("wariness", 3);
        yr2.put("drowsiness", 23);
        
        assertEquals(1, yr2.rank("berry"));
        assertEquals(2, yr2.rank("puppetry"));
        assertEquals(3, yr2.rank("drowsiness"));
        assertEquals(4, yr2.rank("temporariness"));
        assertEquals(5, yr2.rank("auscultating"));
        assertEquals(6, yr2.rank("wariness"));
	}

    @Test
    public void yearlyRecordPut() {
        YearlyRecord yr = new YearlyRecord();

        yr.put("something", 10);
        yr.put("nothing", 30);

        assertEquals(2, yr.rank("something"));

        yr.put("something", 100);

        assertEquals(1, yr.rank("something"));
    }

	public static void main(String... args) {
        jh61b.junit.textui.runClasses(YearlyRecordTest.class);
    }
}
