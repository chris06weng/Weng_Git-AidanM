//create a new directory called objects 
//create a new file called index
//be able to add blobs which as before puts them into objects
//but also creates a new file with the name of the txt file and the hash
//be able to remove the file associated with that txt and remove it from objects

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public class Index {

    public static void init() throws FileNotFoundException {
        File theDir = new File("/Users/chris/Documents/CS/Weng_Git-AidanM/objects/");
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        PrintWriter pw = new PrintWriter(
                "index");
        pw.close();
    }

    public static void add(String fileName) throws NoSuchAlgorithmException, IOException {
        Blob.blob(fileName);

        // add hash file to object
        // add txt name and hash to index.
        PrintWriter pw = new PrintWriter(
                "index");
        pw.print(fileName + ": " + Blob.sha1(Blob.read(fileName)));
        pw.close();
    }

    public static void remove(String fileName) throws IOException, NoSuchAlgorithmException {
        File inputFile = new File(
                "index");
        File tempFile = new File("myTempFile.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String lineToRemove = fileName + ": " + Blob.sha1(Blob.read(fileName));
        String currentLine;

        while ((currentLine = reader.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if (trimmedLine.equals(lineToRemove))
                continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();
        tempFile.renameTo(inputFile);

        // remove file from directory
        Path path = FileSystems.getDefault()
                .getPath("/Users/chris/Documents/CS/Weng_Git-AidanM/objects/"
                        + Blob.sha1(Blob.read(fileName)));
        Files.delete(path);

    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        init();
        String file3 = "test3.txt";
        add(file3);
        String file4 = "test4.txt";
        add(file4);
        System.out.println(Blob.sha1(Blob.read(file3)));
        remove(file3);
    }
}
