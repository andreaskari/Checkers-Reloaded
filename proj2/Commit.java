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
    private HashSet removedFiles;
    private HashSet commitChildren;
    private HashMap committedFilesToPaths;

    public Commit(int newID, String newMessage) {
        commitID = newID;
        commitMessage = newMessage;
        commitDateString = (new Date()).toString();
        commitParent = null;
        commitChildren = new HashSet();
        removedFiles = null;    
        committedFilesToPaths = new HashMap<String, String>();
    }

    public Commit(int newID, String newMessage, Commit parent, Stage currentStage, String snapshot_directory_path) {
        commitID = newID;
        commitMessage = newMessage;
        commitDateString = (new Date()).toString();
        commitParent = parent;
        commitChildren = new HashSet();
        removedFiles = currentStage.markedForRemoval();
        commitParent.addChild(this);
        committedFilesToPaths = new HashMap<String, String>();

        String commitSnapshotDirectory = snapshot_directory_path + newID + "/";
        File commitSnapshotFile = new File(commitSnapshotDirectory);
        commitSnapshotFile.mkdir();
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

    public boolean fileHasBeenCommitted(String filePath) {
        return committedFilesToPaths.containsKey(filePath);
    }

    public String getSnapshotPath(String filePath) {
        return (String) committedFilesToPaths.get(filePath);
    }

    public boolean filePathIsTracked(String filePath) {
        return committedFilesToPaths.containsKey(filePath);
    }
}
