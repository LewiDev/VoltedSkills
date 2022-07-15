package me.lewi.volted;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkillCommand implements CommandExecutor {

    private skills plugin;

    public SkillCommand(skills plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(plugin.getDatabase().isConnected()) {
                plugin.getGuiListener().openSkillsGUI(p);
            } else {
                p.sendMessage(ChatColor.RED + "You cannot use this command as the database is not connected");
                p.sendMessage(ChatColor.RED + "Please contact a member of staff ASAP");
            }

        }
        return false;
    }
}
