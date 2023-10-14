import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Git {
    public static void remove(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("index", true))) {
            writer.write("deleted* " + fileName + "\n");
        }
    }

    public static void edit(String fileName) throws IOException, NoSuchAlgorithmException {
        String newFileName = Blob.blob(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("index", true))) {
            writer.write("edited* " + newFileName + "\n");
        }
    }

    public static void createCommitTree() throws IOException, NoSuchAlgorithmException {
        // Read the index file and process entries to generate the commit tree
        List<String> entries = Files.readAllLines(Paths.get("index"));
        Tree commitTree = new Tree();
        
        for (String entry : entries) {
            if (entry.startsWith("deleted*")) {
                // Handle deleted files
                String fileName = entry.substring("deleted* ".length());
                commitTree.remove(fileName);
            } else if (entry.startsWith("edited*")) {
                // Handle edited files
                String fileName = entry.substring("edited* ".length());
                String blobSHA1 = Blob.blob(fileName);
                commitTree.add("blob : " + blobSHA1 + " : " + fileName);
            }
        }
        
        commitTree.generateBlob();
    }
    
    public static void checkout(String commitSHA1) throws IOException, NoSuchAlgorithmException {
        // Retrieve the root tree file from the given commit
        String commitContents = Blob.readFile(commitSHA1);
        String[] lines = commitContents.split("\n");
        if (lines.length < 1) {
            return; // Invalid commit file
        }
        String rootTreeSHA1 = lines[0];

        // Recursively traverse and recreate the directory structure
        recreateDirectory(rootTreeSHA1, "working_directory");
    }

    private static void recreateDirectory(String treeSHA1, String currentPath) throws IOException, NoSuchAlgorithmException {
        String treeContents = Blob.readFile(treeSHA1);
        String[] entries = treeContents.split("\n");
        
        for (String entry : entries) {
            String[] parts = entry.split(" : ");
            if (parts.length == 3) {
                String objectType = parts[0];
                String objectSHA1 = parts[1];
                String objectName = parts[2];
                
                if (objectType.equals("tree")) {
                    // Recursively recreate subdirectory
                    String subdirectoryPath = currentPath + File.separator + objectName;
                    Files.createDirectories(Paths.get(subdirectoryPath));
                    recreateDirectory(objectSHA1, subdirectoryPath);
                } else if (objectType.equals("blob")) {
                    // Recreate blob file in the appropriate directory
                    String blobContents = Blob.readFile(objectSHA1);
                    String filePath = currentPath + File.separator + objectName;
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                        writer.write(blobContents);
                    }
                }
            }
        }
    }
}