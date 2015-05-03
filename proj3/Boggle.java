import java.util.Random;
import java.util.Scanner;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.PriorityQueue;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Implementation of the sexiest game of the century, Boggle.
 * @author Andre Askarinam
 */
public class Boggle {
    private static String DICT_PATH = "/usr/share/dict/words";

    private static class Letter implements Comparable<Letter> {
        static int numCreated = 0;

        char l;
        int x, y, id;
        TreeSet<Letter> adjacents;

        public Letter(char let, int xC, int yC) {
            l = let;
            x = xC;
            y = yC;
            id = numCreated;
            numCreated += 1;
            adjacents = new TreeSet<Letter>();

        }

        @Override
        public int compareTo(Letter other) {
            return id - other.id;
        }
    }

    private static class WordComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            if (o2.length() == o1.length()) {
                return o1.compareTo(o2);
            }
            return (int) (o2.length() - o1.length());
        }
    }

    /**
     * Takes in Boggle board from stdin, oragnizes, prunes through solutions
     * spits words out to the terminal.
     * @param args Strings are processed as flags for running Boggle.
     */
    public static void main(String[] args) {
        int numWords = 1;
        int width = 4, height = 4;
        String pathToDict = DICT_PATH;
        String pathToBoard = null;

        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equals("-k")) {
                numWords = Integer.parseInt(args[i + 1]);
                if (numWords <= 0) {
                    throw new IllegalArgumentException();
                }
            } else if (args[i].equals("-n")) {
                width = Integer.parseInt(args[i + 1]);
                if (width <= 0) {
                    throw new IllegalArgumentException();
                }
            } else if (args[i].equals("-m")) {
                height = Integer.parseInt(args[i + 1]);
                if (height <= 0) {
                    throw new IllegalArgumentException();
                }
            } else if (args[i].equals("-d")) {
                pathToDict = args[i + 1];
            } else if (args[i].equals("-r")) {
                pathToBoard = args[i + 1];
            }
        }

        File dictFile = new File(pathToDict);

        TSTree tst = new TSTree();
        try {
            Scanner dictIn = new Scanner(dictFile);
            while (dictIn.hasNextLine()) {
                String word = dictIn.nextLine();
                tst.insert(word, (double) word.length()); 
            }
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException();
        }

        int bytesAvailable = 0;
        try {
            bytesAvailable = System.in.available();
        } catch (IOException ex) {

        }

        String board = null;
        Scanner stdin = null;
        if (pathToBoard != null || bytesAvailable > 0) {
            if (pathToBoard != null) {
                try {
                    stdin = new Scanner(new File(pathToBoard));
                } catch (FileNotFoundException ex) {
                    throw new IllegalArgumentException();
                }
            } else {
                stdin = new Scanner(System.in);
            }
            board = stdin.nextLine();
            width = board.length();
            height = 1;

            while (stdin.hasNextLine()) {
                String newLine = stdin.nextLine();
                if (newLine.length() != width) {
                    throw new IllegalArgumentException();
                }
                board += newLine;
                height += 1;
            }
        } else {
            board = "";
            for (int i = 0; i < width * height; i++) {
                Random r = new Random();
                board += (char)(r.nextInt(26) + 'a');
            }
        }

        Letter[][] matrix = new Letter[width][height];
        for (int i = 0; i < board.length(); i++) {
            char l = board.charAt(i);
            int x = i % width;
            int y = i / width;
            matrix[x][y] = new Letter(l, x, y);
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x - 1 >= 0) {
                    if (y - 1 >= 0) {
                        matrix[x][y].adjacents.add(matrix[x - 1][y - 1]);
                    }
                    if (y + 1 < height) {
                        matrix[x][y].adjacents.add(matrix[x - 1][y + 1]);
                    }
                    matrix[x][y].adjacents.add(matrix[x - 1][y]);
                }
                if (x + 1 < width) {
                    if (y - 1 >= 0) {
                        matrix[x][y].adjacents.add(matrix[x + 1][y - 1]);
                    }
                    if (y + 1 < height) {
                        matrix[x][y].adjacents.add(matrix[x + 1][y + 1]);
                    }
                    matrix[x][y].adjacents.add(matrix[x + 1][y]);
                }
                if (y - 1 >= 0) {
                    matrix[x][y].adjacents.add(matrix[x][y - 1]);
                }
                if (y + 1 < height) {
                    matrix[x][y].adjacents.add(matrix[x][y + 1]);
                }
            }
        }

        PriorityQueue<String> words = new PriorityQueue<String>(new WordComparator());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Letter visiting = matrix[x][y];
                adjacentStrings(visiting, words, tst);
            }
        }

        for (int i = 0; i < numWords && !words.isEmpty(); i++) {
            System.out.println(words.poll());
        }
    }

    private static void adjacentStrings(Letter visiting, PriorityQueue<String> words, TSTree tst) {
        TSTNode pointer = tst.getNodeOfChar(tst.root(), visiting.l);
        if (pointer != null) {
            String potentialWord = "" + visiting.l;
            if (pointer.value() != null && !words.contains(potentialWord)) {
                words.add(potentialWord);
            }
            for (Letter toVisit: visiting.adjacents) {
                TreeSet<Letter> visited = new TreeSet<Letter>();
                visited.add(visiting);
                if (!visited.contains(toVisit)) {
                    visited.add(toVisit);
                    adjacentStringsHelper("" + visiting.l, tst, pointer, toVisit, visited, words);
                    visited.remove(toVisit);
                }
            }
        }
    }

    private static void adjacentStringsHelper(String prefix, TSTree tst, TSTNode pointer, 
        Letter visiting, TreeSet<Letter> visited, PriorityQueue<String> words) {

        if (pointer.middle() != null) {
            pointer = tst.getNodeOfChar(pointer.middle(), visiting.l);
            if (pointer != null) {
                String potentialWord = prefix + visiting.l;
                if (pointer.value() != null && !words.contains(potentialWord)) {
                    words.add(potentialWord);
                }
                for (Letter toVisit: visiting.adjacents) {
                    if (!visited.contains(toVisit)) {
                        visited.add(toVisit);
                        adjacentStringsHelper(prefix + visiting.l, tst, pointer, toVisit, visited, words);
                        visited.remove(toVisit);
                    }
                }
            }
        }
    }
}
