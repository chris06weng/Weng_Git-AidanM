//step 1.
//read a file
//hash the contents of said file
//create a new file called that hash, with the original files contents.
//put that new file into a objects folder.

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Blob {
    private static File file;

    public static void blob(String fileName) throws IOException, NoSuchAlgorithmException {
        try {
            String content = readFile(fileName);
            String hash = sha1(content.substring(0, content.length()));

            String folderPath = "objects";
            String newFileName = hash;

            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String filePath = folderPath + File.separator + newFileName;

            File newFile = new File(filePath);
            if (!newFile.exists())
            {
                newFile.createNewFile();
            }

            List<String> contents = readFileToList(fileName);

            PrintWriter pw = new PrintWriter(newFile);
            for (int k = 0; k<contents.size(); k++)
            {
                pw.println(contents.get(k));
            }
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
        
        return content.substring(0, content.length());
    }

    static List<String> readFileToList(String fileName) throws IOException
    {
        List<String> contents = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contents.add(line);
            }
        }
        return contents;
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

    public static void grab (String fileName) throws IOException
    {
        file = new File (fileName);
        if (!file.exists())
        {
            file.createNewFile();
        }
    }
}
