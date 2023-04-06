package de.patboyhd.economychunkloader;

import de.patboyhd.economychunkloader.commands.LoadChunk;
import de.patboyhd.economychunkloader.commands.UnloadChunk;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.print.DocFlavor;
import java.rmi.server.ExportException;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public FileManager data;
    public FileManager config;
    private String config_name = "config.yml";
    private String data_name = "data.yml";


    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getLogger().fine("Plugin wird aktiviert.");

        this.saveDefaultConfig();//Create config file & folder
        this.data = new FileManager(this, data_name);
        this.config = new FileManager(this, config_name);

        setupConfig();

        listenerRegistration();
        commandRegistration();

        syncChunks();

        // TODO Aus der data Datei alle Chunks forceloaden lassen bei enable
        // TODO klasse machen, die inhalt der config datei in ein objekt umwandelt.
        //  In loadChunk nicht mehr config auslesen lassen,
        //  sondern dem Konstruktor das Objekt übergeben
        // TODO /reload-config Befehl
        // TODO /ecc-world-blacklist add/remove/list command, um Welten zu Blacklisten
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

    private void syncChunks() {
        try {
            if (data.getConfig().contains("chunks")) {

                String world_str = null;
                UUID world_uuid = null;
                Integer chunk_x = null;
                Integer chunk_z = null;

                String chunk_coords = null;
                String world_uid = null;

                //un-forceload all chunks in all worlds that are not in data.yml
                for (World world : getServer().getWorlds()) {
                    world_uid = world.getUID().toString();
                    for (Chunk chunk_world : world.getForceLoadedChunks()) {
                        chunk_coords = Integer.toString(chunk_world.getX()) + "," + Integer.toString(chunk_world.getZ());

                        if (data.getConfig().contains("chunks." + chunk_coords)) {
                            if (data.getConfig().getString("chunks." + chunk_coords + ".world").equals(world_uid)) {
                            } else {
                                chunk_world.setForceLoaded(false);
                            }
                        } else {
                            chunk_world.setForceLoaded(false);
                        }
                    }
                }

                //forceload all chunks in data.yml in the respective world
                for (String chunk : data.getConfig().getConfigurationSection("chunks").getKeys(false)) {
                    world_str = data.getConfig().get("chunks." + chunk + ".world").toString();
                    world_uuid = UUID.fromString(world_str);
                    chunk_x = Integer.parseInt(chunk.split(",")[0]);
                    chunk_z = Integer.parseInt(chunk.split(",")[1]);
                    getServer().getWorld(world_uuid).setChunkForceLoaded(chunk_x, chunk_z, true);
                }
            } else {
                this.getLogger().info("No chunks to forceload!");
            }
        } catch (Exception e) {
            this.getLogger().info("Error loading chunks: " + e);
        }

    }
}
