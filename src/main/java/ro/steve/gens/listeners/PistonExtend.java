package ro.steve.gens.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import ro.steve.gens.GensMain;

public class PistonExtend implements Listener {

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if (event.isCancelled()) {
            return;
        }
        var s = GensMain.getInstance().getStorage();
        event.getBlocks().forEach(b -> {
            if (s.isGen(b.getLocation())) {
                event.setCancelled(true);
            }
        });
    }
}
