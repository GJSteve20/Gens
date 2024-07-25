package ro.steve.gens.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataType;
import ro.steve.gens.GensMain;
import ro.steve.gens.item.ItemBuilder;
import ro.steve.gens.utils.Color;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GensInventory implements InventoryHolder {

    private Inventory inv;
    private final YamlConfiguration yaml;
    private final String path;
    private Location location;


    public GensInventory(String path, Location location) {
        yaml = GensMain.getInstance().getConfiguration().getConfig("gui");
        this.path = path;
        if (location != null) {
            this.location = location;
        }
        loadInventory();
        loadFillers();
        loadItems();
    }

    private void loadInventory() {
        inv = Bukkit.createInventory(this, yaml.getInt(path + ".size"), Color.process(yaml.getString(path + ".name")));
    }

    private void loadFillers() {
        if (yaml.getConfigurationSection(path + ".fillers") == null) {
            return;
        }
        List<String> design = yaml.getStringList(path + ".fillers.filler");
        for (int i = 0; i < design.size(); i++) {
            char[] chars = design.get(i).toCharArray();
            for (int j = 0; j < chars.length; j++) {
                try {
                    var item = new ItemBuilder()
                            .type(Material.valueOf(yaml.getString(path + ".fillers.replacer." + chars[j])))
                            .name(" ")
                            .build();
                    inv.setItem(i * 9 + j, item);
                } catch (NullPointerException e1) {
                    inv.setItem(i * 9 + j, null);
                }
            }
        }
    }

    private void loadItems() {
        if (yaml.getConfigurationSection(path + ".items") == null) {
            return;
        }
        var s = GensMain.getInstance().getStorage();
        var g = GensMain.getInstance().getConfiguration().getConfig("gens");
        AtomicInteger tier = new AtomicInteger();
        yaml.getConfigurationSection(path + ".items").getKeys(false).forEach(k -> {
            var p = path + ".items." + k + ".";
            if (location != null) {
                tier.set((int) s.getTier(location));
            } else {
                tier.set(Integer.parseInt(yaml.getString(p + "action").replace("buy_tier_", "")));
            }
            int next = tier.get() + 1;
            var b = new ItemBuilder();
            var material = yaml.getString(p + "material");
            if (material.equalsIgnoreCase("%next_tier%")) {
                material = g.getString("Gens.tier_" + next + ".material");
            }
            if (material.equalsIgnoreCase("%current_tier%")) {
                material = g.getString("Gens.tier_" + tier + ".material");
            }
            b.type(Material.valueOf(material));
            var name = yaml.getString(p + "name");
            if (name.contains("%next_tier_name%")) {
                name = name.replace("%next_tier_name%", g.getString("Gens.tier_" + next + ".name"));
            }
            if (name.equalsIgnoreCase("%current_tier_name%")) {
                name = name.replace("%current_tier_name%", g.getString("Gens.tier_" + tier.get() + ".name"));
            }
            b.name(name);
            b.lore(yaml.getStringList(p + "description"),
                    "%upgrade_cost%;" + g.getString("Gens.tier_" + tier.get() + ".upgrade_cost"),
                    "%next_tier_upgrade_cost%;" + g.getString("Gens.tier_" + next + ".upgrade_cost"));
            if (yaml.get(p + "glowing") != null) {
                b.glowing(yaml.getBoolean(p + "glowing"));
            }
            b.withPersistent("gens-action", PersistentDataType.STRING, yaml.getString(p + "action"));
            if (location != null) {
                var parsedLocation = location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
                b.withPersistent("gens-location", PersistentDataType.STRING, parsedLocation);
            }
            inv.setItem(yaml.getInt(p + "slot"), b.build());
        });
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
