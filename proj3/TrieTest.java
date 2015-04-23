import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TrieTest {

    @Test
    public void testBasic() {
        Trie t = new Trie();
        t.insert("hello");
        t.insert("hey");
        t.insert("goodbye");
        assertTrue(t.find("hell", false));
        assertTrue(t.find("hello", true));
        assertTrue(t.find("good", false));
        assertTrue(!t.find("bye", false));
        assertTrue(!t.find("heyy", false));
        assertTrue(!t.find("hell", true)); 
    }

    @Test
    public void testInsert() {
        Trie t = new Trie();
        boolean threwException = false;
        try {
            t.insert(null);
        } catch (IllegalArgumentException ex) {
            threwException = true;
        }
        assertTrue(threwException);

        threwException = false;
        try {
            t.insert("");
        } catch (IllegalArgumentException ex) {
            threwException = true;
        }
        assertTrue(threwException);
    }

    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TrieTest.class);
    }
}