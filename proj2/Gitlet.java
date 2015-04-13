import java.util.Arrays;
import java.util.HashSet;
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
import java.nio.file.Files;
import java.nio.file.Paths;

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
            removeBranchCommand(args[1]);
        } else if (command.equals("reset")) {
            if (dangerousCommandIsOK()) {
                resetCommand(args[1]);
            }
        } else if (command.equals("merge")) {
            if (dangerousCommandIsOK()) {
                mergeCommand(args[1]);
            }
        } else if (command.equals("rebase")) {
            if (dangerousCommandIsOK()) {
                rebaseCommand(args[1]);
            }
        } else if (command.equals("i-rebase")) {
            if (dangerousCommandIsOK()) {
                interactiveRebaseCommand(args[1]);
            }
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
            } finally {
                output.close();
            }
        } catch (IOException ex) {
            System.out.println("CAUGHT: " + ex);
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
            ObjectInput input = new ObjectInputStream(buffer);
            try {
                obj = input.readObject();
            } finally {
                input.close();
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("CAUGHT: " + ex);
        } catch (IOException ex) {
            System.out.println("CAUGHT: " + ex);            
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
            System.out.println("CAUGHT: " + ex);
        }
        return null;
    }

    private static boolean fileContentsEquals(String firstFilePath, String secondFilePath) {
        return Arrays.equals(getByteCodeFromFile(firstFilePath), 
            getByteCodeFromFile(secondFilePath));
    }

    private static void copyContentsFromSourceToDest(String sourcePath, String destPath) {
        try {
            FileOutputStream copier = new FileOutputStream(destPath);
            try {
                copier.write(getByteCodeFromFile(sourcePath));
            } finally {
                copier.close();
            }
        } catch (IOException ex) {
            System.out.println("CAUGHT: " + ex);
        }
    }

    private static void copyAllFilesFromCommit(Commit switchingCommit, Commit currentCommit) {
        Set<String> currentFilesInDirectory = currentCommit.trackedFilePaths();
        Set<String> filesToPlaceInDirectory = switchingCommit.trackedFilePaths();
        for (String fileInDirectory: currentFilesInDirectory) {
            if (filesToPlaceInDirectory.contains(fileInDirectory)) {
                String fileSnapshotPath = switchingCommit.getSnapshotPath(fileInDirectory);
                copyContentsFromSourceToDest(fileSnapshotPath, fileInDirectory);
            }
        }
    }

    private static boolean dangerousCommandIsOK() { 
        System.out.println("Warning: The command you entered may alter the files in your working"
            + "directory. Uncommitted changes may be lost. Are you sure you want to continue?"
            + " (yes/no)");
 
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String yesNo = null;

        try {
            yesNo = br.readLine();
        } catch (IOException ex) {
            System.out.println("CAUGHT: " + ex);
        }
        boolean saidYes = yesNo.equals("yes");
        if (!saidYes) {
            System.out.println("Did not type 'yes', so aborting.");
        }
        return saidYes;
    }

    private static Commit getSplitPointFromBranches(Branch firstBranch, Branch secondBranch, 
            Commit startingCommit) {
        HashSet<Integer> commitIDs = new HashSet<Integer>();
        Commit firstPointer = firstBranch.head();
        Commit secondPointer = secondBranch.head();
        while (firstPointer != startingCommit) {
            commitIDs.add((Integer) firstPointer.id());
            firstPointer = firstPointer.parent();
        }
        while (secondPointer != startingCommit) {
            if (commitIDs.contains((Integer) secondPointer.id())) {
                return secondPointer;
            }
            secondPointer = secondPointer.parent();
        }
        return null;
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
            System.out.println("A gitlet version control system already exists "
                + "in the current directory.");
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
                if (fileContentsEquals(filePath, previousPath)) {
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
            Branch currentBranch = currentBranchMap.currentBranch();

            int commitID = currentBranchMap.totalNumberCommits();
            Commit newlyCreatedCommit = new Commit(commitID, commitMessage, currentBranch.head(), 
                currentStage, SNAPSHOT_DIRECTORY_PATH);

            currentBranchMap.addCommitToMapOfBranches(newlyCreatedCommit);
            currentBranch.setHead(newlyCreatedCommit);

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
            printLogInfo(pointer);
            pointer = pointer.parent();
        }
    }

    private static void printLogInfo(Commit pointer) {
        System.out.println("====\nCommit " + pointer.id() + ".\n" + pointer.date() 
            + "\n" + pointer.message() + "\n");
    }

    private static void globalLogCommand() {
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        printCommitAndChildren(currentBranchMap.startingCommit());
    }

    private static void printCommitAndChildren(Commit pointer) {
        if (pointer != null) {
            for (Commit child: pointer.children()) {
                printCommitAndChildren(child);
            }            
            printLogInfo(pointer);        
        }
    }

    private static void findCommand(String message) {
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
            copyContentsFromSourceToDest(fileSnapshotPath, fileOrBranch);
        } else if (fileOrBranch.equals(currentBranch.name())) {
            System.out.println("No need to checkout the current branch.");
        } else if (currentBranchMap.containsKey(fileOrBranch) 
            && currentBranchMap.get(fileOrBranch).isActive()) {

            Commit currentHead = currentBranchMap.currentBranch().head();
            Commit headToSwitchTo = currentBranchMap.get(fileOrBranch).head();
            copyAllFilesFromCommit(headToSwitchTo, currentHead);
            currentBranchMap.setCurrentBranch(fileOrBranch);
            writeToBranchMapFile(currentBranchMap);
        } else {
            System.out.println("File does not exist in the most recent commit,"
                + " or no such branch exists.");
        }
    }    

    private static void checkoutTwoArgsCommand(String commitID, String fileName) {
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        if (currentBranchMap.commitIDExists(commitID)) {
            Commit commitToCheck = currentBranchMap.commitForID(commitID);
            if (commitToCheck.fileHasBeenCommitted(fileName)) {
                String fileSnapshotPath = commitToCheck.getSnapshotPath(fileName);
                copyContentsFromSourceToDest(fileSnapshotPath, fileName);
            } else {
                System.out.println("File does not exist in that commit.");
            }
        } else {
            System.out.println("No commit with that id exists.");
        }
    }

    private static void branchCommand(String branchName) {
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        if (currentBranchMap.containsKey(branchName) 
            && currentBranchMap.get(branchName).isActive()) {

            System.out.println("A branch with that name already exists.");
        } else {
            Branch newlyCreatedBranch = new Branch(currentBranchMap.currentBranch(), branchName);
            currentBranchMap.put(branchName, newlyCreatedBranch);
            writeToBranchMapFile(currentBranchMap);
        }
    }

    private static void removeBranchCommand(String branchName) {
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        if (currentBranchMap.currentBranch().name().equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
        } else if (currentBranchMap.containsKey(branchName) 
            && currentBranchMap.get(branchName).isActive()) {

            currentBranchMap.get(branchName).deactivate();
            writeToBranchMapFile(currentBranchMap);
        } else {
            System.out.println("A branch with that name does not exist.");
        }
    }

    private static void resetCommand(String commitID) {
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        if (currentBranchMap.commitIDExists(commitID)) {
            Commit currentHead = currentBranchMap.currentBranch().head();
            Commit headToSwitchTo = currentBranchMap.commitForID(commitID);
            copyAllFilesFromCommit(headToSwitchTo, currentHead);
            currentBranchMap.currentBranch().setHead(headToSwitchTo);
            writeToBranchMapFile(currentBranchMap);
        } else {
            System.out.println("No commit with that id exists.");
        }
    }

    private static void mergeCommand(String branchName) {
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        if (currentBranchMap.currentBranch().name().equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
        } else if (currentBranchMap.containsKey(branchName) 
            && currentBranchMap.get(branchName).isActive()) {

            Commit currentHead = currentBranchMap.currentBranch().head();
            Commit branchHead = currentBranchMap.get(branchName).head();
            Commit splitPoint = getSplitPointFromBranches(currentBranchMap.currentBranch(), 
                currentBranchMap.get(branchName), currentBranchMap.startingCommit());

            Set<String> splitPointFiles = splitPoint.trackedFilePaths();
            for (String fileInSplit: splitPointFiles) {
                String currentHeadFileSnapshotPath = currentHead.getSnapshotPath(fileInSplit);
                String branchHeadFileSnapshotPath = branchHead.getSnapshotPath(fileInSplit);
                String splitHeadFileSnapshotPath = splitPoint.getSnapshotPath(fileInSplit);

                boolean currentFileModified = !fileContentsEquals(currentHeadFileSnapshotPath, 
                    splitHeadFileSnapshotPath);
                boolean branchFileModified = !fileContentsEquals(branchHeadFileSnapshotPath, 
                    splitHeadFileSnapshotPath);

                if (currentFileModified && branchFileModified) {
                    String conflictedFilePath = fileInSplit + ".conflicted";
                    copyContentsFromSourceToDest(branchHeadFileSnapshotPath, conflictedFilePath);
                } else if (branchFileModified) {
                    copyContentsFromSourceToDest(branchHeadFileSnapshotPath, fileInSplit);
                }
            }
            writeToBranchMapFile(currentBranchMap);
        } else {
            System.out.println("A branch with that name does not exist.");
        }
    }

    private static void rebaseCommand(String branchName) {
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        if (currentBranchMap.currentBranch().name().equals(branchName)) {
            System.out.println("Cannot rebase a branch onto itself.");
        } else if (currentBranchMap.containsKey(branchName) 
            && currentBranchMap.get(branchName).isActive()) {

            Commit currentHead = currentBranchMap.currentBranch().head();
            Commit branchHead = currentBranchMap.get(branchName).head();
            Commit splitPoint = getSplitPointFromBranches(currentBranchMap.currentBranch(), 
                currentBranchMap.get(branchName), currentBranchMap.startingCommit());

            Commit historyPointer = currentHead;
            while (historyPointer != null) {
                if (historyPointer == branchHead) {
                    System.out.println("Already up-to-date.");
                    return;
                }
                historyPointer = historyPointer.parent();
            }

            boolean setHead = false;
            Commit branchPointer = branchHead;
            Commit rebaseChain = null;
            Commit previousChild = null;
            while (branchPointer != splitPoint) {
                Commit parent = null;
                if (branchPointer.parent() == splitPoint) {
                    parent = currentHead;
                }
                rebaseChain = new Commit(branchPointer, parent, rebaseChain);
                if (rebaseChain != null && previousChild != null) {
                    previousChild.setParent(rebaseChain);
                }
                previousChild = rebaseChain;
                if (!setHead) {
                    currentBranchMap.get(branchName).setHead(rebaseChain);
                    setHead = true;
                }
                currentBranchMap.addCommitToMapOfBranches(rebaseChain);
                branchPointer = branchPointer.parent();
            }
            currentHead.addChild(rebaseChain);
            writeToBranchMapFile(currentBranchMap);
        } else {
            System.out.println("A branch with that name does not exist.");
        }
    }

    private static void interactiveRebaseCommand(String branchName) {
        BranchMap currentBranchMap = getBranchSetFromFilePath();
        if (currentBranchMap.currentBranch().name().equals(branchName)) {
            System.out.println("Cannot rebase a branch onto itself.");
        } else if (currentBranchMap.containsKey(branchName) 
            && currentBranchMap.get(branchName).isActive()) {

            Commit currentHead = currentBranchMap.currentBranch().head();
            Commit branchHead = currentBranchMap.get(branchName).head();
            Commit splitPoint = getSplitPointFromBranches(currentBranchMap.currentBranch(), 
                currentBranchMap.get(branchName), currentBranchMap.startingCommit());

            Commit historyPointer = currentHead;
            while (historyPointer != null) {
                if (historyPointer == branchHead) {
                    System.out.println("Already up-to-date.");
                    return;
                }
                historyPointer = historyPointer.parent();
            }

            BufferedReader responseReader = new BufferedReader(new InputStreamReader(System.in));
            boolean setHead = false;
            Commit branchPointer = branchHead;
            Commit rebaseChain = null;
            Commit previousChild = null;
            while (branchPointer != splitPoint) {
                System.out.println("Currently replaying:");
                printLogInfo(branchPointer);

                String response = "";
                while (!response.equals("c") && !response.equals("s") && !response.equals("m")) {
                    System.out.println("Would you like to (c)ontinue, (s)kip this commit,"
                        + " or change this commit's (m)essage?");
                    try {
                        response = responseReader.readLine();
                    } catch (IOException ex) {
                        System.out.println("CAUGHT: " + ex);
                    }
                }

                boolean addCommit = true;
                String newMessage = null;
                if (response.equals("s")) {
                    addCommit = false;
                } else if (response.equals("m")) {
                    System.out.println("Please enter a new message for this commit.");
                    try {
                        newMessage = responseReader.readLine();
                    } catch (IOException ex) {
                        System.out.println("CAUGHT: " + ex);
                    }
                }

                if (addCommit) {
                    rebaseChain = new Commit(branchPointer, null, rebaseChain);
                    if (rebaseChain != null && previousChild != null) {
                        previousChild.setParent(rebaseChain);
                    }
                    previousChild = rebaseChain;
                    if (!setHead) {
                        currentBranchMap.get(branchName).setHead(rebaseChain);
                        setHead = true;
                    }
                    if (newMessage != null) {
                        rebaseChain.setMessage(newMessage);
                    }
                    currentBranchMap.addCommitToMapOfBranches(rebaseChain);
                }
                branchPointer = branchPointer.parent();
            }
            if (rebaseChain != null) {
                rebaseChain.setParent(currentHead);
                currentHead.addChild(rebaseChain);
            }
            writeToBranchMapFile(currentBranchMap);
        } else {
            System.out.println("A branch with that name does not exist.");
        }
    }
}
