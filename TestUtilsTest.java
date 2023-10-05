import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;

public class TestUtilsTest {
    private Path testDirectory;

    @Before
    public void setUp() throws IOException {
        // Create a temporary directory for testing
        testDirectory = TestUtils.createTestDirectory();
    }

    @After
    public void tearDown() throws IOException {
        // Delete the test directory after each test
        TestUtils.deleteTestDirectory(testDirectory);
    }

    @Test
    public void testCreateTestDirectory() {
        // Check if the test directory exists
        assertTrue(Files.exists(testDirectory));
        assertTrue(Files.isDirectory(testDirectory));
    }

    @Test
    public void testDeleteTestDirectory() {
        // Check if the test directory exists before deletion
        assertTrue(Files.exists(testDirectory));
        assertTrue(Files.isDirectory(testDirectory));

        // Delete the test directory
        try {
            TestUtils.deleteTestDirectory(testDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Check if the test directory no longer exists
        assertTrue(Files.notExists(testDirectory));
    }
}