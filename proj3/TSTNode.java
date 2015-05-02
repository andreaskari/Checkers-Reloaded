import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class TSTNode implements Comparable {
    private static final int ROOT_NODE_CAPACITY = 256;
    private static final int DEEP_NODE_CAPACITY = 16;

    private char letter;
    private Double value, max;
    private TSTNode left, middle, right;

    private TSTNode[] prioritizedChildren;


    public TSTNode() {
        letter = ' ';
        value = null;
        max = null;
    }

    public TSTNode(char l, Double m) {
        letter = l;
        value = null;
        max = m;
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

    public void setRight(TSTNode r) {
        right = r;
    }

    public TSTNode right() {
        return right;
    }

    public void setLeft(TSTNode l) {
        left = l;
    }

    public TSTNode left() {
        return left;
    }

    public void setMiddle(TSTNode m) {
        middle = m;
    }

    public TSTNode middle() {
        return middle;
    }

    public boolean hasChildren() {
        return middle != null || right != null || left != null;
    }

    public void setPrioritizedChildren() {
        ArrayList<TSTNode> children = new ArrayList<TSTNode>(3);
        if (middle != null) {
            children.add(middle);
        }
        if (left != null) {
            children.add(left);
        }
        if (right != null) {
            children.add(right);
        }
        prioritizedChildren = children.toArray(new TSTNode[children.size()]);
        Arrays.sort(prioritizedChildren);
    }

    public TSTNode[] prioritizedChildren() {
        return prioritizedChildren;
    }

    public int compareTo(Object o) {
        if (o instanceof TSTNode) {
            TSTNode other = (TSTNode) o;
            if (max == other.max()) {
                return (int) (10 * (value - other.value()));
            }
            return (int) (10 * (max - other.max()));
        }
        return 0;
    }
}
