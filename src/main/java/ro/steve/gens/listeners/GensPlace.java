package ro.steve.gens.listeners;

import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataType;
import ro.steve.gens.GensMain;
import ro.steve.gens.utils.PersistentChecker;
import ro.steve.gens.utils.SendMessage;

public class GensPlace implements Listener {

    @EventHandler
    public void onGensPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        var i = event.getItemInHand();
        var c = PersistentChecker.check(i, "gens-tier", PersistentDataType.STRING);
        if (c == null) {
            return;
        }
        var s = GensMain.getInstance().getStorage();
        for (BlockFace face : BlockFace.values()) {
            var adjacentBlock = event.getBlockPlaced().getRelative(face);
            if (s.isGen(adjacentBlock.getLocation())) {
                SendMessage.sendMessage(event.getPlayer(), "gen-next-to-gen");
                event.setCancelled(true);
                return;
            }
        }
        if (!s.addGen(event.getBlockPlaced().getLocation(), (String) c, false)) {
            SendMessage.sendMessage(event.getPlayer(), "dev");
            event.setCancelled(true);
        }
    }
}
