import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class IndexTest {
    private static final String TEST_FILE = "test.txt";
    private static final String TEST_FILE_CONTENT = "This is a test file content.";
    private static final String TEST_DIR = "test_directory";
    private static final String TEST_SUB_DIR = "sub_directory";

    @Before
    public void setUp() throws Exception {
        // Create the test file with content
        try (PrintWriter writer = new PrintWriter(TEST_FILE, "UTF-8")) {
            writer.println(TEST_FILE_CONTENT);
        }

        File testDir = new File(TEST_DIR);
        if (!testDir.exists()) {
            testDir.mkdirs();
        }

        // Create a subdirectory
        File subDir = new File(TEST_DIR + File.separator + TEST_SUB_DIR);
        if (!subDir.exists()) {
            subDir.mkdirs();
        }
    }

    @After
    public void tearDown() throws Exception {
        // Delete the test file if it exists
        File testFile = new File(TEST_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }

        // Delete the Index file if it exists
        File indexFile = new File("Index");
        if (indexFile.exists()) {
            indexFile.delete();
        }

        // Delete the generated object file if it exists
        String hash = Blob.sha1(TEST_FILE_CONTENT);
        File objectFile = new File("objects", hash);
        if (objectFile.exists()) {
            objectFile.delete();
        }

        File testDir = new File(TEST_DIR);
        if (testDir.exists()) {
            deleteDirectory(testDir);
        }
    }

    @Test
    public void testInit() throws Exception {
        // Test the init method
        Index.init();

        // Check if the "objects" directory and "Index" file were created
        File objectsDir = new File("objects");
        File indexFile = new File("Index");
        assertTrue(objectsDir.exists() && objectsDir.isDirectory());
        assertTrue(indexFile.exists() && indexFile.isFile());
    }

    @Test
    public void testAdd() throws Exception {
        // Test the add method
        Index.init();
        Index.add(TEST_FILE);

        // Check if the index file contains the expected entry
        String hash = Blob.sha1(TEST_FILE_CONTENT);
        String expectedEntry = TEST_FILE + ": " + hash;
        File indexFile = new File("Index");
        assertTrue(indexFile.exists() && indexFile.isFile());

        try (Scanner scanner = new Scanner(indexFile)) {
            boolean found = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals(expectedEntry)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    public void testRemove() throws Exception {
        // Test the remove method
        Index.init();
        Index.add(TEST_FILE);

        // Check if the index file contains the entry
        String hash = Blob.sha1(TEST_FILE_CONTENT);
        String expectedEntry = TEST_FILE + ": " + hash;
        File indexFile = new File("Index");
        assertTrue(indexFile.exists() && indexFile.isFile());

        try (Scanner scanner = new Scanner(indexFile)) {
            boolean found = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals(expectedEntry)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }

        // Remove the entry
        Index.remove(TEST_FILE);

        // Check if the index file no longer contains the entry
        List<String> lines = Files.readAllLines(indexFile.toPath());
        boolean found = false;
        for (String line : lines) {
            if (line.equals(expectedEntry)) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testAddDirectory() throws Exception {
        Index index = new Index();

        // Add a directory to the index
        String treeSHA1 = index.addDirectory(TEST_DIR);

        // Check if the index file was updated
        try (BufferedReader reader = new BufferedReader(new FileReader("Index"))) {
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                if (line.contains("tree : " + treeSHA1 + " : " + TEST_DIR)) {
                    found = true;
                    break;
                }
            }

            assertTrue(found);
        }
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}