import java.util.HashSet;
import java.util.HashMap;
import java.io.Serializable;

public class BranchMap extends HashMap<String, Branch> implements Serializable {
    private static final long serialVersionUID = -45447344955473426L;

    private int totalNumberCommits;
    private Commit startingCommit;
    private Branch currentBranch;
    private HashMap idsToCommits;
    private HashMap messagesToCommits;

    public BranchMap() {
        super();
        totalNumberCommits = 1;
        Branch master = new Branch();
        currentBranch = master;
        startingCommit = currentBranch.initialCommit();
        put("master", master);
        idsToCommits = new HashMap<String, Commit>();
        messagesToCommits = new HashMap<String, HashSet<Integer>>();
    }

    public int totalNumberCommits() {
        return totalNumberCommits;
    }

    public Commit startingCommit() {
        return startingCommit;
    }

    public Branch currentBranch() {
        return currentBranch;
    }

    public void setCurrentBranch(String branchName) {
        currentBranch = get(branchName);
    }

    @SuppressWarnings("unchecked")
    public void addCommitToMapOfBranches(Commit newCommit) {
        totalNumberCommits += 1;
        idsToCommits.put("" + newCommit.id(), newCommit);

        // Adds to messagesToCommits map
        String message = newCommit.message();
        if (messagesToCommits.containsKey(message)) {
            HashSet<Integer> commitSet = (HashSet<Integer>) messagesToCommits.get(message);
            commitSet.add(newCommit.id());
        } else {
            HashSet<Integer> commitSet = new HashSet<Integer>();
            commitSet.add(newCommit.id());
            messagesToCommits.put(message, commitSet);
        }
    }

    public boolean commitIDExists(String commitID) {
        return idsToCommits.containsKey(commitID); 
    }

    public Commit commitForID(String commitID) {
        return (Commit) idsToCommits.get(commitID);
    }

    @SuppressWarnings("unchecked")
    public void printCommitsWithMessage(String message) {
        if (messagesToCommits.containsKey(message)) {
            HashSet<Integer> commitSet = (HashSet<Integer>) messagesToCommits.get(message);
            for (Integer id: commitSet) {
                System.out.println(id);
            }
        } else {
            System.out.println("Found no commit with that message.");
        }
    }
}
