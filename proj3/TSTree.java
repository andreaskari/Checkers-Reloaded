import java.util.Comparator;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Arrays;

public class TSTree {
    private TSTNode root = null;

    public TSTNode rootNode() {
        return root;
    }

    public void prioritizeTST() {
        prioritizeRecursively(root);
    }

    private void prioritizeRecursively(TSTNode pointer) {
        if (pointer != null) {
            pointer.setPrioritizedChildren();

            prioritizeRecursively(pointer.left());
            prioritizeRecursively(pointer.middle());
            prioritizeRecursively(pointer.right());
        }
    }

    public void insert(String str, Double val) {
        root = insertNode(root, str.toCharArray(), val, 0);
    }

    private TSTNode insertNode(TSTNode pointer, char[] letters, Double val, int depth) {
        char letter = letters[depth];
        if (pointer == null) {
            pointer = new TSTNode(letter, (Double) val);
        }
        if (pointer.max() < val) {
            pointer.setMax(val);
        }
        if (letter < pointer.letter()) {
            pointer.setLeft(insertNode(pointer.left(), letters, val, depth));
        } else if (letter > pointer.letter()) {
            pointer.setRight(insertNode(pointer.right(), letters, val, depth));
        } else {
            if (depth + 1 < letters.length) {
                // if (pointer.max() < val) {
                //     pointer.setMax(val);
                // }
                pointer.setMiddle(insertNode(pointer.middle(), letters, val, depth + 1));
            } else {
                pointer.setValue(val);
            }
        }
        return pointer;
    }

    public boolean contains(String str) {
        // return containsStr(root, str.toCharArray(), 0);
        return getNodeOfStr(root, str.toCharArray(), 0) != null && getNodeOfStr(root, str.toCharArray(), 0).value() != null;
    }

    public double getWeightOf(String str) {
        // return getWeightOfStr(root, str.toCharArray(), 0);
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

    public ArrayList<String> getTopWeightsOfPartialWords(String partialStr, int numRequested) {
        ArrayList<String> words = new ArrayList<String>(numRequested);
        TSTNode pointer = getNodeOfStr(root, partialStr.toCharArray(), 0);

        if (pointer != null) {
            PriorityQueue<StringAndValue> waitListed = new PriorityQueue<StringAndValue>(numRequested, new SVComparator());
            if (pointer.value() == pointer.max()) {
                words.add(partialStr);
            } else if (pointer.value() != null) {
                waitListed.add(new StringAndValue(partialStr, pointer.value()));
            }

            TSTNode pointerMiddle = pointer.middle();
            if (pointerMiddle != null) {
                if (pointerMiddle.value() == pointer.max()) {
                    words.add(partialStr + pointerMiddle.letter());
                } else if (pointerMiddle.value() != null) {
                    waitListed.add(new StringAndValue(partialStr + pointerMiddle.letter(), pointerMiddle.value()));
                }

                int numChildren = pointer.middle().prioritizedChildren().size();
                if (numChildren > 0) {
                    TSTNode[] sortedPQ = pointer.middle().prioritizedChildren().toArray(new TSTNode[numChildren]);
                    Arrays.sort(sortedPQ);
                    for (int i = sortedPQ.length - 1; words.size() < numRequested && i >= 0; i--) {
                        TSTNode child = sortedPQ[i];

                        if (pointer.middle().middle() != null && child == pointer.middle().middle()) {
                            addDownBranch(child, partialStr + pointer.middle().letter(), waitListed, words, pointer.max());
                        } else {
                            addDownBranch(child, partialStr, waitListed, words, pointer.max());
                        }

                        double minWeight = 0.0;
                        if (i != 0) {
                            TSTNode next = sortedPQ[i-1];
                            minWeight = next.max();
                        }
                        while (waitListed.size() > 0 && words.size() < numRequested && (double) waitListed.peek().value() >= minWeight) {
                            words.add(waitListed.poll().word());
                        }
                    }
                }
            }
        }
        return words;
    }

    private void addDownBranch(TSTNode pointer, String partialStr, 
            PriorityQueue<StringAndValue> pq, ArrayList<String> words, double maxValue) {

        if (pointer.value() != null && pointer.value() == maxValue) {
            words.add(partialStr + pointer.letter());
        } else if (pointer.value() != null) {
            pq.add(new StringAndValue(partialStr + pointer.letter(), pointer.value()));
        }
        if (pointer.hasChildren()) {
            TSTNode[] sortedPQ = pointer.prioritizedChildren().toArray(new TSTNode[pointer.prioritizedChildren().size()]);
            Arrays.sort(sortedPQ);
            for (int i = 0; i < sortedPQ.length; i++) {
                TSTNode child = sortedPQ[i];
                if (child == pointer.middle()) {
                    addDownBranch(child, partialStr + pointer.letter(), pq, words, maxValue);
                } else {
                    addDownBranch(child, partialStr, pq, words, maxValue);
                }
            }
        }
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