import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeMap;
import java.util.PriorityQueue;

/**
 * AlphabetSort takes in an alphabet and words from stdin
 * prints them out to stdout in linear time with respect to
 * max word length and number of words inputted.
 * @author Andre Askarinam
 */
public class AlphabetSort {
    /**
     * Takes in data from stdin, inputs words into Trie if appropriate, 
     * manually goes through Trie's nodes and prints words in sorted order.
     * @param args unused.
     */
    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);

        if (!stdin.hasNextLine()) {
            throw new IllegalArgumentException();
        }

        String alphabet = stdin.nextLine();
        TreeMap<Character, Integer> alphabetMap = 
            new TreeMap<Character, Integer>();

        for (int i = 0; i < alphabet.length(); i++) {
            Character letter = (Character) alphabet.charAt(i);
            if (alphabetMap.containsKey(letter)) {
                throw new IllegalArgumentException();
            }
            alphabetMap.put(letter, (Integer) i);
        }

        if (!stdin.hasNextLine()) {
            throw new IllegalArgumentException();
        }
        
        Trie tree = new Trie(alphabetMap);
        while (stdin.hasNextLine()) {
            String word = stdin.nextLine();
            boolean containsKeysInAlphabet = true;
            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                if (!alphabetMap.containsKey((Character) letter)) {
                    containsKeysInAlphabet = false;
                    break;
                }
            }
            if (containsKeysInAlphabet) {
                tree.insert(word);
            }
        }

        printAllWordsAlphabetically(tree.rootNode(), "");
    }

    /** 
     * Manually goes through Trie's nodes and prints words in sorted order.
     * @param pointer Node is checked and then recursively has children checked.
     * @param incompleteWord String is a helper parameter that stores the word 
     * compiled while traversing through the nodes.
     */
    private static void printAllWordsAlphabetically(Node pointer, String incompleteWord) {
        if (pointer.isTerm()) {
            System.out.println(incompleteWord);
        }
        if (pointer.hasChildren()) {
            PriorityQueue<Character> queue = pointer.childrenToIterateOver();
            while (queue.size() > 0) {
                Character ch = queue.poll();
                printAllWordsAlphabetically(pointer.getChild(ch), incompleteWord + ch);
            }
        }
    }
}
