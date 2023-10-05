import org.junit.Test;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class IndexTest2 {

    @Test
    public void testAddDirectory() {
        try {
            // Create an instance of the Index class
            Index index = new Index();

            // Create a temporary directory for testing
            String testDirPath = "test_directory";
            TestUtils.createTestDirectory(testDirPath);

            // Add the directory to the index
            String result = index.addDirectory(testDirPath);

            // Assert that the result is not null
            assertNotNull(result);

            // Optionally, you can check other conditions based on your implementation
            // For example, you can check if the added directory's contents are correctly indexed.

            // Clean up: Delete the temporary test directory
            TestUtils.deleteTestDirectory(testDirPath);
        } catch (IOException | NoSuchAlgorithmException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }