import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.*;

public class IndexTest2 {

    private Index index;
    private String basePath;

    @Before
    public void setUp() {
        // Define the base path and initialize the Index.
        basePath = "base_directory";
        index = new Index(basePath);
    }

    @Test
    public void testAddDirectory() throws IOException {
        // Directory name to be added.
        String directoryName = "test_directory";

        // Add a directory to the index.
        index.addDirectory(directoryName);

        // Construct the expected directory path.
        Path expectedDirectoryPath = Paths.get(basePath, directoryName);

        // Check if the directory was created.
        assertTrue(Files.isDirectory(expectedDirectoryPath));
    }

    @Test
    public void testAddExistingDirectory() throws IOException {
        // Directory name to be added.
        String directoryName = "test_directory";

        // Create the directory manually before adding it.
        Path directoryPath = Paths.get(basePath, directoryName);
        Files.createDirectories(directoryPath);

        // Attempt to add the directory to the index.
        index.addDirectory(directoryName);

        // Construct the expected directory path.
        Path expectedDirectoryPath = Paths.get(basePath, directoryName);

        // Check if the directory still exists (should not be deleted).
        assertTrue(Files.isDirectory(expectedDirectoryPath));
    }
}