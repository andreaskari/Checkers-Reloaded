import java.util.HashSet;
import java.io.Serializable;

public class Stage implements Serializable {
    private static final long serialVersionUID = 3L;

    HashSet stagedFiles;
    HashSet markedForRemoval;

    public Stage() {
        stagedFiles = new HashSet<String>();
        markedForRemoval = new HashSet<String>();
    }

    public boolean isOnStage(String path) {
        return stagedFiles.contains(path);
    }

    @SuppressWarnings("unchecked")
    public void addToStage(String path) {
        stagedFiles.add(path);
    }

    public void removeFromStage(String path) {
        stagedFiles.remove(path);
    }

    @SuppressWarnings("unchecked")
    public HashSet<String> stagedFiles() {
        return (HashSet<String>) stagedFiles;
    }

    public boolean isMarkedForRemoval(String path) {
        return markedForRemoval.contains(path);
    }

    @SuppressWarnings("unchecked")
    public void markForRemoval(String path) {
        markedForRemoval.add(path);
    }

    public void removeMarkForRemoval(String path) {
        markedForRemoval.remove(path);
    }

    @SuppressWarnings("unchecked")
    public HashSet<String> markedForRemoval() {
        return (HashSet<String>) markedForRemoval;
    }
}
