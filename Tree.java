import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Tree {
    File tree;

    public Tree(String fileName) throws IOException {
        tree = new File(fileName);
        tree.createNewFile();
    }

    public void add(String input) throws NoSuchAlgorithmException, IOException {
        PrintWriter pw = new PrintWriter(tree);
        if (input.substring(0, 4).equals("tree")) {
            pw.print("\n + tree : " + input);
        } else {
            pw.print("\n + blob : " + Blob.sha1(Blob.read(input)) + " : " + input);
        }
        pw.close();
    }

    public void remove(String fileName) throws NoSuchAlgorithmException, IOException {
        String entryToDelete1 = "blob : " + Blob.sha1(Blob.read(fileName)) + fileName;
        String entryToDelete2 = "tree : " + fileName;

        try {
            List<String> indexContents = new ArrayList<>();
            BufferedReader lineReader = new BufferedReader(new FileReader(tree));
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (!line.equals(entryToDelete1) && !line.equals(entryToDelete2)) {
                    indexContents.add(line);
                }
            }
            lineReader.close();

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

        contents.sort(String::compareTo);

        StringBuilder contentString = new StringBuilder();
        for (String line : contents) {
            contentString.append(line);
        }

        return calculateSHA1(contentString.toString());
    }

    private String calculateSHA1(String input) throws NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sha1Hash = new StringBuilder();
            for (byte b : messageDigest) {
                sha1Hash.append(String.format("%02x", b));
            }
            return sha1Hash.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error calculating SHA-1 hash: " + e.getMessage());
            return null;
        }
    }

    public void generateBlob() throws NoSuchAlgorithmException, IOException {
        Blob.blob(tree.getName());
    }

    public String addDirectory(String directoryPath) throws NoSuchAlgorithmException, IOException {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory() || !directory.canRead()) {
            throw new IllegalArgumentException("Invalid or unreadable directory path: " + directoryPath);
        }

        Tree childTree = new Tree(directory.getName());

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String sha1 = Blob.sha1(Blob.read(file.getPath()));
                    childTree.add("blob : " + sha1 + " : " + file.getName());
                } else if (file.isDirectory()) {
                    String childTreeSha1 = addDirectory(file.getPath());
                    childTree.add("tree : " + childTreeSha1 + " : " + file.getName());
                }
            }
        }

        childTree.generateBlob();
        String childTreeSha1 = childTree.getSha();
        add("tree : " + childTreeSha1 + " : " + directory.getName());

        generateBlob();
        String treeSha1 = getSha();

        return treeSha1;
    }

}
