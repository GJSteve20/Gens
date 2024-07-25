package ro.steve.gens.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import ro.steve.gens.commands.GensUpgradeCommand;

public class GensInteract implements Listener {

    @EventHandler
    public void onGensInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (!event.getPlayer().isSneaking()) {
            return;
        }
        new GensUpgradeCommand(event.getPlayer(), event.getClickedBlock());
    }
}
