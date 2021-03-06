package tv.mineinthebox.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.interfaces.CommandTemplate;
import tv.mineinthebox.essentials.interfaces.XPlayer;

public class CmdFly extends CommandTemplate {

	private final xEssentials pl;

	public CmdFly(xEssentials pl, Command cmd, CommandSender sender) {
		super(pl, cmd, sender);
		this.pl = pl;
	}

	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("fly")) {
			if(sender.hasPermission(PermissionKey.CMD_FLY.getPermission())) {
				if(args.length == 0) {
					if(sender instanceof Player) {
						if(pl.getManagers().getPlayerManager().isOnline(sender.getName())) {
							XPlayer xp = pl.getManagers().getPlayerManager().getPlayer(sender.getName());
							if(xp.isFlying()) {
								sendMessage("successfully disabled fly mode");
								xp.setFlying(false);
							} else {
								sendMessage("successfully enabled fly mode");
								xp.setFlying(true);
							}
						} else {
							sendMessage("something went wrong, please reload pl");
						}
					} else {
						getWarning(WarningType.PLAYER_ONLY);
					}
				} else if(args.length == 1 ) {
					if(sender.hasPermission(PermissionKey.IS_ADMIN.getPermission())) {
						if(pl.getManagers().getPlayerManager().isEssentialsPlayer(args[0])) {
							if(pl.getManagers().getPlayerManager().isOnline(args[0])) {
								XPlayer xp = pl.getManagers().getPlayerManager().getPlayer(args[0]);
								if(xp.isFlying()) {
									sendMessage("you successfully disabled fly for player " + xp.getName());
									sendMessageTo(xp.getBukkitPlayer(), sender.getName() + " disabled your fly mode");
									xp.setFlying(false);
								} else {
									sendMessage("you successfully enabled fly for player " + xp.getName());
									sendMessageTo(xp.getBukkitPlayer(), sender.getName() + " enabled your fly mode");
									xp.setFlying(true);
								}
							} else {
								sendMessage("player not online!");
							}
						} else {
							sendMessage("player does not exist!");
						}
					}
				}
			} else {
				getWarning(WarningType.NO_PERMISSION);
			}
		}
		return false;
	}

}
