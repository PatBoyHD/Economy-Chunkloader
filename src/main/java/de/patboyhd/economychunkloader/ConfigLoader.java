package de.patboyhd.economychunkloader;

import org.bukkit.ChatColor;

import java.util.logging.Level;

public class ConfigLoader {
    private String config_name = "config.yml";


    //Attributes
    private String payment_item = "NETHERITE_INGOT";
    private Integer payment_count = 1;
    private Integer max_chunks = 3;
    private Integer max_total_chunks = 300;

    public String getPayment_item() {
        return payment_item;
    }

    public Integer getPayment_count() {
        return payment_count;
    }

    public Integer getMax_chunks() {
        return max_chunks;
    }

    public Integer getMax_total_chunks() {
        return max_total_chunks;
    }

    //Constructor
    public ConfigLoader(Main plugin) {
        FileManager config;
        config = new FileManager(plugin, config_name);
        try {
            payment_item = config.getConfig().getString("Bezahlung.Item");
            payment_count = config.getConfig().getInt("Bezahlung.Anzahl");
            max_chunks = config.getConfig().getInt("Max-Chunks");
            max_total_chunks = config.getConfig().getInt("Max-Total-Chunks");
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error loading config! All configurations will be set to default!", e);
        }
    }
}
