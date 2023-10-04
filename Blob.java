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

            File newFile = new File(filePath);

            PrintWriter pw = new PrintWriter(newFile);
            pw.print(content);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }

    public static String calculateSHA1(String input) throws NoSuchAlgorithmException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : bytes) {
                stringBuilder.append(String.format("%02x", b));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("SHA-1 algorithm is not available", e);
        }
    }
}
