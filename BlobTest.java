import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class BlobTest {

    private final String testFileName = "testfile.txt";
    private final String testContents = "This is a test file content.";
    private final String objectsFolder = "objects";

    @BeforeEach
    public void setUp() {
        // Delete the test file and objects folder if they exist
        File testFile = new File(testFileName);
        if (testFile.exists()) {
            testFile.delete();
        }

        File objectsDir = new File(objectsFolder);
        if (objectsDir.exists()) {
            File[] files = objectsDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            objectsDir.delete();
        }
        else{
            objectsDir.mkdirs();
        }
    }

    @Test
    public void testBlob() {
        try {
            TestUtils.writeFile(testFileName, testContents);

            // Call Blob.blob() to create a new file
            Blob.blob(testFileName);

            // Check if the new file exists in the "objects" folder
            String hashedContents = Blob.sha1(testContents);
            File newFile = new File ("objects" + File.separator + hashedContents);
            assertTrue(newFile.exists());

            // Check the contents of the new file
            String newFileContents = TestUtils.readFile(newFile.getAbsolutePath());
            assertEquals(testContents+"\n", newFileContents);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadFile() throws IOException {
        TestUtils.writeFile(testFileName, testContents);

        try {
            String contents = Blob.readFile(testFileName);
            assertEquals(testContents, contents);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadFileToList() throws IOException {
        TestUtils.writeFile(testFileName, "Line 1\nLine 2\nLine 3");

        try {
            var lines = Blob.readFileToList(testFileName);
            assertEquals(3, lines.size());
            assertEquals("Line 1", lines.get(0));
            assertEquals("Line 2", lines.get(1));
            assertEquals("Line 3", lines.get(2));
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSha1() {
        String input = "This is a test input for SHA1.";

        try {
            String sha1 = Blob.sha1(input);
            assertEquals("b69312e39141616ebd5210ad65d4db9c0256b0ef", sha1);
        } catch (NoSuchAlgorithmException e) {
            fail("NoSuchAlgorithmException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGrab() throws IOException {
        Blob.grab(testFileName);
        File grabbedFile = new File(testFileName);
        assertTrue(grabbedFile.exists());
    }
}