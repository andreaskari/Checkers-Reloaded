import java.util.HashMap;

/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author Andre Askarinam
 */
public class Trie {
    private static final int INITIAL_CAPACITY = 128;

    private Node starter = new Node();

    public boolean find(String s, boolean isFullWord) {
        Node pointer = starter;
        for (int i = 0; i < s.length(); i++) {
            char value = s.charAt(i);
            pointer = pointer.getChild(value);
            if (pointer == null) {
                return false;
            }
        }
        return pointer.isTerm() == isFullWord;
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
                pointer.addChild(new Node(value, i == stringSize - 1));
            }
            pointer = pointer.getChild(value);
        }
    }

    private class Node {
        private char value;
        private boolean isTerm;
        private HashMap<Character, Node> children;

        public Node() {
            value = ' ';
            isTerm = false;
            children = new HashMap<Character, Node>(INITIAL_CAPACITY);
        }

        public Node(char v, boolean it) {
            value = v;
            isTerm = it;
            children = new HashMap<Character, Node>(INITIAL_CAPACITY);
        }

        public char value() {
            return value;
        }

        public boolean isTerm() {
            return isTerm;
        }

        public void setAsTerm() {
            isTerm = true;
        }

        public void addChild(Node child) {
            children.put((Character) child.value(), child);
        }

        public Node getChild(char value) {
            return children.get((Character) value);
        }
    }
}
