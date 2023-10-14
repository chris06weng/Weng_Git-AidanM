import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {
    public static void writeFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    public static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    public static void createDirectory(String directoryPath) throws IOException {
        Files.createDirectories(Paths.get(directoryPath));
    }

    public static void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDirectory(String directoryPath) {
        Path path = Paths.get(directoryPath);
        if (Files.exists(path)) {
            try {
                Files.walk(path)
                        .map(Path::toFile)
                        .sorted((a, b) -> -a.compareTo(b))
                        .forEach(File::delete);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void cleanUpTestEnvironment(String... pathsToDelete) {
        for (String path : pathsToDelete) {
            if (Files.isDirectory(Paths.get(path))) {
                deleteDirectory(path);
            } else {
                deleteFile(path);
            }
        }
    }
}