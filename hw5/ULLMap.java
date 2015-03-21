import java.util.Set; /* java.util.Set needed only for challenge problem. */
import java.util.Iterator;

/** A data structure that uses a linked list to store pairs of keys and values.
 *  Any key must appear at most once in the dictionary, but values may appear multiple
 *  times. Supports get(key), put(key, value), and contains(key) methods. The value
 *  associated to a key is the value in the last call to put with that key. 
 *
 *  For simplicity, you may assume that nobody ever inserts a null key or value
 *  into your map.
 */ 
public class ULLMap<Key, Value> implements Map61B<Key, Value>, Iterable<Key> {
    /** Keys and values are stored in a linked list of Entry objects.
      * This variable stores the first pair in this linked list. You may
      * point this at a sentinel node, or use it as a the actual front item
      * of the linked list. 
      */
    private Entry front;

    @Override
    public Value get(Key key) {
        Entry e = front.get(key);
        return e.val;
    }

    @Override
    public void put(Key key, Value value) {
        front = new Entry(key, value, front);
    }

    @Override
    public boolean containsKey(Key key) {
        return front.get(key) == null;
    }

    @Override
    public int size() {
        int length = 0;
        Entry pointer = front;
        while (pointer != null) {
            length += 1;
            pointer = pointer.next;
        }
        return length;
    }

    @Override
    public void clear() {
        front = null;
    }

    public static <A, B> ULLMap<B, A> invert(ULLMap<A, B> original) {
        ULLMap<B, A> inverse = new ULLMap<B, A>();
        Iterator<A> keys = original.iterator();
        while (keys.hasNext()) {
            A a = keys.next();
            B b = original.get(a);
            inverse.put(b, a);
        }
        return inverse;
    }

    public Iterator<Key> iterator() {
        return new ULLMapIter();
    }

    /** Represents one node in the linked list that stores the key-value pairs
     *  in the dictionary. */
    private class Entry {
    
        /** Stores KEY as the key in this key-value pair, VAL as the value, and
         *  NEXT as the next node in the linked list. */
        public Entry(Key k, Value v, Entry n) {
            key = k;
            val = v;
            next = n;
        }

        /** Returns the Entry in this linked list of key-value pairs whose key
         *  is equal to KEY, or null if no such Entry exists. */
        public Entry get(Key k) { 
            //FILL ME IN (using equals, not ==)
            Entry pointer = this;
            while (pointer != null && !k.equals(pointer.key)) {
                pointer = pointer.next;
            }
            return pointer;
        }

        /** Stores the key of the key-value pair of this node in the list. */
        public Key key;
        /** Stores the value of the key-value pair of this node in the list. */
        public Value val;
        /** Stores the next Entry in the linked list. */
        public Entry next;
    }

    private class ULLMapIter implements Iterator<Key> {
        private Entry pointer;

        public ULLMapIter() {
            pointer = front;
        }

        public boolean hasNext() {
            return pointer != null;
        }

        public Key next() {
            Key key = pointer.key;
            pointer = pointer.next;
            return key;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Methods below are all challenge problems. Will not be graded in any way. 
     * Autograder will not test these. */
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
}