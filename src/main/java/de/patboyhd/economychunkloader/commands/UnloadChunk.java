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

public class UnloadChunk implements CommandExecutor{

    private Main plugin;
    public FileManager data;
    private String data_name = "data.yml";

    public UnloadChunk(Main plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You are not a player, nu uh!");
        } else { //Code goes here
            if (args.length >= 1 && args[0].equals("confirm")) {




                Player player = (Player) sender;
                UUID uuid = player.getUniqueId(); // this should work
                Location location = player.getLocation();
                Chunk chunk = location.getChunk();


                if (chunk.isForceLoaded()) {
                    this.data = new FileManager(this.plugin, data_name);

                    int count = 0;

                    String uuid_data = null;

                    // gets the count if existent
                    if (this.data.getConfig().contains("players." + uuid.toString() + ".count"))
                        count = this.data.getConfig().getInt("players." + uuid.toString() + ".count");

                    // gets the owner uuid if the chunk is owned
                    if (this.data.getConfig().contains("chunks." + chunk))
                        uuid_data = this.data.getConfig().getString("chunks." + chunk + ".owner");

                    // Checks if it's the players Chunk
                    if (uuid.toString().equals(uuid_data)) {
                        chunk.setForceLoaded(false);
                        sender.sendMessage("This Chunk is no longer forceloaded: " + chunk);

                        //Chunk aus der YAMl Datei löschen und count senken
                        data.getConfig().set("chunks." + chunk, null);
                        data.getConfig().set("players." + uuid.toString() + ".count", count - 1);
                        data.saveConfig();
                    } else {
                        sender.sendMessage("You can't unload Chunks of other players!");
                    }
                } else {
                    sender.sendMessage("This Chunk isn't forceloaded to begin with!");
                }

            } else {
                sender.sendMessage("Are you sure you want to un-forceload this Chunk?\nIf you are sure, type /unload_chunk confirm");
            }




        }
        return false;
    }
}
