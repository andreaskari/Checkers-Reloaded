import java.util.Scanner;

/**
 * Implementation of the sexiest game of the century, Boggle.
 * @author Andre Askarinam
 */
public class Boggle {
    /**
     * Takes in Boggle board from stdin, oragnizes, prunes through solutions
     * spits words out to the terminal.
     * @param args Strings are processed as flags for running Boggle.
     */
    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);

        if (!stdin.hasNextLine()) {
            throw new IllegalArgumentException();
        }

    }
}
