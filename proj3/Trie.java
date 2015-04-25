import java.util.HashMap;

/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author Andre Askarinam
 */
public class Trie {
    private Node starter;
    private int size;
    private HashMap<Character, Integer> alphabetMap;

    public Trie() {
        starter = new Node();
        size = 0;
        alphabetMap = null;
    }

    public Trie(HashMap<Character, Integer> alph) {
        starter = new Node(alph);
        size = 0;
        alphabetMap = alph;
    }

    public boolean find(String s, boolean isFullWord) {
        Node pointer = starter;
        for (int i = 0; i < s.length(); i++) {
            char value = s.charAt(i);
            pointer = pointer.getChild(value);
            if (pointer == null) {
                return false;
            }
        }
        return pointer.isTerm() == isFullWord || pointer.isTerm();
    }

    public void insert(String s) {
        if (s == null || s.length() == 0) {
            throw new IllegalArgumentException();
        }

        int stringSize = s.length();
        Node pointer = starter;
        for (int i = 0; i < stringSize; i++) {
            char value = s.charAt(i);
            Node next = pointer.getChild(value);
            if (next == null) {
                pointer.addChild(new Node(value, i == stringSize - 1, alphabetMap));
            }
            pointer = pointer.getChild(value);
        }
        size++;
    }

    public Node rootNode() {
        return starter;
    }
}
