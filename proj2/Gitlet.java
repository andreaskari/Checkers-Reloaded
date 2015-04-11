import java.util.Arrays;
import java.util.Set;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.ObjectOutput;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
            if (args.length < 2) {
                System.out.println("Please enter a commit message.");
            } else {
                commitCommand(args[1]);
            }
        } else if (command.equals("rm")) {
            removeCommand(args[1]);
        } else if (command.equals("log")) {
            logCommand();
        } else if (command.equals("global-log")) {
            globalLogCommand();
        } else if (command.equals("find")) {
            findCommand(args[1]);
        } else if (command.equals("status")) {
            statusCommand();
        } else if (command.equals("checkout")) {
            if (dangerousCommandIsOK()) {
                if (args.length > 2) {
                    checkoutTwoArgsCommand(args[1], args[2]);
                } else {
                    checkoutOneArgCommand(args[1]);
                }
            }
        } else if (command.equals("branch")) {
            branchCommand(args[1]);
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

    private static void writeToBranchMapFile(BranchMap newBranchMap) {
        writeToObjectFile(newBranchMap, BRANCHMAP_FILE_PATH);
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

    private static byte[] getByteCodeFromFile(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException ex) {

        }
        return null;
    }

    private static void copyContentsFromSourceToDestination(String sourcePath, String destPath) {
        try {
            FileOutputStream copier = new FileOutputStream(destPath);
            try {
                copier.write(getByteCodeFromFile(sourcePath));
            } finally {
                copier.close();
            }
        } catch (IOException ex) {
            System.out.println("FAILS: " + ex); // NEEDS TO BE REMOVED
        }
    }

    // private static void createFileFromSourceToDestination(String sourcePath, String destPath) {
    //     try {
    //         byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
    //         Files.write(Paths.get(destPath), fileBytes, StandardOpenOption.CREATE);
    //     } catch (IOException ex) {

    //     }
    // }

    private static boolean dangerousCommandIsOK() { 
        System.out.println("Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue? (yes/no)");
 
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String yesNo = null;

        try {
            yesNo = br.readLine();
        } catch (IOException ioe) {

        }
        boolean saidYes = yesNo.equals("yes");
        if (!saidYes) {
            System.out.println("Did not type 'yes', so aborting.");
        }
        return saidYes;
    }

    /**  Command Execution Methods  */

    private static void initializeCommand() {
        File gitletDirectory = new File(GITLET_DIRECTORY_PATH);
        File snapshotDirectory = new File(SNAPSHOT_DIRECTORY_PATH);
        if (!gitletDirectory.exists()) {
            gitletDirectory.mkdir();
            snapshotDirectory.mkdir();
            writeToStageFile(new Stage());
            writeToBranchMapFile(new BranchMap());
        } else {
            System.out.println("A gitlet version control system already exists in the current directory.");
        }
    }

    private static void addCommand(String filePath) {
        File addedFile = new File(filePath);
        if (addedFile.exists()) {
            Stage currentStage = getStageFromFilePath();
            BranchMap currentBranchMap = getBranchSetFromFilePath();
            Branch currentBranch = currentBranchMap.currentBranch();

            if (currentBranch.head().fileHasBeenCommitted(filePath)) {
                String previousPath = currentBranch.head().getSnapshotPath(filePath);
                if (Arrays.equals(getByteCodeFromFile(filePath), getByteCodeFromFile(previousPath))) { // THIS IS FAULTY AF
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
        if (currentStage.stagedFiles().isEmpty() && currentStage.markedForRemoval().isEmpty()) {
            System.out.println("No changes added to the commit.");
        } else {
            BranchMap currentBranchMap = getBranchSetFromFilePath();

            int commitID = currentBranchMap.totalNumberCommits();
            Commit newlyCreatedCommit = new Commit(commitID, commitMessage, currentBranchMap.currentBranch().head(), currentStage, SNAPSHOT_DIRECTORY_PATH);
            currentBranchMap.addCommitToMapOfBranches(newlyCreatedCommit);

            writeToBranchMapFile(currentBranchMap);
            writeToStageFile(new Stage());
        }
    }

    private static void removeCommand(String filePath) {
        Stage currentStage = getStageFromFilePath();
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        if (currentStage.isOnStage(filePath)) {
            currentStage.removeFromStage(filePath);
            writeToStageFile(currentStage);
        } else if (currentBranchMap.currentBranch().head().filePathIsTracked(filePath)) {
            currentStage.markForRemoval(filePath);
            writeToStageFile(currentStage);
        } else {
            System.out.println("No reason to remove the file.");
        }
    }

    private static void logCommand() {
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        Commit pointer = currentBranchMap.currentBranch().head();
        while (pointer != null) {
            System.out.println("====\nCommit " + pointer.id() + ".\n" + pointer.date() + "\n" + pointer.message() + "\n");
            pointer = pointer.parent();
        }
    }

    private static void globalLogCommand() {
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        printCommitAndChildren(currentBranchMap.currentBranch().initialCommit());
    }

    private static void printCommitAndChildren(Commit pointer) {
        if (pointer != null) {
            for (Commit child: pointer.children()) {
                printCommitAndChildren(child);
            }            
            System.out.println("====\nCommit " + pointer.id() + ".\n" + pointer.date() + "\n" + pointer.message() + "\n");
        }
    }

    private static void findCommand(String message) {
        // Can't find messages that are "stringed together" in Terminal
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        currentBranchMap.printCommitsWithMessage(message);
    }

    private static void statusCommand() {
        Stage currentStage = getStageFromFilePath();
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        System.out.println("=== Branches ===");
        String currentBranchName = currentBranchMap.currentBranch().name();
        for (String branchName: currentBranchMap.keySet()) {
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

    private static void checkoutOneArgCommand(String fileOrBranch) {
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        Branch currentBranch = currentBranchMap.currentBranch();        
        if (currentBranch.head().filePathIsTracked(fileOrBranch)) {
            String fileSnapshotPath = currentBranch.head().getSnapshotPath(fileOrBranch);
            copyContentsFromSourceToDestination(fileSnapshotPath, fileOrBranch);
        } else if (fileOrBranch.equals(currentBranch.name())) {
            System.out.println("No need to checkout the current branch.");
        } else if (currentBranchMap.containsKey(fileOrBranch)) {
            Commit currentHead = currentBranchMap.currentBranch().head();
            Commit headToSwitchTo = currentBranchMap.get(fileOrBranch).head();
            System.out.println(currentBranchMap.currentBranch() == currentBranchMap.get(fileOrBranch));
            System.out.println(currentHead == headToSwitchTo);

            Set<String> currentFilesInDirectory = currentHead.trackedFilePaths();
            Set<String> filesToPlaceInDirectory = headToSwitchTo.trackedFilePaths();
            System.out.println("In: " + filesToPlaceInDirectory + "\nOut: " + currentFilesInDirectory);
            for (String fileInDirectory: currentFilesInDirectory) {
                if (filesToPlaceInDirectory.contains(fileInDirectory)) {
                    String fileSnapshotPath = headToSwitchTo.getSnapshotPath(fileInDirectory);
                    copyContentsFromSourceToDestination(fileSnapshotPath, fileInDirectory);
                } else {
                    try {
                        Files.delete(Paths.get(fileInDirectory));
                    } catch (IOException ex) {
                        System.out.println("WTF"); // TO BE DELETED
                    }
                }
            }
            currentBranchMap.setCurrentBranch(fileOrBranch);
            for (String fileToPossiblyRemove: filesToPlaceInDirectory) {
                if (!currentFilesInDirectory.contains(fileToPossiblyRemove)) {
                    String fileSnapshotPath = headToSwitchTo.getSnapshotPath(fileToPossiblyRemove);
                    copyContentsFromSourceToDestination(fileSnapshotPath, fileToPossiblyRemove);
                }
            }
            writeToBranchMapFile(currentBranchMap);
        } else {
            System.out.println("File does not exist in the most recent commit, or no such branch exists.");
        }
    }    

    private static void checkoutTwoArgsCommand(String commitID, String fileName) {
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        if (currentBranchMap.commitIDExists(commitID)) {
            Commit commitToCheck = currentBranchMap.commitForID(commitID);
            if (commitToCheck.fileHasBeenCommitted(fileName)) {
                String fileSnapshotPath = commitToCheck.getSnapshotPath(fileName);
                copyContentsFromSourceToDestination(fileSnapshotPath, fileName);
            } else {
                System.out.println("File does not exist in that commit.");
            }
        } else {
            System.out.println("No commit with that id exists.");
        }
    }

    private static void branchCommand(String branchName) {
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        Branch newlyCreatedBranch = new Branch(currentBranchMap.currentBranch(), branchName);
        currentBranchMap.put(branchName, newlyCreatedBranch);
        writeToBranchMapFile(currentBranchMap);
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
