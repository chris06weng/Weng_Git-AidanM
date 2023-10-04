import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Scanner;

public class TreeTest {
    private static final String TEST_FILE = "test.txt";
    private static final String TEST_FILE_CONTENT = "This is a test file content.";

    @Before
    public void setUp() throws Exception {
        // Create the test file with content
        try (PrintWriter writer = new PrintWriter(TEST_FILE, "UTF-8")) {
            writer.println(TEST_FILE_CONTENT);
        }
    }

    @After
    public void tearDown() throws Exception {
        // Delete the test file if it exists
        File testFile = new File(TEST_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }

        // Delete the tree file if it exists
        File treeFile = new File("tree.txt");
        if (treeFile.exists()) {
            treeFile.delete();
        }

        // Delete the generated object file if it exists
        String hash = Blob.sha1(TEST_FILE_CONTENT);
        File objectFile = new File("objects", hash);
        if (objectFile.exists()) {
            objectFile.delete();
        }
    }

    @Test
    public void testAddRemoveAndGetSha() throws Exception {
        // Test the add, remove, and getSha methods
        Tree tree = new Tree("tree.txt");

        // Add a blob and a tree entry
        tree.add("blob : " + Blob.sha1(Blob.readFile(TEST_FILE)) + " : " + TEST_FILE);
        tree.add("tree : subdir-hash : subdir");

        // Check if the tree file contains the added entries
        File treeFile = new File("tree.txt");
        assertTrue(treeFile.exists() && treeFile.isFile());

        try (Scanner scanner = new Scanner(treeFile)) {
            boolean foundBlob = false;
            boolean foundTree = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("blob : ") && line.contains(TEST_FILE)) {
                    foundBlob = true;
                } else if (line.contains("tree : subdir-hash : subdir")) {
                    foundTree = true;
                }
            }
            assertTrue(foundBlob);
            assertTrue(foundTree);
        }

        // Remove the blob entry and check if it's removed
        tree.remove(TEST_FILE);
        try (Scanner scanner = new Scanner(treeFile)) {
            boolean foundBlob = false;
            boolean foundTree = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("blob : ") && line.contains(TEST_FILE)) {
                    foundBlob = true;
                } else if (line.contains("tree : subdir-hash : subdir")) {
                    foundTree = true;
                }
            }
            assertFalse(foundBlob);
            assertTrue(foundTree);
        }

        // Get the SHA-1 hash of the tree and check if it's not null
        String treeSha1 = tree.getSha();
        assertNotNull(treeSha1);
    }
}