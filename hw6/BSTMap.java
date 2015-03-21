import java.util.Set;

public class BSTMap<Key extends Comparable, Value> implements Map61B<Key, Value> {
    private Node root = null;
    private int size = 0;

    /** Removes all of the mappings from this map. */
    public void clear() {
        root = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(Key key) {
        return get(key) != null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key. 
     */
    public Value get(Key key) {
        return findInTree(root, key);
    }

    private Value findInTree(Node pointer, Key key) {
        if (pointer == null) {
            return null;
        }
        int indexer = key.compareTo(pointer.key());
        if (indexer > 0) {
            return findInTree(pointer.right(), key);
        } else if (indexer < 0) {
            return findInTree(pointer.left(), key);
        }
        return pointer.value;
    }

   /* Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    /* Associates the specified value with the specified key in this map. */
    public void put(Key key, Value value) {
        if (value == null) {
            return;
        }
        root = augmentTree(root, key, value);
    }

    private Node augmentTree(Node pointer, Key key, Value value) {
        if (pointer == null) {
            size += 1;
            return new Node(key, value);
        }
        int indexer = key.compareTo(pointer.key());
        if (indexer > 0) {
            pointer.setRight(augmentTree(pointer.right(), key, value));
        } else if (indexer < 0) {
            pointer.setLeft(augmentTree(pointer.left(), key, value));
        } else {
            pointer.setValue(value);
        }
        return pointer;
    }

    @Override
    public Value remove(Key key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Value remove(Key key, Value value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Key> keySet() {
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {
        printNode(root);
    }

    private void printNode(Node pointer) {
        printNode(pointer.left());
        System.out.println(pointer.key());
        printNode(pointer.right());
    }
 
    private class Node {
        private Key key;
        private Value value;
        private Node left, right;

        public Node(Key k, Value v) {
            key = k;
            value = v;
            left = null;
            right = null;
        }

        public Key key() {
            return key;
        }

        public void setValue(Value v) {
            value = v;
        }

        public Node left() {
            return left;
        }

        public Node right() {
            return right;
        }

        public void setLeft(Node l) {
            left = l;
        }

        public void setRight(Node r) {
            right = r;
        }
    }
}
