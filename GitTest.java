import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Paths;

public class GitTest {
    @Test
    public void testRemoveAndEdit() {
        try {
            // Add a file to the index
            Git.edit("file.txt");
            
            // Check if the index file has the "edited*" entry
            String indexContents = Blob.readFile("index");
            assertTrue(indexContents.contains("edited* "));
            
            // Remove the file from the index
            Git.remove("file.txt");
            
            // Check if the index file has the "deleted*" entry
            indexContents = Blob.readFile("index");
            assertTrue(indexContents.contains("deleted* "));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateCommitTree() {
        try {
            // Create a commit tree based on the index
            Git.createCommitTree();
            
            // Check if the commit tree is generated successfully
            // You can add assertions here based on your requirements
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCheckout() {
        try {
            // Assuming you have a valid commitSHA1
            String commitSHA1 = "objects/bc79adb7daa83f625050999a43e2fb97a6963a57";
            
            // Create a test working directory
            Files.createDirectories(Paths.get("working_directory"));
            
            // Perform a checkout
            Git.checkout(commitSHA1);
            
            // Check if the working directory is populated correctly
            // You can add assertions here based on your requirements
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}