package de.patboyhd.economychunkloader;

import de.patboyhd.economychunkloader.commands.LoadChunk;
import de.patboyhd.economychunkloader.commands.UnloadChunk;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public DataManager data;
    public ConfigManager config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().fine("Plugin wird aktiviert.");

        this.saveDefaultConfig();//Create config file & folder
        this.data = new DataManager(this);
        this.config = new ConfigManager(this);

        setupConfig();

        listenerRegistration();
        commandRegistration();

        // TODO Aus der data Datei alle Chunks forceloaden lassen bei enable
        // TODO UnloadChunk => nochmal nachfragen. Nur wenn man "unload-chunk confirm" eingibt, soll ers machen!!!
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().fine("Plugin wird deaktiviert.");
    }

    private void listenerRegistration() {
        PluginManager pluginManager = Bukkit.getPluginManager();


    }

    private void commandRegistration() {
        getCommand("load-chunk").setExecutor(new LoadChunk(this));
        getCommand("unload-chunk").setExecutor(new UnloadChunk(this));



    }

    private void setupConfig() {
        if (!config.getConfig().contains("Bezahlung.Item"))
            config.getConfig().set("Bezahlung.Item","NETHERITE_INGOT");
        if (!config.getConfig().contains("Bezahlung.Anzahl"))
            config.getConfig().set("Bezahlung.Anzahl", 1);
        if (!config.getConfig().contains("Max-Chunks"))
            config.getConfig().set("Max-Chunks", 3);
        config.saveConfig();
    }
}
