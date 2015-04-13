import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

/**
 * Class that provides JUnit tests for Gitlet, as well as a couple of utility
 * methods.
 * 
 * @author Joseph Moghadam 
 * @editor Andre Askarinam
 * 
 *         Some code adapted from StackOverflow:
 * 
 *         http://stackoverflow.com/questions
 *         /779519/delete-files-recursively-in-java
 * 
 *         http://stackoverflow.com/questions/326390/how-to-create-a-java-string
 *         -from-the-contents-of-a-file
 * 
 *         http://stackoverflow.com/questions/1119385/junit-test-for-system-out-
 *         println
 * 
 */
public class GitletCodeTests {
    private static final String GITLET_DIR = ".gitlet/";
    private static final String TESTING_DIR = "test_files/";

    /* matches either unix/mac or windows line separators */
    private static final String LINE_SEPARATOR = "\r\n|[\r\n]";

    /**
     * Deletes existing gitlet system, resets the folder that stores files used
     * in testing.
     * 
     * This method runs before every @Test method. This is important to enforce
     * that all tests are independent and do not interact with one another.
     */
    @Before
    public void setUp() {
        File f = new File(GITLET_DIR);
        if (f.exists()) {
            recursiveDelete(f);
        }
        f = new File(TESTING_DIR);
        if (f.exists()) {
            recursiveDelete(f);
        }
        f.mkdirs();
    }

    /**
     * Tests that init creates a .gitlet directory. Does NOT test that init
     * creates an initial commit, which is the other functionality of init.
     */
    @Test
    public void testBasicInitialize() {
        gitlet("init");
        File f = new File(GITLET_DIR);
        assertTrue(f.exists());
    }

