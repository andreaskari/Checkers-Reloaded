import java.util.HashMap;
import java.util.HashSet;
import java.io.Serializable;

public class Branch implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FILE_SNAPSHOT_DIRECTORY_PATH = ".gitlet/Snapshots/";

    private String branchName;
    private Commit branchInitialCommit;
    private Commit branchHead;
    private int branchSize;
    private HashMap trackedFilePaths;
    private HashMap messagesToCommits;

    public Branch() {
        branchName = "master";
        branchInitialCommit = new Commit(0, "initial commit");
        branchHead = branchInitialCommit;
        branchSize = 1;
        trackedFilePaths = new HashMap<String, Integer>();
        messagesToCommits = new HashMap<String, HashSet<Integer>>();
    }

    public Branch(Branch other) {
        branchName = other.name();
        branchInitialCommit = other.initialCommit();
        branchHead = other.head();
        branchSize = other.size();
        trackedFilePaths = other.trackedFilePaths();
        messagesToCommits = other.messagesToCommits();
    }

    public String name() {
        return branchName;
    }

    public Commit initialCommit() {
        return branchInitialCommit;
    }

    public void setHead(Commit newHead) {
        branchHead = newHead;
    }

    public Commit head() {
        return branchHead;
    }

    public int size() {
        return branchSize;
    }

    public void addNewCommit(Commit newCommit) {
        branchSize += 1;
        branchHead = newCommit;
        addCommitToMap(newCommit);
    }

    private void addCommitToMap(Commit newCommit) {
        String message = newCommit.message();
        if (messagesToCommits.containsKey(message)) {
            HashSet<Integer> commitSet = (HashSet<Integer>) messagesToCommits.get(message);
            commitSet.add(newCommit.id());
        } else {
            HashSet<Integer> commitSet = new HashSet<Integer>();
            commitSet.add(newCommit.id());
            messagesToCommits.put(message,  commitSet);
        }
    }

    public void printCommitsWithMessage(String message) {
        if (messagesToCommits.containsKey(message)) {
            HashSet<Integer> commitSet = (HashSet<Integer>) messagesToCommits.get(message);
            for (Integer id: commitSet) {
                System.out.println(id);
            }
        } else {
            System.out.println("Found no commit with that message.");
        }
    }

    public boolean fileHasBeenCommitted(String fileName) {
        return trackedFilePaths.containsKey(fileName);
    }

    public String getSnapshotPath(String fileName) {
        String rawPath = trackedFilePaths.get(fileName) + "/" + fileName;
        return FILE_SNAPSHOT_DIRECTORY_PATH + rawPath;
    }

    public HashMap<String, Integer> trackedFilePaths() {
        return trackedFilePaths;
    }

    public HashMap<String, HashSet<Commit>> messagesToCommits() {
        return messagesToCommits;
    }
}
