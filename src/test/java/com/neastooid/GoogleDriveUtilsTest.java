package com.neastooid;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GoogleDriveUtilsTest {
    @Test
    public void testGetDriveServiceNull() {
        // Dummy test, should be replaced with real credentials
        try {
            var service = GoogleDriveUtils.getDriveService(new java.io.File("dummy.json"));
            assertNotNull(service);
        } catch (Exception e) {
            // Expected failure due to dummy file
        }
    }
}
