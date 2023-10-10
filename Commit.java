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

    public Commit(String parentCommitSHA1, String author, String summary) throws NoSuchAlgorithmException, IOException {
        this.treeSHA1 = createTree();
        this.parentCommitSHA1 = parentCommitSHA1;
        this.author = author;
        this.date = generateDate();
        this.summary = summary;
    }

    public void commit(String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("objects" + File.separator + filePath))) {
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

    public String generateDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(new Date());
    }

    public String createTree() throws NoSuchAlgorithmException, IOException {
        // Create a Tree with the contents of the index file
        Tree tree = new Tree();
        tree.copyIndex();

        // Clear out the index file after transferring contents
        Index.reset();

        // Include an additional entry to the previous Tree
        tree.add("tree : " + tree.getSha());

        return tree.getSha();
    }

    public String getCommitTreeSHA1() throws IOException {
        return treeSHA1;
    }

    public String getParentCommitSHA1() {
        return parentCommitSHA1;
    }
}