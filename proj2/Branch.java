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
    private HashMap splitPoints;

    public Branch() {
        active = true;
        branchName = "master";
        branchInitialCommit = new Commit(0, "initial commit", "master");
        branchHead = branchInitialCommit;
        splitPoints = new HashMap<String, Commit>();
        splitPoints.put("master", branchInitialCommit);
    }

    public Branch(Branch offShoot, String name) {
        active = true;
        branchName = name;
        branchInitialCommit = offShoot.head();
        branchHead = offShoot.head();
        splitPoints = new HashMap<String, Commit>();
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

    public void addPossibleSplitPoint(String branchName, Commit splitPoint) {
        splitPoints.put(branchName, splitPoint);
    }

    public Commit getSplitPoint(String branchName) {
        return (Commit) splitPoints.get(branchName);
    }
}
