package ro.steve.gens.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;
import ro.steve.gens.GensMain;
import ro.steve.gens.inventory.GensInventory;
import ro.steve.gens.item.ItemBuilder;
import ro.steve.gens.utils.PersistentChecker;
import ro.steve.gens.utils.SendMessage;

public class GensInventoryListener implements Listener {

    @EventHandler
    public void onGensInventory(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getView().getTopInventory().getHolder() == null) {
            return;
        }
        if (!(event.getView().getTopInventory().getHolder() instanceof GensInventory)) {
            return;
        }
        if (event.getCurrentItem() == null) {
            return;
        }
        event.setResult(Event.Result.DENY);
        var s = GensMain.getInstance().getStorage();
        var c = GensMain.getInstance().getConfiguration().getConfig("gens");
        var l = PersistentChecker.check(event.getCurrentItem(), "gens-location", PersistentDataType.STRING);
        var p = event.getWhoClicked();
        var e = GensMain.getInstance().getEconomy();
        var a = PersistentChecker.check(event.getCurrentItem(), "gens-action", PersistentDataType.STRING);
        if (a == null) {
            return;
        }
        var o = Bukkit.getOfflinePlayer(p.getUniqueId());
        if (a.toString().startsWith("buy_")) {
            var path = "Gens." + a.toString().replace("buy_", "") + ".";
            var money = c.getDouble(path + "upgrade_cost");
            if (p.getInventory().firstEmpty() == -1) {
                SendMessage.sendMessage(p, "inventory-full");
                return;
            }
            if (!e.has(o, money)) {
                SendMessage.sendMessage(p, "not-enough-money", "%money%;" + money);
                p.closeInventory();
                return;
            }
            var i = new ItemBuilder()
                    .type(Material.valueOf(c.getString(path + "material")))
                    .name(c.getString(path + "name"))
                    .lore(c.getStringList(path + "description"), "%time%;" + c.getInt(path + "time"), "%upgrade_cost%;" + c.getDouble(path + "upgrade_cost"))
                    .glowing(true)
                    .withPersistent("gens-tier", PersistentDataType.STRING, a.toString().replace("buy_", ""))
                    .build();
            e.withdrawPlayer(o, money);
            p.getInventory().addItem(i);
            p.closeInventory();
        }
        if (a.toString().equalsIgnoreCase("upgrade")) {
            if (l == null) {
                p.closeInventory();
                SendMessage.sendMessage(p, "dev");
                return;
            }
            String[] split = l.toString().split(";");
            var location = new Location(Bukkit.getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
            var tier = s.getTier(location);
            if (tier == null) {
                p.closeInventory();
                SendMessage.sendMessage(p, "dev");
                return;
            }
            int t = ((int) tier) + 1;
            var path = "Gens.tier_" + t + ".";
            var money = c.getDouble(path + "upgrade_cost");
            if (!e.has(o, money)) {
                SendMessage.sendMessage(p, "not-enough-money", "%money%;" + money);
                p.closeInventory();
                return;
            }
            e.withdrawPlayer(o, money);
            location.getBlock().setType(Material.valueOf(c.getString(path + "material")));
            s.addGen(location, "tier_" + t, true);
            SendMessage.sendMessage(p, "upgraded", "%tier%;" + t);
            p.closeInventory();
        }
        if (a.toString().equalsIgnoreCase("close")) {
            p.closeInventory();
            SendMessage.sendMessage(p, "upgrade-cancelled");
        }
    }
}
