package ro.steve.gens.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ro.steve.gens.GensMain;

public class SendMessage {

    private static final YamlConfiguration m = GensMain.getInstance().getConfiguration().getConfig("messages");

    public static void sendMessage(Player player, String path, String... replaces) {
        if (m.get(path) != null) {
            var message = m.getString(path);
            for (String replace : replaces) {
                String[] split = replace.split(";");
                message = message.replace(split[0], split[1]);
            }
            player.sendMessage(Color.process(message));
        }
    }

    public static void sendMessage(ConsoleCommandSender sender, String path, String... replaces) {
        if (m.get(path) != null) {
            var message = m.getString(path);
            for (String replace : replaces) {
                String[] split = replace.split(";");
                message = message.replace(split[0], split[1]);
            }
            sender.sendMessage(Color.process(message));
        }
    }

    public static void sendMessage(CommandSender sender, String path, String... replaces) {
        if (sender instanceof Player) {
            sendMessage((Player) sender, path, replaces);
        } else {
            sendMessage((ConsoleCommandSender) sender, path, replaces);
        }
    }
}
