package ro.steve.gens.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.persistence.PersistentDataType;
import ro.steve.gens.GensMain;
import ro.steve.gens.item.ItemBuilder;
import ro.steve.gens.utils.SendMessage;

public class GensGiveCommand {

    public GensGiveCommand(CommandSender s, String[] a) {
        if (!s.hasPermission("gens.give")) {
            SendMessage.sendMessage(s, "no-permission", "%permission%;gens.give");
            return;
        }
        if (a.length <= 2) {
            SendMessage.sendMessage(s, "gens-give-help");
            return;
        }
        if (Bukkit.getPlayer(a[1]) == null) {
            SendMessage.sendMessage(s, "invalid-player", "%player%;" + a[1]);
            return;
        }
        var t = Bukkit.getPlayerExact(a[1]);
        var g = GensMain.getInstance().getConfiguration().getConfig("gens");
        var gen = a[2];
        if (g.get("Gens." + gen) == null) {
            SendMessage.sendMessage(s, "invalid-tier", "%tier%;" + a[2]);
            return;
        }
        var p = "Gens." + gen + ".";
        int amount;
        try {
            amount = Integer.parseInt(a[3]);
            if (amount < 1) {
                amount = amount * -1;
            }
            if (amount == 0) {
                amount = 1;
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e1) {
            amount = 1;
        }
        var i = new ItemBuilder();
        i.type(Material.valueOf(g.getString(p + "material")));
        i.name(g.getString(p + "name"));
        i.lore(g.getStringList(p + "description"), "%time%;" + g.getInt(p + "time"), "%upgrade_cost%;" + g.getDouble(p + "upgrade_cost"));
        i.glowing(true);
        i.amount(amount);
        i.withPersistent("gens-tier", PersistentDataType.STRING, gen);
        if (t.getInventory().firstEmpty() == -1) {
            SendMessage.sendMessage(t, "gens-received-inventory-full",
                    "%player%;" + s.getName(),
                    "%amount%;" + amount,
                    "%tier%;" + a[2].replace("_", " "));
        } else {
            t.getInventory().addItem(i.build());
            SendMessage.sendMessage(t, "gens-received",
                    "%player%;" + s.getName(),
                    "%amount%;" + amount,
                    "%tier%;" + a[2].replace("_", " "));
        }
        SendMessage.sendMessage(s, "gens-give",
                "%player%;" + a[1],
                "%amount%;" + amount,
                "%tier%;" + a[2].replace("_", " "));
    }
}
