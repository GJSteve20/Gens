package ro.steve.gens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.steve.gens.inventory.GensInventory;

public class GensCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (a.length == 0) {
            if (!(s instanceof Player)) {
                new GensHelpCommand(s);
            } else {
                ((Player) s).openInventory(new GensInventory("Buy", null).getInventory());
            }
            return false;
        }
        switch (a[0]) {
            case "give" -> new GensGiveCommand(s, a);
            case "upgrade" -> {
                if (!(s instanceof Player)) {
                    new GensHelpCommand(s);
                } else {
                    new GensUpgradeCommand(s);
                }
            }
            case "sellall" -> {
                if (!(s instanceof Player)) {
                    new GensHelpCommand(s);
                } else {
                    new GensSellAllCommand(s);
                }
            }
            default -> new GensHelpCommand(s);
        }
        return false;
    }
}
