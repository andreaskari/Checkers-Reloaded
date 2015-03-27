import java.util.HashSet;

public class Stage {
    HashSet<String> stagedFiles;
    HashSet<String> markedForRemoval;

    public Stage() {
        stagedFiles = new HashSet<String>();
        markedForRemoval = new HashSet<String>();
    }

    public boolean isOnStage(String path) {
        return stagedFiles.contains(path);
    }

    public void addToStage(String path) {
        stagedFiles.add(path);
    }

    public void removeFromStage(String path) {
        stagedFiles.remove(path);
    }

    public HashSet<String> stagedFiles() {
        return stagedFiles;
    }

    public boolean isMarkedForRemoval(String path) {
        return markedForRemoval.contains(path);
    }

    public void markForRemoval(String path) {
        markedForRemoval.add(path);
    }

    public void removeMarkForRemoval(String path) {
        markedForRemoval.remove(path);
    }

    public HashSet<String> markedForRemoval() {
        return markedForRemoval;
    }
}