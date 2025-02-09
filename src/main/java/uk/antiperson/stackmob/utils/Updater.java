package uk.antiperson.stackmob.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class Updater {

    private final String resourceId;
    private final Plugin sm;
    public Updater(Plugin sm, String resourceId) {
        this.sm = sm;
        this.resourceId = resourceId;
    }

    public CompletableFuture<UpdateResult> checkUpdate() {
        return CompletableFuture.supplyAsync(() -> {
           String latestVersion = getLatestVersion().get("version_number").getAsString();;
           if (latestVersion == null) {
               return new UpdateResult(VersionResult.ERROR);
           }
           String strippedLatest = latestVersion.replaceAll("[^A-Za-z0-9]", "");
           String strippedCurrent = sm.getDescription().getVersion().replaceAll("[^A-Za-z0-9]", "");
           if (strippedCurrent.equals(strippedLatest)) {
               return new UpdateResult(VersionResult.NONE);
           }
           return new UpdateResult(VersionResult.AVAILABLE, latestVersion);
        });
    }

    private JsonObject getLatestVersion(){
        try{
            URL updateUrl = new URL("https://api.modrinth.com/v2/project/" + resourceId + "/version");
            HttpURLConnection connect = (HttpURLConnection) updateUrl.openConnection();
            connect.setRequestMethod("GET");
            connect.setRequestProperty("Accept", "application/json");
            connect.setRequestProperty("Content-Type", "application/json");
            connect.setRequestProperty("User-Agent", sm.getName());
            BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            JsonArray object = JsonParser.parseReader(reader).getAsJsonArray();
            reader.close();
            return object.get(0).getAsJsonObject();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public CompletableFuture<Utilities.DownloadResult> downloadUpdate() {
        return CompletableFuture.supplyAsync(() -> {
            File currentFile = new File(sm.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
            File updateFile = new File(sm.getServer().getUpdateFolderFile(), currentFile.getName());
            JsonObject latestVersion = getLatestVersion();
            String updateUrl = latestVersion.getAsJsonArray("files").get(0).getAsJsonObject().get("url").getAsString();
            return Utilities.downloadFile(updateFile, updateUrl);
        });
    }

    public enum VersionResult {
        AVAILABLE,
        NONE,
        ERROR
    }

    public class UpdateResult {

        private final VersionResult result;
        private String newVersion;
        UpdateResult(VersionResult result, String newVersion) {
            this.result = result;
            this.newVersion = newVersion;
        }

        UpdateResult(VersionResult result) {
            this.result = result;
        }

        public VersionResult getResult() {
            return result;
        }

        public String getNewVersion() {
            return newVersion;
        }
    }
}
