package ro.steve.gens.task;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ro.steve.gens.GensMain;
import ro.steve.gens.item.ItemBuilder;

import java.util.concurrent.atomic.AtomicBoolean;

public class GensTask {

    public GensTask() {
        init();
    }

    private void init() {
        var c = GensMain.getInstance().getConfiguration().getConfig("gens");
        var s = GensMain.getInstance().getStorage();
        if (c.get("Gens") == null) {
            Bukkit.getLogger().info("Gens from gens.yml is null");
            return;
        }
        if (c.getConfigurationSection("Gens") == null) {
            Bukkit.getLogger().info("Gens from gens.yml is null");
            return;
        }
        c.getConfigurationSection("Gens").getKeys(false).forEach(k -> {
            int tier;
            try {
                tier = Integer.parseInt(k.replace("tier_", ""));
            } catch (NumberFormatException e1) {
                Bukkit.getLogger().info("Please update at Gens." + k + ". The id is invalid, update like this: eg.: tier_1");
                return;
            }
            var p = "Gens." + k + ".";
            int time = c.getInt(p + "time");
            var distance = c.getDouble(p + "player_max_distance");
            new BukkitRunnable() {
                final int t = tier;
                public void run() {
                    if (s.getLocation(t).isEmpty()) {
                        return;
                    }
                    s.getLocation(t).forEach(l -> {
                        var splitLocation = l.split(";");
                        var loc = new Location(Bukkit.getWorld(splitLocation[0]),
                                Integer.parseInt(splitLocation[1]),
                                Integer.parseInt(splitLocation[2]),
                                Integer.parseInt(splitLocation[3]));
                        if (loc.getBlock().getType() != Material.valueOf(c.getString(p + "material"))) {
                            s.removeGen(loc);
                            return;
                        }
                        AtomicBoolean nearby = new AtomicBoolean(false);
                        Bukkit.getWorld(splitLocation[0]).getNearbyEntities(loc, distance, distance, distance).forEach(e -> {
                            if (e instanceof Player) {
                                nearby.set(true);
                            }
                        });
                        if (!nearby.get()) {
                            return;
                        }
                        var i = new ItemBuilder()
                                .type(Material.valueOf(c.getString(p + "item.material")))
                                .name(c.getString(p + "item.name"))
                                .lore(c.getStringList(p + "item.description"), "%price%;" + c.getDouble(p + "item.price"))
                                .withPersistent("gens-item", PersistentDataType.STRING, k)
                                .amount(c.getInt(p + "item.amount"))
                                .build();
                        loc.add(0.5, 1.5, 0.5);
                        loc.setDirection(new Vector(0, 0, 0));
                        loc.getWorld().dropItem(loc, i).setVelocity(new Vector(0, 0, 0));
                    });
                }
            }.runTaskTimer(GensMain.getInstance(), 0, 20L * time);
        });
    }
}
