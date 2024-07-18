package ro.steve.gens.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.persistence.PersistentDataType;
import ro.steve.gens.utils.PersistentChecker;

import java.util.Arrays;

public class PrepareCraft implements Listener {

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        Arrays.stream(event.getInventory().getContents()).forEach(i -> {
            if (PersistentChecker.check(i, "gens-item", PersistentDataType.STRING) != null) {
                event.getInventory().setResult(null);
                event.setCancelled(true);
            }
            if (PersistentChecker.check(i, "gens-tier", PersistentDataType.STRING) != null) {
                event.getInventory().setResult(null);
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        Arrays.stream(event.getInventory().getContents()).forEach(i -> {
            if (PersistentChecker.check(i, "gens-item", PersistentDataType.STRING) != null) {
                event.getInventory().setResult(null);
            }
            if (PersistentChecker.check(i, "gens-tier", PersistentDataType.STRING) != null) {
                event.getInventory().setResult(null);
            }
        });
    }

    @EventHandler
    public void onBurning(FurnaceSmeltEvent event) {
        if (PersistentChecker.check(event.getSource(), "gens-item", PersistentDataType.STRING) != null) {
            event.setCancelled(true);
        }
        if (PersistentChecker.check(event.getSource(), "gens-tier", PersistentDataType.STRING) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPrepareBurning(FurnaceStartSmeltEvent event) {
        if (PersistentChecker.check(event.getSource(), "gens-item", PersistentDataType.STRING) != null) {
            event.setTotalCookTime(0);
        }
        if (PersistentChecker.check(event.getSource(), "gens-tier", PersistentDataType.STRING) != null) {
            event.setTotalCookTime(0);
        }
    }

    @EventHandler
    public void onItemConsume(FurnaceBurnEvent event) {
        if (PersistentChecker.check(event.getFuel(), "gens-item", PersistentDataType.STRING) != null) {
            event.setCancelled(true);
            event.setBurning(false);
        }
        if (PersistentChecker.check(event.getFuel(), "gens-tier", PersistentDataType.STRING) != null) {
            event.setCancelled(true);
            event.setBurning(false);
        }
    }
}
