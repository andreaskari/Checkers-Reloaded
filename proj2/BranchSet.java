import java.util.HashSet;
import java.io.Serializable;

public class BranchSet extends HashSet<Branch> implements Serializable {
    private static final long serialVersionUID = -45447344955473426L;

    private Branch currentBranch;

    public BranchSet() {
        super();
        Branch master = new Branch();
        currentBranch = master;
        add(master);
    }

    public Branch currentBranch() {
        return currentBranch;
    }

}
