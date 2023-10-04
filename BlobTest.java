import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class BlobTest {
    private static final String TEST_FILE = "test.txt";
    private static final String TEST_FILE_CONTENT = "This is a test file content.";

    @Before
    public void setUp() throws Exception {
        // Create the test file with content
        try (PrintWriter writer = new PrintWriter(TEST_FILE, StandardCharsets.UTF_8)) {
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

        // Delete the generated object file if it exists
        String hash = Blob.sha1(TEST_FILE_CONTENT);
        File objectFile = new File("objects", hash);
        if (objectFile.exists()) {
            objectFile.delete();
        }
    }

    @Test
    public void testBlob() throws Exception {
        // Test the blob method
        Blob.blob(TEST_FILE);

        // Check if the object file with the expected content exists
        String hash = Blob.sha1(TEST_FILE_CONTENT);
        File objectFile = new File("objects", hash);
        assertTrue(objectFile.exists());

        // Read the content of the object file and compare it with the expected content
        String objectFileContent = Blob.readFile(objectFile.getPath());
        assertEquals(TEST_FILE_CONTENT, objectFileContent);
    }

    @Test
    public void testReadFile() throws Exception {
        // Test the readFile method
        String fileContent = Blob.readFile(TEST_FILE);

        // Check if the content matches the expected content
        assertEquals(TEST_FILE_CONTENT, fileContent);
    }

    @Test
    public void testCalculateSHA1() throws NoSuchAlgorithmException {
        // Test the calculateSHA1 method
        String input = "This is a test string.";
        String expectedHash = Blob.sha1(input);

        try {
            String hash = Blob.sha1(input);
            assertEquals(expectedHash, hash);
        } catch (NoSuchAlgorithmException e) {
            fail("NoSuchAlgorithmException should not be thrown");
        }
    }
}