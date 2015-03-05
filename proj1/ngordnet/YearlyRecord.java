package ngordnet;

import java.util.*;

public class YearlyRecord {
    private TreeMap<String, CountAndRank> recordMap;
    int numEntries;

    /** Creates a new empty YearlyRecord. */
    public YearlyRecord() {
        recordMap = new TreeMap<String, CountAndRank>();
        numEntries = 0;
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
        return recordMap.get(word).count();
    }

    /** Records that WORD occurred COUNT times in this year. */
    public void put(String word, int count) {
        int rank = 1;
        for (String w: recordMap.keySet()) {
            if (recordMap.get(w).count() > count) {
                rank += 1;
            } else {
                recordMap.get(w).incrementRank();
            }
        }
        recordMap.put(word, new CountAndRank(count, rank));
        numEntries += 1;
    }

    /** Returns the number of words recorded this year. */
    public int size() {
        return numEntries;
    }

    /** Returns all words in ascending order of count. */
    public Collection<String> words() {
        Collection<String> words = new ArrayList<String>();
        for (int rank = numEntries; rank > 0; rank--) {
            words.add(wordForRank(rank));
        }
        return words;
    }

    private String wordForRank(int rank) {
        for (String word: recordMap.keySet()) {
            if (recordMap.get(word).rank() == rank) {
                return word;
            }
        }
        return null;
    }

    /** Returns all counts in ascending order of count. */
    public Collection<Number> counts() {
        Collection<Number> counts = new TreeSet<Number>();
        for (String word: recordMap.keySet()) {
            counts.add(recordMap.get(word).count());
        }
        return counts;
    }

    /** Returns rank of WORD. Most common word is rank 1. 
      * If two words have the same rank, break ties arbitrarily. 
      * No two words should have the same rank.
      */
    public int rank(String word) {
        return recordMap.get(word).rank();
    }

    private class CountAndRank {
        private final int count;
        private int rank;

        public CountAndRank(int c, int r) {
            count = c;
            rank = r;
        }

        public void incrementRank() {
            rank += 1;
        }

        public int count() {
            return count;
        }

        public int rank() {
            return rank;
        }
    }
}