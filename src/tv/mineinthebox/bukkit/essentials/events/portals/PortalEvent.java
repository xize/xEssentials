package tv.mineinthebox.bukkit.essentials.events.portals;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import tv.mineinthebox.bukkit.essentials.Configuration;
import tv.mineinthebox.bukkit.essentials.instances.Portal;

public class PortalEvent implements Listener {

	private HashMap<String, Long> time = new HashMap<String, Long>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEnter(PlayerPortalEvent e) {
		if(e.isCancelled()) {
			return;
		}

		e.setCancelled(true);

		Block block = getPortalNearby(e.getPlayer().getLocation().getBlock());
		if(!(block instanceof Block)) {
			return;
		}
		for(Portal portal : Configuration.getPortalConfig().getPortals().values()) {
			List<Block> blocks = Arrays.asList(portal.getInnerBlocks());
			if(blocks.contains(block)) {
				if(Configuration.getPortalConfig().getCooldown() > 0) {
					if(time.containsKey(e.getPlayer().getName())) {
						if(time.get(e.getPlayer().getName()) < System.currentTimeMillis()) {
							time.remove(e.getPlayer().getName());
						} else {
							e.setCancelled(true);
							Long millis = (time.get(e.getPlayer().getName())-System.currentTimeMillis());
							String msg = String.format("%d:%s", TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
							e.getPlayer().sendMessage(ChatColor.GRAY + "cooldown please wait: " + ChatColor.GREEN + msg + ChatColor.GRAY + " minutes");
							return;
						}
					} else {
						Date date = new Date(System.currentTimeMillis());
						date.setSeconds(date.getSeconds()+Configuration.getPortalConfig().getCooldown());
						time.put(e.getPlayer().getName(), date.getTime());
					}
				}
				e.useTravelAgent(false);
				if(portal.isLinked()) {
					Portal linked = portal.getLinkedPortal();
					if(!(linked instanceof Portal)) {
						e.setCancelled(true);
						return;
					}
					e.setTo(linked.getInnerBlocks()[(linked.getInnerBlocks().length-1)].getLocation());
					e.setCancelled(false);
				} else {
					e.setCancelled(true);
				}
				break;
			}
		}
		e.setCancelled(false);
	}

	@EventHandler
	public void onEntityPortal(EntityPortalEvent e) {
		if(e.isCancelled()) {
			return;
		}

		e.setCancelled(true);

		Block block = getPortalNearby(e.getEntity().getLocation().getBlock());
		if(!(block instanceof Block)) {
			return;
		}
		for(Portal portal : Configuration.getPortalConfig().getPortals().values()) {
			List<Block> blocks = Arrays.asList(portal.getInnerBlocks());
			if(blocks.contains(block)) {
				if(e.getEntity() instanceof Item) {
					e.setCancelled(true);
					return;
				}
				e.useTravelAgent(false);
				if(portal.isLinked()) {
					Portal linked = portal.getLinkedPortal();
					if(!(linked instanceof Portal)) {
						e.setCancelled(true);
						return;
					}
					e.setTo(linked.getInnerBlocks()[(linked.getInnerBlocks().length-1)].getLocation());
					e.setCancelled(false);
				} else {
					e.setCancelled(true);
				}
				break;
			}
		}
		e.setCancelled(false);
	}

	public Block getPortalNearby(Block block) {
		BlockFace[] faces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.SELF}; 
		for(BlockFace face : faces) {
			if(block.getRelative(face).getType() == Material.PORTAL) {
				return block.getRelative(face);
			}
		}
		return null;
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(time.containsKey(e.getPlayer().getName())) {
			time.remove(e.getPlayer().getName());
		}
	}
	
	@EventHandler
	public void onQuit(PlayerKickEvent e) {
		if(time.containsKey(e.getPlayer().getName())) {
			time.remove(e.getPlayer().getName());
		}
	}

}