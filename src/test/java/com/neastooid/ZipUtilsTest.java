package com.neastooid;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZipUtilsTest {
    @Test
    public void testZipFolderCreatesZipFile() throws IOException {
        File tempDir = new File("test-folder");
        tempDir.mkdir();
        File testFile = new File(tempDir, "test.txt");
        testFile.createNewFile();
        File zipFile = new File("test.zip");
        ZipUtils.zipFolder(tempDir, zipFile);
        assertTrue(zipFile.exists());
        // Cleanup
        testFile.delete();
        tempDir.delete();
        zipFile.delete();
    }
}
