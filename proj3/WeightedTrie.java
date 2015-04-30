import java.util.Comparator;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Arrays;

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
            if (pointer.max() == null || pointer.max() < val) {
                pointer.setMax(val);
            }
            WeightedNode next = pointer.getChild(letter);
            if (next == null) {
                WeightedNode wNode = null;
                if (i == stringSize - 1) {
                    wNode = new WeightedNode(letter, (Double) val, (Double) val, pointer);
                } else {
                    wNode = new WeightedNode(letter, null, (Double) val, pointer);
                }
                pointer.addChild(wNode);
            }
            pointer = pointer.getChild(letter);
        }
        pointer.setValue(val);
        size += 1;
    }

    public boolean find(String s) {
        WeightedNode pointer = root;
        for (int i = 0; i < s.length(); i++) {
            char letter = s.charAt(i);
            pointer = pointer.getChild(letter);
            if (pointer == null) {
                return false;
            }
        }
        return pointer.value() != null;
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

    public ArrayList<String> getTopWeightsOfPartialWords(String partialStr, int numRequested) {
        ArrayList<String> words = new ArrayList<String>(numRequested);
        WeightedNode pointer = root;
        for (int i = 0; i < partialStr.length(); i++) {
            char letter = partialStr.charAt(i);
            pointer = pointer.getChild(letter);
            if (pointer == null) {
                return words;
            }
        }
        PriorityQueue<StringAndValue> waitListed = new PriorityQueue<StringAndValue>(numRequested, new SVComparator());
        if (pointer.value() == pointer.max()) {
            words.add(partialStr);
            numRequested -= 1;
        } else if (pointer.value() != null) {
            waitListed.add(new StringAndValue(partialStr, pointer.value()));
        }

        int numChildren = pointer.getSortedChildren().size();
        if (numChildren > 0) {
            WeightedNode[] sortedPQ = pointer.getSortedChildren().toArray(new WeightedNode[numChildren]);
            Arrays.sort(sortedPQ);
            for (int i = sortedPQ.length - 1; numRequested > 0 && i >= 0; i--) {
                WeightedNode child = sortedPQ[i];

                addDownBranch(child, partialStr, waitListed, words, child.max(), numRequested - 1);
                numRequested -= 1;

                double minWeight = 0.0;
                if (i != 0) {
                    WeightedNode next = sortedPQ[i-1];
                    minWeight = next.max();
                }
                while (waitListed.size() > 0 && numRequested > 0 && (double) waitListed.peek().value() >= minWeight) {
                    words.add(waitListed.poll().word());
                    numRequested -= 1;
                }
            }
        }
        return words;
    }

    private void addDownBranch(WeightedNode pointer, String partialStr, 
            PriorityQueue<StringAndValue> pq, ArrayList<String> words, double maxValue, int numRequested) {

        while (pointer.getSortedChildren().size() == 1) {
            partialStr += pointer.letter();
            if (pointer.value() != null && pointer.value() == maxValue) {
                words.add(partialStr);
            } else if (pointer.value() != null) {
                pq.add(new StringAndValue(partialStr, pointer.value()));
            }
            WeightedNode next = pointer.getSortedChildren().peek();
            pointer = next;
        }
        partialStr += pointer.letter();
        if (pointer.value() != null && pointer.value() == maxValue) {
            words.add(partialStr);
        } else if (pointer.value() != null) {
            pq.add(new StringAndValue(partialStr, pointer.value()));
        }
        if (pointer.hasChildren()) {
            WeightedNode[] sortedPQ = pointer.getSortedChildren().toArray(new WeightedNode[pointer.getSortedChildren().size()]);
            Arrays.sort(sortedPQ);
            for (int i = 0; i < sortedPQ.length; i++) {
                WeightedNode child = sortedPQ[i];
                addDownBranch(child, partialStr, pq, words, maxValue, numRequested);
            }
        }
    }

    public WeightedNode rootNode() {
        return root;
    }

    private class StringAndValue implements Comparable {
        private Double value;
        private String word;

        public StringAndValue(String w, Double v) {
            value = v;
            word = w;
        }

        public Double value() {
            return value;
        }

        public String word() {
            return word;
        }

        public int compareTo(Object o) {
            return (int) (10 * (value - ((StringAndValue) o).value()));
        }
    }

    private class SVComparator implements Comparator<StringAndValue> {
        public int compare(StringAndValue o1, StringAndValue o2) {
            return -1 * o1.compareTo(o2);
        }

        public boolean equals(Object obj) {
            return this == obj;
        }
    }
}