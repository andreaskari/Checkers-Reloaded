import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Set;

public class HashMapTest {

    /**  Also contains tests for: 
            - containsKey()
            - get()
            - size() 
            - put()             */
    @Test
    public void testDefaultConstructor() {
        MyHashMap<String, Integer> basic = new MyHashMap<String, Integer>();
        assertEquals(0, basic.size());

        basic.put("dog", 5);
        basic.put("cat", 6);
        basic.put("mouse", 7);

        assertEquals(3, basic.size());

        assertTrue(basic.containsKey("dog"));
        assertTrue(basic.containsKey("cat"));
        assertTrue(basic.containsKey("mouse"));
        assertFalse(basic.containsKey("coyote"));

        assertEquals(5, basic.get("dog").intValue());
        assertEquals(6, basic.get("cat").intValue());
        assertEquals(7, basic.get("mouse").intValue());
        assertEquals(null, basic.get("coyote"));
    }

    /**  Also contains tests for: 
            - containsKey()
            - get()
            - size() 
            - put()             */
    @Test
    public void testSizeConstructor() {
        MyHashMap<String, Integer> basic = new MyHashMap<String, Integer>(5);
        assertEquals(0, basic.size());

        basic.put("dog", 5);
        basic.put("cat", 6);
        basic.put("mouse", 7);

        assertEquals(3, basic.size());

        assertTrue(basic.containsKey("dog"));
        assertTrue(basic.containsKey("cat"));
        assertTrue(basic.containsKey("mouse"));
        assertFalse(basic.containsKey("coyote"));

        assertEquals(5, basic.get("dog").intValue());
        assertEquals(6, basic.get("cat").intValue());
        assertEquals(7, basic.get("mouse").intValue());
        assertEquals(null, basic.get("coyote"));
    }

    /**  Also contains tests for: 
            - containsKey()
            - get()
            - size() 
            - put()             */
    @Test
    public void testLoadFactorConstructor() {
        MyHashMap<String, Integer> basic = new MyHashMap<String, Integer>(2, 1.0f);
        assertEquals(0, basic.size());

        basic.put("dog", 5);
        basic.put("cat", 6);
        basic.put("mouse", 7);
        basic.put("cheetah", 1000);
        basic.put("sphinx", 9001);

        assertEquals(5, basic.size());

        assertTrue(basic.containsKey("dog"));
        assertTrue(basic.containsKey("cat"));
        assertTrue(basic.containsKey("mouse"));
        assertFalse(basic.containsKey("coyote"));

        assertEquals(5, basic.get("dog").intValue());
        assertEquals(6, basic.get("cat").intValue());
        assertEquals(7, basic.get("mouse").intValue());
        assertEquals(1000, basic.get("cheetah").intValue());
        assertEquals(9001, basic.get("sphinx").intValue());
        assertEquals(null, basic.get("coyote"));
    }

    @Test
    public void testClearAndSize() {
        MyHashMap<String, Integer> basic = new MyHashMap<String, Integer>(5);
        assertEquals(0, basic.size());

        basic.put("dog", 5);
        basic.put("cat", 6);
        basic.put("mouse", 7);

        assertEquals(3, basic.size());

        basic.clear();

        assertEquals(0, basic.size());

        basic.put("dog", 5);
        basic.put("cat", 6);
        basic.put("mouse", 7);

        assertEquals(3, basic.size());
    }

    @Test 
    public void testRemoveKey() {
        MyHashMap<String, Integer> basic = new MyHashMap<String, Integer>(5);
        assertEquals(0, basic.size());

        basic.put("dog", 5);
        basic.put("cat", 6);
        basic.put("mouse", 7);

        assertEquals(3, basic.size());

        Integer value = basic.remove("dog");

        assertEquals(5, value.intValue());
        assertEquals(2, basic.size());

        basic.remove("cat");
        basic.remove("mouse");

        assertEquals(0, basic.size());
    }

    @Test
    public void testRemoveKeyValue() {
        MyHashMap<String, Integer> basic = new MyHashMap<String, Integer>(5);
        assertEquals(0, basic.size());

        basic.put("dog", 5);
        basic.put("cat", 6);
        basic.put("mouse", 7);

        assertEquals(3, basic.size());

        Integer value = basic.remove("dog", 4);

        assertEquals(null, value);
        assertEquals(3, basic.size());
        assertTrue(basic.containsKey("dog"));

        basic.remove("cat", 6);
        basic.remove("mouse", 7);

        assertEquals(1, basic.size());
        assertTrue(basic.containsKey("dog"));
        assertFalse(basic.containsKey("cat"));
        assertFalse(basic.containsKey("mouse"));
        assertFalse(basic.containsKey("coyote"));
    }

    @Test
    public void testKeySet() {
        MyHashMap<String, Integer> basic = new MyHashMap<String, Integer>();
        assertEquals(0, basic.size());

        basic.put("dog", 5);
        basic.put("cat", 6);
        basic.put("mouse", 7);

        assertEquals(3, basic.size());

        Set<String> keySet = basic.keySet();

        assertTrue(keySet.contains("dog"));
        assertTrue(keySet.contains("cat"));
        assertTrue(keySet.contains("mouse"));
        assertFalse(keySet.contains("coyote"));

        assertEquals(3, keySet.size());
    }

    public static void main(String[] args) {
        System.exit(jh61b.junit.textui.runClasses(HashMapTest.class));
    }
}