package tv.mineinthebox.essentials.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.enums.ParticleEffect;
import tv.mineinthebox.essentials.instances.xEssentialsPlayer;

public class RealisticWaterManager {

	private final Random rand = new Random();
	private boolean cancel = false;

	public void stop() {
		this.cancel = true;
	}

	public boolean isRunning() {
		return !cancel;
	}
	
	private final LinkedList<Item> fishes = new LinkedList<Item>();

	public void start() {
		this.cancel = false;
		new BukkitRunnable() {

			@Override
			public void run() {
				if(cancel) {
					cancel();
				}
				for(Player p : xEssentials.getOnlinePlayers()) {
					xEssentialsPlayer xp = xEssentials.getManagers().getPlayerManager().getPlayer(p.getName());
					if(hasWaterBlocksNearby(p)) {
						if(p.getLocation().getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType() != Material.STATIONARY_WATER && !xp.isVanished()) {
							Block[] water = getWaterBlocks(p);
							if(water.length > 16) {
								Block b = water[rand.nextInt(water.length)];
								ParticleEffect.SPLASH.display(b.getLocation().getBlock().getRelative(BlockFace.UP).getLocation(), 5, 2, 5, 100, 10);
								Location loc = b.getLocation();
								int y = rand.nextInt(4);
								loc.setY(loc.getY()+(y > 0 ? y : 4));
								ItemStack stack = new ItemStack(Material.RAW_FISH, 1, (short)rand.nextInt(4));
								ItemMeta meta = stack.getItemMeta();
								meta.setDisplayName("fakefish");
								stack.setItemMeta(meta);
								Item fish = b.getWorld().dropItem(loc, stack);
								fish.setMetadata("fakefish", new FixedMetadataValue(xEssentials.getPlugin(), "its fake"));
								fishes.add(fish);	
							}
						}
					}
				}
				//iterate
				Iterator<Item> items = fishes.iterator();
				
				while(items.hasNext()) {
					Item item = items.next();
					if(item.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
						item.teleport(item.getLocation().add(0, -180, 0));
						items.remove();
					} else {
						if(item.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.STATIONARY_WATER && item.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
							item.teleport(item.getLocation().add(0, -180, 0));
							items.remove();
						}
					}
				}
				//fishes.clear(); //fixes mutation glitch when a stack passes a other item and is changed to a other object, this clears it all and we will wait for natural despawn.
			}

		}.runTaskTimer(xEssentials.getPlugin(), 0L, 10L);
	}

	private boolean hasWaterBlocksNearby(Player p) {
		if(getWaterBlocks(p).length > 0) {
			return true;
		}
		return false;
	}

	private Block[] getWaterBlocks(Player p) {
		int range = 16;

		Location loc = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
		loc.setX(loc.getX()-(range/2));
		loc.setY(loc.getY()-1);
		loc.setZ(loc.getZ()-(range/2));
		List<Block> blocks = new ArrayList<Block>();
		for(int x = 0; x < range; x++) {
				for(int z = 0; z < range; z++) {
					Block block = loc.getWorld().getBlockAt(loc.getBlockX()+x, loc.getBlockY(), loc.getBlockZ()+z);
					if(block.getType() == Material.STATIONARY_WATER && block.getRelative(BlockFace.DOWN).getType() == Material.STATIONARY_WATER) {
						blocks.add(block);
					}
			}
		}
		return blocks.toArray(new Block[blocks.size()]);
	}

}
