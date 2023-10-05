import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Commit {
    private String treeSHA1;
    private String parentCommitSHA1;
    private String author;
    private String date;
    private String summary;

    public Commit(String treeSHA1, String parentCommitSHA1, String author, String summary) {
        this.treeSHA1 = treeSHA1;
        this.parentCommitSHA1 = parentCommitSHA1;
        this.author = author;
        this.date = generateDate();
        this.summary = summary;
    }

    public void writeToFile(String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println(treeSHA1);
            writer.println(parentCommitSHA1 != null ? parentCommitSHA1 : "");
            writer.println(""); // Placeholder for the SHA1 of the next commit (blank initially)
            writer.println(author);
            writer.println(date);
            writer.println(summary);
        }
    }

    public String generateSHA1() throws NoSuchAlgorithmException, IOException {
        String contents = treeSHA1 + "\n" + (parentCommitSHA1 != null ? parentCommitSHA1 : "") + "\n"
                + "\n" + author + "\n" + date + "\n" + summary;

        return Blob.sha1(contents);
    }

    public String getDate() {
        return date;
    }

    public String createTree(String treeName) throws NoSuchAlgorithmException, IOException {
        Tree tree = new Tree(treeName);
        return tree.getSha();
    }

    public String generateDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(new Date());
    }
}