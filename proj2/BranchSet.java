import java.util.HashSet;

public class BranchSet extends HashSet<Branch> {
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
