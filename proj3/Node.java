import java.util.HashMap;
import java.util.PriorityQueue;

public class Node {
    private static final int NODE_CAPACITY = 256;

    private char letter;
    private boolean isTerm;
    private HashMap<Character, Node> children;
    private PriorityQueue<Character> keySet;

    public Node() {
        letter = ' ';
        isTerm = false;
        children = new HashMap<Character, Node>(NODE_CAPACITY);
    }

    public Node(HashMap<Character, Integer> alphabetMap) {
        this();
        if (alphabetMap == null) {
            keySet = null;
        } else {
            keySet = new PriorityQueue<Character>(NODE_CAPACITY, new AlphabetComparator(alphabetMap));
        }
    }

    public Node(char l, boolean it, HashMap<Character, Integer> alphabetMap) {
        this(alphabetMap);
        letter = l;
        isTerm = it;
    }

    public char letter() {
        return letter;
    }

    public boolean isTerm() {
        return isTerm;
    }

    public void setAsTerm() {
        isTerm = true;
    }

    public void addChild(Node child) {
        if (keySet != null) {
            keySet.add((Character) child.letter());
        }
        children.put((Character) child.letter(), child);
    }

    public Node getChild(char letter) {
        return children.get((Character) letter);
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public PriorityQueue<Character> childrenToIterateOver() {
        return keySet;
    }
}
