package tv.mineinthebox.bukkit.essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tv.mineinthebox.bukkit.essentials.Warnings;
import tv.mineinthebox.bukkit.essentials.enums.PermissionKey;

public class CmdHand {
	
	@SuppressWarnings("deprecation")
	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("hand")) {
			if(sender.hasPermission(PermissionKey.CMD_HAND.getPermission())) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					p.sendMessage(ChatColor.GRAY + "the item in your hand has a data value of: " + ChatColor.GREEN + p.getItemInHand().getType().getId() + "("+p.getItemInHand().getType().name()+")" + ChatColor.GRAY + " and has a sub data value of: " + ChatColor.GREEN + p.getItemInHand().getDurability());
				} else {
					Warnings.getWarnings(sender).consoleMessage();
				}
			} else {
				Warnings.getWarnings(sender).noPermission();
			}
		}
		return false;
	}

}
