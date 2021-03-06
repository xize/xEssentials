package tv.mineinthebox.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.interfaces.CommandTemplate;

public class CmdGetChunkFile extends CommandTemplate {

	public CmdGetChunkFile(xEssentials pl, Command cmd, CommandSender sender) {
		super(pl, cmd, sender);
	}

	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("getchunkfile")) {
			if(sender.hasPermission(PermissionKey.CMD_GET_CHUNK_FILE.getPermission())) {
				if(sender instanceof Player) {
					if(args.length == 0) {
						Player p = (Player) sender;
						Chunk chunk = p.getLocation().getChunk();
						int x = chunk.getX() >> 5; //or do (chunk.getX()/32)
						int z = chunk.getZ() >> 5; //or do (chunk.getZ()/32)
						sendMessage("the chunk is called in this file: r." + x + "."+z+".mca in folder " + Bukkit.getWorldContainer().toString() + "/"+p.getWorld().getName());
					} else if(args.length == 1) {
						if(args[0].equalsIgnoreCase("help")) {
							showHelp();
						} else {
							sendMessage("unknown argument!");
						}
					} else if(args.length == 2) {
						try {
							Player p = (Player)sender;
							int coordx = Integer.parseInt(args[0]);
							int coordz = Integer.parseInt(args[1]);
							Location loc = new Location(p.getWorld(), coordx, 0, coordz);
							Chunk chunk = loc.getChunk();
							int x = chunk.getX() >> 5;
							int z= chunk.getZ() >> 5;
							
							sendMessage("the chunk is called in this file: r." + x + "."+z+".mca in folder " + Bukkit.getWorldContainer().toString() + "/"+p.getWorld().getName());
						} catch(NumberFormatException e) {
							sendMessage("last 2 arguments have to be a number!");
						}
					}
				} else {
					getWarning(WarningType.PLAYER_ONLY);
				}
			} else {
				getWarning(WarningType.NO_PERMISSION);
			}
		}
		return false;
	}

	@Override
	public void showHelp() {
		sender.sendMessage(ChatColor.GOLD + ".oO___[Chunk File Finder]___Oo.");
		sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/getchunkfile help " + ChatColor.WHITE + ": shows help");	
		sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/getchunkfile " + ChatColor.WHITE + ": gets the chunk file on your location");
		sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/getchunkfile <x> <z> " + ChatColor.WHITE + ": gets the chunk file on x and z axis");
	}

}
