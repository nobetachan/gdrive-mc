# gdrive-mc

## Build

```bash
mvn clean package
```

## Test

```bash
mvn test
```

## Struktur

- `src/main/java/com/neastooid/` : Kode utama plugin
- `src/test/java/com/neastooid/` : Unit test (JUnit)
- `src/resources/config.yml` : Konfigurasi plugin
- `src/resources/plugin.yml` : Metadata plugin

## Contoh Penggunaan

1. Letakkan file kredensial Google di lokasi sesuai `config.yml`
2. Jalankan server Minecraft dengan plugin ini
3. Backup otomatis akan berjalan sesuai interval
4. Untuk backup manual, gunakan perintah `/backup`