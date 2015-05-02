import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class TSTNode implements Comparable {
    private static final int ROOT_NODE_CAPACITY = 256;
    private static final int DEEP_NODE_CAPACITY = 16;

    private char letter;
    private String str;
    private Double value, max;
    private TSTNode left, middle, right;

    public TSTNode() {
        letter = ' ';
        str = "";
        value = null;
        max = null;
    }

    public TSTNode(char l, Double m, String s) {
        letter = l;
        str = s;
        value = null;
        max = m;
    }

    public char letter() {
        return letter;
    }

    public String toString() {
        return str;
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

    public int compareTo(Object o) {
        if (o instanceof TSTNode) {
            TSTNode other = (TSTNode) o;
            return (int) (10 * (other.max() - max));
        }
        return 0;
    }
}
