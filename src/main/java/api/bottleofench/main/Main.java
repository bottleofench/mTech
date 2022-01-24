package api.bottleofench.main;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin {

    private static JavaPlugin plugin;

    public Main() {
        plugin = this;
    }
    public static Plugin getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {

        File config = new File(getDataFolder().getPath() + "/config.yml");
        if (!config.exists()) {
            saveDefaultConfig();
        }
        reloadConfig();


        getCommand("mtech").setExecutor(new Command());
        getCommand("mtech").setTabCompleter(new TabCompleter());
    }

    @Override
    public void onDisable() {

    }

    public static String format (String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
