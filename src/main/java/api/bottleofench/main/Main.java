package api.bottleofench.main;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin {

    private static JavaPlugin plugin;
    private static final int config_version = 1;

    public Main() {
        plugin = this;
    }
    public static Plugin getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        File config = new File(getDataFolder().getPath() + "/config.yml");
        if (!(config.exists()) || (config_version != getInstance().getConfig().getInt("config-version")) || (getInstance().getConfig().getString("config-version") == null)) {
            saveDefaultConfig();
        }
        reloadConfig();
        getCommand("mtech").setExecutor(new Command());
        getCommand("mtech").setTabCompleter(new TabCompleter());
    }

    @Override
    public void onDisable() {}

    public static String format (String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
