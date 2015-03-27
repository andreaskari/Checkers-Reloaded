public class Branch {
	private String branchName;
	private Commit branchInitialCommit;
	private Commit branchHead;
	private int branchSize;

	public Branch() {
		branchName = "master";
		branchInitialCommit = new Commit(0, "initial commit");
		branchHead = branchInitialCommit;
		branchSize = 1;
	}

	public Branch(String name, Commit initialCommit, Commit head, int size) {
		branchName = name;
		branchInitialCommit = initialCommit;
		branchHead = head;
		branchSize = size;
	}

	public String name() {
		return branchName;
	}

	public Commit head() {
		return branchHead;
	}

	public int size() {
		return branchSize;
	}

	public Commit startingCommit() {
		return branchInitialCommit;
	}
}