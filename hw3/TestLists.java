import static org.junit.Assert.*;
import org.junit.Test;

public class TestLists {
	@Test
	public void testInsertApp() {
		ApplicableIntList started = new ApplicableIntList(4, null);
		started.insert(5);
		started.insert(4);
		started.insert(2);
		System.out.println(started.toString());
		assertEquals(2, started.head);
		assertEquals(4, started.tail.head);
		assertEquals(4, started.tail.tail.head);
		assertEquals(5, started.tail.tail.tail.head);
	} 

	@Test
	public void testInsertSorted() {
		SortedComparableList started = new SortedComparableList(4, null);
		started.insert(6);
		started.insert(4);
		started.insert(2);
		System.out.println(started.toString());
		started.squish();
		System.out.println(started.toString());
		SortedComparableList fromTheBottom = new SortedComparableList(10, null);
		fromTheBottom.insert(5);
		fromTheBottom.insert(0);
		fromTheBottom.insert(2);
		started.extend(fromTheBottom);
		System.out.println(started.toString());
		started.twin();
		System.out.println(started.toString());
		fromTheBottom.twin();
		System.out.println(fromTheBottom.toString());
	} 

	public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestLists.class);
    }
}