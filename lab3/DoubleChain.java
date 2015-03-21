
public class DoubleChain {
	
	private DNode head;
	
	public DoubleChain(double val) {
		/* your code here. -- DONE */
		head = new DNode(null, val, null); 
	}

	public DNode getFront() {
		return head;
	}

	/** Returns the last item in the DoubleChain. */		
	public DNode getBack() {
		/* your code here -- DONE */
		DNode pointer = head;
		while (pointer.next != null) {
			pointer = pointer.next;
		}
		return pointer;
	}
	
	/** Adds D to the front of the DoubleChain. */	
	public void insertFront(double d) {
		/* your code here -- DONE */
		head = new DNode(null, d, head);
		head.next.prev = head;
	}
	
	/** Adds D to the back of the DoubleChain. */	
	public void insertBack(double d) {
		/* your code here -- DONE */
		DNode back = new DNode(getBack(), d, null);
		back.prev.next = back;
	}
	
	/** Removes the last item in the DoubleChain and returns it. 
	  * This is an extra challenge problem. */
	public DNode deleteBack() {
		/* your code here */
		if (head == null) {
			return null;
		}
		DNode originalBack = getBack();
		originalBack.prev.next = null;
		originalBack.prev = null;
		return originalBack;
	}
	
	/** Returns a string representation of the DoubleChain. 
	  * This is an extra challenge problem. */
	public String toString() {
		/* your code here */		
		String str = "<[" + head.val;
		DNode pointer = head.next;
		while (pointer != null) {
			str = str + ", " + pointer.val;
			pointer = pointer.next;
		}
		return str + "]>";
	}

	public static class DNode {
		public DNode prev;
		public DNode next;
		public double val;
		
		private DNode(double val) {
			this(null, val, null);
		}
		
		private DNode(DNode prev, double val, DNode next) {
			this.prev = prev;
			this.val = val;
			this.next =next;
		}
	}
	
}
