import java.util.HashSet;
import java.util.Date;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.lang.ClassNotFoundException;

public class Commit implements Serializable {
    private static final long serialVersionUID = 7526472295622776147L;
    
    private int commitID;
    private String commitMessage;
    private String commitDateString;
    private Commit commitParent;
    private HashSet<String> commitedFiles;
    private HashSet<String> removedFiles;
    private HashSet<Commit> commitChildren;

    public Commit(int newID, String newMessage) {
        commitID = newID;
        commitMessage = newMessage;
        commitDateString = (new Date()).toString();
        commitParent = null;
        commitChildren = new HashSet<Commit>();
        commitedFiles = null;
        removedFiles = null;       
    }

    public Commit(int newID, String newMessage, Commit parent, Stage currentStage) {
        commitID = newID;
        commitMessage = newMessage;
        commitDateString = (new Date()).toString();
        commitParent = parent;
        commitChildren = new HashSet<Commit>();
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

    public Commit parent() {
        return commitParent;
    }

    public String date() {
        return commitDateString;
    }

    // private void writeObject(ObjectOutputStream out) throws IOException {

    // }
 
    // private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        
    // }

    // private void readObjectNoData() throws ObjectStreamException {
        
    // }
}
