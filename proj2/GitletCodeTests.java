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

public class GitletCodeTests {
	private static final String GITLET_DIRECTORY_PATH = ".gitlet/";
    private static final String STAGE_FILE_PATH = ".gitlet/Stage.ser";
    private static final String BRANCHES_FILE_PATH = ".gitlet/Branches.ser";
    private static final String TESTING_DIRECTORY_PATH = "test_files/";

    // private static final String LINE_SEPARATOR = "\r\n|[\r\n]";

    @Before
    public void setUp() {
        File f = new File(GITLET_DIRECTORY_PATH);
        if (f.exists()) {
            recursiveDelete(f);
        }
        f = new File(TESTING_DIRECTORY_PATH);
        if (f.exists()) {
            recursiveDelete(f);
        }
        f.mkdirs();
    }

    private static void recursiveDelete(File d) {
        if (d.isDirectory()) {
            for (File f : d.listFiles()) {
                recursiveDelete(f);
            }
        }
        d.delete();
    }

    @Test
    public void initCreatesFirstCommit() {

    }

	public static void main(String[] args) {
        jh61b.junit.textui.runClasses(GitletExtensiveTesting.class);
    }
}