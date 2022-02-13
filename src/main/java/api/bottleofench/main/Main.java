package api.bottleofench.main;

import com.sun.management.OperatingSystemMXBean;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.management.ManagementFactory;

public final class Main extends JavaPlugin {

    private static long LAST_START_TIME = 0L;
    private static JavaPlugin plugin;
    private static final int config_version = 3;

    public Main() {
        plugin = this;
    }
    public static Plugin getInstance() {
        return plugin;
    }

    public static long getLastStartTime() {
        return LAST_START_TIME;
    }

    @Override
    @SuppressWarnings("all")
    public void onEnable() {
        LAST_START_TIME = System.currentTimeMillis();

        File config = new File(getDataFolder().getPath() + File.separator + "config.yml");
        if (!(config.exists()) || (config_version != getInstance().getConfig().getInt("config-version")) || (getInstance().getConfig().getString("config-version") == null)) {
            saveDefaultConfig();
        }
        reloadConfig();
        getCommand("mtech").setExecutor(new Command());
        getCommand("mtech").setTabCompleter(new TabCompleter());

        Bukkit.getLogger().info(Main.colorChat("&7[&6mTech&7] &aSuccessfully enabled!"));

        Bukkit.getScheduler().runTaskTimer(getInstance(), () -> {
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                    OperatingSystemMXBean.class);
            Runtime runtime = Runtime.getRuntime();

            if ((osBean.getCpuLoad() * 100) > 90) {
                Bukkit.getLogger().info(Main.getInstance().getConfig().getString("messages.cpu-overloaded").replace("&", "§"));
            }
            if (((runtime.maxMemory() - runtime.freeMemory()) / 1048576) >= ((runtime.maxMemory() / 1048576) - 100)) {
                Bukkit.getLogger().info(Main.getInstance().getConfig().getString("messages.ram-clogged").replace("&", "§"));
            }
        },20,20);
    }

    @Override
    public void onDisable() {Bukkit.getLogger().info(Main.colorChat("&7[&6mTech&7] &cSuccessfully disabled!"));}

    public static String colorChat(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}