package api.bottleofench.main;

import com.sun.management.OperatingSystemMXBean;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args[0].equals("reload")) {
            Main.getInstance().reloadConfig();
            sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.reload")));
        }
        else if (args[0].equals("server-profile")) {
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                    OperatingSystemMXBean.class);
            for (String s : Main.getInstance().getConfig().getStringList("messages.server-profile")) {
                sender.sendMessage(Main.format(s)
                        .replace("%tps%", String.valueOf(String.format("%.1f", Bukkit.getServer().getTPS()[0])).replace(",", "."))
                        .replace("%os%", System.getProperty("os.name"))
                        .replace("%ram_using%", String.valueOf((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / 1048576))
                        .replace("%ram_max%", String.valueOf(Runtime.getRuntime().maxMemory() / 1048576))
                        .replace("%cpu_using%", String.valueOf((int) (osBean.getCpuLoad() * 100))));
            }
        }
        else {
            if (!Bukkit.getWorlds().contains(Bukkit.getWorld(args[0]))) {
                sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.unknown-world-error")));
            }
            World world = Bukkit.getWorld(args[0]);
            assert world != null;

            if (args.length == 3 && args[2].equals("reset")) {
                world.setMonsterSpawnLimit(70);
                world.setAnimalSpawnLimit(15);
                world.setWaterAnimalSpawnLimit(5);
                world.setAmbientSpawnLimit(10);
                world.setWaterAmbientSpawnLimit(5);
                sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.spawn-limits.reset"))
                        .replace("%world%", args[0]));
            }
            if (args.length == 4 && args[2].equals("monsters")) {
                try {
                    world.setMonsterSpawnLimit(Integer.parseInt(args[3]));
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.spawn-limits.monsters"))
                            .replace("%world%", args[0]).replace("%value%", args[3]));
                }
                catch (NumberFormatException e) {
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                }
            }
            if (args.length == 4 && args[2].equals("animals")) {
                try {
                    world.setAnimalSpawnLimit(Integer.parseInt(args[3]));
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.spawn-limits.animals"))
                            .replace("%world%", args[0]).replace("%value%", args[3]));
                }
                catch (NumberFormatException e) {
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                }
            }
            if (args.length == 4 && args[2].equals("ambient")) {
                try {
                    world.setAmbientSpawnLimit(Integer.parseInt(args[3]));
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.spawn-limits.ambient"))
                            .replace("%world%", args[0]).replace("%value%", args[3]));
                }
                catch (NumberFormatException e) {
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                }
            }
            if (args.length == 4 && args[2].equals("water-animals")) {
                try {
                    world.setWaterAnimalSpawnLimit(Integer.parseInt(args[3]));
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.spawn-limits.water-animals"))
                            .replace("%world%", args[0]).replace("%value%", args[3]));
                }
                catch (NumberFormatException e) {
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                }
            }
            if (args.length == 4 && args[2].equals("water-ambient")) {
                try {
                    world.setWaterAmbientSpawnLimit(Integer.parseInt(args[3]));
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.spawn-limits.water-ambient"))
                            .replace("%world%", args[0]).replace("%value%", args[3]));
                }
                catch (NumberFormatException e) {
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                }
            }
            if (args.length == 2 && args[1].equals("world-profile")) {
                for (String s : Main.getInstance().getConfig().getStringList("messages.world-profile")) {
                    sender.sendMessage(Main.format(s).replace("%world%", args[0])
                            .replace("%entity_count%", String.valueOf(world.getEntityCount()))
                            .replace("%player_count%", String.valueOf(world.getPlayerCount()))
                            .replace("%view_distance%", String.valueOf(world.getViewDistance()))
                            .replace("%sim_distance%", String.valueOf(world.getSimulationDistance()))
                            .replace("%loaded_chunks_count%", String.valueOf(Arrays.stream(world.getLoadedChunks()).collect(Collectors.toList()).size())));
                }
            }

            if (args.length == 3 && args[1].equals("pvp")) {
                if (args[2].equals("true") || args[2].equals("false")) {
                    boolean f = Boolean.getBoolean(args[2]);
                    world.setPVP(f);
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.pvp-mode-change")).replace("%value%", args[2]));
                }
                else {
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                }
            }

            if (args.length == 4 && args[1].equals("gamerule")) {
                if (!Arrays.stream(GameRule.values()).collect(Collectors.toList()).contains(GameRule.getByName(args[2]))) {
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                }
                if (!(args[3].equals("true") || args[3].equals("false") || args[3].matches("-?\\d+(\\.\\d+)?"))) {
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                }
                else {
                    world.setGameRuleValue(args[2], args[3]);
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.gamerule-value-change")
                            .replace("%world%", args[0])));
                }
            }
        }
        return false;
    }
}