    /**
     * Tests that checking out a file name will restore the version of the file
     * from the previous commit. Involves init, add, commit, and checkout.
     */
    @Test
    public void testBasicCheckout() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");
        writeFile(wugFileName, "This is not a wug.");
        gitlet("checkout", wugFileName);
        assertEquals(wugText, getText(wugFileName));
    }

    /**
     * Tests that log prints out commit messages in the right order. Involves
     * init, add, commit, and log.
     */
    @Test
    public void testBasicLog() {
        gitlet("init");
        String commitMessage1 = "initial commit";

        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("add", wugFileName);
        String commitMessage2 = "added wug";
        gitlet("commit", commitMessage2);

        String logContent = gitlet("log");
        assertArrayEquals(new String[] { commitMessage2, commitMessage1 },
                extractCommitMessages(logContent));
    }

    @Test
    public void testInitAndAdd() {
        String gitletExistsWarning = "A gitlet version control system already exists in the current directory.\n";
        String addNonexistentfileWarning = "File does not exist.\n";
        String noModificationWarning = "File has not been modified since the last commit.\n";
        String noCommitChangesWarning = "No changes added to the commit.\n";
        String noCommitMessageWarning= "Please enter a commit message.\n";

        String commitMessage1 = "initial commit";
        String commitMessage2 = "added wug";
        String commitMessage3 = "this should fail";

        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";

        gitlet("init");

        createFile(wugFileName, wugText);
        gitlet("add", wugFileName);

        String receivedWarning = gitlet("init");
        assertEquals(gitletExistsWarning, receivedWarning);

        receivedWarning = gitlet("add", "nonExistentFilename.txt");
        assertEquals(addNonexistentfileWarning, receivedWarning);

        gitlet("commit", commitMessage2);

        receivedWarning = gitlet("add", wugFileName);
        assertEquals(noModificationWarning, receivedWarning);

        receivedWarning = gitlet("commit");
        assertEquals(noCommitMessageWarning, receivedWarning);

        receivedWarning = gitlet("commit", commitMessage3);
        assertEquals(noCommitChangesWarning, receivedWarning);

        String logContent = gitlet("log");
        assertArrayEquals(new String[] { commitMessage2, commitMessage1 },
                extractCommitMessages(logContent));
    }

    @Test
    public void testRemoveMethod() {
        String noReasonToMoveWarning = "No reason to remove the file.\n";
        String empty = "";

        String commitMessage2 = "added wug";
        String commitMessage3 = "this should fail";

        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";

        gitlet("init");

        createFile(wugFileName, wugText);

        String receivedWarning = gitlet("rm", wugFileName);
        assertEquals(noReasonToMoveWarning, receivedWarning);

        gitlet("add", wugFileName);

        receivedWarning = gitlet("rm", wugFileName);
        assertEquals(empty, receivedWarning);

        gitlet("add", wugFileName);

        gitlet("add", "nonExistentFilename.txt");

        receivedWarning = gitlet("rm", "nonExistentFilename.txt");
        assertEquals(noReasonToMoveWarning, receivedWarning);

        gitlet("commit", commitMessage2);

        receivedWarning = gitlet("rm", wugFileName);
        assertEquals(empty, receivedWarning);

        receivedWarning = gitlet("rm", wugFileName);
        assertEquals(empty, receivedWarning);
    }

    @Test
    public void testCheckoutCommitsChange() {
        String dangerousWarning = "Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue? (yes/no)\n";

        String fileOrBranchDoesntExistWarning = "File does not exist in the most recent commit, or no such branch exists.\n";
        String firstWugText = "This is a wug.1";
        String secondWugText = "This is a newer wug.\nLike omg.2";
        String thirdWugText = "This is a brand new wug.\nLike no way\nHahaa3";  
        String fourthWugTest = "This is an even newer new wug.\n#hello\nYoungMulah babayyy\n\n\nSinceDay4";

        String wugFileName = TESTING_DIR + "wug.txt";

        String commitMessage1 = "First";
        String commitMessage2 = "Second";
        String commitMessage3 = "Third";

        gitlet("init");
        createFile(wugFileName, firstWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage1);
        assertEquals(firstWugText, getText(wugFileName));

        String receivedWarning = gitlet("checkout", "nonExistentFile.txt");
        assertEquals(dangerousWarning + fileOrBranchDoesntExistWarning, receivedWarning);

        writeFile(wugFileName, secondWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage2);
        assertEquals(secondWugText, getText(wugFileName));

        writeFile(wugFileName, thirdWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage3);
        assertEquals(thirdWugText, getText(wugFileName));

        writeFile(wugFileName, fourthWugTest);
        assertEquals(fourthWugTest, getText(wugFileName));

        gitlet("checkout", wugFileName);
        assertEquals(thirdWugText, getText(wugFileName));

        writeFile(wugFileName, fourthWugTest);
        assertEquals(fourthWugTest, getText(wugFileName));

        gitlet("checkout", "1", wugFileName);
        assertEquals(firstWugText, getText(wugFileName));

        gitlet("checkout", "2", wugFileName);
        assertEquals(secondWugText, getText(wugFileName));

        gitlet("checkout", "3", wugFileName);
        assertEquals(thirdWugText, getText(wugFileName));

        receivedWarning = gitlet("checkout", "nonExistentFile.txt");
        assertEquals(dangerousWarning + fileOrBranchDoesntExistWarning, receivedWarning);
    }

    @Test
    public void testCheckoutBranchesChange() {
        String dangerousWarning = "Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue? (yes/no)\n";

        String fileOrBranchDoesntExistWarning = "File does not exist in the most recent commit, or no such branch exists.\n";
        String commitIDNonexistentWarning = "No commit with that id exists.\n";
        String nonexistentFileInCommitWarning = "File does not exist in that commit.\n";
        String onCurrentBranchWarning = "No need to checkout the current branch.\n";

        String firstWugText = "This is a wug.1";
        String flowerWugText = "This is a flower wug.\nFlowers.2";
        String masterWugText = "This is a master new wug.\nLike no way\nMaaster3";  
        String secondWugText = "Basic ass wug.4";

        String flowerText = "I am a flower.";
        String masterText = "I am the king\nMaster";

        String wugFileName = TESTING_DIR + "wug.txt";
        String flowerFileName = TESTING_DIR + "flower.txt";
        String masterFileName = TESTING_DIR + "master.txt";

        String commitMessage1 = "First";
        String commitMessage2 = "SecondFlower";
        String commitMessage3 = "ThirdMaster";
        String commitMessage4 = "FourthMaster";

        gitlet("init");
        createFile(wugFileName, firstWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage1);
        assertEquals(firstWugText, getText(wugFileName));

        gitlet("branch", "flower");
        gitlet("checkout", "flower");
        assertEquals(firstWugText, getText(wugFileName));

        String receivedWarning = gitlet("checkout", "nonExistentFile.txt");
        assertEquals(dangerousWarning + fileOrBranchDoesntExistWarning, receivedWarning);

        receivedWarning = gitlet("checkout", "nonExistentBranch");
        assertEquals(dangerousWarning + fileOrBranchDoesntExistWarning, receivedWarning);

        receivedWarning = gitlet("checkout", "flower");
        assertEquals(dangerousWarning + onCurrentBranchWarning, receivedWarning);

        writeFile(wugFileName, flowerWugText);
        createFile(flowerFileName, flowerText);
        gitlet("add", wugFileName);
        gitlet("add", flowerFileName);
        gitlet("commit", commitMessage2);
        assertEquals(flowerWugText, getText(wugFileName));
        assertEquals(flowerText, getText(flowerFileName));

        gitlet("checkout", "master");
        assertEquals(firstWugText, getText(wugFileName));

        writeFile(wugFileName, masterWugText);
        createFile(masterFileName, masterText);
        gitlet("add", wugFileName);
        gitlet("add", masterFileName);
        gitlet("commit", commitMessage3);
        assertEquals(masterWugText, getText(wugFileName));
        assertEquals(masterText, getText(masterFileName));

        receivedWarning = gitlet("checkout", "nonExistentFile.txt");
        assertEquals(dangerousWarning + fileOrBranchDoesntExistWarning, receivedWarning);

        receivedWarning = gitlet("checkout", "nonExistentBranch");
        assertEquals(dangerousWarning + fileOrBranchDoesntExistWarning, receivedWarning);

        receivedWarning = gitlet("checkout", "master");
        assertEquals(dangerousWarning + onCurrentBranchWarning, receivedWarning);

        receivedWarning = gitlet("checkout", "2", "nonExistentFile.txt");
        assertEquals(dangerousWarning + nonexistentFileInCommitWarning, receivedWarning);

        receivedWarning = gitlet("checkout", "17", wugFileName);
        assertEquals(dangerousWarning + commitIDNonexistentWarning, receivedWarning);

        gitlet("checkout", "flower");
        assertEquals(flowerWugText, getText(wugFileName));

        gitlet("checkout", "master");
        assertEquals(masterWugText, getText(wugFileName));

        writeFile(wugFileName, secondWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage4);
        assertEquals(secondWugText, getText(wugFileName));

        gitlet("checkout", "2", wugFileName);
        assertEquals(flowerWugText, getText(wugFileName));

        receivedWarning = gitlet("checkout", "nonExistentFile.txt");
        assertEquals(dangerousWarning + fileOrBranchDoesntExistWarning, receivedWarning);

        receivedWarning = gitlet("checkout", "nonExistentBranch");
        assertEquals(dangerousWarning + fileOrBranchDoesntExistWarning, receivedWarning);

        receivedWarning = gitlet("checkout", "master");
        assertEquals(dangerousWarning + onCurrentBranchWarning, receivedWarning);

        receivedWarning = gitlet("checkout", "2", "nonExistentFile.txt");
        assertEquals(dangerousWarning + nonexistentFileInCommitWarning, receivedWarning);

        receivedWarning = gitlet("checkout", "17", wugFileName);
        assertEquals(dangerousWarning + commitIDNonexistentWarning, receivedWarning);
    }

    @Test
    public void testLogAndGlobalLog() {
        String firstWugText = "This is a wug.1";
        String flowerWugText = "This is a flower wug.\nFlowers.2";
        String masterWugText = "This is a master new wug.\nLike no way\nMaaster3";  
        String secondWugText = "Basic ass wug.4";

        String flowerText = "I am a flower.";
        String masterText = "I am the king\nMaster";

        String wugFileName = TESTING_DIR + "wug.txt";
        String flowerFileName = TESTING_DIR + "flower.txt";
        String masterFileName = TESTING_DIR + "master.txt";

        String initialCommit = "initial commit";
        String commitMessage1 = "First";
        String commitMessage2 = "SecondFlower";
        String commitMessage3 = "ThirdMaster";
        String commitMessage4 = "FourthMaster";

        gitlet("init");
        createFile(wugFileName, firstWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage1);
        assertEquals(firstWugText, getText(wugFileName));

        gitlet("branch", "flower");
        gitlet("checkout", "flower");
        assertEquals(firstWugText, getText(wugFileName));

        writeFile(wugFileName, flowerWugText);
        createFile(flowerFileName, flowerText);
        gitlet("add", wugFileName);
        gitlet("add", flowerFileName);
        gitlet("commit", commitMessage2);
        assertEquals(flowerWugText, getText(wugFileName));
        assertEquals(flowerText, getText(flowerFileName));

        String logContent = gitlet("log");
        assertArrayEquals(new String[] { commitMessage2, commitMessage1, initialCommit }, extractCommitMessages(logContent));

        logContent = gitlet("global-log");
        assertArrayEquals(new String[] { commitMessage2, commitMessage1, initialCommit }, extractCommitMessages(logContent));

        gitlet("checkout", "master");
        assertEquals(firstWugText, getText(wugFileName));

        writeFile(wugFileName, masterWugText);
        createFile(masterFileName, masterText);
        gitlet("add", wugFileName);
        gitlet("add", masterFileName);
        gitlet("commit", commitMessage3);
        assertEquals(masterWugText, getText(wugFileName));
        assertEquals(masterText, getText(masterFileName));

        logContent = gitlet("log");
        assertArrayEquals(new String[] { commitMessage3, commitMessage1, initialCommit }, extractCommitMessages(logContent));

        logContent = gitlet("global-log");
        assertArrayEquals(new String[] { commitMessage3, commitMessage2, commitMessage1, initialCommit }, extractCommitMessages(logContent));

        gitlet("checkout", "flower");
        assertEquals(flowerWugText, getText(wugFileName));

        logContent = gitlet("log");
        assertArrayEquals(new String[] { commitMessage2, commitMessage1, initialCommit }, extractCommitMessages(logContent));

        logContent = gitlet("global-log");
        assertArrayEquals(new String[] { commitMessage2, commitMessage3, commitMessage1, initialCommit }, extractCommitMessages(logContent));
    }

    @Test
    public void testFind() {
        String findWarning = "Found no commit with that message.\n";

        String firstWugText = "This is a wug.1";
        String flowerWugText = "This is a flower wug.\nFlowers.2";
        String masterWugText = "This is a master new wug.\nLike no way\nMaaster3";  
        String secondWugText = "Basic ass wug.4";

        String flowerText = "I am a flower.";
        String masterText = "I am the king\nMaster";

        String wugFileName = TESTING_DIR + "wug.txt";
        String flowerFileName = TESTING_DIR + "flower.txt";
        String masterFileName = TESTING_DIR + "master.txt";

        String commitMessage1 = "First";
        String commitMessage2 = "Second";
        String commitMessage3 = "Second";
        String commitMessage4 = "Third";

        gitlet("init");
        createFile(wugFileName, firstWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage1);
        assertEquals(firstWugText, getText(wugFileName));

        gitlet("branch", "flower");
        gitlet("checkout", "flower");
        assertEquals(firstWugText, getText(wugFileName));

        writeFile(wugFileName, flowerWugText);
        createFile(flowerFileName, flowerText);
        gitlet("add", wugFileName);
        gitlet("add", flowerFileName);
        gitlet("commit", commitMessage2);
        assertEquals(flowerWugText, getText(wugFileName));
        assertEquals(flowerText, getText(flowerFileName));

        String findContent = gitlet("find", commitMessage1);
        assertEquals(findContent, "1\n");

        findContent = gitlet("find", commitMessage2);
        assertEquals(findContent, "2\n");

        findContent = gitlet("find", commitMessage3);
        assertEquals(findContent, "2\n");

        findContent = gitlet("find", commitMessage4);
        assertEquals(findContent, findWarning);

        gitlet("checkout", "master");
        assertEquals(firstWugText, getText(wugFileName));

        writeFile(wugFileName, masterWugText);
        createFile(masterFileName, masterText);
        gitlet("add", wugFileName);
        gitlet("add", masterFileName);
        gitlet("commit", commitMessage3);
        assertEquals(masterWugText, getText(wugFileName));
        assertEquals(masterText, getText(masterFileName));

        findContent = gitlet("find", commitMessage1);
        assertEquals(findContent, "1\n");

        findContent = gitlet("find", commitMessage2);
        assertEquals(findContent, "2\n3\n");

        findContent = gitlet("find", commitMessage3);
        assertEquals(findContent, "2\n3\n");

        findContent = gitlet("find", commitMessage4);
        assertEquals(findContent, findWarning);
    }

    @Test
    public void testBranch() {
        String branchNameExistsWarning = "A branch with that name already exists.\n";
        String empty = "";

        String firstWugText = "This is a wug.1";
        String flowerWugText = "This is a flower wug.\nFlowers.2";
        String masterWugText = "This is a master new wug.\nLike no way\nMaaster3";  
        String secondWugText = "Basic ass wug.4";

        String flowerText = "I am a flower.";

        String wugFileName = TESTING_DIR + "wug.txt";
        String flowerFileName = TESTING_DIR + "flower.txt";

        String commitMessage1 = "First";
        String commitMessage2 = "Second";

        gitlet("init");
        createFile(wugFileName, firstWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage1);
        assertEquals(firstWugText, getText(wugFileName));

        String receivedWarning = gitlet("branch", "master");
        assertEquals(branchNameExistsWarning, receivedWarning);

        receivedWarning = gitlet("branch", "flower");
        assertEquals(empty, receivedWarning);

        gitlet("checkout", "flower");
        assertEquals(firstWugText, getText(wugFileName));

        writeFile(wugFileName, flowerWugText);
        createFile(flowerFileName, flowerText);
        gitlet("add", wugFileName);
        gitlet("add", flowerFileName);
        gitlet("commit", commitMessage2);
        assertEquals(flowerWugText, getText(wugFileName));
        assertEquals(flowerText, getText(flowerFileName));

        receivedWarning = gitlet("branch", "master");
        assertEquals(branchNameExistsWarning, receivedWarning);

        receivedWarning = gitlet("branch", "flower");
        assertEquals(branchNameExistsWarning, receivedWarning);
    }

    @Test
    public void testRemoveBranch() {
        String dangerousWarning = "Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue? (yes/no)\n";
        String empty = "";

        String fileOrBranchDoesntExistWarning = "File does not exist in the most recent commit, or no such branch exists.\n";
        String cantRemoveCurrentBranchWarning = "Cannot remove the current branch.\n";
        String nonExistentBranchToRemoveWarning = "A branch with that name does not exist.\n";

        String firstWugText = "This is a wug.1";
        String flowerWugText = "This is a flower wug.\nFlowers.2";
        String masterWugText = "This is a master new wug.\nLike no way\nMaaster3";  
        String secondWugText = "Basic ass wug.4";

        String flowerText = "I am a flower.";
        String masterText = "I am the king\nMaster";

        String wugFileName = TESTING_DIR + "wug.txt";
        String flowerFileName = TESTING_DIR + "flower.txt";
        String masterFileName = TESTING_DIR + "master.txt";

        String commitMessage1 = "First";
        String commitMessage2 = "SecondFlower";
        String commitMessage3 = "ThirdMaster";
        String commitMessage4 = "FourthMaster";

        gitlet("init");
        createFile(wugFileName, firstWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage1);
        assertEquals(firstWugText, getText(wugFileName));

        gitlet("branch", "flower");
        gitlet("checkout", "flower");
        assertEquals(firstWugText, getText(wugFileName));

        writeFile(wugFileName, flowerWugText);
        createFile(flowerFileName, flowerText);
        gitlet("add", wugFileName);
        gitlet("add", flowerFileName);
        gitlet("commit", commitMessage2);
        assertEquals(flowerWugText, getText(wugFileName));
        assertEquals(flowerText, getText(flowerFileName));

        gitlet("checkout", "master");
        assertEquals(firstWugText, getText(wugFileName));

        writeFile(wugFileName, masterWugText);
        createFile(masterFileName, masterText);
        gitlet("add", wugFileName);
        gitlet("add", masterFileName);
        gitlet("commit", commitMessage3);
        assertEquals(masterWugText, getText(wugFileName));
        assertEquals(masterText, getText(masterFileName));

        gitlet("checkout", "flower");
        assertEquals(flowerWugText, getText(wugFileName));

        gitlet("checkout", "master");
        assertEquals(masterWugText, getText(wugFileName));

        String receivedWarning = gitlet("rm-branch", "nonExistentBranch");
        assertEquals(nonExistentBranchToRemoveWarning, receivedWarning);

        receivedWarning = gitlet("rm-branch", "flower");
        assertEquals(empty, receivedWarning);

        receivedWarning = gitlet("rm-branch", "flower");
        assertEquals(nonExistentBranchToRemoveWarning, receivedWarning);

        receivedWarning = gitlet("checkout", "flower");
        assertEquals(dangerousWarning + fileOrBranchDoesntExistWarning, receivedWarning);

        writeFile(wugFileName, secondWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage4);
        assertEquals(secondWugText, getText(wugFileName));

        gitlet("checkout", "2", wugFileName);
        assertEquals(flowerWugText, getText(wugFileName));

        String masterLogContent = gitlet("log");

        receivedWarning = gitlet("branch", "flower");
        assertEquals(empty, receivedWarning);

        receivedWarning = gitlet("checkout", "flower");
        assertEquals(dangerousWarning, receivedWarning);

        String flowerLogContent = gitlet("log");
        assertEquals(masterLogContent, flowerLogContent);

        writeFile(wugFileName, secondWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage4);
        assertEquals(secondWugText, getText(wugFileName));

        gitlet("checkout", "2", wugFileName);
        assertEquals(flowerWugText, getText(wugFileName));
    }

    @Test
    public void testReset() {
        String dangerousWarning = "Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue? (yes/no)\n";

        String nonexistentIDWarning = "No commit with that id exists.\n";

        String firstWugText = "This is a wug.1";
        String flowerWugText = "This is a flower wug.\nFlowers.2";
        String masterWugText = "This is a master new wug.\nLike no way\nMaaster3";  
        String secondWugText = "Basic ass wug.4";

        String flowerText = "I am a flower.";
        String masterText = "I am the king\nMaster";

        String wugFileName = TESTING_DIR + "wug.txt";
        String flowerFileName = TESTING_DIR + "flower.txt";
        String masterFileName = TESTING_DIR + "master.txt";

        String commitMessage1 = "First";
        String commitMessage2 = "SecondFlower";
        String commitMessage3 = "ThirdMaster";
        String commitMessage4 = "FourthMaster";

        gitlet("init");
        createFile(wugFileName, firstWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage1);
        assertEquals(firstWugText, getText(wugFileName));

        gitlet("branch", "flower");
        gitlet("checkout", "flower");
        assertEquals(firstWugText, getText(wugFileName));

        String receivedWarning = gitlet("reset", "nonExistentID");
        assertEquals(dangerousWarning + nonexistentIDWarning, receivedWarning);

        writeFile(wugFileName, flowerWugText);
        createFile(flowerFileName, flowerText);
        gitlet("add", wugFileName);
        gitlet("add", flowerFileName);
        gitlet("commit", commitMessage2);
        assertEquals(flowerWugText, getText(wugFileName));
        assertEquals(flowerText, getText(flowerFileName));

        gitlet("checkout", "master");
        assertEquals(firstWugText, getText(wugFileName));

        writeFile(wugFileName, masterWugText);
        createFile(masterFileName, masterText);
        gitlet("add", wugFileName);
        gitlet("add", masterFileName);
        gitlet("commit", commitMessage3);
        assertEquals(masterWugText, getText(wugFileName));
        assertEquals(masterText, getText(masterFileName));

        receivedWarning = gitlet("reset", "nonExistentID");
        assertEquals(dangerousWarning + nonexistentIDWarning, receivedWarning);

        writeFile(wugFileName, secondWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage4);
        assertEquals(secondWugText, getText(wugFileName));

        gitlet("reset", "3");
        assertEquals(masterWugText, getText(wugFileName));
        assertEquals(masterText, getText(masterFileName));

        gitlet("reset", "4");
        assertEquals(secondWugText, getText(wugFileName));
        assertEquals(masterText, getText(masterFileName));

        gitlet("checkout", "2", wugFileName);
        assertEquals(flowerWugText, getText(wugFileName));
    }

    @Test
    public void testMerge() {
        String dangerousWarning = "Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue? (yes/no)\n";

        String nonexistentBranchWarning = "A branch with that name does not exist.\n";
        String sameBranchWarning = "Cannot merge a branch with itself.\n";

        String firstWugText = "This is a wug.1";
        String secondWugText = "This is a flower wug.\nFlowers.2";
        String thirdWugText = "This is a master new wug.\nLike no way\nMaaster3";  
        String fourthWugText = "Basic ass wug.4";

        String firstMasterText = "I am the king\nMaster";
        String secondMasterText = "I am the second master. Shahanshah";
        String thirdMasterText = "I am supreme king of all, said Jehosephat.";

        String firstFlowerText = "I am a flower.";
        String secondFlowerText = "I am a new flower.";
        String thirdFlowerText = "I am a stupid boring ass freaking flower.\nJK I love flowers.";

        String wugFileName = TESTING_DIR + "wug.txt";
        String flowerFileName = TESTING_DIR + "flower.txt";
        String masterFileName = TESTING_DIR + "master.txt";

        String commitMessage1 = "First";
        String commitMessage2 = "Second";
        String commitMessage3 = "Third";
        String commitMessage4 = "FourthMaster";

        gitlet("init");

        createFile(wugFileName, firstWugText);
        createFile(flowerFileName, firstFlowerText);
        createFile(masterFileName, firstMasterText);

        gitlet("add", wugFileName);
        gitlet("add", flowerFileName);
        gitlet("add", masterFileName);
        gitlet("commit", commitMessage1);
        assertEquals(firstWugText, getText(wugFileName));
        assertEquals(firstFlowerText, getText(flowerFileName));
        assertEquals(firstMasterText, getText(masterFileName));

        gitlet("branch", "flower");
        gitlet("checkout", "flower");
        assertEquals(firstWugText, getText(wugFileName));
        assertEquals(firstFlowerText, getText(flowerFileName));
        assertEquals(firstMasterText, getText(masterFileName));

        writeFile(wugFileName, secondWugText);
        writeFile(flowerFileName, secondFlowerText);
        gitlet("add", wugFileName);
        gitlet("add", flowerFileName);
        gitlet("commit", commitMessage2);
        assertEquals(secondWugText, getText(wugFileName));
        assertEquals(secondFlowerText, getText(flowerFileName));
        assertEquals(firstMasterText, getText(masterFileName));

        String receivedWarning = gitlet("merge", "nonExistentBranch");
        assertEquals(dangerousWarning + nonexistentBranchWarning, receivedWarning);

        receivedWarning = gitlet("merge", "flower");
        assertEquals(dangerousWarning + sameBranchWarning, receivedWarning);

        writeFile(wugFileName, thirdWugText);
        writeFile(flowerFileName, thirdFlowerText);
        gitlet("add", wugFileName);
        gitlet("add", flowerFileName);
        gitlet("commit", commitMessage2);
        assertEquals(thirdWugText, getText(wugFileName));
        assertEquals(thirdFlowerText, getText(flowerFileName));
        assertEquals(firstMasterText, getText(masterFileName));

        gitlet("checkout", "master");
        assertEquals(firstWugText, getText(wugFileName));

        writeFile(wugFileName, fourthWugText);
        writeFile(masterFileName, secondMasterText);
        gitlet("add", wugFileName);
        gitlet("add", flowerFileName);
        gitlet("commit", commitMessage3);
        assertEquals(fourthWugText, getText(wugFileName));
        assertEquals(firstFlowerText, getText(flowerFileName));
        assertEquals(secondMasterText, getText(masterFileName));

        receivedWarning = gitlet("merge", "nonExistentBranch");
        assertEquals(dangerousWarning + nonexistentBranchWarning, receivedWarning);

        receivedWarning = gitlet("merge", "master");
        assertEquals(dangerousWarning + sameBranchWarning, receivedWarning);

        receivedWarning = gitlet("merge", "flower");
        assertEquals(dangerousWarning, receivedWarning);

        assertEquals(fourthWugText, getText(wugFileName));
        assertEquals(thirdFlowerText, getText(flowerFileName));
        assertEquals(secondMasterText, getText(masterFileName));
        assertTrue((new File(wugFileName + ".conflicted")).exists());
        assertEquals(thirdWugText, getText(wugFileName + ".conflicted"));
    }

    @Test
    public void testMergeCase2() {
        String dangerousWarning = "Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue? (yes/no)\n";

        String nonexistentBranchWarning = "A branch with that name does not exist.\n";
        String sameBranchWarning = "Cannot merge a branch with itself.\n";

        String firstWugText = "This is a wug.1";
        String secondWugText = "This is a flower wug.\nFlowers.2";
        String thirdWugText = "This is a master new wug.\nLike no way\nMaaster3";  
        String fourthWugText = "Basic ass wug.4";

        String firstFlowerText = "I am a flower.";
        String secondFlowerText = "I am a new flower.";
        String thirdFlowerText = "I am a stupid boring ass freaking flower.\nJK I love flowers.";
        String fourthFlowerText = "Back to the stupid flower.";

        String wugFileName = TESTING_DIR + "wug.txt";
        String flowerFileName = TESTING_DIR + "flower.txt";

        String commitMessage1 = "First";
        String commitMessage2 = "Second";
        String commitMessage3 = "Third";
        String commitMessage4 = "Fourth";

        gitlet("init");

        createFile(wugFileName, firstWugText);
        createFile(flowerFileName, firstFlowerText);

        gitlet("add", wugFileName);
        gitlet("add", flowerFileName);
        gitlet("commit", commitMessage1);
        assertEquals(firstWugText, getText(wugFileName));
        assertEquals(firstFlowerText, getText(flowerFileName));

        gitlet("branch", "flower");
        gitlet("checkout", "flower");
        assertEquals(firstWugText, getText(wugFileName));
        assertEquals(firstFlowerText, getText(flowerFileName));

        writeFile(wugFileName, secondWugText);
        writeFile(flowerFileName, secondFlowerText);
        gitlet("add", wugFileName);
        gitlet("add", flowerFileName);
        gitlet("commit", commitMessage2);
        assertEquals(secondWugText, getText(wugFileName));
        assertEquals(secondFlowerText, getText(flowerFileName));

        String receivedWarning = gitlet("merge", "nonExistentBranch");
        assertEquals(dangerousWarning + nonexistentBranchWarning, receivedWarning);

        receivedWarning = gitlet("merge", "flower");
        assertEquals(dangerousWarning + sameBranchWarning, receivedWarning);

        gitlet("checkout", "master");

        receivedWarning = gitlet("merge", "flower");
        assertEquals(dangerousWarning, receivedWarning);

        assertEquals(secondWugText, getText(wugFileName));
        assertEquals(secondFlowerText, getText(flowerFileName));

        gitlet("checkout", "flower");

        writeFile(wugFileName, thirdWugText);
        writeFile(flowerFileName, thirdFlowerText);
        gitlet("add", wugFileName);
        gitlet("add", flowerFileName);
        gitlet("commit", commitMessage3);
        assertEquals(thirdWugText, getText(wugFileName));
        assertEquals(thirdFlowerText, getText(flowerFileName));

        writeFile(wugFileName, fourthWugText);
        writeFile(flowerFileName, fourthFlowerText);
        gitlet("add", wugFileName);
        gitlet("add", flowerFileName);
        gitlet("commit", commitMessage4);
        assertEquals(fourthWugText, getText(wugFileName));
        assertEquals(fourthFlowerText, getText(flowerFileName));

        gitlet("checkout", "master");
        assertEquals(firstWugText, getText(wugFileName));

        receivedWarning = gitlet("merge", "nonExistentBranch");
        assertEquals(dangerousWarning + nonexistentBranchWarning, receivedWarning);

        receivedWarning = gitlet("merge", "master");
        assertEquals(dangerousWarning + sameBranchWarning, receivedWarning);

        receivedWarning = gitlet("merge", "flower");
        assertEquals(dangerousWarning, receivedWarning);

        assertEquals(fourthWugText, getText(wugFileName));
        assertEquals(fourthFlowerText, getText(flowerFileName));
    }

    @Test
    public void testRebase() {
        String dangerousWarning = "Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue? (yes/no)\n";

        String nonexistentBranchWarning = "A branch with that name does not exist.\n";
        String sameBranchWarning = "Cannot rebase a branch onto itself.\n";
        String alreadyRebasedWarning = "Already up-to-date.\n";

        String wugFileName = TESTING_DIR + "wug.txt";

        String initialCommit = "initial commit";
        String firstWugText = "This is a wg";
        String secondWugText = "This is a wug";
        String thirdWugText = "This is a wug."; 
        String fourthWugText = "This is a wug..";
        String fifthWugText = "This is a wug!";
        String sixthWugText = "This is a wug!!";
        String seventhWugText = "This is a wug??";

        String commitMessage1 = "First";
        String commitMessage2 = "Second";
        String commitMessage3 = "Third";
        String commitMessage4 = "Fourth";
        String commitMessage5 = "Fifth";
        String commitMessage6 = "Sixth";
        String commitMessage7 = "Seventh";

        gitlet("init");

        createFile(wugFileName, firstWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage1);
        assertEquals(firstWugText, getText(wugFileName));

        writeFile(wugFileName, secondWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage2);
        assertEquals(secondWugText, getText(wugFileName));

        gitlet("branch", "branch1");

        writeFile(wugFileName, thirdWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage3);
        assertEquals(thirdWugText, getText(wugFileName));

        writeFile(wugFileName, fourthWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage4);
        assertEquals(fourthWugText, getText(wugFileName));

        gitlet("checkout", "branch1");

        writeFile(wugFileName, fifthWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage5);
        assertEquals(fifthWugText, getText(wugFileName));

        gitlet("branch", "branch2");

        writeFile(wugFileName, sixthWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage6);
        assertEquals(sixthWugText, getText(wugFileName));

        gitlet("checkout", "branch2");

        writeFile(wugFileName, seventhWugText);
        gitlet("add", wugFileName);
        gitlet("commit", commitMessage7);
        assertEquals(seventhWugText, getText(wugFileName));

        gitlet("checkout", "master");
        assertEquals(fourthWugText, getText(wugFileName));

        String receivedWarning = gitlet("rebase", "nonExistentBranch");
        assertEquals(dangerousWarning + nonexistentBranchWarning, receivedWarning);

        receivedWarning = gitlet("rebase", "master");
        assertEquals(dangerousWarning + sameBranchWarning, receivedWarning);

        receivedWarning = gitlet("rebase", "branch1");
        assertEquals(dangerousWarning, receivedWarning);
        assertEquals(fourthWugText, getText(wugFileName));

        String logContent = gitlet("log");
        assertArrayEquals(new String[] { commitMessage4, commitMessage3, commitMessage2, commitMessage1, initialCommit }, extractCommitMessages(logContent));

        gitlet("checkout", "branch1");
        assertEquals(sixthWugText, getText(wugFileName));

        logContent = gitlet("log");
        assertArrayEquals(new String[] { commitMessage6, commitMessage5, commitMessage4, commitMessage3, commitMessage2, commitMessage1, initialCommit }, extractCommitMessages(logContent));

        receivedWarning = gitlet("rebase", "master");
        assertEquals(dangerousWarning + alreadyRebasedWarning, receivedWarning);

        gitlet("checkout", "branch2");
        assertEquals(seventhWugText, getText(wugFileName));

        logContent = gitlet("log");
        assertArrayEquals(new String[] { commitMessage7, commitMessage5, commitMessage2, commitMessage1, initialCommit }, extractCommitMessages(logContent));
    }

    /**
     * Convenience method for calling Gitlet's main. Anything that is printed
     * out during this call to main will NOT actually be printed out, but will
     * instead be returned as a string from this method.
     * 
     * Prepares a 'yes' answer on System.in so as to automatically pass through
     * dangerous commands.
     * 
     * The '...' syntax allows you to pass in an arbitrary number of String
     * arguments, which are packaged into a String[].
     */
    private static String gitlet(String... args) {
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        ByteArrayOutputStream printingResults = new ByteArrayOutputStream();
        try {
            /*
             * Below we change System.out, so that when you call
             * System.out.println(), it won't print to the screen, but will
             * instead be added to the printingResults object.
             */
            System.setOut(new PrintStream(printingResults));

            /*
             * Prepares the answer "yes" on System.In, to pretend as if a user
             * will type "yes". You won't be able to take user input during this
             * time.
             */
            String answer = "yes";
            InputStream is = new ByteArrayInputStream(answer.getBytes());
            System.setIn(is);

            /* Calls the main method using the input arguments. */
            Gitlet.main(args);

        } finally {
            /*
             * Restores System.out and System.in (So you can print normally and
             * take user input normally again).
             */
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
        return printingResults.toString();
    }

    private static String gitletForInteractiveRebase(String response, String... args) {
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        ByteArrayOutputStream printingResults = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(printingResults));

            InputStream is = new ByteArrayInputStream(response.getBytes());
            System.setIn(is);

            Gitlet.main(args);

        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
        return printingResults.toString();
    }

    /**
     * Returns the text from a standard text file (won't work with special
     * characters).
     */
    private static String getText(String fileName) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(fileName));
            return new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Creates a new file with the given fileName and gives it the text
     * fileText.
     */
    private static void createFile(String fileName, String fileText) {
        File f = new File(fileName);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeFile(fileName, fileText);
    }

    /**
     * Replaces all text in the existing file with the given text.
     */
    private static void writeFile(String fileName, String fileText) {
        FileWriter fw = null;
        try {
            File f = new File(fileName);
            fw = new FileWriter(f, false);
            fw.write(fileText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes the file and all files inside it, if it is a directory.
     */
    private static void recursiveDelete(File d) {
        if (d.isDirectory()) {
            for (File f : d.listFiles()) {
                recursiveDelete(f);
            }
        }
        d.delete();
    }

    /**
     * Returns an array of commit messages associated with what log has printed
     * out.
     */
    private static String[] extractCommitMessages(String logOutput) {
        String[] logChunks = logOutput.split("====");
        int numMessages = logChunks.length - 1;
        String[] messages = new String[numMessages];
        for (int i = 0; i < numMessages; i++) {
            // System.out.println(logChunks[i + 1]);
            String[] logLines = logChunks[i + 1].split(LINE_SEPARATOR);
            messages[i] = logLines[3];
        }
        return messages;
    }

    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(GitletCodeTests.class);
    }
}
