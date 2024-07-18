package ro.steve.gens.listeners;

import org.bukkit.entity.Enderman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import ro.steve.gens.GensMain;

public class EntityChangeBlock implements Listener {

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (!(event.getEntity() instanceof Enderman)) {
            return;
        }
        var s = GensMain.getInstance().getStorage();
        if (s.isGen(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
}
