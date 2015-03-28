import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class MyHashMap<K, V> implements Map61B<K, V> {
    private static int DEFAULT_CAPACITY = 100;
    private static double DEFAULT_LOAD_FACTOR = 0.8;
    private static double RESIZE_FACTOR = 1.5;

    private int capacity;
    private double loadFactor;
    private int numItems;
    private ArrayList<Node> pairKVArray;

    private HashSet<K> keySet;
    private boolean keySetValid;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        loadFactor = DEFAULT_LOAD_FACTOR;
        clear();
    }

    public MyHashMap(int initialSize) {
        capacity = initialSize;
        loadFactor = DEFAULT_LOAD_FACTOR;
        clear();
    }

    public MyHashMap(int initialSize, float loadFactor) {
        capacity = initialSize;
        this.loadFactor = (double) loadFactor;
        clear();
    }

    public void clear() {
        numItems = 0;
        pairKVArray = new ArrayList<Node>(capacity);
        for (int i = 0; i < capacity; i++) {
            pairKVArray.add(null);
        }
        keySet = new HashSet<K>();
        keySetValid = true;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V get(K key) {
        int index = getKeyHashIndex(key, capacity);
        Node chain = pairKVArray.get(index);
        while (chain != null) {
            if (chain.key().equals(key)) {
                return chain.value();
            }
            chain = chain.next();
        }
        return null;
    }

    public int size() {
        return numItems;
    }

    public void put(K key, V value) {
        if ((double) (numItems + 1) / capacity > loadFactor) {
            resize();
            put(key, value);
        } else {
            // check if need to override a key that already is inputted?
            putInThisArray(key, value, pairKVArray, capacity);
            keySet.add(key);
            numItems += 1;
        }
    }

    private void putInThisArray(K key, V value, ArrayList<Node> arrayList, int givenCapacity) {
        int index = getKeyHashIndex(key, givenCapacity);
        Node chain = arrayList.get(index);
        arrayList.remove(index);
        arrayList.add(index, new Node(key, value, chain));
    }

    public V remove(K key) {
        V value = get(key);
        if (value == null) {
            return null;
        }
        int index = getKeyHashIndex(key, capacity);
        Node chain = pairKVArray.get(index);
        if (chain.key().equals(key)) {
            pairKVArray.remove(index);
            pairKVArray.add(index, chain.next());
        }
        while (chain.next != null) {
            if (chain.next().key().equals(key)) {
                chain.next().setToNode(chain.next().next());
                break;
            }
            chain = chain.next();
        }
        numItems -= 1;
        return value;
    }

    public V remove(K key, V value) {
        if (key == null) {
            return null;
        }
        V storedValue = get(key);
        if (value == null || storedValue == null || value != storedValue) {
            return null;
        }
        return remove(key);
    }

    public Set<K> keySet() {
        if (!keySetValid) {
            HashSet<K> newKeySet = new HashSet<K>();
            for (int i = 0; i < capacity; i++) {
                Node chain = pairKVArray.get(i);
                while (chain != null) {
                    newKeySet.add(chain.key());
                    chain = chain.next();
                }
            }
            keySet = newKeySet;
            keySetValid = true;
        }
        return keySet;
    }

    private int getKeyHashIndex(K key, int givenCapacity) {
        return (int) Math.abs(key.hashCode() % givenCapacity);
    }

    private void resize() {
        int newCapacity = (int) (RESIZE_FACTOR * capacity);
        ArrayList<Node> newKVArray = new ArrayList<Node>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            newKVArray.add(null);
        }
        for (int i = 0; i < capacity; i++) {
            Node chain = pairKVArray.get(i);
            while (chain != null) {
                putInThisArray(chain.key(), chain.value(), newKVArray, newCapacity);
                chain = chain.next();
            }
        }
        capacity = newCapacity;
        pairKVArray = newKVArray;
    }

    private class Node {
        private K key;
        private V value;
        private Node next;

        public Node(K k, V v) {
            key = k;
            value = v;
            next = null;
        }

        public Node(K k, V v, Node n) {
            key = k;
            value = v;
            next = n;
        }

        public K key() {
            return key;
        }

        public V value() {
            return value;
        }

        public Node next() {
            return next;
        }

        public void setToNode(Node n) {
            key = n.key();
            value = n.value();
            next = n.next();
        }
    }
}
