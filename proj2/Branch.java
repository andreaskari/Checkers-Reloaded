import java.util.HashMap;
import java.io.Serializable;

public class Branch implements Serializable {
    private static final String FILE_SNAPSHOT_DIRECTORY_PATH = ".gitlet/Snapshots/";

    private String branchName;
    private Commit branchInitialCommit;
    private Commit branchHead;
    private HashMap<String, Integer> trackedFilePaths;
    private int branchSize;

    public Branch() {
        branchName = "master";
        branchInitialCommit = new Commit(0, "initial commit");
        branchHead = branchInitialCommit;
        trackedFilePaths = new HashMap<String, Integer>();
        branchSize = 1;
    }

    public Branch(Branch other) {
        branchName = other.name();
        branchInitialCommit = other.initialCommit();
        branchHead = other.head();
        branchSize = other.size();
    }

    public String name() {
        return branchName;
    }

    public Commit initialCommit() {
        return branchInitialCommit;
    }

    public Commit head() {
        return branchHead;
    }

    public int size() {
        return branchSize;
    }

    public boolean fileHasBeenCommitted(String fileName) {
        return trackedFilePaths.containsKey(fileName);
    }

    public String getSnapshotPath(String fileName) {
        String rawPath = trackedFilePaths.get(fileName) + "/" + fileName;
        return FILE_SNAPSHOT_DIRECTORY_PATH + rawPath;
    }
}
