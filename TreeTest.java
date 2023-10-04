import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TreeTest {
    @Test
    @DisplayName("Test Tree")
    void testAddAndRemoveInTree() throws NoSuchAlgorithmException, IOException {
        // Add a blob entry
        String content = "Tree test content";
        // Write content to the testFile
        File tree = new File("treeTest.txt");
        tree.createNewFile();
        PrintWriter pw = new PrintWriter(tree);
        pw.print(content);
        pw.close();
        Tree test = new Tree("test");
        test.add("treeTest.txt");
        assertTrue(tree.exists());

        // Read the contents of the testFile and check if it contains the added blob
        // entry
        String fileContents = Blob.readFile("test");
        assertTrue(fileContents.contains("blob : " + Blob.sha1(content) + " : " + "treeTest.txt"));

        test.remove("treeTest.txt");
        String fileContents2 = Blob.readFile("test");
        assertEquals(fileContents2, "");
    }

    @Test
    @DisplayName("Test addDirectory")
    public void testAddDirectory() throws IOException, NoSuchAlgorithmException {
        // Define a temporary directory for testing
        File tempDir = createTempDir();

        // Create some files and subdirectories in the temporary directory
        File file1 = createTempFile(tempDir, "file1.txt");
        File file2 = createTempFile(tempDir, "file2.txt");
        File subDir = createTempDir(tempDir, "subdir");
        File file3 = createTempFile(subDir, "file3.txt");

        // Create a Tree instance for the temporary directory
        Tree tree = new Tree("test_tree.txt");

        // Add the directory to the Tree
        String treeSha1 = tree.addDirectory(tempDir.getAbsolutePath());

        // Ensure that the SHA-1 hash is not null
        assertNotNull(treeSha1);

        // Remove the temporary files and directory
        assertTrue(file1.delete());
        assertTrue(file2.delete());
        assertTrue(file3.delete());
        assertTrue(subDir.delete());
        assertTrue(tempDir.delete());

        // Clean up the test_tree.txt file
        tree.remove("test_tree.txt");
    }

    private File createTempDir() {
        try {
            File tempDir = File.createTempFile("temp", Long.toString(System.nanoTime()));
            if (tempDir.delete() && tempDir.mkdir()) {
                return tempDir;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File createTempFile(File directory, String fileName) throws IOException {
        File tempFile = new File(directory, fileName);
        if (tempFile.createNewFile()) {
            return tempFile;
        }
        return null;
    }

    private File createTempDir(File parent, String dirName) {
        File tempDir = new File(parent, dirName);
        if (tempDir.mkdir()) {
            return tempDir;
        }
        return null;
    }
}
