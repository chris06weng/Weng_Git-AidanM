import java.io.*;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Index {

    public static void init() throws IOException {
        File objectsDir = new File("objects");
        if (!objectsDir.exists()) {
            objectsDir.mkdirs();
        }

        File indexFile = new File("Index");
        if (!indexFile.exists()) {
            try {
                indexFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String empty = "";
        Files.write(indexFile.toPath(), empty.getBytes());
    }

    public static void add(String fileName) throws NoSuchAlgorithmException, IOException {
        String content = Blob.readFile(fileName);
        String hash = Blob.sha1(content);

        File objectFile = new File("objects", hash);
        if (!objectFile.exists()) {
            try (PrintWriter pw = new PrintWriter(objectFile)) {
                pw.print(content);
            }
        }

        try (PrintWriter indexWriter = new PrintWriter(new FileWriter("Index", true))) {
            indexWriter.println(fileName + ": " + hash + "\n");
        }
    }

    public static void remove(String fileName) throws IOException, NoSuchAlgorithmException {
        String content = Blob.readFile(fileName);
        String hash = Blob.sha1(content);

        File objectFile = new File("objects", hash);
        if (objectFile.exists()) {
            objectFile.delete();
        }

        File indexFile = new File("Index");
        if (indexFile.exists()) {
            List<String> updatedEntries = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(indexFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.startsWith(fileName + ": ")) {
                        updatedEntries.add(line);
                    }
                }
            }

            try (PrintWriter pw = new PrintWriter(new FileWriter(indexFile))) {
                for (String entry : updatedEntries) {
                    pw.println(entry);
                }
            }
        }
    }

    public String addDirectory(String directoryPath) throws NoSuchAlgorithmException, IOException {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory() || !directory.canRead()) {
            throw new IllegalArgumentException("Invalid directory path: " + directoryPath);
        }

        Tree tree = new Tree(directory.getName());
        String treeSHA1 = tree.addDirectory(directoryPath);

        // Add the tree entry to the index
        PrintWriter pw = new PrintWriter("Index");
        pw.println("tree : " + treeSHA1 + " : " + directory.getName());
        pw.close();

        return treeSHA1;
    }
}