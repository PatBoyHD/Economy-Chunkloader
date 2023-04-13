package de.patboyhd.economychunkloader.commands;

import de.patboyhd.economychunkloader.ConfigLoader;
import de.patboyhd.economychunkloader.FileManager;
import de.patboyhd.economychunkloader.Main;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class AdminLoadChunk implements CommandExecutor {

    private Main plugin;
    public FileManager data;
    public ConfigLoader config;
    private String data_name = "data.yml";

    public AdminLoadChunk(Main plugin, ConfigLoader config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You are not a player, nu uh!");
        } else { //Code goes here
            Player player = (Player) sender;

            if (player.isOp()) {
                Location location = player.getLocation();
                Chunk chunk = location.getChunk();
                String chunk_coords = Integer.toString(chunk.getX()) + "," + Integer.toString(chunk.getZ());
                String world = player.getWorld().getUID().toString();

                if (chunk.isForceLoaded()) {
                    sender.sendMessage("This chunk is already forceloaded! ");
                } else {
                    this.data = new FileManager(this.plugin, data_name);
                    int max_chunks = this.config.getMax_total_chunks();
                    int chunks_total_count = this.data.getConfig().getInt("chunks-count.count") + data.getConfig().getInt("chunks-count.admin-count") + 1;

                    if (chunks_total_count > max_chunks) {
                        player.sendMessage("Warning! You are surpassing the serverwide limit of forcelaoded chunks! (" +
                                chunks_total_count + "/" + max_chunks + ")");
                    }
                    chunk.setForceLoaded(true);
                    sender.sendMessage("This chunk is now forceloaded: " + chunk_coords);

                    //UUID mit dem Chunk in eine YAML Datei speichern
                    data.getConfig().set("chunks." + chunk_coords + ".owner", "server");
                    data.getConfig().set("chunks." + chunk_coords + ".world", world);
                    data.getConfig().set("chunks-count.admin-count", data.getConfig().getInt("chunks-count.admin-count") + 1);
                    data.saveConfig();

                }
            } else
                player.sendMessage("You do not have the permission to use this command.");
        }
        return false;
    }
}
