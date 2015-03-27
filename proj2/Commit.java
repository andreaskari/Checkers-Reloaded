import java.util.HashSet;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.lang.ClassNotFoundException;

public class Commit implements Serializable {
    private int commitID;
    private String commitMessage;
    private HashSet<String> commitedFiles;
    private HashSet<String> removedFiles;
    private HashSet<Commit> commitChildren;

    public Commit(int newID, String newMessage) {
        commitID = newID;
        commitMessage = newMessage;
        commitChildren = new HashSet<Commit>();
    }

    public Commit(int newID, String newMessage, Stage currentStage) {
        this(newID, newMessage);
        commitedFiles = currentStage.stagedFiles();
        removedFiles = currentStage.markedForRemoval();
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
        return commitChildren;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {

    }
 
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        
    }

    private void readObjectNoData() throws ObjectStreamException {
        
    }
}
