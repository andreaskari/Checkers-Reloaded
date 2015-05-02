import java.util.Scanner;
import java.io.File;
/**
 * Implementation of the sexiest game of the century, Boggle.
 * @author Andre Askarinam
 */
public class Boggle {
    private static String DICT_PATH = "/usr/share/dict/words";

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

        if (!(new File(pathToDict)).exists()) {
            throw new IllegalArgumentException();
        }

        Scanner stdin = null;
        if (pathToBoard == null) {
            stdin = new Scanner(System.in);
        } else {
            stdin = new Scanner(pathToBoard);
        }
        String board = stdin.nextLine();

        int boardWidth = board.length();
        while (stdin.hasNextLine()) {
            String newLine = stdin.nextLine();
            if (newLine.length() != boardWidth) {
                throw new IllegalArgumentException();
            }
            board += "\n" + newLine;
        }

        System.out.println(board);
    }
}
