import java.util.Scanner;
import java.util.HashMap;

public class AlphabetSort {
    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);

        String alphabet = stdin.nextLine();
        HashMap<Character, Integer> alphabetMap = 

        Trie tree = new Trie();
        while (stdin.hasNextLine()) {
            String input = stdin.nextLine();
            tree.insert(input);
            System.out.println(input);
        }
    }
}