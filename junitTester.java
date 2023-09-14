import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class junitTester {
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        /*
         * Utils.writeStringToFile("git_junit_test.txt", "test file contents");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }

    @AfterAll
    static void takeDownAfterClass() throws Exception {
        /*
         * Utils.deleteFile("git_junit_test.txt");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }

    @Test
    @DisplayName("Test if Blobs are created correctly")
    void testBlob() throws Exception {
        // Run code
        Blob.blob("test.txt");
        Path path = Paths.get("objects");

        // Test accuracy
        File file = new File(Blob.sha1(Blob.read("test.txt")));
        assertTrue(file.exists());
        assertTrue(Files.exists(path));
        assertEquals(Blob.read(Blob.sha1(Blob.read("test.txt"))), "Hello how is it going");
    }

    @Test
    @DisplayName("Test if Blobs are created correctly")
    void testCreateBlob() throws Exception {
        try {
            // Manually create the files and folders before the 'testAddFile'
            // MyGitProject myGitClassInstance = new MyGitProject();
            // myGitClassInstance.init();
            // TestHelper.runTestSuiteMethods("testCreateBlob", file1.getName());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        // Check blob exists in the objects folder
        File file_junit1 = new File("objects/" + file1.methodToGetSha1());
        assertTrue("Blob file to add not found", file_junit1.exists());
        // Read file contents
        String indexFileContents = MyUtilityClass.readAFileToAString("objects/" +
                file1.methodToGetSha1());
        assertEquals("File contents of Blob don't match file contents pre-blob creation", indexFileContents,
                file1.getContents());
    }

}

    @Test
    @DisplayName("Test init, add, remove")
    void testMethods() throws Exception {

    }
}
