import java.util.HashMap;
import java.util.HashSet;
import java.io.Serializable;

public class Branch implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean active;
    private String branchName;
    private Commit branchInitialCommit;
    private Commit branchHead;
    private int branchSize;

    @SuppressWarnings("unchecked")
    public Branch() {
        active = true;
        branchName = "master";
        branchInitialCommit = new Commit(0, "initial commit");
        branchHead = branchInitialCommit;
    }

    public Branch(Branch offShoot, String name) {
        active = true;
        branchName = name;
        branchInitialCommit = offShoot.head();
        branchHead = offShoot.head();
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

    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }
}
