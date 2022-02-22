package api.bottleofench.main;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class LanguageManager {
    private static FileConfiguration info;
    public LanguageManager() {
        if (Main.getInstance().getConfig().getString("language-file") == null) {
            Bukkit.getLogger().info("Failed to create language file! Please download default config file from GitHub or create custom language file!");
            Bukkit.getPluginManager().disablePlugin(Main.getInstance());
        }
        else {
            String loaded_lang = Main.getInstance().getConfig().getString("language-file");

            File languagefile = new File(Main.getInstance().getDataFolder() + File.separator + "lang" + File.separator, loaded_lang);

            if (!languagefile.exists()) {
                new File(Main.getInstance().getDataFolder().getPath() + File.separator + "lang").mkdirs();

                Main.getInstance().saveResource("lang" + File.separator + loaded_lang, true);
                try {
                    languagefile.createNewFile();
                }
                catch(Exception e) {
                    e.printStackTrace();
                    Bukkit.getLogger().info("Failed to create language file!");
                }
            }

            info = YamlConfiguration.loadConfiguration(languagefile);
        }
    }
    public static String get(String key) {
        return info.getString(key);
    }

    public static List<String> getStringList(String key) {
        return info.getStringList(key);
    }
}
