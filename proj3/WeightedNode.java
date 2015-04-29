import java.util.HashMap;
import java.util.PriorityQueue;

public class WeightedNode implements Comparable {
    private static final int ROOT_NODE_CAPACITY = 256;
    private static final int DEEP_NODE_CAPACITY = 16;

    private char letter;
    private Double value;
    private Double max;
    private WeightedNode middleNode;
    private HashMap<Character, WeightedNode> children;
    private PriorityQueue<WeightedNode> sortedChildren;

    public WeightedNode() {
        letter = ' ';
        value = null;
        max = null;
        sortedChildren = null;
        middleNode = null;
        children = new HashMap<Character, WeightedNode>(ROOT_NODE_CAPACITY);
    }

    public WeightedNode(char l, Double v, Double m) {
        letter = l;
        value = v;
        max = m;
        sortedChildren = null;
        middleNode = null;
        children = new HashMap<Character, WeightedNode>(DEEP_NODE_CAPACITY);
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

    public void setMiddleChild(WeightedNode child) {
        middleNode = child;
    }

    public WeightedNode getMiddleChild() {
        return middleNode;
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
