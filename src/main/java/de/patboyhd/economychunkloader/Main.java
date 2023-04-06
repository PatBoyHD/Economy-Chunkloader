package de.patboyhd.economychunkloader;

import de.patboyhd.economychunkloader.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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

        setupFiles();

        listenerRegistration();
        commandRegistration();

        syncChunks();

        // TODO klasse machen, die inhalt der config datei in ein objekt umwandelt.
        //  In loadChunk nicht mehr config auslesen lassen, sondern dem Konstruktor das Objekt übergeben
        // TODO /reload-config Befehl
        // TODO /ecc-world-blacklist add/remove/list command, um Welten zu Blacklisten
        // TODO Siehe LoadChunk
        // TODO /admin-list-chunks a list of all loaded chunks
        // TODO /list-chunks lists ur own chunks, maybe yml for every player????
        // TODO /unload-chunk and admin-unload-chunk take parameters of the chunk coords
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
        getCommand("admin-load-chunk").setExecutor(new AdminLoadChunk(this));
        getCommand("admin-unload-chunk").setExecutor(new AdminUnloadChunk(this));
        getCommand("admin-chunk-count").setExecutor(new AdminChunkCount(this));
    }

    private void setupFiles() {
        //config.yml
        if (!config.getConfig().contains("Bezahlung.Item"))
            config.getConfig().set("Bezahlung.Item","NETHERITE_INGOT");
        if (!config.getConfig().contains("Bezahlung.Anzahl"))
            config.getConfig().set("Bezahlung.Anzahl", 1);
        if (!config.getConfig().contains("Max-Chunks"))
            config.getConfig().set("Max-Chunks", 3);
        if (!config.getConfig().contains("Max-Total-Chunks"))
            config.getConfig().set("Max-Total-Chunks", 300);

        //data.yml
        if (!data.getConfig().contains("chunks-count")) {

            data.getConfig().set("chunks-count.count", 0);
            data.getConfig().set("chunks-count.admin-count", 0);
        }
        config.saveConfig();
        data.saveConfig();
    }

    private void syncChunks() {
        try {
            String chunk_coords = null;
            String world_uid = null;
            if (data.getConfig().contains("chunks") && data.getConfig().getConfigurationSection("chunks").getKeys(false) != null) {

                String world_str = null;
                UUID world_uuid = null;
                Integer chunk_x = null;
                Integer chunk_z = null;


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
                //un-forceload all chunks in all worlds that are not in data.yml
                for (World world : getServer().getWorlds()) {
                    for (Chunk chunk_world : world.getForceLoadedChunks()) {
                        chunk_world.setForceLoaded(false);
                    }
                }
            }
        } catch (Exception e) {
            this.getLogger().info("Error loading chunks: " + e);
        }

    }
}
