import java.io.File;
import java.io.OutputStream;
import java.io.ObjectOutput;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Gitlet {
    private static final String GITLET_DIRECTORY_PATH = "./.gitlet/";
    private static final String STAGE_FILE_PATH = "./.gitlet/Stage.ser";
    private static final String BRANCHES_FILE_PATH = "./.gitlet/Branches.ser";

    public static void main(String[] args) {
        String command = args[0];
        if (command.equals("init")) {
            initializeCommand();
        } else if (command.equals("add")) {
            addCommand();
        } else if (command.equals("commit")) {
            commitCommand();
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

    private static void writeToBranchesFile(BranchSet newBranchSet) {
        try {
            OutputStream file = new FileOutputStream(BRANCHES_FILE_PATH);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            try {
                output.writeObject(newBranchSet);
            }
            finally {
                output.close();
            }
        } catch (IOException ex) {

        }
    }

    // private static void writeToStageFile(Stage newStageFile) {
    //     try {
    //         OutputStream file = new FileOutputStream(STAGE_FILE_PATH);
    //         OutputStream buffer = new BufferedOutputStream(file);
    //         ObjectOutput output = new ObjectOutputStream(buffer);
    //         try {
    //             output.writeObject(newStageFile);
    //         }
    //         finally {
    //             output.close();
    //         }
    //     } catch (IOException ex) {
            
    //     }
    // }

    private static void initializeCommand() {
        File gitletDirectory = new File(GITLET_DIRECTORY_PATH);
        if (!gitletDirectory.exists()) {
            gitletDirectory.mkdir();
            BranchSet initialBranchSet = new BranchSet();
            writeToBranchesFile(initialBranchSet);
        } else {
            System.out.println("A gitlet version control system already exists in the current directory.");
        }
    }


    private static void addCommand() {

    }

    private static void commitCommand() {

    }

    private static void removeCommand() {

    }

    private static void logCommand() {

    }

    private static void globalLogCommand() {

    }

    private static void findCommand() {

    }

    private static void statusCommand() {

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