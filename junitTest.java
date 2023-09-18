import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class junitTest {
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

        Blob.blob(file.getName());

        File file1 = new File(Blob.sha1(Blob.read(file.getName())));
        assertTrue(file1.exists());
        // assertEquals(Blob.read(Blob.sha1(Blob.read("junit-test.txt"))), "Junit
        // tester");
    }

    @Test
    @DisplayName("Test Index")
    void testCreateBlob() throws Exception {
        Index.init();
        Index.add("junittester.txt");

        // Check blob exists in the objects folder
        File file_junit1 = new File(
                "/Users/chris/Documents/CS/Weng_Git-AidanM/objects/" + Blob.sha1("junittester.txt"));

        assertTrue("Blob file to add not found", file_junit1.exists());
        // Read file contents
        String indexFileContents = Blob.read("/Users/chris/Documents/CS/Weng_Git-AidanM/objects/" +
                file_junit1);
        assertEquals("File contents of Blob don't match file contents pre-blob creation", indexFileContents,
                "this is the junit tester");
    }

    Tree testTree = new Tree("treetest.txt");
    File testFile = new File("ref.txt");

    @Test
    void testAdd() throws NoSuchAlgorithmException, IOException {
        // Add a tree entry
        testTree.add("tree: testdir");

        // Read the contents of the testFile and check if it contains the added entry
        String fileContents = TestUtils.readFile(testFile);
        assertTrue(fileContents.contains("tree: testdir"));
    }

    @Test
    void testAddBlob() throws NoSuchAlgorithmException, IOException {
        // Add a blob entry
        String content = "This is a test blob content.";
        TestUtils.writeFile(testFile, content); // Write content to the testFile
        testTree.add(testFile.getName());

        // Read the contents of the testFile and check if it contains the added blob
        // entry
        String fileContents = TestUtils.readFile(testFile);
        assertTrue(fileContents.contains("blob : " + TestUtils.sha1(content) + " : " + testFile.getName()));
    }

    @Test
    void testRemove() throws NoSuchAlgorithmException, IOException {
        // Add a tree entry and a blob entry
        testTree.add("tree: testdir");
        String content = "This is a test blob content.";
        TestUtils.writeFile(testFile, content);
        testTree.add(testFile.getName());

        // Remove the blob entry
        testTree.remove(testFile.getName());

        // Read the contents of the testFile and check if it does not contain the
        // removed blob entry
        String fileContents = TestUtils.readFile(testFile);
        assertFalse(fileContents.contains("blob : " + TestUtils.sha1(content) + " : " + testFile.getName()));
    }

    @Test
    void testGenerateBlob() throws NoSuchAlgorithmException, IOException {
        // Add a tree entry
        testTree.add("tree: testdir");

        // Generate a blob for the tree
        testTree.generateBlob();

        // Check if a blob file with the same name as the tree exists
        File blobFile = new File(testFile.getName() + ".blob");
        assertTrue(blobFile.exists());
    }
}
