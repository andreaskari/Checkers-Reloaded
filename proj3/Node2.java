import java.util.HashMap;

public class TSTNode {
    private static final int ROOT_NODE_CAPACITY = 256;
    private static final int INITIAL_CAPACITY = 128;

    private int max;
    private int value;

    public Node() {
        value = ;
        isTerm = false;
        children = new HashMap<Character, Node>(ROOT_NODE_CAPACITY);
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
