import java.io.File;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.ObjectOutput;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Gitlet {
    private static final String GITLET_DIRECTORY_PATH = ".gitlet/";
    private static final String STAGE_FILE_PATH = ".gitlet/Stage.ser";
    private static final String BRANCHMAP_FILE_PATH = ".gitlet/Branches.ser";
    private static final String SNAPSHOT_DIRECTORY_PATH = ".gitlet/Snapshots/";

    public static void main(String[] args) {
        String command = args[0];
        if (command.equals("init")) {
            initializeCommand();
        } else if (command.equals("add")) {
            addCommand(args[1]);
        } else if (command.equals("commit")) {
            commitCommand(args[1]);
        } else if (command.equals("rm")) {
            removeCommand();
        } else if (command.equals("log")) {
            logCommand();
        } else if (command.equals("global-log")) {
            globalLogCommand();
        } else if (command.equals("find")) {
            findCommand();
        } else if (command.equals("status")) {
            statusCommand();
        } else if (command.equals("checkout")) {
            checkoutCommand();
        } else if (command.equals("branch")) {
            branchCommand();
        } else if (command.equals("rm-branch")) {
            removeBranchCommand();
        } else if (command.equals("reset")) {
            resetCommand();
        } else if (command.equals("merge")) {
            mergeCommand();
        } else if (command.equals("rebase")) {
            rebaseCommand();
        } else if (command.equals("i-rebase")) {
            interactiveRebaseCommand();
        }
    }

    /**  General Helper Methods  */ 

    private static void writeToObjectFile(Object newObject, String filePath) {
        try {
            OutputStream file = new FileOutputStream(filePath);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            try {
                output.writeObject(newObject);
            }
            finally {
                output.close();
            }
        } catch (IOException ex) {
            
        }
    }

    private static void writeToStageFile(Stage newStage) {
        writeToObjectFile(newStage, STAGE_FILE_PATH);
    }

    private static void writeToBranchSetFile(BranchMap newBranchSet) {
        writeToObjectFile(newBranchSet, BRANCHMAP_FILE_PATH);
    }

    private static Object getObjectFromFile(String filePath) {
        Object obj = null;
        try {
            InputStream file = new FileInputStream(filePath);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream (buffer);
            try {
                obj = input.readObject();
            }
            finally {
                input.close();
            }
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        } catch(IOException ex){
            System.out.println(ex);
        }
        return obj;
    }

    private static Stage getStageFromFilePath() {
        return (Stage) getObjectFromFile(STAGE_FILE_PATH);
    }

    private static BranchMap getBranchSetFromFilePath() {
        return (BranchMap) getObjectFromFile(BRANCHMAP_FILE_PATH);
    }

    /**  Command Execution Methods  */

    private static void initializeCommand() {
        File gitletDirectory = new File(GITLET_DIRECTORY_PATH);
        File snapshotDirectory = new File(SNAPSHOT_DIRECTORY_PATH);
        if (!gitletDirectory.exists()) {
            gitletDirectory.mkdir();
            snapshotDirectory.mkdir();
            writeToStageFile(new Stage());
            writeToBranchSetFile(new BranchMap());
        } else {
            System.out.println("A gitlet version control system already exists in the current directory.");
        }
    }

    private static void addCommand(String filePath) {
        File addedFile = new File(filePath);
        if (addedFile.exists()) {
            Stage currentStage = getStageFromFilePath();
            BranchMap currentBranchSet = getBranchSetFromFilePath();
            Branch currentBranch = currentBranchSet.currentBranch();

            // filePath may need previous processing for directories
            if (currentBranch.fileHasBeenCommitted(filePath)) {
                String previousPath = currentBranch.getSnapshotPath(filePath);
                File previousFile = new File(previousPath);
                if (previousFile.equals(addedFile)) {
                    System.out.println("File has not been modified since the last commit.");
                } else {
                    if (currentStage.isMarkedForRemoval(filePath)) {
                        currentStage.removeMarkForRemoval(filePath);
                    }
                    currentStage.addToStage(filePath);
                    writeToStageFile(currentStage);
                }
            } else {
                currentStage.addToStage(filePath);
                writeToStageFile(currentStage);
            }
        } else {
            System.out.println("File does not exist.");
        }
    }

    private static void commitCommand(String commitMessage) {
        Stage currentStage = getStageFromFilePath();
        BranchMap currentBranchSet = getBranchSetFromFilePath();
        Branch currentBranch = currentBranchSet.currentBranch();
        Commit currentHead = currentBranch.head();

        int commitID = currentBranch.size();
        Commit newlyCreatedCommit = new Commit(commitID, commitMessage, currentHead, currentStage);
        currentBranch.setHead(newlyCreatedCommit);
        
        writeToBranchSetFile(currentBranchSet);
        writeToStageFile(new Stage());
    }

    private static void removeCommand() {

    }

    private static void logCommand() {
        BranchMap currentBranchSet = getBranchSetFromFilePath();
        Commit pointer = currentBranchSet.currentBranch().head();
        while (pointer != null) {
            System.out.println("====\nCommit " + pointer.id() + ".\n" + pointer.date() + "\n" + pointer.message() + "\n");
            pointer = pointer.parent();
        }
    }

    private static void globalLogCommand() {
        BranchMap currentBranchSet = getBranchSetFromFilePath();
        printCommitAndChildren(currentBranchSet.currentBranch().initialCommit());
    }

    private static void printCommitAndChildren(Commit pointer) {
        if (pointer != null) {
            for (Commit child: pointer.children()) {
                printCommitAndChildren(child);
            }            
        System.out.println("====\nCommit " + pointer.id() + ".\n" + pointer.date() + "\n" + pointer.message() + "\n");
        }
    }

    private static void findCommand() {

    }

    private static void statusCommand() {
        Stage currentStage = getStageFromFilePath();
        BranchMap currentBranchSet = getBranchSetFromFilePath();
        System.out.println("=== Branches ===");
        String currentBranchName = currentBranchSet.currentBranch().name();
        for (String branchName: currentBranchSet.keySet()) {
            if (branchName == currentBranchName) {
                System.out.print("*");
            }
            System.out.println(branchName);
        }

        System.out.println("\n=== Staged Files ===");
        for (String fileName: currentStage.stagedFiles()) {
            System.out.println(fileName);
        }

        System.out.println("\n=== Files Marked for Removal ===");
        for (String fileName: currentStage.markedForRemoval()) {
            System.out.println(fileName);
        }
        System.out.println();
    }

    private static void checkoutCommand() {

    }

    private static void branchCommand() {

    }

    private static void removeBranchCommand() {

    }

    private static void resetCommand() {

    }

    private static void mergeCommand() {

    }

    private static void rebaseCommand() {

    }

    private static void interactiveRebaseCommand() {

    }
}
