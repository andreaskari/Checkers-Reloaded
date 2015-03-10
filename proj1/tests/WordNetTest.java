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
    public void wordNetSum() {
        WordNet wn = new WordNet("./wordnet/synsets14.txt", "./wordnet/hyponyms14.txt");

        int sum = 0;
        System.out.println();
        for (String h : wn.hyponyms("event")) {
            System.out.println(h);
            sum += 1;
        }
        assertEquals(22, sum);

        sum = 0;
        System.out.println();
        for (String h : wn.hyponyms("adjustment")) {
            System.out.println(h);
            sum += 1;
        }
        System.out.println();
        assertEquals(5, sum);
    }

	@Test
	public void wordNetViceroy() {
		WordNet wn = new WordNet("./wordnet/synsets.txt", "./wordnet/hyponyms.txt");

        System.out.println();
        for (String h : wn.hyponyms("viceroy")) {
            System.out.println(h);
        }
        System.out.println();
	}

	@Test
	public void wordNetDinosaur() {
		WordNet wn = new WordNet("./wordnet/synsets.txt", "./wordnet/hyponyms.txt");

        System.out.println();
        for (String h : wn.hyponyms("dinosaur")) {
            System.out.println(h);
        }
        System.out.println();
	}

    @Test
    public void wordNetWhiteMatter() {
        WordNet wn = new WordNet("./wordnet/synsets1000-subgraph.txt", "./wordnet/hyponyms1000-subgraph.txt");

        System.out.println();
        for (String h : wn.hyponyms("white_matter")) {
            System.out.println(h);
        }
        System.out.println();
    }

	public static void main(String... args) {
        jh61b.junit.textui.runClasses(WordNetTest.class);
    } 
}

