package tv.mineinthebox.essentials.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.PermissionKey;
import tv.mineinthebox.essentials.interfaces.CommandTemplate;

public class CmdGive extends CommandTemplate {
	
	private final xEssentials pl;
	
	public CmdGive(xEssentials pl, Command cmd, CommandSender sender) {
		super(pl, cmd, sender);
		this.pl = pl;
	}

	private List<String> getContainedMaterials(String material) {
		List<String> list = new ArrayList<String>();
		for(String mat : pl.getConfiguration().getMaterials()) {
			if(mat.startsWith(material.toUpperCase())) {
				list.add(mat);
			}
		}
		return list;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("give")) {
			if(sender.hasPermission(PermissionKey.CMD_GIVE.getPermission())) {
				if(args.length == 2) {
					List<String> list = getContainedMaterials(args[1]);
					return list;
				} else if(args.length == 3) {
					List<String> list = getContainedMaterials(args[1]);
					return list;
				}	
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(cmd.getName().equalsIgnoreCase("give")) {
			if(sender.hasPermission(PermissionKey.CMD_GIVE.getPermission())) {
				if(args.length == 0) {
					showHelp();
				} else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("help")) {
						showHelp();
					} else {
						sendMessage("we don't know much about this argument!");
					}
				} else if(args.length == 2) {
					try {
						Player p = Bukkit.getPlayer(args[0]);
						if(p instanceof Player) {
							if(args[1].contains(":")) {
								String[] data = args[1].split(":");
								if(isNumberic(data[0])) {
									if(!isNumberic(data[1])) {
										sendMessage("the sub data value needs to be a number!");
										return false;
									}
									Material mat = Material.getMaterial(Integer.parseInt(data[0]));
									Short dura = Short.parseShort(data[1]);
									ItemStack item = new ItemStack(mat, 1);
									item.setDurability(dura);
									sendMessage("giving " + item.getType().name() + ":"+dura + " amount: 1 to player " + p.getName());
									try {
										p.getInventory().addItem(item);
										sendMessageTo(p,"you retrieved items from " + sender.getName());
									} catch(IllegalArgumentException e) {
										sendMessage(p.getName() + " is to full");
									}
								} else {
									Material mat = Material.getMaterial(data[0].toUpperCase());
									if(!isNumberic(data[1])) {
										sendMessage("the sub data value needs to be a number!");
										return false;
									}
									Short dura = Short.parseShort(data[1]);
									ItemStack item = new ItemStack(mat, 1);
									item.setDurability(dura);
									try {
										p.getInventory().addItem(item);
										sendMessageTo(p, "you retrieved items from " + sender.getName());
									} catch(IllegalArgumentException e) {
										sendMessage(p.getName() + " is to full");
									}
								}						
							} else {
								if(isNumberic(args[1])) {
									Material mat = Material.getMaterial(Integer.parseInt(args[1]));
									ItemStack item = new ItemStack(mat, 1);
									try {
										p.getInventory().addItem(item);
										sendMessageTo(p, "you retrieved items from " + sender.getName());
									} catch(IllegalArgumentException e) {
										sendMessage(p.getName() + " is to full");
									}
								} else {
									Material mat = Material.getMaterial(args[1].toUpperCase());
									ItemStack item = new ItemStack(mat, 1);
									try {
										p.getInventory().addItem(item);
										sendMessageTo(p, "you retrieved items from " + sender.getName());
									} catch(IllegalArgumentException e) {
										sendMessage(p.getName() + " is to full");
									}
								}
							}
						} else {
							sendMessage("this player is not online!");
						}
					} catch(NullPointerException e) {
						sendMessage("invalid item!");
					}
				} else if(args.length == 3) {
					try {
						Player p = Bukkit.getPlayer(args[0]);
						if(p instanceof Player) {
							if(isNumberic(args[2])) {
								int amount = Integer.parseInt(args[2]);
								if(args[1].contains(":")) {
									String[] data = args[1].split(":");
									if(isNumberic(data[0])) {
										if(!isNumberic(data[1])) {
											sendMessage("the sub data value needs to be a number!");
											return false;
										}
										Material mat = Material.getMaterial(Integer.parseInt(data[0]));
										Short dura = Short.parseShort(data[1]);
										ItemStack item = new ItemStack(mat, amount);
										item.setDurability(dura);
										sendMessage("giving " + item.getType().name() + ":"+dura + " amount: 1 to player " + p.getName());
										try {
											p.getInventory().addItem(item);
											sendMessageTo(p, "you retrieved items from " + sender.getName());
										} catch(IllegalArgumentException e) {
											sendMessage(p.getName() + " is to full");
										}
									} else {
										Material mat = Material.getMaterial(data[0].toUpperCase());
										if(!isNumberic(data[1])) {
											sendMessage("the sub data value needs to be a number!");
											return false;
										}
										Short dura = Short.parseShort(data[1]);
										ItemStack item = new ItemStack(mat, amount);
										item.setDurability(dura);
										try {
											p.getInventory().addItem(item);
											sendMessageTo(p, "you retrieved items from " + sender.getName());
										} catch(IllegalArgumentException e) {
											sendMessage(p.getName() + " is to full");
										}
									}
								} else {
									if(isNumberic(args[1])) {
										Material mat = Material.getMaterial(Integer.parseInt(args[1]));
										ItemStack item = new ItemStack(mat, amount);
										try {
											p.getInventory().addItem(item);
											sendMessageTo(p, "you retrieved items from " + sender.getName());
										} catch(IllegalArgumentException e) {
											sendMessage(p.getName() + " is to full");
										}
									} else {
										Material mat = Material.getMaterial(args[1].toUpperCase());
										ItemStack item = new ItemStack(mat, amount);
										try {
											p.getInventory().addItem(item);
											sendMessageTo(p, "you retrieved items from " + sender.getName());
										} catch(IllegalArgumentException e) {
											sendMessage(p.getName() + " is to full");
										}
									}
								}
							} else {
								sendMessage("invalid amount given in!");
							}
						} else {
							sendMessage("this player is not online!");
						}
					} catch(NullPointerException e) {
						sendMessage("invalid item!");
					}
				}
			} else {
				getWarning(WarningType.NO_PERMISSION);
			}
		}
		return false;
	}

	private Boolean isNumberic(String s) {
		try {
			Integer i = Integer.parseInt(s);
			if(i != null) {
				return true;
			}
		} catch(NumberFormatException e) {
			
		}
		return false;
	}

	@Override
	public void showHelp() {
		sender.sendMessage(ChatColor.GOLD + ".oO___[give help]___Oo.");
		sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/give " + ChatColor.WHITE + ": shows help");
		sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/give help " + ChatColor.WHITE + ": shows help");
		sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/give <player> <item:subdata> " + ChatColor.WHITE + ": gives the player one item");
		sender.sendMessage(ChatColor.RED + "Admin: " + ChatColor.GRAY + "/give <player> <item:subdata> <amount> " + ChatColor.WHITE + " gives the player a item with a custom amount!");
	}
}
