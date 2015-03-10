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
                           + wordFile + ", " + countFile + ", " + synsetFile +
                           ", and " + hyponymFile + ".");

        String helpPrompt = "Acceptible Commands:";
        helpPrompt += "\nquit:                Program exits";
        helpPrompt += "\nhelp:                Provides a list of acceptible commands.";
        helpPrompt += "\nrange [start] [end]: resets the start and end years.";
        helpPrompt += "\ncount [word] [year]: print the count of [word] in the given year";
        helpPrompt += "\nhyponyms [word]:     prints all hyponyms of [word].";
        helpPrompt += "\nhistory [words...]:  plots rel. frequency of [words] from start to end.";
        helpPrompt += "\nhypohist [words...]: plots rel. frequency of hyponyms of [words] from start to end.";

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
                    Plotter.plotAllWords(ngMap, arguments, startYear, endYear);
                } else {
                    Plotter.plotWeightHistory(ngMap, arguments[0], startYear, endYear);
                }
            } else if (command.equals("hypohist")) {
                if (arguments.length > 1) {
                    Plotter.plotCategoryWeights(ngMap, wNet, arguments, startYear, endYear);
                } else {
                    Plotter.plotCategoryWeights(ngMap, wNet, arguments[0], startYear, endYear);
                }
            } else {
                System.out.println("Invalid command.");  
            }
        }
    }
} 
