import java.util.ArrayList;

public class WeightedTrie {
    private WeightedNode root;
    private int size;

    public WeightedTrie() {
        root = new WeightedNode();
        size = 0;
    }

    public void prioritizeWeightedTrie() {
        prioritizeRecursively(root);
    }

    private void prioritizeRecursively(WeightedNode pointer) {
        pointer.sortChildren();
        for (WeightedNode child: pointer.getSortedChildren()) {
            prioritizeRecursively(child);
        }
    }

    public void insert(String str, Double val) {
        int stringSize = str.length();
        WeightedNode pointer = root;
        for (int i = 0; i < stringSize; i++) {
            char letter = str.charAt(i);
            if (pointer.max() < val) {
                pointer.setMax(val);
            }
            WeightedNode next = pointer.getChild(letter);
            if (next == null) {
                WeightedNode wNode = null;
                if (i == stringSize - 1) {
                    wNode = new WeightedNode(letter, (Double) val, (Double) val);
                } else {
                    wNode = new WeightedNode(letter, null, (Double) val);
                }
                pointer.addChild(wNode);
                pointer.setMiddleChild(wNode);
            }
            pointer = pointer.getChild(letter);
        }
        pointer.setValue(val);
        size += 1;
    }

    public double getWeightOfWord(String str) {
        WeightedNode pointer = root;
        for (int i = 0; i < str.length(); i++) {
            char letter = str.charAt(i);
            pointer = pointer.getChild(letter);
            if (pointer == null) {
                return 0.0;
            }
        }
        return pointer.value();
    }

    public Iterable<String> getTopWeightsOfPartialWords(String partialStr, int num) {
        ArrayList<String> words = new ArrayList<String>(num);
        WeightedNode pointer = root;
        for (int i = 0; i < partialStr.size(); i++) {
            char letter = partialStr.charAt(i);
            pointer = pointer.getChild(letter);
            if (pointer == null) {
                return words;
            }
        }
        for (int i = 0; i < num; i++) {
            // How to traverse and 
        }
    }

    public WeightedNode rootNode() {
        return root;
    }
}