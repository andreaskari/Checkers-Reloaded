import java.util.HashMap;
import java.util.PriorityQueue;

public class WeightedNode implements Comparable {
    private static final int ROOT_NODE_CAPACITY = 256;
    private static final int DEEP_NODE_CAPACITY = 16;

    private char letter;
    private Double value;
    private Double max;
    private WeightedNode parent;
    private HashMap<Character, WeightedNode> children;
    private PriorityQueue<WeightedNode> sortedChildren;
    // private PriorityQueue<WeightedNode> sortedAdjacent;

    public WeightedNode() {
        letter = ' ';
        value = null;
        max = null;
        sortedChildren = null;
        parent = null;
        children = new HashMap<Character, WeightedNode>(ROOT_NODE_CAPACITY);
        // sortedAdjacent = new PriorityQueue<WeightedNode>();
    }

    public WeightedNode(char l, Double v, Double m, WeightedNode p) {
        letter = l;
        value = v;
        max = m;
        sortedChildren = null;
        parent = p;
        children = new HashMap<Character, WeightedNode>(DEEP_NODE_CAPACITY);
        // sortedAdjacent = new PriorityQueue<WeightedNode>();
    }

    public char letter() {
        return letter;
    }

    public void setValue(Double v) {
        value = v;
    }

    public Double value() {
        return value;
    }

    public void setMax(Double m) {
        max = m;
    }

    public Double max() {
        return max;
    }

    public WeightedNode parent() {
        return parent;
    }

    public void addChild(WeightedNode child) {
        children.put((Character) child.letter(), child);
    }

    public WeightedNode getChild(char letter) {
        return children.get((Character) letter);
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public void sortChildren() {
        sortedChildren = new PriorityQueue<WeightedNode>();
        for (WeightedNode child: children.values()) {
            sortedChildren.add(child);   
        }
    }

    public PriorityQueue<WeightedNode> getSortedChildren() {
        return sortedChildren;
    }

    // public PriorityQueue<WeightedNode> getSortedAdjacent() {
    //     return sortedAdjacent;
    // } 

    public int compareTo(Object o) {
        if (o instanceof WeightedNode) {
            if (max == ((WeightedNode) o).max()) {
                return (int) (10 * (value - ((WeightedNode) o).value()));
            }
            return (int) (10 * (max - ((WeightedNode) o).max()));
        }
        return 0;
    }
}
