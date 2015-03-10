package ngordnet;

import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;
import java.util.Collections;

/** Provides a simple user interface for exploring WordNet and NGram data.
 *  @author Andre Askarinam
 */
public class NgordnetUI {
    public static void main(String[] args) {
        In in = new In("./ngordnet/ngordnetui.config");
        System.out.println("Reading ngordnetui.config...");

        String wordFile = in.readString();
        String countFile = in.readString();
        String synsetFile = in.readString();
        String hyponymFile = in.readString();
        System.out.println("\nBased on ngordnetui.config, using the following: "
                           + wordFile + ", " + countFile + ", " + synsetFile 
                           + ", and " + hyponymFile + ".");

        String helpPrompt = helpString();

        /* Command Line Attributes */
        System.out.println("Reading " + wordFile + " and " + countFile);
        NGramMap ngMap = new NGramMap(wordFile, countFile);
        System.out.println("Reading " + synsetFile + " and " + hyponymFile);
        WordNet wNet = new WordNet(synsetFile, hyponymFile);

        int startYear = Collections.min(ngMap.totalCountHistory().keySet());
        int endYear = Collections.max(ngMap.totalCountHistory().keySet());

        while (true) {
            System.out.print("> ");
            String line = StdIn.readLine();
            String[] rawTokens = line.split(" ");
            String command = rawTokens[0];
            String[] arguments = new String[rawTokens.length - 1];
            System.arraycopy(rawTokens, 1, arguments, 0, rawTokens.length - 1);

            if (command.equals("quit")) {
                break;
            } else if (command.equals("help")) {
                System.out.println(helpPrompt);
            } else if (command.equals("range")) {
                try {
                    startYear = Integer.parseInt(arguments[0]);
                    endYear = Integer.parseInt(arguments[1]);
                } catch (Exception e) {
                    System.out.println("Invalid 'range' command.");  
                }
            } else if (command.equals("count")) {
                try {
                    String word = arguments[0];
                    int year = Integer.parseInt(arguments[1]);
                    System.out.println(ngMap.countInYear(word, year));
                } catch (Exception e) {
                    System.out.println("Invalid 'count' command.");  
                }
            } else if (command.equals("hyponyms")) {
                try {
                    String word = arguments[0];
                    System.out.println(wNet.hyponyms(word));
                } catch (Exception e) {
                    System.out.println("Invalid 'hyponyms' command.");  
                }
            } else if (command.equals("history")) {
                try {
                    if (arguments.length > 1) {
                        Plotter.plotAllWords(ngMap, arguments, startYear, endYear);
                    } else {
                        Plotter.plotWeightHistory(ngMap, arguments[0], startYear, endYear);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid 'history' command.");  
                }
            } else if (command.equals("hypohist")) {
                try {
                    if (arguments.length > 1) {
                        Plotter.plotCategoryWeights(ngMap, wNet, arguments, startYear, endYear);
                    } else {
                        Plotter.plotCategoryWeights(ngMap, wNet, arguments[0], startYear, endYear);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid 'hypohist' command.");  
                }
            } else if (command.equals("wordlength")) {
                Plotter.plotProcessedHistory(ngMap, startYear, endYear, new WordLengthProcessor());
            } else if (command.equals("zipf")) {
                try {
                    int year = Integer.parseInt(arguments[0]);
                    Plotter.plotZipfsLaw(ngMap, year);
                } catch (Exception e) {
                    System.out.println("Invalid 'zipf' command.");  
                }
            } else {
                System.out.println("Invalid command.");  
            }
        }
    }

    public static String helpString() {
        String help = "Acceptible Commands:";
        help += "\nquit:                Program exits";
        help += "\nhelp:                Provides a list of acceptible commands.";
        help += "\nrange [start] [end]: resets the start and end years.";
        help += "\ncount [word] [year]: print the count of [word] in the given year";
        help += "\nhyponyms [word]:     prints all hyponyms of [word].";
        help += "\nhistory [words...]:  plots rel. frequency of [words] from start to end.";
        help += "\nhypohist [words...]: plots rel. frequency of hyponyms of [words] ";
        help += "from start to end.";
        help += "\nwordlength:          plots the length of the average word from start to end.";
        help += "\nzipf year:           plots the count of every word vs. its rank on a log log plot.";
        return help;
    }
} 
