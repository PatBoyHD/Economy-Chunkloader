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

public class LoadChunk implements CommandExecutor {

    private Main plugin;
    public FileManager data;
    public ConfigLoader config;
    private String data_name = "data.yml";

    public LoadChunk(Main plugin, ConfigLoader config) {
        this.plugin = plugin;
        this.config = config;
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
            String chunk_coords = Integer.toString(chunk.getX()) + "," + Integer.toString(chunk.getZ());
            String world = player.getWorld().getUID().toString();

            if (chunk.isForceLoaded()) {
                sender.sendMessage("This chunk is already forceloaded! ");
            } else {



                int max_count = this.config.getMax_chunks();
                String item = this.config.getPayment_item();
                int max_chunks = this.config.getMax_total_chunks();

                Material item_material = Material.matchMaterial(this.config.getPayment_item());

                //Material item_material = (Material) this.config.getConfig().get("Payment.Item");

                int item_min_count = this.config.getPayment_count();


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

                        this.data = new FileManager(this.plugin, data_name);


                        int count = 0;

                        if (this.data.getConfig().contains("players." + uuid.toString() + ".count"))
                            count = this.data.getConfig().getInt("players." + uuid.toString() + ".count");

                        if (count < max_count) {
                            // TODO this could be far more outside
                            if (data.getConfig().getInt("chunks-count.count") < max_chunks) {
                                chunk.setForceLoaded(true);
                                sender.sendMessage("This chunk is now forceloaded: " + chunk_coords);
                                player.getInventory().removeItem(new ItemStack(item_material, item_min_count));

                                //safe UUID together with chunk in the data.yml file and increment count
                                data.getConfig().set("chunks." + chunk_coords + ".owner", uuid.toString());
                                data.getConfig().set("chunks." + chunk_coords + ".world", world);
                                data.getConfig().set("players." + uuid.toString() + ".count", count + 1);
                                data.getConfig().set("chunks-count.count", data.getConfig().getInt("chunks-count.count") + 1);
                                data.saveConfig();
                            } else {
                                sender.sendMessage("Could not forceload this chunk! \n" +
                                        "The serverwide maximum number of forceloaded Chunks has been reached!");
                            }
                        } else {
                            sender.sendMessage("You reached the maximum number of chunks you can forceload (" + count + ")!");
                        }

                    } else {
                        sender.sendMessage("You don't have enough " + item_material.name() + ". You need atleast " + item_min_count + "!");
                    }
                } catch (Exception e) {
                    sender.sendMessage("An error occurred whilst executing this command: " + e);
                }

            }
            return true;
        }

        return false;
    }
}
