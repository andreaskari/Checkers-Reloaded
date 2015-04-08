import java.util.HashSet;
import java.util.Date;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.lang.ClassNotFoundException;

public class Commit implements Serializable {
    private static final long serialVersionUID = 2L;

    private int commitID;
    private String commitMessage;
    private String commitDateString;
    private Commit commitParent;
    private HashSet commitedFiles;
    private HashSet removedFiles;
    private HashSet commitChildren;

    public Commit(int newID, String newMessage) {
        commitID = newID;
        commitMessage = newMessage;
        commitDateString = (new Date()).toString();
        commitParent = null;
        commitChildren = new HashSet();
        commitedFiles = null;
        removedFiles = null;       
    }

    public Commit(int newID, String newMessage, Commit parent, Stage currentStage) {
        commitID = newID;
        commitMessage = newMessage;
        commitDateString = (new Date()).toString();
        commitParent = parent;
        commitChildren = new HashSet();
        commitedFiles = currentStage.stagedFiles();
        removedFiles = currentStage.markedForRemoval();
        commitParent.addChild(this);
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
}
