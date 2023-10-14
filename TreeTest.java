import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class TreeTest {
    private static final String TEST_FILE = "test.txt";
    private Tree tree;

    @Before
    public void setUp() throws Exception {
        try {
            tree = new Tree();
        } catch (IOException e) {
            fail("Tree not created.");
        }

        File file = new File("test.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        PrintWriter writer = new PrintWriter(file);
        writer.print("Tree test");
        writer.close();
    }

    @After
    public void tearDown() throws Exception {
        // // Delete the test file if it exists
        // File testFile = new File(TEST_FILE);
        // if (testFile.exists()) {
        // testFile.delete();
        // }

        // // Delete the tree file if it exists
        // File treeFile = new File("tree.txt");
        // if (treeFile.exists()) {
        // treeFile.delete();
        // }

        // // Delete the generated object file if it exists
        // String hash = Blob.sha1(TEST_FILE_CONTENT);
        // File objectFile = new File("objects", hash);
        // if (objectFile.exists()) {
        // objectFile.delete();
        // }

        File file = new File("test.txt");
        if (file.exists()) {
            file.delete();
        }

        File objectsDir = new File("objects");
        if (objectsDir.exists()) {
            deleteDirectory(objectsDir);
        }
    }

    // a good unit test, makes sure that my constructor works properly
    // generate a tree file

    @Test
    @DisplayName("Testing the tree constructor to make a tree file")
    public void testTreeConstructor() throws IOException {

        File treeFile = new File("objects" + File.separator + "Index");
        assertTrue(treeFile.exists());

    }

    @Test
    @DisplayName("Testing if I can write to the tree")
    public void testWriteToTree() throws Exception {
        Tree tree = new Tree(); // should create a Tree file

        File treeFile = new File("objects" + File.separator + "Index");
        assertTrue(treeFile.exists());

        // programatically create Files
        File testFile = new File(TEST_FILE);
        testFile.createNewFile();

        // String fileContents = Blob.readFile(TEST_FILE);
        // String fileSha = Blob.sha1(fileContents);

        String newLine = "blob : f432dd426cf8d1206f1e3aea9595ea553c5b7733 : test.txt";
        tree.add(TEST_FILE); // exception

        String treeContents = Blob.readFile("objects" + File.separator + "Index");

        assertEquals(newLine, treeContents);

    }

    @Test
    public void testAddRemoveAndGetSha() throws Exception {
        // Test the add, remove, and getSha methods
        Tree tree = new Tree();

        // Add a blob and a tree entry
        tree.add(TEST_FILE);
        tree.add("tree : subdir-hash : subdir");

        // Check if the tree file contains the added entries
        File treeFile = new File("objects" + File.separator + "Index");
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

    @Test
    public void testAddDirectoryBasic() throws IOException {
        Index.init();
        String directoryPath = "testBasicDirectory";

        File directory = new File(directoryPath);

        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created successfully.");
            } else {
                System.err.println("Failed to create the directory.");
            }
        } else {
            System.out.println("Directory already exists.");
        }

        try {
            String sha1 = tree.addDirectory(directoryPath);
            // Assert that the SHA1 returned matches the expected SHA1 for the test
            // directory
            assertEquals("8d2c7dfe73c6e999ab6e2c6c1220d5e87a0181dd", sha1);
        } catch (NoSuchAlgorithmException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testAddDirectoryAdvanced() {
        String directoryPath = "testAdvancedDirectory";

        File directory = new File(directoryPath);

        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created successfully.");
            } else {
                System.err.println("Failed to create the directory.");
            }
        } else {
            System.out.println("Directory already exists.");
        }

        try {
            String sha1 = tree.addDirectory(directoryPath);
            // Assert that the SHA1 returned matches the expected SHA1 for the advanced test
            // directory
            assertEquals("cff77b6f14a10b3fbebb93e4ed5447631bd99f64", sha1);
        } catch (NoSuchAlgorithmException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
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
