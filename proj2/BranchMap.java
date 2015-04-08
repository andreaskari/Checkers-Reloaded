import java.util.HashMap;
import java.io.Serializable;

public class BranchMap extends HashMap<String, Branch> implements Serializable {
    private static final long serialVersionUID = -45447344955473426L;

    private Branch currentBranch;

    public BranchMap() {
        super();
        Branch master = new Branch();
        currentBranch = master;
        put("master", master);
    }

    public Branch currentBranch() {
        return currentBranch;
    }

    public void setCurrentBranch(String branchName) {
        currentBranch = get(branchName);
    }
}
