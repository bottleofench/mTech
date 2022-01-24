package api.bottleofench.main;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!sender.hasPermission("mtech")) return null;
        if (args.length == 1) {
            List<String> worlds = new ArrayList<>();
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

        if (args.length == 2) {
            List<String> settings = new ArrayList<>();

            settings.add("world-profile"); settings.add("spawn-limit"); settings.add("view-distance"); settings.add("pvp");
            settings.add("gamerule");
            List<String> result = new ArrayList<>();
            for (String b : settings) {
                if (b.toLowerCase().startsWith(args[1].toLowerCase()))
                    result.add(b);
            }
            return result;

        }

        if (args.length == 3 && args[1].equals("spawn-limit")) {
            List<String> settings = new ArrayList<>();
            settings.add("monsters"); settings.add("animals"); settings.add("ambient");

            List<String> result = new ArrayList<>();
            for (String b : settings) {
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
        return null;
    }
}
