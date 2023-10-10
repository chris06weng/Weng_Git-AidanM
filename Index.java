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
            indexWriter.println("blob : " + hash + " : " + fileName + "\n");
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

    public static void addDirectory(String directoryPath) throws IOException,
            NoSuchAlgorithmException {
        Tree tree = new Tree();
        tree.addDirectory(directoryPath);
        try (PrintWriter pw = new PrintWriter(new FileWriter("Index", true))) {
            String treeSha1 = tree.getSha();
            String directoryName = tree.getName();
            pw.println("tree : " + treeSha1 + " : " + directoryName);
        }
    }
}