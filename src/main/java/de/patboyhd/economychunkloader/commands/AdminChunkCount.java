package de.patboyhd.economychunkloader.commands;

import de.patboyhd.economychunkloader.ConfigLoader;
import de.patboyhd.economychunkloader.FileManager;
import de.patboyhd.economychunkloader.Main;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminChunkCount implements CommandExecutor {

    private Main plugin;
    public FileManager data;
    public ConfigLoader config;
    private String data_name = "data.yml";

    public AdminChunkCount(Main plugin, ConfigLoader config) {
        this.plugin = plugin;
        this.config = config;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            this.data = new FileManager(this.plugin, data_name);
            int max_chunks = this.config.getMax_total_chunks();
            int chunks_total_count = this.data.getConfig().getInt("chunks-count.count") + data.getConfig().getInt("chunks-count.admin-count");
            sender.sendMessage("There are " + chunks_total_count + "/" + max_chunks + " chunks.");
        } else { //Code goes here
            Player player = (Player) sender;

            if (player.isOp()) {
                this.data = new FileManager(this.plugin, data_name);
                int max_chunks = this.config.getMax_total_chunks();
                int chunks_total_count = this.data.getConfig().getInt("chunks-count.count") + data.getConfig().getInt("chunks-count.admin-count");
                player.sendMessage("There are " + chunks_total_count + "/" + max_chunks + " chunks.");
            } else
                player.sendMessage("You do not have the permission to use this command.");
        }
        return false;
    }
}
