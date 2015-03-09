package ngordnet;

import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.StyleManager.ChartTheme;
import com.xeiam.xchart.ChartBuilder;

/** Utility class for generating plots. */
public class Plotter {
    /** Creates a plot of the TimeSeries TS. Labels the graph with the
      * given TITLE, XLABEL, YLABEL, and LEGEND. */
    public static void plotTS(TimeSeries<? extends Number> ts, String title, 
                              String xlabel, String ylabel, String legend) {
        Collection xValues = ts.years();
        Collection yValues = ts.values();    

        Chart chart = QuickChart.getChart(title, ylabel, xlabel, legend, xValues, yValues);
        new SwingWrapper(chart).displayChart();  
    }

    /** Creates a plot of the absolute word counts for WORD from STARTYEAR
      * to ENDYEAR, using NGM as a data source. */
    public static void plotCountHistory(NGramMap ngm, String word, 
                                      int startYear, int endYear) {
        TimeSeries countHistory = ngm.countHistory(word, startYear, endYear);
        String title = "Count history of " + word + " from " + startYear + " to " + endYear;
        String xlabel = "Year";
        String ylabel = "Count";
        String legend = word;
        plotTS(countHistory, title, xlabel, ylabel, legend);
    }

    /** Creates a plot of the normalized weight counts for WORD from STARTYEAR
      * to ENDYEAR, using NGM as a data source. */
    public static void plotWeightHistory(NGramMap ngm, String word, 
                                       int startYear, int endYear) {
        TimeSeries weightHistory = ngm.weightHistory(word, startYear, endYear);
        String title = "Weight history of " + word + " from " + startYear + " to " + endYear;
        String xlabel = "Year";
        String ylabel = "Weight";
        String legend = word;
        plotTS(weightHistory, title, xlabel, ylabel, legend);
    }

    /** Creates a plot of the processed history from STARTYEAR to ENDYEAR, using
      * NGM as a data source, and the YRP as a yearly record processor. */
    // public static void plotProcessedHistory(NGramMap ngm, int startYear, int endYear,
    //                                         YearlyRecordProcessor yrp) {

    // }

    /** Creates a plot of the total normalized count of WN.hyponyms(CATEGORYLABEL)
      * from STARTYEAR to ENDYEAR using NGM and WN as data sources. */
    public static void plotCategoryWeights(NGramMap ngm, WordNet wn, String categoryLabel,
                                            int startYear, int endYear) {
        Set<String> hyponyms = wn.hyponyms(categoryLabel);
        TimeSeries summedWeightHistory = ngm.summedWeightHistory(hyponyms, startYear, endYear);
        String title = "Summed Weight of " + categoryLabel + " from " + startYear + " to " + endYear;
        String xlabel = "Year";
        String ylabel = "Weight";
        String legend = "Hyponyms of " + categoryLabel;
        plotTS(summedWeightHistory, title, xlabel, ylabel, legend);
    }

    /** Creates overlaid category weight plots for each category label in CATEGORYLABELS
      * from STARTYEAR to ENDYEAR using NGM and WN as data sources. */
    public static void plotCategoryWeights(NGramMap ngm, WordNet wn, String[] categoryLabels,
                                            int startYear, int endYear) {

        Chart chart = new ChartBuilder().width(800).height(600).xAxisTitle("years").yAxisTitle("data").build();

        for (String categoryLabel : categoryLabels) {
            Set words = wn.hyponyms(categoryLabel);        
            TimeSeries bundle = ngm.summedWeightHistory(words, startYear, endYear);
            chart.addSeries(categoryLabel, bundle.years(), bundle.data());
        }
        new SwingWrapper(chart).displayChart();
    }

    /** Makes a plot showing overlaid individual normalized count for every word in WORDS
      * from STARTYEAR to ENDYEAR using NGM as a data source. */
    public static void plotAllWords(NGramMap ngm, String[] words, int startYear, int endYear) {
        Chart chart = new ChartBuilder().width(800).height(600).xAxisTitle("years").yAxisTitle("data").build();

        for (String word : words) {
            TimeSeries bundle = ngm.weightHistory(word, startYear, endYear);
            chart.addSeries(word, bundle.years(), bundle.data());
        }
        new SwingWrapper(chart).displayChart();
    } 

    /** Returns the numbers from max to 1, inclusive in decreasing order. 
      * Private, so you don't have to implement if you don't want to. */
    // private static Collection<Number> downRange(int max) {

    // }

    /** Plots the count (or weight) of every word against the rank of every word on a
      * log-log plot. Uses data from YEAR, using NGM as a data source. */
    // public static void plotZipfsLaw(NGramMap ngm, int year) {

    // }
} 