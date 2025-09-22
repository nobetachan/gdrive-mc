package com.neastooid;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;

public class GoogleDriveUtils {

    public static Drive getDriveService(java.io.File credentialsFile) throws IOException {
        GoogleCredential credential = GoogleCredential.fromStream(new java.io.FileInputStream(credentialsFile))
                .createScoped(java.util.Collections.singleton("https://www.googleapis.com/auth/drive.file"));

        return new Drive.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("MinecraftBackupPlugin")
                .build();
    }

    public static void uploadFile(Drive driveService, java.io.File localFile, String folderId) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(localFile.getName());
        fileMetadata.setParents(java.util.Collections.singletonList(folderId));

        FileContent mediaContent = new FileContent("application/zip", localFile);
        driveService.files().create(fileMetadata, mediaContent).execute();
    }
}
