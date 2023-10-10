import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

public class MainJunitTest {

    private Commit commit1;
    private Commit commit2;
    private Commit commit3;
    private Commit commit4;

    @BeforeEach
    public void setUp() throws IOException, NoSuchAlgorithmException {
        Index.init();
        File file1 = new File("test_commit1.txt");
        if (!file1.exists()) {
            file1.createNewFile();
        }
        PrintWriter writer1 = new PrintWriter(file1);
        writer1.print("Testing commit method one");
        writer1.close();
        Index.add("test_commit1.txt");

        // Creating Commit 1
        commit1 = new Commit(null, "Author1", "Summary1");
        commit1.commit("commit1.txt");

        Index.init();
        File file2 = new File("test_commit2.txt");
        if (!file2.exists()) {
            file2.createNewFile();
        }
        PrintWriter writer2 = new PrintWriter(file2);
        writer2.print("Testing commit method two");
        writer2.close();
        Index.add("test_commit2.txt");

        // Creating Commit 2
        commit2 = new Commit(commit1.generateSHA1(), "Author2", "Summary2");
        commit2.commit("commit2.txt");

        Index.init();
        File file3 = new File("test_commit3.txt");
        if (!file3.exists()) {
            file3.createNewFile();
        }
        PrintWriter writer3 = new PrintWriter(file3);
        writer3.print("Testing commit method three");
        writer3.close();
        Index.add("test_commit3.txt");

        // Creating Commit 3
        commit3 = new Commit(commit2.generateSHA1(), "Author3", "Summary3");
        commit3.commit("commit3.txt");

        Index.init();
        File file4 = new File("test_commit4.txt");
        if (!file4.exists()) {
            file4.createNewFile();
        }
        PrintWriter writer4 = new PrintWriter(file4);
        writer4.print("Testing commit method four");
        writer4.close();
        Index.add("test_commit4.txt");

        // Creating Commit 4
        commit4 = new Commit(commit3.generateSHA1(), "Author4", "Summary4");
        commit4.commit("commit4.txt");
    }

    @Test
    public void testSingleCommit() throws NoSuchAlgorithmException, IOException {
        assertEquals(commit1.getCommitTreeSHA1(), commit1.generateSHA1());
        assertNull(commit1.getDate());
        assertNull(commit1.getParentCommitSHA1());

        // Perform additional assertions as needed for added files, tree contents, etc.
    }

    @Test
    public void testTwoCommits() throws NoSuchAlgorithmException, IOException {
        assertEquals(commit2.getCommitTreeSHA1(), commit2.generateSHA1());
        assertEquals("treeSHA1_1", commit2.getParentCommitSHA1());
        assertNull(commit2.getDate());

        // Perform additional assertions as needed for added files, tree contents, etc.
    }

    @Test
    public void testFourCommits() throws NoSuchAlgorithmException, IOException {
        assertEquals(commit4.getCommitTreeSHA1(), commit4.generateSHA1());
        assertEquals("treeSHA1_3", commit4.getParentCommitSHA1());
        assertNull(commit4.getDate());

        // Perform additional assertions as needed for added files, tree contents, etc.
    }
}