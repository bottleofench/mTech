package api.bottleofench.main;

import com.sun.management.OperatingSystemMXBean;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.lang.management.ManagementFactory;
import java.util.Arrays;

public class Command implements CommandExecutor {
    @Override
    @SuppressWarnings("all")
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 0) sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
        else if (args[0].equals("reload")) {
            Main.getInstance().reloadConfig();
            sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.reload")));
        }
        else if (args[0].equals("server-profile")) {
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                    OperatingSystemMXBean.class);
            Runtime runtime = Runtime.getRuntime();

            int chunks = 0;
            for (World w : Bukkit.getWorlds()) {
                chunks += w.getLoadedChunks().length;
            }

            if (Main.getInstance().getConfig().getBoolean("use-hastebin-for-profiles")) {
                StringBuilder profile = new StringBuilder();
                for (String s : Main.getInstance().getConfig().getStringList("messages.server-profile")) {
                    profile.append(ChatColor.stripColor(Main.colorChat(s))
                            .replace("%os%", osBean.getName())
                            .replace("%arch%", osBean.getArch())
                            .replace("%ram_using%", String.valueOf((runtime.maxMemory() - runtime.freeMemory()) / 1048576))
                            .replace("%ram_max%", String.valueOf(runtime.maxMemory() / 1048576))
                            .replace("%cpu_using%", String.valueOf((int) (osBean.getCpuLoad() * 100)))
                            .replace("%core%",  Bukkit.getServer().getVersion())
                            .replace("%uptime%", String.valueOf((System.currentTimeMillis() - Main.getLastStartTime()) / 1000 / 60))
                            .replace("%loaded_chunks_count%", String.valueOf(chunks))
                            .replace("%player_count%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                            .replace("%online-mode%", String.valueOf(Bukkit.getOnlineMode()))).append("\n");
                }

                sender.sendMessage("Server profile: \n" + HastebinAPI.post(profile.toString(), false));
            }
            else {
                for (String s : Main.getInstance().getConfig().getStringList("messages.server-profile")) {
                    sender.sendMessage(Main.colorChat(s)
                            .replace("%os%", osBean.getName())
                            .replace("%arch%", osBean.getArch())
                            .replace("%ram_using%", String.valueOf((runtime.maxMemory() - runtime.freeMemory()) / 1048576))
                            .replace("%ram_max%", String.valueOf(runtime.maxMemory() / 1048576))
                            .replace("%cpu_using%", String.valueOf((int) (osBean.getCpuLoad() * 100)))
                            .replace("%core%",  Bukkit.getServer().getVersion())
                            .replace("%uptime%", String.valueOf((System.currentTimeMillis() - Main.getLastStartTime()) / 1000 / 60))
                            .replace("%loaded_chunks_count%", String.valueOf(chunks))
                            .replace("%player_count%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                            .replace("%online-mode%", String.valueOf(Bukkit.getOnlineMode())));
                }
            }
        }
        else {
            if (!Bukkit.getWorlds().contains(Bukkit.getWorld(args[0]))) {
                sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.unknown-world-error")));
            }
            World world = Bukkit.getWorld(args[0]);
            assert world != null;

            if (args.length == 3) {
                if (args[2].equals("reset") && args[1].equals("spawn-limit")) {
                    world.setMonsterSpawnLimit(70);
                    world.setAnimalSpawnLimit(15);
                    world.setWaterAnimalSpawnLimit(5);
                    world.setAmbientSpawnLimit(10);
                    world.setWaterAmbientSpawnLimit(5);
                    sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.spawn-limits.reset"))
                            .replace("%world%", args[0]));
                }
                if (args[2].equals("reset") && args[1].equals("ticks-per-mob-spawn")) {
                    world.setTicksPerMonsterSpawns(1);
                    world.setTicksPerAnimalSpawns(400);
                    world.setTicksPerWaterSpawns(1);
                    world.setTicksPerWaterUndergroundCreatureSpawns(1);
                    world.setTicksPerAmbientSpawns(1);
                    world.setTicksPerWaterAmbientSpawns(1);
                    sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.ticks-per-mob-spawn.reset"))
                            .replace("%world%", args[0]));
                }
                if (args[1].equals("difficulty")) {
                    if (!Arrays.stream(Difficulty.values()).toList().contains(Difficulty.valueOf(args[2]))) {
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                    }
                    else {
                        world.setDifficulty(Difficulty.valueOf(args[2]));
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.difficulty-value-change")
                                .replace("%world%", args[0])));
                    }
                }
                if (args[1].equals("pvp")) {
                    if (args[2].equals("true") || args[2].equals("false")) {
                        boolean f = Boolean.getBoolean(args[2]);
                        world.setPVP(f);
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.pvp-mode-change")).replace("%value%", args[2]));
                    }
                    else {
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                    }
                }
            }

            if (args.length == 4 && args[1].equals("ticks-per-mob-spawn")) {
                if (args[2].equals("monsters")) {
                    try {
                        world.setTicksPerMonsterSpawns(Integer.parseInt(args[3]));
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.ticks-per-mob-spawn.monsters"))
                                .replace("%world%", args[0]).replace("%value%", args[3]));
                    }
                    catch (NumberFormatException e) {
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                    }
                }
                if (args[2].equals("animals")) {
                    try {
                        world.setTicksPerAnimalSpawns(Integer.parseInt(args[3]));
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.ticks-per-mob-spawn.animals"))
                                .replace("%world%", args[0]).replace("%value%", args[3]));
                    }
                    catch (NumberFormatException e) {
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                    }
                }
                if (args[2].equals("ambient")) {
                    try {
                        world.setTicksPerAmbientSpawns(Integer.parseInt(args[3]));
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.ticks-per-mob-spawn.ambient"))
                                .replace("%world%", args[0]).replace("%value%", args[3]));
                    }
                    catch (NumberFormatException e) {
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                    }
                }
                if (args[2].equals("water-ambient")) {
                    try {
                        world.setTicksPerWaterAmbientSpawns(Integer.parseInt(args[3]));
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.ticks-per-mob-spawn.water-animals"))
                                .replace("%world%", args[0]).replace("%value%", args[3]));
                    }
                    catch (NumberFormatException e) {
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                    }
                }
                if (args[2].equals("water")) {
                    try {
                        world.setTicksPerWaterSpawns(Integer.parseInt(args[3]));
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.ticks-per-mob-spawn.water-ambient"))
                                .replace("%world%", args[0]).replace("%value%", args[3]));
                    }
                    catch (NumberFormatException e) {
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                    }
                }
                if (args[2].equals("underground-creature-spawns")) {
                    try {
                        world.setTicksPerWaterUndergroundCreatureSpawns(Integer.parseInt(args[3]));
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.ticks-per-mob-spawn.water-ambient"))
                                .replace("%world%", args[0]).replace("%value%", args[3]));
                    }
                    catch (NumberFormatException e) {
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                    }
                }
            }

            if (args.length == 4 && args[1].equals("spawn-limit")) {
                if (args[2].equals("monsters")) {
                    try {
                        world.setMonsterSpawnLimit(Integer.parseInt(args[3]));
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.spawn-limits.monsters"))
                                .replace("%world%", args[0]).replace("%value%", args[3]));
                    }
                    catch (NumberFormatException e) {
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                    }
                }
                if (args[2].equals("animals")) {
                    try {
                        world.setAnimalSpawnLimit(Integer.parseInt(args[3]));
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.spawn-limits.animals"))
                                .replace("%world%", args[0]).replace("%value%", args[3]));
                    }
                    catch (NumberFormatException e) {
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                    }
                }
                if (args[2].equals("ambient")) {
                    try {
                        world.setAmbientSpawnLimit(Integer.parseInt(args[3]));
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.spawn-limits.ambient"))
                                .replace("%world%", args[0]).replace("%value%", args[3]));
                    }
                    catch (NumberFormatException e) {
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                    }
                }
                if (args[2].equals("water-animals")) {
                    try {
                        world.setWaterAnimalSpawnLimit(Integer.parseInt(args[3]));
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.spawn-limits.water-animals"))
                                .replace("%world%", args[0]).replace("%value%", args[3]));
                    }
                    catch (NumberFormatException e) {
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                    }
                }
                if (args[2].equals("water-ambient")) {
                    try {
                        world.setWaterAmbientSpawnLimit(Integer.parseInt(args[3]));
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.spawn-limits.water-ambient"))
                                .replace("%world%", args[0]).replace("%value%", args[3]));
                    }
                    catch (NumberFormatException e) {
                        sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                    }
                }
            }
            if (args.length == 2 && args[1].equals("world-profile")) {
                if (Main.getInstance().getConfig().getBoolean("use-hastebin-for-profiles")) {
                    StringBuilder profile = new StringBuilder();
                    for (String s : Main.getInstance().getConfig().getStringList("messages.world-profile")) {
                        profile.append(ChatColor.stripColor(Main.colorChat(s))
                                .replace("%world%", args[0])
                                .replace("%entity_count%", String.valueOf(world.getEntities().size())
                                .replace("%player_count%", String.valueOf(world.getPlayers().size())
                                .replace("%view_distance%", String.valueOf(world.getViewDistance())
                                .replace("%sim_distance%", String.valueOf(world.getSimulationDistance())
                                .replace("%loaded_chunks_count%", String.valueOf(Arrays.stream(world.getLoadedChunks()).toList().size()))))))).append("\n");
                    }

                    sender.sendMessage("World profile: \n" + HastebinAPI.post(profile.toString(), false));
                }
                else {
                    for (String s : Main.getInstance().getConfig().getStringList("messages.world-profile")) {
                        sender.sendMessage(Main.colorChat(s)
                                .replace("%world%", args[0])
                                .replace("%entity_count%", String.valueOf(world.getEntities().size()))
                                .replace("%player_count%", String.valueOf(world.getPlayers().size()))
                                .replace("%view_distance%", String.valueOf(world.getViewDistance()))
                                .replace("%sim_distance%", String.valueOf(world.getSimulationDistance()))
                                .replace("%loaded_chunks_count%", String.valueOf(Arrays.stream(world.getLoadedChunks()).toList().size())));
                    }
                }
            }

            if (args.length == 4 && args[1].equals("gamerule")) {
                if (!Arrays.stream(GameRule.values()).toList().contains(GameRule.getByName(args[2]))) {
                    sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                }
                if (!(args[3].equals("true") || args[3].equals("false") || args[3].matches("-?\\d+(\\.\\d+)?"))) {
                    sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.command-error-syntax")));
                }
                else {
                    world.setGameRuleValue(args[2], args[3]);
                    sender.sendMessage(Main.colorChat(Main.getInstance().getConfig().getString("messages.gamerule-value-change")
                            .replace("%world%", args[0])));
                }
            }
        }
        return false;
    }
}
