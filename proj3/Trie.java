/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author Andre Askarinam
 */
public class Trie {
    private Node starter = new Node();
    private int size = 0;

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
                pointer.addChild(new Node(value, i == stringSize - 1));
            }
            pointer = pointer.getChild(value);
        }
        size++;
    }
}
