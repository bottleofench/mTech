package api.bottleofench.main;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> worlds = new ArrayList<>();
            if (sender.hasPermission("mtech.reload")) worlds.add("reload");
            if (sender.hasPermission("mtech.player")) worlds.add("player");
            if (sender.hasPermission("mtech.server-profile")) worlds.add("server-profile");
            for (World world : Bukkit.getWorlds()) {
                worlds.add(world.getName());
            }

            List<String> result = new ArrayList<>();
            for (String b : worlds) {
                if (b.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(b);
            }
            return result;

        }

        if (args.length == 2 && args[0].equals("player")) {
            List<String> players = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                players.add(player.getName());
            }

            List<String> result = new ArrayList<>();
            for (String b : players) {
                if (b.toLowerCase().startsWith(args[1].toLowerCase()))
                    result.add(b);
            }
            return result;

        }

        if (args.length == 3 && args[0].equals("player")) {
            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayerExact(args[1]))) {
                List<String> settings = new ArrayList<>();

                if (sender.hasPermission("mtech.player.player-profile")) settings.add("player-profile");

                List<String> result = new ArrayList<>();
                for (String b : settings) {
                    if (b.toLowerCase().startsWith(args[2].toLowerCase()))
                        result.add(b);
                }
                return result;
            }
        }

        if (args.length == 2 && Bukkit.getWorlds().contains(Bukkit.getWorld(args[0]))) {
            List<String> settings = new ArrayList<>();

            if (sender.hasPermission("mtech.world.world-profile")) settings.add("world-profile");
            if (sender.hasPermission("mtech.world.spawn-limit")) settings.add("spawn-limit");
            if (sender.hasPermission("mtech.world.pvp")) settings.add("pvp");
            if (sender.hasPermission("mtech.world.gamerule")) settings.add("gamerule");
            if (sender.hasPermission("mtech.world.difficulty")) settings.add("difficulty");
            if (sender.hasPermission("mtech.world.ticks-per-mob-spawn")) settings.add("ticks-per-mob-spawn");
            List<String> result = new ArrayList<>();
            for (String b : settings) {
                if (b.toLowerCase().startsWith(args[1].toLowerCase()))
                    result.add(b);
            }
            return result;

        }

        if (args.length == 3 && args[1].equals("ticks-per-mob-spawn")) {
            List<String> settings = new ArrayList<>();
            if (sender.hasPermission("mtech.world.ticks-per-mob-spawn.reset")) settings.add("reset");
            if (sender.hasPermission("mtech.world.ticks-per-mob-spawn.monsters")) settings.add("monsters");
            if (sender.hasPermission("mtech.world.ticks-per-mob-spawn.animals")) settings.add("animals");
            if (sender.hasPermission("mtech.world.ticks-per-mob-spawn.ambient")) settings.add("ambient");
            if (sender.hasPermission("mtech.world.ticks-per-mob-spawn.water-ambient")) settings.add("water-ambient");
            if (sender.hasPermission("mtech.world.ticks-per-mob-spawn.water-animals")) settings.add("water-animals");

            List<String> result = new ArrayList<>();
            for (String b : settings) {
                if (b.toLowerCase().startsWith(args[2].toLowerCase()))
                    result.add(b);
            }
            return result;
        }

        if (args.length == 3 && args[1].equals("spawn-limit")) {
            List<String> settings = new ArrayList<>();
            if (sender.hasPermission("mtech.world.spawn-limit.reset")) settings.add("reset");
            if (sender.hasPermission("mtech.world.spawn-limit.monsters")) settings.add("monsters");
            if (sender.hasPermission("mtech.world.spawn-limit.animals")) settings.add("animals");
            if (sender.hasPermission("mtech.world.spawn-limit.ambient")) settings.add("ambient");
            if (sender.hasPermission("mtech.world.spawn-limit.water-ambient")) settings.add("water-ambient");
            if (sender.hasPermission("mtech.world.spawn-limit.water-animals")) settings.add("water-animals");

            List<String> result = new ArrayList<>();
            for (String b : settings) {
                if (b.toLowerCase().startsWith(args[2].toLowerCase()))
                    result.add(b);
            }
            return result;
        }

        if (args.length == 3 && args[1].equals("difficulty")) {
            if (sender.hasPermission("mtech.world.difficulty")) {
                List<String> difficulties = new ArrayList<>();
                for (Difficulty difficulty : Difficulty.values()) {
                    difficulties.add(difficulty.name());
                }

                List<String> result = new ArrayList<>();
                for (String b : difficulties) {
                    if (b.toLowerCase().startsWith(args[2].toLowerCase()))
                        result.add(b);
                }
                return result;
            }
        }

        if (args.length == 3 && args[1].equals("pvp")) {
            if (sender.hasPermission("mtech.world.pvp")) {
                List<String> bool = new ArrayList<>();
                bool.add("true"); bool.add("false");

                List<String> result = new ArrayList<>();
                for (String b : bool) {
                    if (b.toLowerCase().startsWith(args[2].toLowerCase()))
                        result.add(b);
                }
                return result;
            }
        }

        if (args.length == 3 && args[1].equals("gamerule")) {
            if (sender.hasPermission("mtech.world.gamerule")) {
                List<String> settings = new ArrayList<>();
                for (GameRule gameRule : GameRule.values()) {
                    settings.add(gameRule.getName());
                }

                List<String> result = new ArrayList<>();
                for (String b : settings) {
                    if (b.toLowerCase().startsWith(args[2].toLowerCase()))
                        result.add(b);
                }
                return result;
            }
        }

        if (args.length == 4 && args[1].equals("gamerule")) {
            if (sender.hasPermission("mtech.world.gamerule")) {
                List<String> gameRules = new ArrayList<>();
                List<String> bool = new ArrayList<>();
                bool.add("true"); bool.add("false");
                gameRules.add(GameRule.SPAWN_RADIUS.getName()); gameRules.add(GameRule.MAX_COMMAND_CHAIN_LENGTH.getName());
                gameRules.add(GameRule.MAX_ENTITY_CRAMMING.getName()); gameRules.add(GameRule.PLAYERS_SLEEPING_PERCENTAGE.getName());
                gameRules.add(GameRule.RANDOM_TICK_SPEED.getName());

                if (!gameRules.contains(GameRule.getByName(args[2]).getName())) {
                    List<String> result = new ArrayList<>();
                    for (String b : bool) {
                        if (b.toLowerCase().startsWith(args[3].toLowerCase()))
                            result.add(b);
                    }
                    return result;
                }
                return null;
            }
        }
        return null;
    }
}
