import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Date;
import java.io.Serializable;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Commit implements Serializable {
    private static final long serialVersionUID = 2L;

    private int commitID;
    private String commitMessage;
    private String commitDateString;
    private String commitBranchCreated;
    private Commit commitParent;
    private HashSet commitChildren;
    private HashMap committedFilesToPaths;

    public Commit(int newID, String newMessage, String branchCreated) {
        commitID = newID;
        commitMessage = newMessage;
        commitDateString = (new Date()).toString();
        commitBranchCreated = branchCreated;
        commitParent = null;
        commitChildren = new HashSet();
        committedFilesToPaths = new HashMap<String, String>();
    }

    public Commit(int newID, String newMessage, Commit parent, String branchCreated, Stage currentStage, String snapshot_directory_path) {
        commitID = newID;
        commitMessage = newMessage;
        commitDateString = (new Date()).toString();
        commitBranchCreated = branchCreated;
        commitParent = parent;
        commitChildren = new HashSet();
        commitParent.addChild(this);
        committedFilesToPaths = new HashMap<String, String>();

        String commitSnapshotDirectory = snapshot_directory_path + newID + "/";
        File commitSnapshotFile = new File(commitSnapshotDirectory);
        commitSnapshotFile.mkdir();
        for (String oldFilePath: parent.committedFilesToPathsMap().keySet()) {
            committedFilesToPaths.put(oldFilePath, parent.getSnapshotPath(oldFilePath));
        }
        for (String filePath: currentStage.stagedFiles()) {
            String fileSnapshotPath = commitSnapshotFile + "/" + filePath;
            File snapshotFile = new File(fileSnapshotPath);
            snapshotFile.getParentFile().mkdirs();
            try {
                byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
                Files.write(Paths.get(fileSnapshotPath), fileBytes, StandardOpenOption.CREATE);
            } catch (IOException ex) {

            }
            committedFilesToPaths.put(filePath, fileSnapshotPath);
        }
        for (String filePath: currentStage.markedForRemoval()) {
            committedFilesToPaths.remove(filePath);
        }
        System.out.println(committedFilesToPaths); // NEEDS TO BE REMOVED LATER
    }

    public Commit(Commit otherCommit, int newID) {
        commitID = newID;
        commitMessage = otherCommit.message();
        commitDateString = otherCommit.date();
        commitParent = otherCommit.parent();
        commitChildren = otherCommit.children();
        committedFilesToPaths = otherCommit.committedFilesToPathsMap();
    }

    public int id() {
        return commitID;
    }

    public String message() {
        return commitMessage;
    }

    public void addChild(Commit child) {
        commitChildren.add(child);
    }

    public boolean hasChildren() {
        return commitChildren.isEmpty();
    }

    public HashSet<Commit> children() {
        return (HashSet<Commit>) commitChildren;
    }

    public Commit parent() {
        return commitParent;
    }

    public String date() {
        return commitDateString;
    }

    public String birthingBranch() {
        return commitBranchCreated;
    }

    public boolean fileHasBeenCommitted(String filePath) {
        return committedFilesToPaths.containsKey(filePath);
    }

    public String getSnapshotPath(String filePath) {
        return (String) committedFilesToPaths.get(filePath);
    }

    public boolean filePathIsTracked(String filePath) {
        return committedFilesToPaths.containsKey(filePath);
    }

    public Set<String> trackedFilePaths() {
        return ((HashMap<String, String>) committedFilesToPaths).keySet();
    }

    public HashMap<String, String> committedFilesToPathsMap() {
        return (HashMap<String, String>) committedFilesToPaths;
    }
}
