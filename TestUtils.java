import java.io.File;

public class TestUtils {

    // Create a temporary test directory for testing
    public static void createTestDirectory(String directoryPath) {
        try {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test directory: " + directoryPath, e);
        }
    }

    // Delete a temporary test directory after testing
    public static void deleteTestDirectory(String directoryPath) {
        try {
            File directory = new File(directoryPath);
            if (directory.exists()) {
                deleteRecursive(directory);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete test directory: " + directoryPath, e);
        }
    }

    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }
}
