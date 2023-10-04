//step 1.
//read a file
//hash the contents of said file
//create a new file called that hash, with the original files contents.
//put that new file into a objects folder.

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Blob {
    public static void blob(String fileName) throws IOException, NoSuchAlgorithmException {
        try {
            String content = readFile(fileName);
            String hash = calculateSHA1(content);

            String folderPath = "objects";
            String newFileName = hash;

            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String filePath = folderPath + File.separator + newFileName;

            try (PrintWriter pw = new PrintWriter(filePath, StandardCharsets.UTF_8)) {
                pw.print(content);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    public static String sha1(String input) throws NoSuchAlgorithmException {
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
        blob("test.txt");

        String file2 = "test2.txt";
        blob(file2);
    }
}
