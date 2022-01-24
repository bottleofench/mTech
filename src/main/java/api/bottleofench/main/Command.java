package api.bottleofench.main;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args[0].equals("reload")) {
            Main.getInstance().reloadConfig();
            sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.reload")));
        }
        else {
            if (!Bukkit.getWorlds().contains(Bukkit.getWorld(args[0]))) {
                sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.unknown-world-error")));
            }
            World world = Bukkit.getWorld(args[0]);

            if (args.length == 4 && args[2].equals("monsters")) {
                world.setMonsterSpawnLimit(Integer.parseInt(args[3]));
                sender.sendMessage("Вы успешно установили ограничение на спавн монстров в мире " + args[0] + " на " + args[3]);
            }
            if (args.length == 4 && args[2].equals("animals")) {
                world.setAnimalSpawnLimit(Integer.parseInt(args[3]));
                sender.sendMessage("Вы успешно установили ограничение на спавн животных в мире " + args[0] + " на " + args[3]);
            }
            if (args.length == 4 && args[2].equals("ambient")) {
                world.setAmbientSpawnLimit(Integer.parseInt(args[3]));
                sender.sendMessage("Вы успешно установили ограничение на спавн эмбиент-мобов в мире " + args[0] + " на " + args[3]);
            }

            if (args.length == 2 && args[1].equals("world-profile")) {
                for (String s : Main.getInstance().getConfig().getStringList("messages.world-profile")) {
                    sender.sendMessage(Main.format(s).replace("%world%", args[0])
                            .replace("%entity_count%", String.valueOf(world.getEntityCount()))
                            .replace("%player_count%", String.valueOf(world.getPlayerCount()))
                            .replace("%view_distance%", String.valueOf(world.getViewDistance()))
                            .replace("%loaded_chunks_count%", String.valueOf(Arrays.stream(world.getLoadedChunks()).collect(Collectors.toList()).size())));
                }
            }

            if (args.length == 3 && args[1].equals("pvp")) {
                if (args[2].equals("true") || args[2].equals("false")) {
                    boolean f = Boolean.getBoolean(args[2]);
                    world.setPVP(f);
                    sender.sendMessage(Main.format(Main.getInstance().getConfig().getString("messages.pvp")).replace("%value%", args[2]));
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
