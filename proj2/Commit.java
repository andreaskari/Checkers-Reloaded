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
    private Commit commitParent;
    private HashSet commitChildren;
    private HashMap committedFilesToPaths;

    public Commit(int newID, String newMessage) {
        commitID = newID;
        commitMessage = newMessage;
        commitDateString = (new Date()).toString();
        commitParent = null;
        commitChildren = new HashSet();
        committedFilesToPaths = new HashMap<String, String>();
    }

    @SuppressWarnings("unchecked")
    public Commit(int newID, String newMessage, Commit parent, Stage currentStage, 
        String snapShotDirectoryPath) {

        commitID = newID;
        commitMessage = newMessage;
        commitDateString = (new Date()).toString();
        commitParent = parent;
        commitChildren = new HashSet<Commit>();
        commitParent.addChild(this);
        committedFilesToPaths = new HashMap<String, String>();

        String commitSnapshotDirectory = snapShotDirectoryPath + newID + "/";
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
                System.out.println("CAUGHT: " + ex);
            }
            committedFilesToPaths.put(filePath, fileSnapshotPath);
        }
        for (String filePath: currentStage.markedForRemoval()) {
            committedFilesToPaths.remove(filePath);
        }
    }

    @SuppressWarnings("unchecked")
    public Commit(Commit other, Commit parent, Commit child) {
        commitID = other.id();
        commitMessage = other.message();
        commitDateString = other.date();
        commitParent = parent;
        commitChildren = new HashSet<Commit>();
        if (child != null) {
            commitChildren.add(child);
        }
        committedFilesToPaths = new HashMap<String, String>(other.committedFilesToPathsMap());
    }

    public int id() {
        return commitID;
    }

    public String message() {
        return commitMessage;
    }

    public void setMessage(String message) {
        commitMessage = message;
    }

    @SuppressWarnings("unchecked")
    public void addChild(Commit child) {
        commitChildren.add(child);
    }

    public boolean hasChildren() {
        return commitChildren.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public HashSet<Commit> children() {
        return (HashSet<Commit>) commitChildren;
    }

    public Commit parent() {
        return commitParent;
    }

    public void setParent(Commit parent) {
        commitParent = parent;
    }

    public String date() {
        return commitDateString;
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

    @SuppressWarnings("unchecked")
    public Set<String> trackedFilePaths() {
        return ((HashMap<String, String>) committedFilesToPaths).keySet();
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, String> committedFilesToPathsMap() {
        return (HashMap<String, String>) committedFilesToPaths;
    }
}
