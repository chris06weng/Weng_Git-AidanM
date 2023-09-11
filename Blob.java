//step 1.
//read a file
//hash the contents of said file
//create a new file called that hash, with the original files contents.
//put that new file into a objects folder.

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.util.logging.*;

public class Blob {
    public static void blob(String fileName) throws IOException, NoSuchAlgorithmException {
        String str = sha1(read(fileName));
        write(read(fileName), str);
    }

    public static void write(String input, String outputFile) throws FileNotFoundException {

        PrintWriter pw = new PrintWriter(
                "/Users/aidanmichaelson/Documents/Honors Topics in Computer Science/Git-AidanM/objects/" + outputFile);
        pw.print(input);
        pw.close();
    }

    public static String read(String fileName) throws IOException {
        StringBuilder str = new StringBuilder("");
        BufferedReader ar = new BufferedReader(new FileReader(fileName));
        while (ar.ready()) {
            str.append((char) ar.read());
        }
        ar.close();
        return str.toString();
    }

    static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        // System.out.println("Hello " + sha1("Hello"));
        String file = "test.txt";
        blob(file);

        String file2 = "test2.txt";
        blob(file2);
    }
}
