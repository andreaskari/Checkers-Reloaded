package ngordnet;

import java.util.TreeMap;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class YearlyRecord {
    private TreeMap<String, Integer> countMap;
    private TreeMap<String, Integer> rankMap;
    private Comparator<String> stringComparator;
    private Comparator<Number> countComparator;
    private List<String> cachedWords;
    private List<Number> cachedCount;
    private boolean cacheWordsUpdated;
    private boolean cacheCountUpdated;
    private boolean rankMapUpdated;
    private int numEntries;


    /** Creates a new empty YearlyRecord. */
    public YearlyRecord() {
        countMap = new TreeMap<String, Integer>();
        rankMap = new TreeMap<String, Integer>();
        numEntries = 0;
        cacheWordsUpdated = false;
        cacheCountUpdated = false;
        rankMapUpdated = false;
        cachedWords = new ArrayList<String>();
        cachedCount = new ArrayList<Number>();

        stringComparator = new Comparator<String>() {
            public int compare(String string1, String string2) {
                double myc1 = countMap.get(string1).doubleValue();
                double myc2 = countMap.get(string2).doubleValue();
                return (int) (myc1 - myc2);
            }
        };

        countComparator = new Comparator<Number>() {
            public int compare(Number count1, Number count2) {
                return (int) (count1.doubleValue() - count2.doubleValue());
            }
        };
    }

    /** Creates a YearlyRecord using the given data. */
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {
        this();
        Iterator<String> words = otherCountMap.keySet().iterator();
        Iterator<Integer> counts = otherCountMap.values().iterator();
        while (words.hasNext() && counts.hasNext()) {
            put(words.next(), counts.next());
        }
    }

    /** Returns the number of times WORD appeared in this year. */
    public int count(String word) {
        if (countMap.containsKey(word)) {
            return countMap.get(word);
        }
        return 0;
    }

    /** Records that WORD occurred COUNT times in this year. */
    public void put(String word, int count) {
        if (!countMap.containsKey(word)) {
            numEntries += 1;
        }

        countMap.put(word, count);

        cachedWords.add(word);
        cachedCount.add(count);
        cacheWordsUpdated = false;
        cacheCountUpdated = false;
        rankMapUpdated = false;
    }

    /** Returns the number of words recorded this year. */
    public int size() {
        return numEntries;
    }

    /** Returns all words in ascending order of count. */
    public Collection<String> words() {
        if (cacheWordsUpdated) {
            return cachedWords;
        }

        Collections.sort(cachedWords, stringComparator);
        cacheWordsUpdated = true;
        return cachedWords;
    }

    /** Returns all counts in ascending order of count. */
    public Collection<Number> counts() {
        if (cacheCountUpdated) {
            return cachedCount;
        }

        Collections.sort(cachedCount, countComparator);
        cacheCountUpdated = true;
        return cachedCount;
    }

    public void updateRankMap() {
        if (rankMapUpdated) {
            return;
        }
        counts();
        words();

        rankMap = new TreeMap<String, Integer>();

        int rank = 1;
        for (String word: cachedWords) {
            rankMap.put(word, rank);
            rank += 1;
        }
        
        rankMapUpdated = true;
    }

    /** Returns rank of WORD. Most common word is rank 1. 
      * If two words have the same rank, break ties arbitrarily. 
      * No two words should have the same rank.
      */
    public int rank(String word) {
        updateRankMap();
        return numEntries - rankMap.get(word).intValue() + 1;    
    }
}
