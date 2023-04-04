package de.patboyhd.economychunkloader.commands;

import de.patboyhd.economychunkloader.ConfigManager;
import de.patboyhd.economychunkloader.DataManager;
import de.patboyhd.economychunkloader.Main;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.UUID;

public class LoadChunk implements CommandExecutor {

    private Main plugin;
    public DataManager data;
    public ConfigManager config;

    public LoadChunk(Main plugin) {
        this.plugin = plugin;
    }





    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You are not a player, nu uh!");
        } else { //Code goes here
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId(); // this should work
            Location location = player.getLocation();
            Chunk chunk = location.getChunk();
            //String chunk_coords = Integer.toString(chunk.getX()) + "," + Integer.toString(chunk.getZ());


            if (chunk.isForceLoaded()) {
                sender.sendMessage("This Chunk is already forceloaded! ");
            } else {


                this.config = new ConfigManager(this.plugin);

                int max_count = this.config.getConfig().getInt("Max-Chunks");
                String item = this.config.getConfig().get("Bezahlung.Item").toString();

                Material item_material = Material.matchMaterial(this.config.getConfig().get("Bezahlung.Item").toString());

                //Material item_material = (Material) this.config.getConfig().get("Bezahlung.Item");

                int item_min_count = this.config.getConfig().getInt("Bezahlung.Anzahl");


                int item_count = 0;

                try {
                    for (ItemStack item_inv : player.getInventory().getContents()) {
                        if (item_inv == null)
                            continue;
                        if (item_inv.getType().toString().equals(item)) {
                            item_count += item_inv.getAmount();

                        }
                    }

                    if (item_count >= item_min_count) {

                        this.data = new DataManager(this.plugin);


                        int count = 0;

                        if (this.data.getConfig().contains("players." + uuid.toString() + ".count"))
                            count = this.data.getConfig().getInt("players." + uuid.toString() + ".count");

                        if (count < max_count) {
                            chunk.setForceLoaded(true);
                            sender.sendMessage("This Chunk will now always be loaded: " + chunk.toString());
                            player.getInventory().removeItem(new ItemStack(item_material, item_min_count));

                            //UUID mit dem Chunk in eine YAML Datei speichern und count erhöhen
                            data.getConfig().set("chunks." + chunk + ".owner", uuid.toString());
                            data.getConfig().set("players." + uuid.toString() + ".count", count + 1);
                            data.saveConfig();
                        } else {
                            sender.sendMessage("You reached the maximum number of Chunks you can load (" + count + ")!");
                        }

                    } else {
                        sender.sendMessage("Du hast nicht genügend " + item_material.name() + ". Du brauchst " + item_min_count + "!");
                    }
                } catch (Exception e) {
                    sender.sendMessage("Error! " + e);
                }

            }

        }

        return false;
    }
}
