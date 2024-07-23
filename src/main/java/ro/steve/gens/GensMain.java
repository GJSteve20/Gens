package ro.steve.gens;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ro.steve.gens.commands.GensCommands;
import ro.steve.gens.configuration.Configuration;
import ro.steve.gens.listeners.GensListener;
import ro.steve.gens.storage.Storage;
import ro.steve.gens.task.GensTask;

public class GensMain extends JavaPlugin {

    private static GensMain I;
    private Economy E = null;

    private Configuration C;
    private Storage S;

    @Override
    public void onEnable() {
        I = this;
        setupEconomy();
        C = new Configuration(this);
        S = new Storage(this);
        new GensCommands(this);
        new GensListener(this);
        new GensTask();
    }

    public static GensMain getInstance() {
        return I;
    }

    public Configuration getConfiguration() {
        return C;
    }

    public Storage getStorage() {
        return S;
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        E = rsp.getProvider();
    }

    public Economy getEconomy() {
        return E;
    }
}
