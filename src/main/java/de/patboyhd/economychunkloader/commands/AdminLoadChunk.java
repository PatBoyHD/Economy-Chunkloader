package de.patboyhd.economychunkloader.commands;

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
    public FileManager config;
    private String config_name = "config.yml";
    private String data_name = "data.yml";

    public AdminLoadChunk(Main plugin) {
        this.plugin = plugin;
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
                    sender.sendMessage("This Chunk is already forceloaded! ");
                } else {
                    this.data = new FileManager(this.plugin, data_name);
                    chunk.setForceLoaded(true);
                    sender.sendMessage("This Chunk will now always be loaded: " + chunk_coords);

                    //UUID mit dem Chunk in eine YAML Datei speichern
                    data.getConfig().set("chunks." + chunk_coords + ".owner", "server");
                    data.getConfig().set("chunks." + chunk_coords + ".world", world);
                    data.saveConfig();
                }
            } else
                player.sendMessage("You do not have the permission to use this command.");
        }
        return false;
    }
}
