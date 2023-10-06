import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class TreeTest {
    private static final String TEST_FILE = "test.txt";
    private static final String TEST_FILE_CONTENT = "This is a test file content.";
    private Tree tree;

    @Before
    public void setUp() throws Exception {
        // // Create the test file with content
        // try (PrintWriter writer = new PrintWriter(TEST_FILE, "UTF-8")) {
        // writer.println(TEST_FILE_CONTENT);
        // }

        // String testDirectoryPath = "test_directory";
        // deleteDirectory(testDirectoryPath); // Clean up if it already exists
        // tree = new Tree("root");
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
    }

    // a good unit test, makes sure that my constructor works properly
    // generate a tree file

    @Test
    @DisplayName("Testing the tree constructor to make a tree file")
    public void testTreeConstructor() throws IOException {
        Tree tree = new Tree(); // should create a Tree file

        File treeFile = new File("Tree");
        assertTrue(treeFile.exists());

    }

    @Test
    @DisplayName("Testing if I can write to the tree")
    public void testWriteToTree() throws Exception {
        Tree tree = new Tree(); // should create a Tree file

        File treeFile = new File("Tree");
        assertTrue(treeFile.exists());

        // programatically create Files
        File testFile = new File(TEST_FILE);
        testFile.createNewFile();

        String fileContents = Blob.readFile(TEST_FILE);
        String fileSha = Blob.sha1(fileContents);

        String newLine = "blob : " + fileSha + " : " + TEST_FILE;
        tree.add(TEST_FILE); // exception

        String treeContents = Blob.readFile("Tree");

        assertEquals(newLine, treeContents);

    }

    // @Test
    // public void testAddRemoveAndGetSha() throws Exception {
    // // Test the add, remove, and getSha methods
    // Tree tree = new Tree("tree.txt");

    // // Add a blob and a tree entry
    // tree.add("blob : " + Blob.sha1(Blob.readFile(TEST_FILE)) + " : " +
    // TEST_FILE);
    // tree.add("tree : subdir-hash : subdir");

    // // Check if the tree file contains the added entries
    // File treeFile = new File("tree.txt");
    // assertTrue(treeFile.exists() && treeFile.isFile());

    // try (Scanner scanner = new Scanner(treeFile)) {
    // boolean foundBlob = false;
    // boolean foundTree = false;
    // while (scanner.hasNextLine()) {
    // String line = scanner.nextLine();
    // if (line.contains("blob : ") && line.contains(TEST_FILE)) {
    // foundBlob = true;
    // } else if (line.contains("tree : subdir-hash : subdir")) {
    // foundTree = true;
    // }
    // }
    // assertTrue(foundBlob);
    // assertTrue(foundTree);
    // }

    // // Remove the blob entry and check if it's removed
    // tree.remove(TEST_FILE);
    // try (Scanner scanner = new Scanner(treeFile)) {
    // boolean foundBlob = false;
    // boolean foundTree = false;
    // while (scanner.hasNextLine()) {
    // String line = scanner.nextLine();
    // if (line.contains("blob : ") && line.contains(TEST_FILE)) {
    // foundBlob = true;
    // } else if (line.contains("tree : subdir-hash : subdir")) {
    // foundTree = true;
    // }
    // }
    // assertFalse(foundBlob);
    // assertTrue(foundTree);
    // }

    // // Get the SHA-1 hash of the tree and check if it's not null
    // String treeSha1 = tree.getSha();
    // assertNotNull(treeSha1);
    // }

    // @Test
    // public void testAddDirectory() throws NoSuchAlgorithmException, IOException {
    // // Create a test directory structure
    // createDirectoryStructure("test_directory", "dir1", "dir1/subdir", "dir2",
    // "file1.txt");

    // // Add the test directory to the tree
    // String treeSha1 = tree.addDirectory("test_directory");

    // // Ensure the treeSha1 is not null or empty
    // assertNotNull(treeSha1);
    // assertFalse(treeSha1.isEmpty());

    // // You can add more assertions to verify the correctness of your method.
    // // For example, you can check if specific entries were added to the tree.
    // assertTrue(tree.contains("tree : " + treeSha1 + " : test_directory"));
    // assertTrue(tree.contains("tree : " + treeSha1 + " : dir1"));
    // assertTrue(tree.contains("tree : " + treeSha1 + " : dir2"));
    // assertTrue(tree.contains("blob : <sha1-of-file1> : file1.txt"));
    // }

    // // Helper method to create a directory structure for testing
    // private void createDirectoryStructure(String rootDir, String... paths) throws
    // IOException {
    // for (String path : paths) {
    // Path dirPath = Paths.get(rootDir, path);
    // Files.createDirectories(dirPath);

    // // Create a dummy file within each directory
    // Path filePath = dirPath.resolve("dummy.txt");
    // Files.createFile(filePath);
    // }
    // }

    // // Helper method to delete a directory and its contents
    // private void deleteDirectory(String directoryPath) throws IOException {
    // Path path = Paths.get(directoryPath);
    // if (Files.exists(path)) {
    // Files.walk(path)
    // .map(Path::toFile)
    // .sorted((o1, o2) -> -o1.compareTo(o2))
    // .forEach(File::delete);
    // }
    // }
}
