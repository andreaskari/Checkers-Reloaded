import java.util.HashMap;
import java.util.PriorityQueue;

public class TSTNode implements Comparable {
    private static final int NODE_CAPACITY = 256;

    private char letter;
    private Integer value;
    private Integer max;
    private TSTNode middleNode;
    // private HashMap<Character, TSTNode> children;
    private PriorityQueue<TSTNode> sortedChildren;

    public TSTNode(char l, Integer v, Integer m, TSTNode parent, boolean isMiddleChild) {
        letter = l;
        value = v;
        max = m;
        // children = new HashMap<Character, TSTNode>(NODE_CAPACITY);
        parent.addChild(this);
        if (isMiddleChild) {
            parent.setMiddleChild(this);
        }
    }

    public char letter() {
        return letter;
    }

    public int value() {
        return value.intValue();
    }

    public void setMax(Integer m) {
        max = m;
    }

    public int max() {
        return max.intValue();
    }

    public void setMiddleChild(TSTNode child) {
        middleNode = child;
    }

    public TSTNode getMiddleChild() {
        return middleNode;
    }

    public void addChild(TSTNode child) {
        sortedChildren.add(child.letter());
        // children.put((Character) child.letter(), child);
    }

    // public TSTNode getChild(char letter) {
    //     return children.get((Character) letter);
    // }

    public PriorityQueue<Character> getSortedChildren() {
        return sortedChildren;
    }

    public int compareTo(TSTNode o) {
        return max - o.max();
    }
}
