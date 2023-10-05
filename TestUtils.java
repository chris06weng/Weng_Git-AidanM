import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {
    public static Path createTestDirectory() throws IOException {
        // Specify the directory path
        Path directoryPath = Paths.get("test_directory");

        // Create the directory
        Files.createDirectories(directoryPath);

        return directoryPath;
    }

    public static void deleteTestDirectory(Path directoryPath) throws IOException {
        // Delete the test directory and its contents recursively
        if (Files.exists(directoryPath)) {
            Files.walk(directoryPath)
                    .map(Path::toFile)
                    .sorted((o1, o2) -> -o1.compareTo(o2))
                    .forEach(File::delete);
        }
    }
}
