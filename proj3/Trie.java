import java.util.TreeMap;

/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author Andre Askarinam
 */
public class Trie {
    private Node root;
    private int size;
    private TreeMap<Character, Integer> alphabetMap;

    /**
     * Initializes required data structures from parallel arrays.
     */
    public Trie() {
        root = new Node();
        size = 0;
        alphabetMap = null;
    }

    /**
     * Initializes required data structures from parallel arrays.
     * @param alph TreeMap of of Characters and their Integer weight.
     */
    public Trie(TreeMap<Character, Integer> alph) {
        root = new Node(alph);
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
        Node pointer = root;
        for (int i = 0; i < s.length(); i++) {
            char letter = s.charAt(i);
            pointer = pointer.getChild(letter);
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
        Node pointer = root;
        for (int i = 0; i < stringSize; i++) {
            char letter = s.charAt(i);
            Node next = pointer.getChild(letter);
            if (next == null) {
                pointer.addChild(new Node(letter, i == stringSize - 1, alphabetMap));
            }
            pointer = pointer.getChild(letter);
        }
        pointer.setAsTerm();
        size += 1;
    }

    /**
     * Returns the starting root node of the Trie.
     * @return root Node.
     */
    public Node rootNode() {
        return root;
    }
}
