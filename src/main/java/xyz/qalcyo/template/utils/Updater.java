package xyz.qalcyo.template.utils;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import xyz.matthewtgm.requisite.util.GuiHelper;
import xyz.matthewtgm.requisite.util.Multithreading;
import xyz.matthewtgm.requisite.util.Notifications;
import xyz.qalcyo.template.ForgeTemplate;
import xyz.qalcyo.template.gui.DownloadConfirmGui;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class Updater {
    public static String updateUrl = "";
    public static String latestTag;
    public static boolean shouldUpdate = false;

    public static void update() {
        Multithreading.runAsync(() -> {
            JsonObject latestRelease = APIUtil.getJSONResponse("https://api.github.com/repos/Qalcyo/" + ForgeTemplate.ID + "/releases/latest");
            latestTag = latestRelease.get("tag_name").getAsString();
            DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(StringUtils.substringBefore(ForgeTemplate.VER, "-"));
            DefaultArtifactVersion latestVersion = new DefaultArtifactVersion(StringUtils.substringBefore(StringUtils.substringAfter(latestTag, "v"), "-"));

            if (ForgeTemplate.VER.contains("BETA") || currentVersion.compareTo(latestVersion) < 0) {
                return;
            }
            updateUrl = latestRelease.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();
            if (!updateUrl.isEmpty()) {
                Notifications.push(ForgeTemplate.NAME, ForgeTemplate.NAME + " has a new update (" + latestTag + ")! Click here to download it automatically!", () -> {
                    GuiHelper.open(new DownloadConfirmGui(Minecraft.getMinecraft().currentScreen));
                });
                shouldUpdate = true;
            }
        });
    }

    public static boolean download(String url, File file) {
        if (file.exists()) return true;
        url = url.replace(" ", "%20");
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            HttpResponse downloadResponse = APIUtil.builder.build().execute(new HttpGet(url));
            byte[] buffer = new byte[1024];

            int read;
            while ((read = downloadResponse.getEntity().getContent().read(buffer)) > 0) {
                fileOut.write(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Adapted from RequisiteLaunchwrapper under LGPLv3
     * https://github.com/Qalcyo/RequisiteLaunchwrapper/blob/main/LICENSE
     */
    public static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Opening Deleter task...");
            try {
                String runtime = getJavaRuntime();
                if (Minecraft.isRunningOnMac) {

                    Desktop.getDesktop().open(ForgeTemplate.jarFile.getParentFile());
                }
                File file = new File(ForgeTemplate.modDir.getParentFile(), "Deleter-1.2.jar");
                Runtime.getRuntime()
                        .exec("\"" + runtime + "\" -jar \"" + file.getAbsolutePath() + "\" \"" + ForgeTemplate.jarFile.getAbsolutePath() + "\"");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.currentThread().interrupt();
        }));
    }

    /**
     * Gets the current Java runtime being used.
     * @link https://stackoverflow.com/a/47925649
     */
    public static String getJavaRuntime() throws IOException {
        String os = System.getProperty("os.name");
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator +
                (os != null && os.toLowerCase(Locale.ENGLISH).startsWith("windows") ? "java.exe" : "java");
        if (!new File(java).isFile()) {
            throw new IOException("Unable to find suitable java runtime at "+java);
        }
        return java;
    }
}
