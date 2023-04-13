package de.patboyhd.economychunkloader.commands;

import de.patboyhd.economychunkloader.FileManager;
import de.patboyhd.economychunkloader.Main;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AdminUnloadChunk implements CommandExecutor{

    private Main plugin;
    public FileManager data;
    private String data_name = "data.yml";

    public AdminUnloadChunk(Main plugin) {
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


                if (chunk.isForceLoaded()) {
                    if (args.length >= 1 && args[0].equals("confirm")) {
                        this.data = new FileManager(this.plugin, data_name);

                        chunk.setForceLoaded(false);
                        sender.sendMessage("This chunk will now no longer be forceloaded: " + chunk_coords);

                        //Chunk aus der YAMl Datei löschen
                        String chunk_owner = data.getConfig().getString("chunks." + chunk_coords + ".owner");
                        if (!chunk_owner.equals("server")) {
                            int count = this.data.getConfig().getInt("players." + chunk_owner + ".count");
                            data.getConfig().set("players." + chunk_owner + ".count", count - 1);
                            data.getConfig().set("chunks-count.count", data.getConfig().getInt("chunks-count.count") - 1);
                        } else {
                            data.getConfig().set("chunks-count.admin-count", data.getConfig().getInt("chunks-count.admin-count") - 1);
                        }
                        data.getConfig().set("chunks." + chunk_coords, null);
                        data.saveConfig();
                    } else {
                        sender.sendMessage("Are you sure you want to un-forceload this chunk?\nIf you are, type /unload_chunk confirm");
                    }
                } else {
                    sender.sendMessage("This Chunk isn't forceloaded to begin with!");
                }
            } else {
                player.sendMessage("You do not have the permission to use this command.");
            }
            
        }
        return false;
    }
}
