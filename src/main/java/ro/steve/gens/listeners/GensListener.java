package ro.steve.gens.listeners;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class GensListener {

    public GensListener(Plugin plugin) {
        PluginManager m = plugin.getServer().getPluginManager();
        m.registerEvents(new EntityChangeBlock(), plugin);
        m.registerEvents(new GensBreak(), plugin);
        m.registerEvents(new GensExplode(), plugin);
        m.registerEvents(new GensInteract(), plugin);
        m.registerEvents(new GensInventoryListener(), plugin);
        m.registerEvents(new GensPlace(), plugin);
        m.registerEvents(new PistonExtend(), plugin);
        m.registerEvents(new PrepareCraft(), plugin);
    }
}
