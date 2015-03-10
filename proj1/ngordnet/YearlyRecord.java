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
    private TreeMap<String, Integer> recordMap;
    private List<String> cachedWords;
    private List<Number> cachedCount;
    private boolean cacheWordsUpdated;
    private boolean cacheCountUpdated;
    private int numEntries;

    /** Creates a new empty YearlyRecord. */
    public YearlyRecord() {
        recordMap = new TreeMap<String, Integer>();
        numEntries = 0;
        cacheWordsUpdated = false;
        cacheCountUpdated = false;
        cachedWords = new ArrayList<String>();
        cachedCount = new ArrayList<Number>();
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
        if (recordMap.containsKey(word)) {
            return recordMap.get(word);
        }
        return 0;
    }

    /** Records that WORD occurred COUNT times in this year. */
    public void put(String word, int count) {
        if (!recordMap.containsKey(word)) {
            numEntries += 1;
        }

        recordMap.put(word, count);
        cachedWords.add(word);
        cachedCount.add(count);
        cacheWordsUpdated = false;
        cacheCountUpdated = false;
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

        Comparator<String> stringComparator = new Comparator<String>() {
            public int compare(String string1, String string2) {
                double myc1 = recordMap.get(string1).doubleValue();
                double myc2 = recordMap.get(string2).doubleValue();
                return (int) (myc1 - myc2);
            }
        };

        cachedWords = new ArrayList(recordMap.keySet());
        Collections.sort(cachedWords, stringComparator);
        cacheWordsUpdated = true;
        return cachedWords;
    }

    /** Returns all counts in ascending order of count. */
    public Collection<Number> counts() {
        if (cacheCountUpdated) {
            return cachedCount;
        }

        Comparator<Number> countComparator = new Comparator<Number>() {
            public int compare(Number count1, Number count2) {
                return (int) (count1.doubleValue() - count2.doubleValue());
            }
        };

        cachedCount = new ArrayList(recordMap.values());
        Collections.sort(cachedCount, countComparator);
        cacheCountUpdated = true;
        return cachedCount;
    }

    /** Returns rank of WORD. Most common word is rank 1. 
      * If two words have the same rank, break ties arbitrarily. 
      * No two words should have the same rank.
      */
    public int rank(String word) {
        counts();
        words();

        return numEntries - cachedWords.indexOf(word);    
    }
}
