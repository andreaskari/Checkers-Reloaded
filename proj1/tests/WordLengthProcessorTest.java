package tests;

import ngordnet.YearlyRecord;
import ngordnet.WordLengthProcessor;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class WordLengthProcessorTest {

	@Test
	public void yearlyRecordBasic() {
        YearlyRecord yr = new YearlyRecord();
        yr.put("sheep", 100);
        yr.put("dog", 300);
        WordLengthProcessor wlp = new WordLengthProcessor();

        assertEquals(3.5, wlp.process(yr), 0.01); //prints 3.5
	}

	public static void main(String... args) {
        jh61b.junit.textui.runClasses(WordLengthProcessorTest.class);
    }
}

