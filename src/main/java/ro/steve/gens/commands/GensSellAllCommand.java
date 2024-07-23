package ro.steve.gens.commands;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import ro.steve.gens.GensMain;
import ro.steve.gens.utils.PersistentChecker;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class GensSellAllCommand {

    public GensSellAllCommand(CommandSender s) {
        var items = new AtomicInteger(0);
        var money = new AtomicDouble(0.0);
        var c = GensMain.getInstance().getConfiguration().getConfig("gens");
        Arrays.stream(((Player) s).getInventory().getContents()).forEach(i -> {
            if (i == null) {
                return;
            }
            var check = PersistentChecker.check(i, "gens-item", PersistentDataType.STRING);
            if (check != null) {
                items.set(items.get() + i.getAmount());
                money.set(money.get() + (i.getAmount() * c.getDouble("Gens." + check + ".item.price")));
                ((Player) s).getInventory().remove(i);
            }
        });
        if (items.get() == 0) {
            //send message no items
        } else {
            GensMain.getInstance().getEconomy().depositPlayer(Bukkit.getOfflinePlayer(((Player) s).getUniqueId()), money.get());
        }
    }
}
