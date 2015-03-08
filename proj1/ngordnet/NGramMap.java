package ngordnet;

import edu.princeton.cs.introcs.In;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Collections;

public class NGramMap {
    private TreeMap<Integer, YearlyRecord> wordsCountsRanksPerYear;
    private TreeMap<Integer, YearlyRecord> wordsSourcesRanksPerYear;
    private TimeSeries<Long> totalWordsPerYear;
    private TimeSeries<Integer> totalPagesPerYear;
    private TimeSeries<Integer> totalSourcesPerYear;

    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    public NGramMap(String wordsFilename, String countsFilename) {
        In wordsInput = new In(wordsFilename);
        wordsCountsRanksPerYear = new TreeMap<Integer, YearlyRecord>();
        wordsSourcesRanksPerYear = new TreeMap<Integer, YearlyRecord>();

        while (wordsInput.hasNextLine()) {
            String[] segments =  wordsInput.readLine().split("\\t");

            String word = segments[0];
            Integer year = new Integer(segments[1]);
            Integer count = new Integer(segments[2]);
            Integer sources = new Integer(segments[3]);

            if (wordsCountsRanksPerYear.get(year) == null) {
                wordsCountsRanksPerYear.put(year, new YearlyRecord());
                wordsSourcesRanksPerYear.put(year, new YearlyRecord());
            }

            wordsCountsRanksPerYear.get(year).put(word, count);
            wordsSourcesRanksPerYear.get(year).put(word, sources);
        }

        In countsInput = new In(countsFilename);
        totalWordsPerYear = new TimeSeries<Long>();
        totalPagesPerYear = new TimeSeries<Integer>();
        totalSourcesPerYear = new TimeSeries<Integer>();

        while (countsInput.hasNextLine()) {
            String[] segments =  countsInput.readLine().split(",");

            Integer year = new Integer(segments[0]);
            Long totalCount = new Long(segments[1]);
            Integer totalPages = new Integer(segments[2]);
            Integer totalSources = new Integer(segments[3]);

            totalWordsPerYear.put(year, totalCount);
            totalPagesPerYear.put(year, totalPages);
            totalSourcesPerYear.put(year, totalSources);
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
            wordCount.put(year, countInYear(word, year));
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
        TimeSeries<Long> copyOfTotalWordsPerYear = new TimeSeries<Long>(totalWordsPerYear);
        for (int year = startYear; year <= endYear; year++) {
            if (!copyOfTotalWordsPerYear.years().contains(year)) {
                copyOfTotalWordsPerYear.put(new Integer(year), new Long(0));
            }
        }
        return countHistory(word, startYear, endYear).dividedBy(copyOfTotalWordsPerYear);
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
