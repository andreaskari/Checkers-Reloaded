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

        final int minYear = Collections.min(ngMap.totalCountHistory().keySet());
        final int maxYear = Collections.max(ngMap.totalCountHistory().keySet());

        int startYear = minYear;
        int endYear = maxYear;

        while (true) {
            System.out.print("> ");
            String line = StdIn.readLine();
            String[] rawTokens = line.split(" ");
            String command = rawTokens[0];
            String[] arguments = new String[rawTokens.length - 1];
            System.arraycopy(rawTokens, 1, arguments, 0, rawTokens.length - 1);

            if (commandErrorHandling(command, arguments)) {
                
            } else if (command.equals("quit")) {
                break;
            } else if (command.equals("help")) {
                System.out.println(helpPrompt);
            } else if (command.equals("range")) {
                startYear = Integer.parseInt(arguments[0]);
                endYear = Integer.parseInt(arguments[1]);
            } else if (command.equals("count")) {
                String word = arguments[0];
                int year = Integer.parseInt(arguments[1]);
                System.out.println(ngMap.countInYear(word, year));
            } else if (command.equals("hyponyms")) {
                String word = arguments[0];
                System.out.println(wNet.hyponyms(word));
            } else if (command.equals("history")) {
                if (arguments.length > 1) {
                    String[] words = wordsAreInNGMap(ngMap, arguments);
                    if (words.length > 0) {
                        Plotter.plotAllWords(ngMap, words, startYear, endYear);
                    }
                } else if (wordIsInNGMap(ngMap, arguments[0])) {
                    Plotter.plotWeightHistory(ngMap, arguments[0], startYear, endYear);
                }
            } else if (command.equals("hypohist")) {
                if (arguments.length > 1) {
                    String[] words = wordsAreInNGMap(ngMap, arguments);
                    if (words.length > 0) {
                        Plotter.plotCategoryWeights(ngMap, wNet, words, startYear, endYear);
                    }
                } else if (wordIsInNGMap(ngMap, arguments[0])) {
                    Plotter.plotCategoryWeights(ngMap, wNet, arguments[0], startYear, endYear);
                }
            } else if (command.equals("wordlength")) {
                Plotter.plotProcessedHistory(ngMap, startYear, endYear, new WordLengthProcessor());
            } else if (command.equals("zipf")) {
                int year = Integer.parseInt(arguments[0]);
                if (minYear <= year && year <= maxYear) {
                    Plotter.plotZipfsLaw(ngMap, year);
                } else {
                    System.out.println(year + " is not in stored data.");
                }
            } else {
                System.out.println("Invalid command.");  
            }
        }
    }

    private static String helpString() {
        String help = "Acceptible Commands:";
        help += "\nquit:                Program exits";
        help += "\nhelp:                Provides a list of acceptible commands.";
        help += "\nrange [start] [end]: resets the start and end years.";
        help += "\ncount [word] [year]: print the count of [word] in the given year";
        help += "\nhyponyms [word]:     prints all hyponyms of [word].";
        help += "\nhistory [words...]:  plots rel. frequency of [words] from start to end.";
        help += "\nhypohist [words...]: plots rel. frequency of hyponyms of [words] from start ";
        help += "to end.";
        help += "\nwordlength:          plots the length of the average word from start to end.";
        help += "\nzipf year:           plots the count of every word vs. its rank on a log log"; 
        help += "plot.";
        return help;
    }

    private static boolean isInteger(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        for (int x = 0; x < s.length(); x++) {
            char c = s.charAt(x);
            if ((c < '0') || (c > '9')) {
                return false;
            }
        }
        return true;
    }

    private static boolean wordIsInNGMap(NGramMap ngm, String word) {
        if (ngm.countHistory(word).years().isEmpty()) {
            System.out.println(word + " is not in stored data.");
            return false;
        }
        return true;
    }

    private static String[] wordsAreInNGMap(NGramMap ngm, String[] words) {
        String[] availableWords = new String[words.length];
        int numAvailable = 0;
        for (String word: words) {
            if (wordIsInNGMap(ngm, word)) {
                availableWords[numAvailable] = word;
                numAvailable += 1;
            }
        }
        String[] correctSize = new String[numAvailable];
        System.arraycopy(availableWords, 0, correctSize, 0, numAvailable);
        return correctSize;
    }

    private static boolean commandErrorHandling(String command, String[] arguments) {
        if (command.equals("range")) {
            if (arguments.length < 2 || !isInteger(arguments[0]) || !isInteger(arguments[1])) {
                System.out.println("Invalid 'range' command");
                return true;
            }
        } else if (command.equals("count")) {
            if (arguments.length < 2 || !isInteger(arguments[1])) {
                System.out.println("Invalid 'count' command");
                return true;
            }
        } else if (command.equals("hyponyms")) {
            if (arguments.length < 1) {
                System.out.println("Invalid 'hyponyms' command");
                return true;
            }
        } else if (command.equals("history")) {
            if (arguments.length < 1) {
                System.out.println("Invalid 'history' command");
                return true;
            }
        } else if (command.equals("hypohist")) {
            if (arguments.length < 1) {
                System.out.println("Invalid 'hypohist' command");
                return true;
            }
        } else if (command.equals("zipf")) {
            if (arguments.length < 1 || !isInteger(arguments[0])) {
                System.out.println("Invalid 'zipf' command");
                return true;
            }
        }
        return false;
    }
} 
