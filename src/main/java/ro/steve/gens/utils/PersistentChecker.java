package ro.steve.gens.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PersistentChecker {

    public static Object check(ItemStack item, String namespacedKey, PersistentDataType type) {
        if (item.getItemMeta() == null) {
            return null;
        }
        if (!item.getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft(namespacedKey), type)) {
            return null;
        }
        return item.getItemMeta().getPersistentDataContainer().get(NamespacedKey.minecraft(namespacedKey), type);
    }
}
