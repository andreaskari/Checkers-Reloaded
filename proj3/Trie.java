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

    /**
     * Initializes required data structures from parallel arrays.
     */
    public Trie() {
        starter = new Node();
        size = 0;
        alphabetMap = null;
    }

    /**
     * Initializes required data structures from parallel arrays.
     * @param alph HashMap of of Characters and their Integer weight.
     */
    public Trie(HashMap<Character, Integer> alph) {
        starter = new Node(alph);
        size = 0;
        alphabetMap = alph;
    }

    /**
     * Checks necessary nodes linearly to see if word s exists.
     * @param s String that we are looking for.
     * @param isFullWord boolean that denotes whether s is a complete word.
     * @return boolean whether the word is there and is a full word.
     */
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

    /**
     * Inserts s into nodes of the Trie.
     * @param s String that we inserting.
     */
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

    /**
     * Returns the starting root node of the Trie.
     * @return root Node.
     */
    public Node rootNode() {
        return starter;
    }
}
