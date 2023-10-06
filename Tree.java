import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tree {
    private File tree;
    String record;

    public Tree() throws IOException {
        tree = new File("Tree");
        if (!tree.exists())
            tree.createNewFile();
    }

    public Tree(String fileName) throws IOException {
        tree = new File(fileName);
        tree.createNewFile();
        record = "";
    }

    public void add(String fileName) throws NoSuchAlgorithmException, IOException {

        // make a file with filename
        // file exist?
        File inputFile = new File(fileName);

        try (

                PrintWriter pw = new PrintWriter(new FileWriter(tree, true))

        )

        {
            if (input.startsWith("tree")) {
                pw.println("+ " + input);
                record += input;
            } else if (input.startsWith("blob")) {
                pw.println("+ " + input);
                record += input;
            } else {
                throw new IllegalArgumentException("Invalid input format: " + input);
            }
        }
    }

    public void remove(String fileName) throws NoSuchAlgorithmException, IOException {
        String entryToDelete1 = "+ blob : " + Blob.sha1(Blob.readFile(fileName)) + " : " + fileName;
        String entryToDelete2 = "+ tree : " + fileName;

        try {
            List<String> indexContents = new ArrayList<>();
            try (BufferedReader lineReader = new BufferedReader(new FileReader(tree))) {
                String line;
                while ((line = lineReader.readLine()) != null) {
                    if (!line.equals(entryToDelete1) && !line.equals(entryToDelete2)) {
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
                    String sha1 = Blob.sha1(Blob.readFile(file.getPath()));
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

    public boolean contains(String input) {
        return record.contains(input);
    }
}