import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tree {
    private File tree;

    public Tree() throws IOException {
        init();

        String folderPath = "objects";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String filePath = folderPath + File.separator + "Index";

        tree = new File(filePath);
        if (!tree.exists())
            tree.createNewFile();
    }

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

    public Tree(String fileName) throws IOException {
        reset(fileName);
        tree = new File(fileName);
        tree.createNewFile();
    }

    public String getName() throws NoSuchAlgorithmException, IOException {
        return Blob.sha1(Blob.readFile("Index"));
    }

    public void add(String fileName) throws NoSuchAlgorithmException, IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(tree, true));
        File inputFile = new File(fileName);
        if (inputFile.exists()) {
            Blob.blob(fileName);
            pw.println("blob : " + Blob.sha1(Blob.readFile(fileName)) + " : " + fileName);
        } else if (fileName.startsWith("tree")) {
            pw.println(fileName);
        } else {
            pw.close();
            throw new IllegalArgumentException("Invalid input format: " + fileName);
        }
        pw.close();
    }

    public void remove(String fileName) throws NoSuchAlgorithmException, IOException {
        String entryToDelete1 = "blob : " + Blob.sha1(Blob.readFile(fileName)) + " : " + fileName;
        String entryToDelete2 = "tree : " + fileName;
        String entryToDelete3 = fileName;

        try {
            List<String> indexContents = new ArrayList<>();
            try (BufferedReader lineReader = new BufferedReader(new FileReader(tree))) {
                String line;
                while ((line = lineReader.readLine()) != null) {
                    if (!line.equals(entryToDelete1) && !line.startsWith(entryToDelete2) && !line.contains(entryToDelete3)) {
                        indexContents.add(line);
                    }
                }
            }

            try (BufferedWriter indexWriter = new BufferedWriter(new FileWriter(tree))) {
                for (String contents : indexContents) {
                    indexWriter.write(contents);
                    indexWriter.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public String getSha() throws IOException, NoSuchAlgorithmException {
        List<String> contents = new ArrayList<>();

        try (BufferedReader lineReader = new BufferedReader(new FileReader(tree))) {
            String line;
            while ((line = lineReader.readLine()) != null) {
                contents.add(line);
            }
        }

        Collections.sort(contents);

        StringBuilder contentString = new StringBuilder();
        for (String line : contents) {
            contentString.append(line);
        }

        return sha1(contentString.toString());
    }

    private String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public String addDirectory(String directoryPath) throws NoSuchAlgorithmException, IOException {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory() || !directory.canRead()) {
            throw new IllegalArgumentException("Invalid or unreadable directory path: " + directoryPath);
        }

        Tree childTree = new Tree("subIndex");

        // Iterate through files and subdirectories in the given directory
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    // Compute SHA1 for files and add as a blob entry
                    String sha1 = Blob.sha1(Blob.readFile(file.getPath()));
                    childTree.add("blob : " + sha1 + " : " + file.getName());
                } else if (file.isDirectory()) {
                    // Recursively add subdirectories using childTree
                    String childTreeSha1 = childTree.addDirectory(file.getPath());
                    childTree.add("tree : " + childTreeSha1 + " : " + file.getName());
                }
            }
        }

        // Generate a blob for the childTree and add as a tree entry to the current Tree
        childTree.generateBlob();
        String childTreeSha1 = childTree.getSha();
        add("tree : " + childTreeSha1 + " : " + directory.getName());

        // Generate a blob for the current Tree and return its SHA1
        generateBlob();
        String treeSha1 = getSha();

        return treeSha1;
    }

    public void generateBlob() throws IOException, NoSuchAlgorithmException {
        List<String> contents = new ArrayList<>();

        try (BufferedReader lineReader = new BufferedReader(new FileReader(tree))) {
            String line;
            while ((line = lineReader.readLine()) != null) {
                contents.add(line);
            }
        }

        Collections.sort(contents);

        StringBuilder contentString = new StringBuilder();
        for (String line : contents) {
            contentString.append(line);
            contentString.append('\n'); // Add newline character
        }

        String blobContent = contentString.toString();

        // Calculate SHA1 hash of the content and save it as a file
        String sha1 = sha1(blobContent);
        File blobFile = new File("objects/" + sha1);
        try (PrintWriter writer = new PrintWriter(blobFile)) {
            writer.print(blobContent);
        }
    }

    public static String copyIndex() throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("Index"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        String indexContents = content.toString();

        return indexContents;
    }

    private static void reset(String fileName) throws IOException {
        File file = new File(fileName);
        String empty = "";
        if (file.exists())
            Files.write(file.toPath(), empty.getBytes());
    }

    public static void save() throws NoSuchAlgorithmException, IOException
    {
        Blob.blob("Index");
    }
}