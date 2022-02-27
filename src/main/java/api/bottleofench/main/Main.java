package api.bottleofench.main;

import com.sun.management.OperatingSystemMXBean;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

public final class Main extends JavaPlugin {

    private static final List<String> good_cores = Arrays.asList("Paper", "Purpur", "Pufferfish");

    private static long LAST_START_TIME = 0L;
    private static JavaPlugin plugin;
    public Main() {
        plugin = this;
    }
    public static Plugin getInstance() {
        return plugin;
    }
    public static List<String> getGoodCores() {
        return good_cores;
    }
    public static long getLastStartTime() {
        return LAST_START_TIME;
    }

    @Override
    @SuppressWarnings("all")
    public void onEnable() {
        LAST_START_TIME = System.currentTimeMillis();

        File config = new File(getDataFolder().getPath() + File.separator + "config.yml");
        if (!config.exists()) {
            saveDefaultConfig();
        }

        if (Main.getInstance().getConfig().getString("config-version") != null) {
            saveDefaultConfig();
        }

        reloadConfig();
        getCommand("mtech").setExecutor(new Command());
        getCommand("mtech").setTabCompleter(new TabCompleter());

        new LangManager();

        Bukkit.getLogger().info(colorChat("&7[&6mTech&7] &aSuccessfully enabled!"));

        Bukkit.getScheduler().runTaskTimer(getInstance(), () -> {
            if (Main.getInstance().getConfig().getBoolean("enable-warnings")) {
                OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                        OperatingSystemMXBean.class);
                Runtime runtime = Runtime.getRuntime();

                if ((osBean.getCpuLoad() * 100) > 90) {
                    Bukkit.getLogger().info(Main.getColorString("cpu-overloaded"));
                }
                long ram = (runtime.maxMemory() - runtime.freeMemory()) / 1048576;
                long clogged_ram_value = (runtime.maxMemory() / 1048576) - 100;
                if (ram >= clogged_ram_value) {
                    Bukkit.getLogger().info(Main.getColorString("ram-clogged"));
                }
            }
        },20,20);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        Bukkit.getLogger().info(Main.colorChat("&7[&6mTech&7] &cSuccessfully disabled!"));
    }

    static String getColorString(String str) {
        return ChatColor.translateAlternateColorCodes('&', LangManager.get(str));
    }

    static String colorChat(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}