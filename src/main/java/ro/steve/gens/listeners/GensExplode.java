package ro.steve.gens.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataType;
import ro.steve.gens.GensMain;
import ro.steve.gens.item.ItemBuilder;

public class GensExplode implements Listener {

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }
        var s = GensMain.getInstance().getStorage();
        var c = GensMain.getInstance().getConfiguration().getConfig("gens");
        event.blockList().forEach(b -> {
            if (!s.isGen(b.getLocation())) {
                return;
            }
            if (s.getTier(b.getLocation()) == null) {
                return;
            }
            var path = "Gens.tier_" + s.getTier(b.getLocation()) + ".";
            var i = new ItemBuilder()
                    .type(Material.valueOf(c.getString(path + "material")))
                    .glowing(true)
                    .name(c.getString(path + "name"))
                    .lore(c.getStringList(path + "description"), "%time%;" + c.getInt(path + "time"), "%upgrade_cost%;" + c.getDouble(path + "upgrade_cost"))
                    .withPersistent("gens-tier", PersistentDataType.STRING, "tier_" + s.getTier(b.getLocation()))
                    .build();
            b.getLocation().getWorld().dropItem(b.getLocation(), i);
            s.removeGen(b.getLocation());
            b.setType(Material.AIR);
        });
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }
        var s = GensMain.getInstance().getStorage();
        var c = GensMain.getInstance().getConfiguration().getConfig("gens");
        event.blockList().forEach(b -> {
            if (!s.isGen(b.getLocation())) {
                return;
            }
            if (s.getTier(b.getLocation()) == null) {
                return;
            }
            var path = "Gens.tier_" + s.getTier(b.getLocation()) + ".";
            var i = new ItemBuilder()
                    .type(Material.valueOf(c.getString(path + "material")))
                    .glowing(true)
                    .name(c.getString(path + "name"))
                    .lore(c.getStringList(path + "description"), "%time%;" + c.getInt(path + "time"), "%upgrade_cost%;" + c.getDouble(path + "upgrade_cost"))
                    .withPersistent("gens-tier", PersistentDataType.STRING, "tier_" + s.getTier(b.getLocation()))
                    .build();
            b.getLocation().getWorld().dropItem(b.getLocation(), i);
            s.removeGen(b.getLocation());
            b.setType(Material.AIR);
        });
    }
}
