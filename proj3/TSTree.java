import java.util.Comparator;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Arrays;

public class TSTree {
    private TSTNode root = null;

    public TSTNode rootNode() {
        return root;
    }

    public void insert(String str, Double val) {
        root = insertNode(root, str, val, 0);
    }

    private TSTNode insertNode(TSTNode pointer, String letters, Double val, int depth) {
        char letter = letters.charAt(depth);
        if (pointer == null) {
            pointer = new TSTNode(letter, (Double) val, letters.substring(0, depth + 1));
        }
        if (pointer.max() < val) {
            pointer.setMax(val);
        }
        if (letter < pointer.letter()) {
            pointer.setLeft(insertNode(pointer.left(), letters, val, depth));
        } else if (letter > pointer.letter()) {
            pointer.setRight(insertNode(pointer.right(), letters, val, depth));
        } else {
            if (depth + 1 < letters.length()) {
                pointer.setMiddle(insertNode(pointer.middle(), letters, val, depth + 1));
            } else {
                pointer.setValue(val);
            }
        }
        return pointer;
    }

    public boolean contains(String str) {
        return getNodeOfStr(root, str.toCharArray(), 0) != null && getNodeOfStr(root, str.toCharArray(), 0).value() != null;
    }

    public double getWeightOf(String str) {
        TSTNode node = getNodeOfStr(root, str.toCharArray(), 0);
        if (node == null || node.value() == null) {
            return 0.0;
        }
        return node.value();
    }

    private TSTNode getNodeOfStr(TSTNode pointer, char[] letters, int depth) {
        if (pointer == null) {
            return null;
        } 
        char letter = letters[depth];
        if (letter < pointer.letter()) {
            return getNodeOfStr(pointer.left(), letters, depth);
        } else if (letter > pointer.letter()) {
            return getNodeOfStr(pointer.right(), letters, depth);
        } else {
            if (depth == letters.length - 1) {
                return pointer;
            } else {
                return getNodeOfStr(pointer.middle(), letters, depth + 1);
            }
        }
    }

    private void collect(TSTNode pointer, StringBuilder prefix, Queue<String> queue) {
        if (pointer == null) {
            return;
        }
        collect(pointer.left(), prefix, queue);
        if (pointer.value() != null && pointer.value() != 0.0) {
            queue.enqueue(prefix.toString() + pointer.letter());
        }
        collect(pointer.middle(), prefix.append(pointer.letter()), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(pointer.right(), prefix, queue);
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> queue = new Queue<String>();
        TSTNode pointer = getNodeOfStr(root, prefix.toCharArray(), 0);
        if (pointer == null) {
            return queue;
        }
        if (pointer.value() != null && pointer.value() != 0.0) {
            queue.enqueue(prefix);
        }
        collect (pointer.middle(), new StringBuilder(prefix), queue);
        return queue;
    }
    
    public ArrayList<String> keysWithPrefix(String prefix, int k) {
        PriorityQueue<TSTNode> fringe = new PriorityQueue<TSTNode>();
        PriorityQueue<TSTNode> topRequested = new PriorityQueue<TSTNode>(6, new MinNodeComparator());

        TSTNode pointer = getNodeOfStr(root, prefix.toCharArray(), 0);
        StringBuilder prefixBuilder = new StringBuilder(prefix);
        ArrayList<String> strList = new ArrayList<String>();

        if (pointer == null || k == 0) {
            return strList;
        }
        if (pointer.value() != null) {
            topRequested.add(pointer);
        }
        if (!prefix.equals("")) {
            pointer = pointer.middle();
        }
        if (pointer != null) {
            fringe.add(pointer);
        }
        
        while (!fringe.isEmpty()) {
            pointer = fringe.poll();
            if (pointer.left() != null) {
                fringe.add(pointer.left());
            }
            if (pointer.middle() != null) {
                fringe.add(pointer.middle());
            }
            if (pointer.right() != null) {
                fringe.add(pointer.right());
            }
            
            prefixBuilder.append(pointer.letter());
            if (pointer.value() != null) {
                topRequested.add(pointer);
                if (topRequested.size() > k) {
                    topRequested.poll();
                }
            }
            if (topRequested.size() >= k && !fringe.isEmpty()) {
                double largestMax = fringe.peek().max();
                double kth = topRequested.peek().value();
                if (largestMax <= kth) {
                    break;
                }
            }
        }
        
        Stack<TSTNode> stack = new Stack<TSTNode>();
        while(!topRequested.isEmpty()) {
            TSTNode top = topRequested.poll();
            stack.push(top);
        }
        while (!stack.isEmpty()) {
            TSTNode top = stack.pop();
            strList.add(top.toString());
        }
        return strList;
    }

    class MinNodeComparator implements Comparator<TSTNode> {
        @Override
        public int compare(TSTNode o1, TSTNode o2) {
            return (int) (o1.value() - o2.value());
        }
    }
}