import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IndexTest2 {
    private static final String TEST_DIRECTORY = "test_directory";

    @Before
    public void setUp() {
        // Create the test directory before each test case
        TestUtils.createTestDirectory(TEST_DIRECTORY);
    }

    @After
    public void tearDown() {
        // Delete the test directory after each test case
        TestUtils.deleteTestDirectory(TEST_DIRECTORY);
    }

    @Test
    public void testAddDirectory() {
        try {
            Index index = new Index();
            String directoryPath = TEST_DIRECTORY;

            String sha1 = index.addDirectory(directoryPath);

            assertNotNull(sha1);
            assertTrue(sha1.length() > 0);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}