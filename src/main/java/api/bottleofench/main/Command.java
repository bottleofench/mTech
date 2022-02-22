package api.bottleofench.main;

import com.sun.management.OperatingSystemMXBean;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;
import java.util.Arrays;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 0) sender.sendMessage(Main.getColorString("command-error-syntax"));
        else if (args[0].equals("reload") && sender.hasPermission("mtech.reload")) {
            if (sender.hasPermission("mtech.reload")) {
                Main.getInstance().reloadConfig();
                new LanguageManager();
                sender.sendMessage(Main.getColorString("reload"));
            }
            else {
                sender.sendMessage(Main.getColorString("no-permissions"));
            }
        }
        else if (args[0].equals("server-profile")) {
            if (sender.hasPermission("mtech.server-profile")) {
                OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                        OperatingSystemMXBean.class);
                Runtime runtime = Runtime.getRuntime();

                int chunks = 0;
                for (World w : Bukkit.getWorlds()) {
                    chunks += w.getLoadedChunks().length;
                }

                if (Main.getInstance().getConfig().getBoolean("use-hastebin-for-profiles")) {
                    StringBuilder profile = new StringBuilder();
                    for (String s : LanguageManager.getStringList("server-profile")) {
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
                    sender.sendMessage(Main.getColorString("hastebin.server-profile").replace("%link%", HastebinAPI.post(profile.toString())));
                }
                else {
                    for (String s : LanguageManager.getStringList("server-profile")) {
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
                sender.sendMessage(Main.getColorString("no-permissions"));
            }
        }
        else if (args[0].equals("player")) {
            if (args.length == 1 || args.length == 2) sender.sendMessage(Main.getColorString("no-permissions"));
            if (args[2].equals("player-profile")) {
                if (sender.hasPermission("mtech.player.player-profile")) {

                    Player p = Bukkit.getPlayerExact(args[1]);

                    if (Main.getInstance().getConfig().getBoolean("use-hastebin-for-profiles")) {
                        StringBuilder profile = new StringBuilder();
                        for (String s : LanguageManager.getStringList("player-profile")) {
                            profile.append(ChatColor.stripColor(Main.colorChat(s))
                                    .replace("%nick%", p.getName())
                                    .replace("%ip%", p.getAddress().getHostName())
                                    .replace("%x%", String.valueOf((int) p.getLocation().getX()))
                                    .replace("%y%", String.valueOf((int) p.getLocation().getY()))
                                    .replace("%z%", String.valueOf((int) p.getLocation().getZ()))
                                    .replace("%health%",  String.valueOf(p.getHealth()))
                                    .replace("%food%", String.valueOf(p.getFoodLevel()))
                                    .replace("%saturation%", String.valueOf(p.getSaturation()))
                                    .replace("%time%", String.valueOf(p.getStatistic(Statistic.TOTAL_WORLD_TIME) / 20 / 60))
                                    .replace("%gamemode%", String.valueOf(p.getGameMode()))
                                    .replace("%deaths%", String.valueOf(p.getStatistic(Statistic.DEATHS)))).append("\n");
                        }
                        sender.sendMessage(Main.getColorString("hastebin.player-profile").replace("%link%", HastebinAPI.post(profile.toString())));
                    }
                    else {
                        for (String s : LanguageManager.getStringList("player-profile")) {
                            sender.sendMessage(Main.colorChat(s)
                                    .replace("%nick%", p.getName())
                                    .replace("%ip%", p.getAddress().getHostName())
                                    .replace("%x%", String.valueOf((int) p.getLocation().getX()))
                                    .replace("%y%", String.valueOf((int) p.getLocation().getY()))
                                    .replace("%z%", String.valueOf((int) p.getLocation().getZ()))
                                    .replace("%health%",  String.valueOf(p.getHealth()))
                                    .replace("%food%", String.valueOf(p.getFoodLevel()))
                                    .replace("%saturation%", String.valueOf(p.getSaturation()))
                                    .replace("%time%", String.valueOf(p.getStatistic(Statistic.TOTAL_WORLD_TIME) / 20 / 60 / 60))
                                    .replace("%gamemode%", String.valueOf(p.getGameMode()))
                                    .replace("%deaths%", String.valueOf(p.getStatistic(Statistic.DEATHS))));
                        }
                    }
                }
                else {
                    sender.sendMessage(Main.getColorString("no-permissions"));
                }
            }
        }
        else {
            if (!Bukkit.getWorlds().contains(Bukkit.getWorld(args[0]))) {
                sender.sendMessage(Main.getColorString("unknown-world-error"));
            }
            else {
                World world = Bukkit.getWorld(args[0]);
                assert world != null;
                if (args.length == 3) {
                    if (args[2].equals("reset") && args[1].equals("spawn-limit")) {
                        if (sender.hasPermission("mtech.world.spawn-limit")) {
                            world.setMonsterSpawnLimit(70);
                            world.setAnimalSpawnLimit(15);
                            world.setWaterAnimalSpawnLimit(5);
                            world.setAmbientSpawnLimit(10);
                            world.setWaterAmbientSpawnLimit(5);
                            sender.sendMessage(Main.getColorString("spawn-limits.reset")
                                    .replace("%world%", args[0]));
                        }
                        else {
                            sender.sendMessage(Main.getColorString("no-permissions"));
                        }
                    }
                    if (args[2].equals("reset") && args[1].equals("ticks-per-mob-spawn")) {
                        if (sender.hasPermission("mtech.world.ticks-per-mob-spawn.reset")) {
                            world.setTicksPerMonsterSpawns(1);
                            world.setTicksPerAnimalSpawns(400);
                            world.setTicksPerWaterSpawns(1);
                            world.setTicksPerWaterUndergroundCreatureSpawns(1);
                            world.setTicksPerAmbientSpawns(1);
                            world.setTicksPerWaterAmbientSpawns(1);
                            sender.sendMessage(Main.getColorString("ticks-per-mob-spawn.reset")
                                    .replace("%world%", args[0]));
                        }
                        else {
                            sender.sendMessage(Main.getColorString("no-permissions"));
                        }
                    }
                    if (args[1].equals("difficulty")) {
                        if (sender.hasPermission("mtech.world.difficulty")) {
                            if (!Arrays.stream(Difficulty.values()).toList().contains(Difficulty.valueOf(args[2]))) {
                                sender.sendMessage(Main.getColorString("command-error-syntax"));
                            } else {
                                world.setDifficulty(Difficulty.valueOf(args[2]));
                                sender.sendMessage(Main.colorChat(Main.getColorString("difficulty-value-change")
                                        .replace("%world%", args[0])));
                            }
                        }
                        else {
                            sender.sendMessage(Main.getColorString("no-permissions"));
                        }
                    }
                    if (args[1].equals("pvp")) {
                        if (sender.hasPermission("mtech.world.pvp")) {
                            if (args[2].equals("true") || args[2].equals("false")) {
                                boolean f = Boolean.getBoolean(args[2]);
                                world.setPVP(f);
                                sender.sendMessage(Main.getColorString("pvp-mode-change").replace("%value%", args[2]));
                            } else {
                                sender.sendMessage(Main.getColorString("command-error-syntax"));
                            }
                        }
                    }
                }
                else if (args.length == 4 && args[1].equals("ticks-per-mob-spawn")) {
                    if (args[2].equals("monsters")) {
                        if (sender.hasPermission("mtech.world.ticks-per-mob-spawn.monsters")) {
                            try {
                                world.setTicksPerMonsterSpawns(Integer.parseInt(args[3]));
                                sender.sendMessage(Main.getColorString("ticks-per-mob-spawn.monsters")
                                        .replace("%world%", args[0]).replace("%value%", args[3]));
                            } catch (NumberFormatException e) {
                                sender.sendMessage(Main.getColorString("command-error-syntax"));
                            }
                        }
                        else {
                            sender.sendMessage(Main.getColorString("no-permissions"));
                        }
                    }
                    if (args[2].equals("animals")) {
                        if (sender.hasPermission("mtech.world.ticks-per-mob-spawn.animals")) {
                            try {
                                world.setTicksPerAnimalSpawns(Integer.parseInt(args[3]));
                                sender.sendMessage(Main.getColorString("ticks-per-mob-spawn.animals")
                                        .replace("%world%", args[0]).replace("%value%", args[3]));
                            } catch (NumberFormatException e) {
                                sender.sendMessage(Main.getColorString("command-error-syntax"));
                            }
                        }
                        else {
                            sender.sendMessage(Main.getColorString("no-permissions"));
                        }
                    }
                    if (args[2].equals("ambient")) {
                        if (sender.hasPermission("mtech.world.ticks-per-mob-spawn.ambient")) {
                            try {
                                world.setTicksPerAmbientSpawns(Integer.parseInt(args[3]));
                                sender.sendMessage(Main.getColorString("ticks-per-mob-spawn.ambient")
                                        .replace("%world%", args[0]).replace("%value%", args[3]));
                            } catch (NumberFormatException e) {
                                sender.sendMessage(Main.getColorString("command-error-syntax"));
                            }
                        }
                        else {
                            sender.sendMessage(Main.getColorString("no-permissions"));
                        }
                    }
                    if (args[2].equals("water-ambient")) {
                        if (sender.hasPermission("mtech.world.ticks-per-mob-spawn.water-ambient")) {
                            try {
                                world.setTicksPerWaterAmbientSpawns(Integer.parseInt(args[3]));
                                sender.sendMessage(Main.getColorString("ticks-per-mob-spawn.water-animals")
                                        .replace("%world%", args[0]).replace("%value%", args[3]));
                            } catch (NumberFormatException e) {
                                sender.sendMessage(Main.getColorString("command-error-syntax"));
                            }
                        }
                        else {
                            sender.sendMessage(Main.getColorString("no-permissions"));
                        }
                    }
                    if (args[2].equals("water")) {
                        if (sender.hasPermission("mtech.world.ticks-per-mob-spawn.water")) {
                            try {
                                world.setTicksPerWaterSpawns(Integer.parseInt(args[3]));
                                sender.sendMessage(Main.getColorString("ticks-per-mob-spawn.water-ambient")
                                        .replace("%world%", args[0]).replace("%value%", args[3]));
                            } catch (NumberFormatException e) {
                                sender.sendMessage(Main.getColorString("command-error-syntax"));
                            }
                        }
                        else {
                            sender.sendMessage(Main.getColorString("no-permissions"));
                        }
                    }
                    else {
                        sender.sendMessage(Main.getColorString("no-permissions"));
                    }
                }
                else if (args.length == 4 && args[1].equals("spawn-limit")) {
                        if (args[2].equals("monsters")) {
                            if (sender.hasPermission("mtech.world.spawn-limit.monsters")) {
                                try {
                                    world.setMonsterSpawnLimit(Integer.parseInt(args[3]));
                                    sender.sendMessage(Main.getColorString("spawn-limits.monsters")
                                            .replace("%world%", args[0]).replace("%value%", args[3]));
                                } catch (NumberFormatException e) {
                                    sender.sendMessage(Main.getColorString("command-error-syntax"));
                                }
                            }
                            else {
                                sender.sendMessage(Main.getColorString("no-permissions"));
                            }
                        }
                        if (args[2].equals("animals")) {
                            if (sender.hasPermission("mtech.world.spawn-limit.animals")) {
                                try {
                                    world.setAnimalSpawnLimit(Integer.parseInt(args[3]));
                                    sender.sendMessage(Main.getColorString("spawn-limits.animals")
                                            .replace("%world%", args[0]).replace("%value%", args[3]));
                                } catch (NumberFormatException e) {
                                    sender.sendMessage(Main.getColorString("command-error-syntax"));
                                }
                            }
                            else {
                                sender.sendMessage(Main.getColorString("no-permissions"));
                            }
                        }
                        if (args[2].equals("ambient")) {
                            if (sender.hasPermission("mtech.world.spawn-limit.ambient")) {
                                try {
                                    world.setAmbientSpawnLimit(Integer.parseInt(args[3]));
                                    sender.sendMessage(Main.getColorString("spawn-limits.ambient")
                                            .replace("%world%", args[0]).replace("%value%", args[3]));
                                } catch (NumberFormatException e) {
                                    sender.sendMessage(Main.getColorString("command-error-syntax"));
                                }
                            }
                            else {
                                sender.sendMessage(Main.getColorString("no-permissions"));
                            }
                        }
                        if (args[2].equals("water-animals")) {
                            if (sender.hasPermission("mtech.world.spawn-limit.water-animals")) {
                                try {
                                    world.setWaterAnimalSpawnLimit(Integer.parseInt(args[3]));
                                    sender.sendMessage(Main.getColorString("spawn-limits.water-animals")
                                            .replace("%world%", args[0]).replace("%value%", args[3]));
                                } catch (NumberFormatException e) {
                                    sender.sendMessage(Main.getColorString("command-error-syntax"));
                                }
                            }
                            else {
                                sender.sendMessage(Main.getColorString("no-permissions"));
                            }
                        }
                        if (args[2].equals("water-ambient")) {
                            if (sender.hasPermission("mtech.world.spawn-limit.water-ambient")) {
                                try {
                                    world.setWaterAmbientSpawnLimit(Integer.parseInt(args[3]));
                                    sender.sendMessage(Main.getColorString("spawn-limits.water-ambient")
                                            .replace("%world%", args[0]).replace("%value%", args[3]));
                                } catch (NumberFormatException e) {
                                    sender.sendMessage(Main.getColorString("command-error-syntax"));
                                }
                            }
                            else {
                                sender.sendMessage(Main.getColorString("no-permissions"));
                            }
                        }
                }
                else if (args.length == 2 && args[1].equals("world-profile")) {
                    if (sender.hasPermission("mtech.world.world-profile")) {
                        long tile_entity_count = 0L;
                        for (Chunk chunk : world.getLoadedChunks()) {
                            tile_entity_count += chunk.getTileEntities().length;
                        }
                        if (Main.getInstance().getConfig().getBoolean("use-hastebin-for-profiles")) {
                            StringBuilder profile = new StringBuilder();
                            for (String s : LanguageManager.getStringList("world-profile")) {
                                profile.append(ChatColor.stripColor(Main.colorChat(s))
                                        .replace("%world%", args[0])
                                        .replace("%entity_count%", String.valueOf(world.getEntities().size()))
                                        .replace("%player_count%", String.valueOf(world.getPlayers().size()))
                                        .replace("%view_distance%", String.valueOf(world.getViewDistance()))
                                        .replace("%sim_distance%", String.valueOf(world.getSimulationDistance()))
                                        .replace("%living_entity_count%", String.valueOf(world.getLivingEntities().size()))
                                        .replace("%tile_entity_count%", String.valueOf(tile_entity_count))
                                        .replace("%worldborder%", String.valueOf(world.getWorldBorder().getSize()))
                                        .replace("%loaded_chunks_count%", String.valueOf(Arrays.stream(world.getLoadedChunks()).toList().size()))).append("\n");
                            }
                            sender.sendMessage(Main.getColorString("hastebin.world-profile").replace("%link%", HastebinAPI.post(profile.toString())));
                        }
                        else {
                            for (String s : LanguageManager.getStringList("world-profile")) {
                                sender.sendMessage(Main.colorChat(s)
                                        .replace("%world%", args[0])
                                        .replace("%entity_count%", String.valueOf(world.getEntities().size()))
                                        .replace("%player_count%", String.valueOf(world.getPlayers().size()))
                                        .replace("%view_distance%", String.valueOf(world.getViewDistance()))
                                        .replace("%sim_distance%", String.valueOf(world.getSimulationDistance()))
                                        .replace("%living_entity_count%", String.valueOf(world.getLivingEntities().size()))
                                        .replace("%tile_entity_count%", String.valueOf(tile_entity_count))
                                        .replace("%worldborder%", String.valueOf(world.getWorldBorder().getSize()))
                                        .replace("%loaded_chunks_count%", String.valueOf(Arrays.stream(world.getLoadedChunks()).toList().size())));
                            }
                        }
                    }
                    else {
                        sender.sendMessage(Main.getColorString("no-permissions"));
                    }
                } else if (args.length == 4 && args[1].equals("gamerule")) {
                    if (sender.hasPermission("mtech.world.gamerule")) {
                        if (!Arrays.stream(GameRule.values()).toList().contains(GameRule.getByName(args[2]))) {
                            sender.sendMessage(Main.getColorString("command-error-syntax"));
                        }
                        if (!(args[3].equals("true") || args[3].equals("false") || args[3].matches("-?\\d+(\\.\\d+)?"))) {
                            sender.sendMessage(Main.getColorString("command-error-syntax"));
                        } else {
                            world.setGameRuleValue(args[2], args[3]);
                            sender.sendMessage(Main.getColorString("gamerule-value-change")
                                    .replace("%world%", args[0]));
                        }
                    }
                    else {
                        sender.sendMessage(Main.getColorString("no-permissions"));
                    }
                } else {
                    sender.sendMessage(Main.getColorString("command-error-syntax"));
                }
            }
        }
        return false;
    }
}
