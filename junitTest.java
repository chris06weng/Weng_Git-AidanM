import static org.junit.Assert.*;

import java.io.File;
import java.io.PrintWriter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class junitTest {
    @Test
    @DisplayName("Test if Blobs are created correctly")
    void testBlob() throws Exception {
        // Run code
        File file = new File("junit-test.txt");
        PrintWriter pw = new PrintWriter(file);
        pw.print("Junit tester");
        pw.close();
        Index.add(file.getName());

        File file1 = new File(Blob.sha1(Blob.read("junit-test.txt")));
        assertTrue(file1.exists());
        assertEquals(Blob.read(Blob.sha1(Blob.read("junit-test.txt"))), "Junit tester");
    }

    @Test
    @DisplayName("Test Index")
    void testCreateBlob() throws Exception {
        Index.init();
        Index.add("junittester.txt");

        // Check blob exists in the objects folder
        File file_junit1 = new File(
                "/Users/chris/Desktop/Honots/Weng_Git-AidanM/objects/" + Blob.sha1("junittester.txt"));

        assertTrue("Blob file to add not found", file_junit1.exists());
        // Read file contents
        String indexFileContents = Blob.read("/Users/chris/Desktop/Honots/Weng_Git-AidanM/objects/" +
                file_junit1);
        assertEquals("File contents of Blob don't match file contents pre-blob creation", indexFileContents,
                "this is the junit tester");
    }
}
