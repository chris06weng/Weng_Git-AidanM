import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestBlob {
    @Test
    @DisplayName("Test if Blobs are created correctly")
    void testBlob() throws Exception {
        // Run code
        File file = new File("junit-test.txt");
        file.createNewFile();

        PrintWriter pw = new PrintWriter(file);
        pw.print("Junit tester");
        pw.close();
        assertTrue(file.exists());

        Blob.blob("junit-test.txt");

        String content = Blob.read("junit-test.txt");
        String hash = Blob.sha1(content);

        File file1 = new File("objects", hash);
        assertTrue(file1.exists());
        assertEquals(Blob.read("junit-test.txt"), "Junit tester");
    }

    @Test
    void testGenerateBlob() throws NoSuchAlgorithmException, IOException {
        Tree testTree = new Tree("testTree");
        File testFile = new File("junittester.txt");

        // Add a tree entry
        testTree.add("tree: testdir");

        // Generate a blob for the tree
        testTree.generateBlob();

        // Check if a blob file with the same name as the tree exists
        File blobFile = new File(testFile.getName() + ".blob");
        assertTrue(blobFile.exists());
    }
}