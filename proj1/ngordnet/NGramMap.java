package ngordnet;

import edu.princeton.cs.introcs.In;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Collections;

public class NGramMap {
    private TreeMap<Integer, YearlyRecord> wordsCountsRanksPerYear;
    private TimeSeries<Long> totalWordsPerYear;

    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    public NGramMap(String wordsFilename, String countsFilename) {
        In wordsInput = new In(wordsFilename);
        wordsCountsRanksPerYear = new TreeMap<Integer, YearlyRecord>();

        String[] segments;
        String word;
        Integer year;
        Integer count;

        while (wordsInput.hasNextLine()) {
            segments =  wordsInput.readLine().split("\\t");

            word = segments[0];
            year = new Integer(segments[1]);
            count = new Integer(segments[2]);

            if (wordsCountsRanksPerYear.get(year) == null) {
                wordsCountsRanksPerYear.put(year, new YearlyRecord());
            }

            wordsCountsRanksPerYear.get(year).put(word, count);
        }

        In countsInput = new In(countsFilename);
        totalWordsPerYear = new TimeSeries<Long>();

        Long totalCount;

        while (countsInput.hasNextLine()) {
            segments =  countsInput.readLine().split(",");

            year = new Integer(segments[0]);
            totalCount = new Long(segments[1]);

            totalWordsPerYear.put(year, totalCount);
        }
    }
    
    /** Returns the absolute count of WORD in the given YEAR. If the word
      * did not appear in the given year, return 0. */
    public int countInYear(String word, int year) {
        if (wordsCountsRanksPerYear.get(year) != null && 
            wordsCountsRanksPerYear.get(year).words().contains(word)) {
            return wordsCountsRanksPerYear.get(year).count(word);
        }
        return 0;
    }

    /** Returns a defensive copy of the YearlyRecord of WORD. */
    public YearlyRecord getRecord(int year) {
        YearlyRecord copy = new YearlyRecord();
        YearlyRecord original = wordsCountsRanksPerYear.get(year);
        for (String word: original.words()) {
            copy.put(word, original.count(word));
        }
        return copy;
    }

    /** Returns the total number of words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory() {
        return new TimeSeries<Long>(totalWordsPerYear);
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear) {
        TimeSeries<Integer> wordCount = new TimeSeries<Integer>();
        for (int year = startYear; year <= endYear; year++) {
            int count = countInYear(word, year);
            if (count != 0) {
                wordCount.put(year, count);
            }
        }
        return wordCount;
    }

    /** Provides a defensive copy of the history of WORD. */
    public TimeSeries<Integer> countHistory(String word) {
        int startYear = Collections.min(wordsCountsRanksPerYear.keySet());
        int endYear = Collections.max(wordsCountsRanksPerYear.keySet());
        return countHistory(word, startYear, endYear);
    }

    /** Provides the relative frequency of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear) {
        TimeSeries<Integer> countHistory = countHistory(word, startYear, endYear);
        TimeSeries<Long> copyOfTotalWordsPerYear = new TimeSeries<Long>();
        for (int year = startYear; year <= endYear; year++) {
            if (countHistory.years().contains(year)) {
                copyOfTotalWordsPerYear.put(year, totalWordsPerYear.get(year));
            }
        }
        return countHistory.dividedBy(copyOfTotalWordsPerYear);
    }

    /** Provides the relative frequency of WORD. */
    public TimeSeries<Double> weightHistory(String word) {
        int startYear = Collections.min(wordsCountsRanksPerYear.keySet());
        int endYear = Collections.max(wordsCountsRanksPerYear.keySet());
        return weightHistory(word, startYear, endYear);
    }

    /** Provides the summed relative frequency of all WORDS between
      * STARTYEAR and ENDYEAR. If a word does not exist, ignore it rather
      * than throwing an exception. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words, 
                              int startYear, int endYear) {
        TimeSeries<Double> totalSum = new TimeSeries<Double>();
        for (String word: words) {
            totalSum = totalSum.plus(weightHistory(word, startYear, endYear));
        }
        return totalSum;
    }

    /** Returns the summed relative frequency of all WORDS. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words) {
        int startYear = Collections.min(wordsCountsRanksPerYear.keySet());
        int endYear = Collections.max(wordsCountsRanksPerYear.keySet());
        return summedWeightHistory(words, startYear, endYear);
    }

    /** Provides processed history of all words between STARTYEAR and ENDYEAR as processed
      * by YRP. */
    // public TimeSeries<Double> processedHistory(int startYear, int endYear,
    //                                            YearlyRecordProcessor yrp) {

    // }

    // /** Provides processed history of all words ever as processed by YRP. */
    // public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp) {

    // }
}
