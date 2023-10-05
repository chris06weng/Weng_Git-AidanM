import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

public class CommitTest {
    private static final String TEST_TREE_SHA1 = "8c411a89ed6d846f064ed0decdba3a857f0d1667";
    private static final String TEST_PARENT_COMMIT_SHA1 = "f924e482dd33576fd0de90b6376f1671b08b5f52";
    private static final String TEST_AUTHOR = "Mr. Theiss";
    private static final String TEST_SUMMARY = "This commit is amazing";

    @Before
    public void setUp() throws Exception {
        // Create a test directory for the objects
        File objectsDir = new File("objects");
        if (!objectsDir.exists()) {
            objectsDir.mkdirs();
        }
    }

    @After
    public void tearDown() throws Exception {
        // Delete the test directory for the objects
        File objectsDir = new File("objects");
        if (objectsDir.exists()) {
            deleteDirectory(objectsDir);
        }
    }

    @Test
    public void testWriteToFile() throws Exception {
        Commit commit = new Commit(TEST_TREE_SHA1, TEST_PARENT_COMMIT_SHA1, TEST_AUTHOR, TEST_SUMMARY);
        String filePath = "objects/" + commit.generateSHA1();

        commit.writeToFile(filePath);

        File commitFile = new File(filePath);
        assertTrue(commitFile.exists() && commitFile.isFile());

        // Verify the content of the written file
        try (BufferedReader reader = new BufferedReader(new FileReader(commitFile))) {
            assertEquals(TEST_TREE_SHA1, reader.readLine());
            assertEquals(TEST_PARENT_COMMIT_SHA1, reader.readLine());
            assertEquals("", reader.readLine());
            assertEquals(TEST_AUTHOR, reader.readLine());
            assertNotNull(reader.readLine()); // Date format can vary
            assertEquals(TEST_SUMMARY, reader.readLine());
        }
    }

    @Test
    public void testGenerateSHA1() throws Exception {
        Commit commit = new Commit(TEST_TREE_SHA1, TEST_PARENT_COMMIT_SHA1, TEST_AUTHOR, TEST_SUMMARY);
        String expectedSHA1 = Blob.sha1(
                TEST_TREE_SHA1 + "\n" + TEST_PARENT_COMMIT_SHA1 + "\n\n" + TEST_AUTHOR + "\n" + commit.generateDate()
                        + "\n" + TEST_SUMMARY);

        assertEquals(expectedSHA1, commit.generateSHA1());
    }

    @Test
    public void testGetDate() throws Exception {
        Commit commit = new Commit(TEST_TREE_SHA1, TEST_PARENT_COMMIT_SHA1, TEST_AUTHOR, TEST_SUMMARY);
        assertNotNull(commit.getDate());
    }

    @Test
    public void testCreateTree() throws Exception {
        Commit commit = new Commit(TEST_TREE_SHA1, TEST_PARENT_COMMIT_SHA1, TEST_AUTHOR, TEST_SUMMARY);
        String treeSHA1 = commit.createTree("test_tree");
        assertNotNull(treeSHA1);
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}