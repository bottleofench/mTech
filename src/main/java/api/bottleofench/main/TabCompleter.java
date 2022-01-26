package api.bottleofench.main;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("mtech")) return null;
        if (args.length == 1) {
            List<String> worlds = new ArrayList<>();
            worlds.add("reload"); worlds.add("server-profile");
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

        if (args.length == 2 && Bukkit.getWorlds().contains(Bukkit.getWorld(args[0]))) {
            List<String> settings = new ArrayList<>();

            settings.add("world-profile"); settings.add("spawn-limit"); settings.add("pvp");
            settings.add("gamerule"); settings.add("difficulty"); settings.add("ticks-per-mob-spawn");
            List<String> result = new ArrayList<>();
            for (String b : settings) {
                if (b.toLowerCase().startsWith(args[1].toLowerCase()))
                    result.add(b);
            }
            return result;

        }

        if (args.length == 3 && args[1].equals("ticks-per-mob-spawn")) {
            List<String> settings = new ArrayList<>();
            settings.add("reset"); settings.add("monsters"); settings.add("animals"); settings.add("ambient");
            settings.add("water-ambient"); settings.add("water-animals");

            List<String> result = new ArrayList<>();
            for (String b : settings) {
                if (b.toLowerCase().startsWith(args[2].toLowerCase()))
                    result.add(b);
            }
            return result;
        }

        if (args.length == 3 && args[1].equals("spawn-limit")) {
            List<String> settings = new ArrayList<>();
            settings.add("reset"); settings.add("monsters"); settings.add("animals"); settings.add("ambient");
            settings.add("water-ambient"); settings.add("water-animals");

            List<String> result = new ArrayList<>();
            for (String b : settings) {
                if (b.toLowerCase().startsWith(args[2].toLowerCase()))
                    result.add(b);
            }
            return result;
        }

        if (args.length == 3 && args[1].equals("difficulty")) {
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

        if (args.length == 3 && args[1].equals("pvp")) {
            List<String> bool = new ArrayList<>();
            bool.add("true"); bool.add("false");

            List<String> result = new ArrayList<>();
            for (String b : bool) {
                if (b.toLowerCase().startsWith(args[2].toLowerCase()))
                    result.add(b);
            }
            return result;
        }

        if (args.length == 3 && args[1].equals("gamerule")) {
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

        if (args.length == 4 && args[1].equals("gamerule")) {
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
        return null;
    }
}
