import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

public class CommitTest {

    @Test
    public void testTreeSha() {
        try {
            Commit commit = new Commit("prevSha123", "John Doe", "Sample commit message");
            commit.tree = new Tree("sampleTree.txt");

            String treeSha = commit.treeSha();
            assertNotNull(treeSha);
            assertFalse(treeSha.isEmpty());

        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testPush() {
        try {
            Commit commit = new Commit("prevSha123", "John Doe", "Sample commit message");
            commit.tree = new Tree("sampleTree.txt");
            commit.sha = "testSha123";
            commit.nextSha = "nextSha123";

            commit.push();

            File commitFile = new File("objects/testSha123");
            assertTrue(commitFile.exists());

        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetSha() {
        try {
            Commit commit = new Commit("prevSha123", "John Doe", "Sample commit message");
            commit.tree = new Tree("sampleTree.txt");

            String sha = commit.getSha();
            assertNotNull(sha);
            assertFalse(sha.isEmpty());

        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testWriteInNewCommit() {
        try {
            File testDir = new File("testDir");
            testDir.mkdir();

            File treeFile = new File(testDir, "sampleTree.txt");
            FileWriter treeWriter = new FileWriter(treeFile);
            treeWriter.write("Sample tree content");
            treeWriter.close();

            Commit commit = new Commit("prevSha123", "John Doe", "Sample commit message");
            commit.tree = new Tree(treeFile.getAbsolutePath());

            commit.writeInNewCommit();

            File newCommitFile = new File("objects/prevSha123");
            assertTrue(newCommitFile.exists());

            recursiveDelete(testDir);

        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    private void recursiveDelete(File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                recursiveDelete(subFile);
            }
        }
        file.delete();
    }
}
