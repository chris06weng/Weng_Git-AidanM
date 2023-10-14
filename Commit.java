import java.io.*;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commit {
    File commitFile;
    private String treeSHA1;
    private String parentCommitSHA1;
    private String author;
    private String date;
    private String summary;
    private String contents;
    private String commitName;

    public Commit(String parentCommitSHA1, String author, String summary) throws NoSuchAlgorithmException, IOException {
        File commitFile = new File ("Commit");
        if (!commitFile.exists())
        {
            commitFile.createNewFile();
        }
        else
        {
            String empty = "";
            Files.write(commitFile.toPath(), empty.getBytes());
        }

        this.parentCommitSHA1 = parentCommitSHA1;
        this.author = author;
        this.date = generateDate();
        this.summary = summary;

        String currentTree = this.getTreeContents();
        treeSHA1 = Blob.sha1(currentTree);

        PrintWriter pw = new PrintWriter ("Commit");
        pw.println(treeSHA1);
        pw.println(parentCommitSHA1 != null ? parentCommitSHA1 : "");
        pw.println("");
        pw.println(this.author);
        pw.println(date);
        pw.println(this.summary);
        pw.close();

        commitName = Blob.sha1(Blob.readFile("Commit"));
        updateHead();
        if (!parentCommitSHA1.equals("")) {
            updatePrevCommit();
        }

        Blob.blob("Commit");
    }

    private void updatePrevCommit() throws FileNotFoundException, IOException
    {
        File file = new File (parentCommitSHA1);
        List<String> prev = new ArrayList<>();
        try (BufferedReader lineReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = lineReader.readLine()) != null) {
                prev.add(line);
            }
        }

        PrintWriter pw = new PrintWriter (file);
        pw.println(prev.get(0));
        pw.println(prev.get(1));
        pw.println(commitName);
        pw.println(prev.get(3));
        pw.println(prev.get(4));
        pw.println(prev.get(5));
        pw.close();
    }

    private void updateHead() throws IOException
    {
        File head = new File ("Head");
        if (!head.exists())
        {
            head.createNewFile();
        }

        Files.write(head.toPath(), commitName.getBytes());
    }

    public String generateSHA1() throws NoSuchAlgorithmException, IOException {
        contents = Blob.readFile("Commit");
        return Blob.sha1(contents);
    }

    public String getDate() {
        return date;
    }

    public String generateDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(new Date());
    }

    public String getTreeContents() throws NoSuchAlgorithmException, IOException {
        String contents = Tree.copyIndex();
        Tree.save();

        return contents;
    }

    public String getCommitTreeSHA1() throws IOException {
        return treeSHA1;
    }

    public String getName()
    {
        return commitName;
    }

    public String getParentCommitSHA1() {
        return parentCommitSHA1;
    }
}