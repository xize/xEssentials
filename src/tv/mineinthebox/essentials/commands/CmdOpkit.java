package tv.mineinthebox.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.OpKit;
import tv.mineinthebox.essentials.enums.PermissionKey;

public class CmdOpkit extends CommandTemplate {
	
	public CmdOpkit(xEssentials pl, Command cmd, CommandSender sender) {
		super(pl, cmd, sender);
	}
	
	private final Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "op kit"); {
		inv.setItem(0, OpKit.STONE_KIT.getButton());
		inv.setItem(4, OpKit.IRON_KIT.getButton());
		inv.setItem(8, OpKit.DIAMOND_KIT.getButton());
	}
	
	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("opkit")) {
			if(sender.hasPermission(PermissionKey.CMD_OPKIT.getPermission())) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					sendMessage(ChatColor.GREEN + "[Op kit] " + ChatColor.GRAY + "opening opkit selector!");
					p.openInventory(inv);
				} else {
					getWarning(WarningType.PLAYER_ONLY);
				}
			} else {
				getWarning(WarningType.NO_PERMISSION);
			}
		}
		return false;
	}

}