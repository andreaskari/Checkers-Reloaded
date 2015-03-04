package tests;

import ngordnet.WordNet;
import static org.junit.Assert.*;
import org.junit.Test;

public class WordNetTest {

	@Test
	public void wordNetBasic() {
		WordNet wn = new WordNet("./wordnet/synsets11.txt", "./wordnet/hyponyms11.txt");

		assertTrue(wn.isNoun("jump"));
        assertTrue(wn.isNoun("leap"));
        assertTrue(wn.isNoun("nasal_decongestant"));

        int numNouns = 0;
        for (String noun : wn.nouns()) {
            numNouns += 1;
        }
        assertEquals(12, numNouns);
	}

	@Test
	public void wordNetViceroy() {
		WordNet wn = new WordNet("./wordnet/synsets.txt", "./wordnet/hyponyms.txt");

        for (String h : wn.hyponyms("viceroy")) {
            System.out.println(h);
        }
	}

	@Test
	public void wordNetDinosaur() {
		WordNet wn = new WordNet("./wordnet/synsets.txt", "./wordnet/hyponyms.txt");

        for (String h : wn.hyponyms("dinosaur")) {
            System.out.println(h);
        }
	}

	public static void main(String... args) {
        jh61b.junit.textui.runClasses(WordNetTest.class);
    } 
}

