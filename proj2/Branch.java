import java.util.HashMap;
import java.util.HashSet;
import java.io.Serializable;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Branch implements Serializable {
    private static final long serialVersionUID = 1L;

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
        trackedFilePaths = new HashMap<String, String>();
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

    public void addNewCommit(Commit newCommit, Stage currentStage, String snapshot_directory_path) {
        branchSize += 1;
        branchHead = newCommit;
        addCommitToMap(newCommit);

        String commitSnapshotDirectory = snapshot_directory_path + newCommit.id() + "/";
        File commitSnapshotFile = new File(commitSnapshotDirectory);
        commitSnapshotFile.mkdir();
        for (String filePath: currentStage.stagedFiles()) {
            String fileSnapshotPath = commitSnapshotFile + "/" + filePath;
            System.out.println(fileSnapshotPath);
            File snapshotFile = new File(fileSnapshotPath);
            snapshotFile.getParentFile().mkdirs();
            try {
                byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
                Files.write(Paths.get(fileSnapshotPath), fileBytes, StandardOpenOption.CREATE);
            } catch (IOException ex) {
                System.out.println(ex); // NEEDS TO BE REMOVED LATER
            }
            trackedFilePaths.put(filePath, fileSnapshotPath);
        }
        for (String filePath: currentStage.markedForRemoval()) {
            trackedFilePaths.remove(filePath);
        }
        System.out.println(trackedFilePaths);
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

    public boolean fileHasBeenCommitted(String filePath) {
        return trackedFilePaths.containsKey(filePath);
    }

    public String getSnapshotPath(String filePath) {
        return (String) trackedFilePaths.get(filePath);
    }

    public HashMap<String, String> trackedFilePaths() {
        return (HashMap<String, String>) trackedFilePaths;
    }

    public HashMap<String, HashSet<Commit>> messagesToCommits() {
        return (HashMap<String, HashSet<Commit>>) messagesToCommits;
    }
}
