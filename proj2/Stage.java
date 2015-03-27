import java.util.HashSet;

public class Stage {
	HashSet<String> stagedFiles;
	HashSet<String> markedForRemoval;

	public Stage() {
		stagedFiles = new HashSet<String>();
		markedForRemoval = new HashSet<String>();
	}

	public void addToStage(String path) {
		stagedFiles.add(path);
	}

	public HashSet<String> stagedFiles() {
		return stagedFiles;
	}

	public void markForRemoval(String path) {
		markedForRemoval.add(path);
	}

	public HashSet<String> markedForRemoval() {
		return markedForRemoval;
	}
}