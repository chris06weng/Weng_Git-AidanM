import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Commit {

    private String sha, author, desc, treeSha, prevSha, nextSha;
    private Tree tree;

    public Commit(String sha, String author, String desc) throws IOException, NoSuchAlgorithmException {
        prevSha = sha;
        treeSha = treeSha();
        this.author = author;
        this.desc = desc;
    }

    public Commit(String author, String desc) throws IOException, NoSuchAlgorithmException {
        treeSha = treeSha();
        this.author = author;
        this.desc = desc;
    }

    public String treeSha() throws IOException, NoSuchAlgorithmException {
        return tree.getSha();
    }

    public String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public void push() throws IOException {
        FileWriter write = new FileWriter(new File("objects/" + sha));
        write.write(treeSha + "\n" + prevSha + "\n" + nextSha + "\n" + author + "\n" + getDate() + "\n" + desc);
        write.close();
    }

    public String getSha() {
        String input = (treeSha + "\n" + prevSha + "\n" + author + "\n" + getDate() + "\n" + desc);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeInNewCommit() throws IOException {
        File orginalFile = new File("objects/" + prevSha);
        File newFile = new File("balls");
        BufferedReader reader = new BufferedReader(new FileReader(orginalFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
        String curr;
        int i = 0;

        while ((curr = reader.readLine()) != null) {
            if (i == 2) {
                writer.write(getSha());
            } else
                writer.write(curr);

            if (i != 5)
                writer.write("\n");
            i++;
        }
        writer.close();
        reader.close();
        newFile.renameTo(orginalFile);
    }

}