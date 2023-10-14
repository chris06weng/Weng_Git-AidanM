import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.*;

public class CommitTest {

    @Test
    public void testCreateCommit() throws IOException, NoSuchAlgorithmException {
        // Create a Commit
        String parentCommitSHA1 = "";  // No parent commit
        String author = "Chris Weng";
        String summary = "Initial commit";

        Commit commit = new Commit(parentCommitSHA1, author, summary);

        // Verify Commit Properties
        assertNotNull(commit);

        // Verify the commit file contents
        String expectedContents = commit.getCommitTreeSHA1() + "\n\n\n" + author + "\n" + commit.generateDate() + "\n" + summary;
        String commitContents = TestUtils.readFile("Commit");
        assertEquals(expectedContents + "\n", commitContents);

        // Verify commit tree SHA1
        String commitTreeSHA1 = commit.getCommitTreeSHA1();
        assertNotNull(commitTreeSHA1);
        assertTrue(commitTreeSHA1.length() > 0);
    }

    @Test
    public void testCreateCommitWithParent() throws IOException, NoSuchAlgorithmException {
        // Create a parent Commit
        String parentCommitSHA1 = "";  // No parent commit
        String author1 = "Chris Weng";
        String summary1 = "Initial commit";
        Commit parentCommit = new Commit(parentCommitSHA1, author1, summary1);

        // Create a Commit with a parent
        String author2 = "Mr. Lopez";
        String summary2 = "Second commit";

        Commit commit = new Commit("objects/" + parentCommit.getName(), author2, summary2);
        System.out.println(parentCommit.getName());

        // Verify Commit Properties
        assertNotNull(commit);

        // Verify the commit file contents
        String expectedContents = commit.getCommitTreeSHA1() + "\n" + "objects/" + parentCommit.getName() + "\n\n" + author2 + "\n" + commit.generateDate() + "\n" + summary2 + "\n";
        String commitContents = TestUtils.readFile("Commit");
        assertEquals(expectedContents, commitContents);

        // Verify commit tree SHA1
        String commitTreeSHA1 = commit.getCommitTreeSHA1();
        assertNotNull(commitTreeSHA1);
        assertTrue(commitTreeSHA1.length() > 0);
    }
}