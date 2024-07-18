package ro.steve.gens.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataType;
import ro.steve.gens.GensMain;
import ro.steve.gens.inventory.GensInventory;
import ro.steve.gens.item.ItemBuilder;

public class GensBreak implements Listener {

    @EventHandler
    public void onGensBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        var s = GensMain.getInstance().getStorage();
        var c = GensMain.getInstance().getConfiguration().getConfig("gens");
        var l = event.getBlock().getLocation();
        var p = event.getPlayer();
        if (!s.isGen(l)) {
            return;
        }
        if (s.getTier(l) == null) {
            //send error message
        } else {
            var path = "Gens.tier_" + s.getTier(l) + ".";
            var i = new ItemBuilder()
                    .type(Material.valueOf(c.getString(path + "material")))
                    .name(c.getString(path + "name"))
                    .lore(c.getStringList(path + "description"), "%time%;" + c.getInt(path + "time"), "%upgrade_cost%;" + c.getDouble(path + "upgrade_cost"))
                    .withPersistent("gens-tier", PersistentDataType.STRING, "tier_" + s.getTier(l))
                    .glowing(true)
                    .build();
            if (p.getInventory().firstEmpty() == -1) {
                p.getWorld().dropItem(p.getLocation(), i);
            } else {
                p.getInventory().addItem(i);
            }
        }
        s.removeGen(l);
    }
}
