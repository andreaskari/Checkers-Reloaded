import java.util.HashMap;
import java.util.PriorityQueue;

public class Node {
    private static final int ROOT_NODE_CAPACITY = 256;
    private static final int INITIAL_CAPACITY = 128;

    private char value;
    private boolean isTerm;
    private HashMap<Character, Node> children;
    private PriorityQueue<Character> keySet;

    public Node() {
        value = ' ';
        isTerm = false;
        children = new HashMap<Character, Node>(ROOT_NODE_CAPACITY);
    }

    public Node(HashMap<Character, Integer> alphabetMap) {
        this();
        if (alphabetMap == null) {
            keySet = null;
        } else {
            keySet = new PriorityQueue<Character>(INITIAL_CAPACITY, new AlphabetComparator(alphabetMap));
        }
    }

    public Node(char v, boolean it, HashMap<Character, Integer> alphabetMap) {
        this(alphabetMap);
        value = v;
        isTerm = it;
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
        if (keySet != null) {
            keySet.add((Character) child.value());
        }
        children.put((Character) child.value(), child);
    }

    public Node getChild(char value) {
        return children.get((Character) value);
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public PriorityQueue<Character> childrenToIterateOver() {
        return keySet;
    }
}
