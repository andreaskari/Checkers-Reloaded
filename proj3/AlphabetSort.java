import java.util.Scanner;
import java.util.HashMap;
import java.util.PriorityQueue;

public class AlphabetSort {
    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);

        if (!stdin.hasNextLine()) {
            throw new IllegalArgumentException();
        }

        String alphabet = stdin.nextLine();
        HashMap<Character, Integer> alphabetMap = new HashMap<Character, Integer>(alphabet.length());
        for (int i = 0; i < alphabet.length(); i++) {
            Character letter = (Character) alphabet.charAt(i);
            if (alphabetMap.containsKey(letter)) {
                throw new IllegalArgumentException();
            }
            alphabetMap.put(letter, (Integer) i);
        }

        Trie tree = new Trie(alphabetMap);
        while (stdin.hasNextLine()) {
            tree.insert(stdin.nextLine());
        }

        printAllWordsAlphabetically(tree.rootNode(), "");
    }

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
