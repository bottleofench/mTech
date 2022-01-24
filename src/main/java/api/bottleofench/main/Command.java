package api.bottleofench.main;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Bukkit.getWorlds().contains(Bukkit.getWorld(args[0]))) {
            sender.sendMessage("Вы пытаетесь изменить несуществующий мир!");
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
            sender.sendMessage("Профайл мира " + args[0]);
            sender.sendMessage("Кол-во энтити: " + world.getEntityCount());
            sender.sendMessage("Кол-во игроков: " + world.getPlayerCount());
            sender.sendMessage("Дальность прорисовки: " + world.getViewDistance());
            sender.sendMessage("Загружено чанков: " + Arrays.stream(world.getLoadedChunks()).collect(Collectors.toList()).size());
            sender.sendMessage("");
        }

        if (args.length == 4 && args[1].equals("gamerule")) {
            if (!Arrays.stream(GameRule.values()).collect(Collectors.toList()).contains(GameRule.getByName(args[2]))) {
                sender.sendMessage("Что-то пошло не так! Проверьте написание команды!");
            }
            if (!(args[3].equals("true") || args[3].equals("false") || args[3].matches("-?\\d+(\\.\\d+)?"))) {
                sender.sendMessage("Что-то пошло не так! Проверьте написание команды!");
            }
            else {
                world.setGameRuleValue(args[2], args[3]);
                sender.sendMessage("Игровое правило было успешно изменено для мира " + args[0]);
            }
        }

        return false;
    }
}
