package ro.steve.gens.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataType;
import ro.steve.gens.GensMain;
import ro.steve.gens.utils.PersistentChecker;

public class GensPlace implements Listener {

    @EventHandler
    public void onGensPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        var i = event.getItemInHand();
        var g = GensMain.getInstance().getConfiguration().getConfig("gens");
        var c = PersistentChecker.check(i, "gens-tier", PersistentDataType.STRING);
        if (c == null) {
            return;
        }
        var s = GensMain.getInstance().getStorage();
        if (!s.addGen(event.getBlockPlaced().getLocation(), (String) c, false)) {
            event.setCancelled(true);
        }
    }
}
