package ro.steve.gens.configuration;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class Configuration {

    private final Plugin plugin;

    public Configuration(Plugin plugin) {
        this.plugin = plugin;
        loadDefaults("messages", "gens", "gui");
    }

    private void loadDefaults(String... configs) {
        for (String config : configs) {
            getConfig(config);
        }
    }

    public YamlConfiguration getConfig(String name) {
        var file = new File(plugin.getDataFolder(), name + ".yml");
        if (!file.exists()) {
            plugin.getLogger().info("File " + name + ".yml doesn't exists, creating a new one...");
            plugin.saveResource(name + ".yml", true);
        }
        return YamlConfiguration.loadConfiguration(file);
    }
}
