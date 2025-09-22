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
            getLogger().info("[GdriveMC] === MULAI BACKUP ===");
            File backupFolder = new File(getConfig().getString("backup.backup-folder"));
            getLogger().info("[GdriveMC] Folder backup: " + backupFolder.getAbsolutePath());
            if (!backupFolder.exists()) {
                backupFolder.mkdirs();
                getLogger().info("[GdriveMC] Folder backup dibuat.");
            }

            String credsPath = getConfig().getString("google.credentials-file");
            if (credsPath == null || credsPath.isEmpty()) {
                getLogger().warning("[GdriveMC] Kredensial Google Drive belum diatur di config!");
                getLogger().warning("[GdriveMC] Backup dibatalkan.");
                return;
            }
            File creds = new File(credsPath);
            if (!creds.exists()) {
                getLogger().warning("[GdriveMC] File kredensial tidak ditemukan: " + creds.getAbsolutePath());
                getLogger().warning("[GdriveMC] Backup dibatalkan.");
                return;
            }

            File zipFile = new File(backupFolder, "backup_" + System.currentTimeMillis() + ".zip");
            getLogger().info("[GdriveMC] Membuat file zip: " + zipFile.getName());
            ZipUtils.zipFolder(getServer().getWorldContainer(), zipFile);
            getLogger().info("[GdriveMC] File zip selesai dibuat.");

            // Upload to Google Drive
            getLogger().info("[GdriveMC] Mengambil kredensial Google Drive dari: " + creds.getAbsolutePath());
            var driveService = GoogleDriveUtils.getDriveService(creds);
            getLogger().info("[GdriveMC] Upload file ke Google Drive...");
            GoogleDriveUtils.uploadFile(driveService, zipFile, getConfig().getString("google.folder-id"));
            getLogger().info("[GdriveMC] Upload ke Google Drive selesai.");

            // Hapus file zip lokal setelah upload
            if (zipFile.exists()) {
                if (zipFile.delete()) {
                    getLogger().info("[GdriveMC] File backup lokal dihapus: " + zipFile.getName());
                } else {
                    getLogger().warning("[GdriveMC] Gagal menghapus file backup lokal: " + zipFile.getName());
                }
            }

            getLogger().info("[GdriveMC] === BACKUP SELESAI DAN DIUPLOAD ===");
        } catch (Exception e) {
            getLogger().severe("[GdriveMC] Gagal backup: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
