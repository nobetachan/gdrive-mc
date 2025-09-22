package com.neastooid;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class GdriveMCPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("Gdrive-MC by NESSID enabled!");

        int interval = getConfig().getInt("backup.interval-minutes") * 60 * 20; // ticks
        getServer().getScheduler().runTaskTimerAsynchronously(this, this::doBackup, 0L, interval);
    }

    private void doBackup() {
        try {
            File backupFolder = new File(getConfig().getString("backup.backup-folder"));
            if (!backupFolder.exists()) backupFolder.mkdirs();

            File zipFile = new File(backupFolder, "backup_" + System.currentTimeMillis() + ".zip");
            ZipUtils.zipFolder(getServer().getWorldContainer(), zipFile);

            // Upload to Google Drive
            File creds = new File(getConfig().getString("google.credentials-file"));
            var driveService = GoogleDriveUtils.getDriveService(creds);
            GoogleDriveUtils.uploadFile(driveService, zipFile, getConfig().getString("google.folder-id"));

            // Hapus file zip lokal setelah upload
            if (zipFile.exists()) {
                if (zipFile.delete()) {
                    getLogger().info("File backup lokal dihapus: " + zipFile.getName());
                } else {
                    getLogger().warning("Gagal menghapus file backup lokal: " + zipFile.getName());
                }
            }

            getLogger().info("Backup berhasil dan diupload ke Google Drive!");
        } catch (Exception e) {
            getLogger().severe("Gagal backup: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
