package tv.mineinthebox.essentials.minigames.chickentennis;

import java.io.File;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.interfaces.XPlayer;
import tv.mineinthebox.essentials.minigames.MinigameArena;
import tv.mineinthebox.essentials.minigames.MinigameType;
import tv.mineinthebox.essentials.minigames.chickentennis.events.ChickenTennisBallEvent;

public class ChickenTennisArena implements MinigameArena {

	private final ItemStack shovel = new ItemStack(Material.IRON_SPADE) {{
		ItemMeta meta = getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Chicken spade");
		meta.setLore(Arrays.asList(new String[] {
				ChatColor.LIGHT_PURPLE + "Chicken spade!",
				"",
				ChatColor.GRAY + "left click on the chicken to launch him once when fallen to the other side you score!",
		}));
		setItemMeta(meta);
	}};

	private final File f;
	private final String name;
	private final Location spawnpoint1;
	private final Location spawnpoint2;
	private final Location chickenlocation;
	private final int boundx;
	private final int boundz;
	private final int win;
	private Chicken chicken;
	private boolean isStarted = false;

	private XPlayer p1;
	private XPlayer p2;

	public ChickenTennisArena(File f, FileConfiguration con) {
		this.f = f;
		this.name = f.getName().replace(".yml", "");
		this.spawnpoint1 = new Location(Bukkit.getWorld(con.getString("game.spawnpoint1.world")), con.getInt("game.spawnpoint1.x"), con.getInt("game.spawnpoint1.y"), con.getInt("game.spawnpoint1.z"), con.getInt("game.spawnpoint1.yaw"), con.getInt("game.spawnpoint1.pitch"));
		this.spawnpoint2 = new Location(Bukkit.getWorld(con.getString("game.spawnpoint2.world")), con.getInt("game.spawnpoint2.x"), con.getInt("game.spawnpoint2.y"), con.getInt("game.spawnpoint2.z"), con.getInt("game.spawnpoint2.yaw"), con.getInt("game.spawnpoint2.pitch"));
		this.chickenlocation = new Location(Bukkit.getWorld(con.getString("game.chicken.world")), con.getInt("game.chicken.x"), con.getInt("game.chicken.y"), con.getInt("game.chicken.z"));
		this.boundx = con.getInt("bounds.x");
		this.boundz = con.getInt("bounds.z");
		this.win = con.getInt("score");
	}

	/**
	 * @author xize
	 * <p>returns the name of the arena</p>
	 * @return String
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @author xize
	 * <p>returns the location of spawn 1</p>
	 * @return Location
	 */
	public Location getSpawnPoint1() {
		return this.spawnpoint1;
	}

	/**
	 * @author xize
	 * <p>returns the location of spawn 2</p>
	 * @return Location
	 */
	public Location getSpawnPoint2() {
		return this.spawnpoint2;
	}

	/**
	 * @author xize
	 * <p>returns the players in this arena!</p>
	 * @return XPlayer[]
	 */
	public XPlayer[] getPlayers() {
		return new XPlayer[] {p1, p2};
	}

	/**
	 * @author xize
	 * <p>returns the chicken spawn location</p>
	 * @return Location
	 */
	public Location getChickenSpawnLocation() {
		return chickenlocation;
	}

	/**
	 * @author xize
	 * <p>returns the x bounds</p>
	 * @return int
	 */
	public int getBoundsX() {
		return boundx;
	}

	/**
	 * @author xize
	 * <p>returns the z bounds</p>
	 * @return int
	 */
	public int getBoundsZ() {
		return boundz;
	}

	/**
	 * @author xize
	 * <p>returns the amount of score a player should have to win, and when the game ends</p>
	 * @return int
	 */
	public int getWinScore() {
		return win;
	}

	/**
	 * @author xize
	 * <p>returns true if the arena is full, otherwise false</p>
	 * @return boolean
	 */
	@Override
	public boolean isFull() {
		return (p1 != null && p2 != null);
	}

	public boolean isStarted() {
		return isStarted;
	}

	/**
	 * @author xize
	 * <p>this adds the player as long the player is in survival, and also saves the inventory of the player before reset</p>
	 * @see XPlayer#saveInventory()
	 * @param xp - the player to be added.
	 */
	@Override
	public void addPlayer(XPlayer xp) {
		if(p1 == null) {
			if(xp.getPlayer().getGameMode() != GameMode.SURVIVAL) {
				xp.getPlayer().sendMessage(ChatColor.RED + "you cannot play minigames aslong you are not in survival");
				return;
			}
			setInventory(xp);
			this.p1 = xp;
			this.p1.getPlayer().teleport(getSpawnPoint1());
			Bukkit.broadcastMessage(ChatColor.GRAY + xp.getUser() + " has joined chicken tennis arena " + ChatColor.GREEN + getName());
		} else if(p2 == null) {
			if(xp.getPlayer().getGameMode() != GameMode.SURVIVAL) {
				xp.getPlayer().sendMessage(ChatColor.RED + "you cannot play minigames aslong you are not in survival");
				return;
			}
			setInventory(xp);
			this.p2 = xp;
			this.p2.getPlayer().teleport(getSpawnPoint2());

			this.chicken = (Chicken)chickenlocation.getWorld().spawnEntity(chickenlocation, EntityType.CHICKEN);
			//chicken.setVelocity(new Vector(0,0,0));
			chicken.setCustomName(ChatColor.GOLD + "tennis ball");
			chicken.setCustomNameVisible(true);

			this.isStarted = false;

			initalizeChickenGroundCheck();
			
			new BukkitRunnable() {
				private int countdown = 10;

				@Override
				public void run() {
					if(countdown == 0) {
						p1.getPlayer().sendMessage(ChatColor.GRAY + "game is started!");
						p1.getPlayer().playSound(p1.getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL, 1F, 1F);
						p2.getPlayer().sendMessage(ChatColor.GRAY + "game is started!");
						p2.getPlayer().playSound(p2.getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL, 1F, 1F);
						cancel();
					} else {
						p1.getPlayer().sendMessage(ChatColor.GRAY + "count down: " + countdown);
						p1.getPlayer().playSound(p1.getPlayer().getLocation(), Sound.NOTE_PLING, 1F, 1F);
						p2.getPlayer().sendMessage(ChatColor.GRAY + "count down: " + countdown);
						p2.getPlayer().playSound(p2.getPlayer().getLocation(), Sound.NOTE_PLING, 1F, 1F);
					}
					countdown--;
				}

			}.runTaskTimer(xEssentials.getPlugin(), 3L, 10L);

		} else {
			throw new IllegalArgumentException("cannot add player to ChickenTennis minigame, as both slots are full!");
		}
	}

	private ChickenTennisArena getArena() {
		return this;
	}
	
	private void initalizeChickenGroundCheck() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if(chicken != null) {
					if(chicken.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
						if(chicken.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WEB) {
							Location loc = chicken.getLocation();
							chicken.remove();
							chicken = (Chicken)loc.getWorld().spawnEntity(getChickenSpawnLocation(), EntityType.CHICKEN);
							chicken.setCustomName(ChatColor.GOLD + "tennis ball");
							chicken.setCustomNameVisible(true);
						} else {
							Location loc = chicken.getLocation();
							Bukkit.getPluginManager().callEvent(new ChickenTennisBallEvent(loc, getArena()));
							chicken.remove();
							chicken = null;
							chicken = (Chicken)loc.getWorld().spawnEntity(getChickenSpawnLocation(), EntityType.CHICKEN);
							chicken.setCustomName(ChatColor.GOLD + "tennis ball");
							chicken.setCustomNameVisible(true);	
						}
					}
				} else {
					cancel();
				}
			}
		}.runTaskTimer(xEssentials.getPlugin(), 0L, 1L);
	}

	/**
	 * @author xize
	 * <p>returns true if the player is inside the arena, otherwise false</p>
	 * @param xp - the player to be searched for
	 * @return boolean
	 */
	public boolean containsPlayer(XPlayer xp) {
		if(p1.equals(xp)) {
			return true;
		} else if(p2.equals(xp)) {
			return true;
		}
		return false;
	}

	/**
	 * @author xize
	 * <p>removes the player from the arena, this means the game is automatic ended and inventories will backup</p>
	 * @see XPlayer#loadInventory()
	 * @param xp
	 */
	@Override
	public void removePlayer(XPlayer xp) {
		if(p1.equals(xp)) {
			p1.loadInventory();
			this.p1 = null;
		} else if(p2.equals(xp)) {
			p2.loadInventory();
			this.p2 = null;
		}
	}

	private void setInventory(XPlayer xp) {
		xp.saveInventory();
		xp.getPlayer().getInventory().clear();
		xp.getPlayer().getInventory().addItem(shovel);
	}

	@Override
	public MinigameType getType() {
		return MinigameType.CHICKEN_TENNIS;
	}

	/**
	 * @author xize
	 * <p>unregister the arena and removes the file from the minigame server</p>
	 */
	@Override
	public void remove() {
		xEssentials.getManagers().getMinigameManager().removeMinigame(getType(), this);
		f.delete();
	}
	
	@Override
	public void reset() {
		if(this.chicken != null) {
			this.chicken.remove();
			this.chicken = null;
		}
		if(p1 != null) {
			this.p1.getPlayer().removeMetadata("gameType", xEssentials.getPlugin());
			this.p1.getPlayer().removeMetadata("game", xEssentials.getPlugin());
			this.p1.getPlayer().removeMetadata("gameScore", xEssentials.getPlugin());
			this.p1.loadInventory();
			this.p1.getPlayer().chat("/spawn");
			this.p1 = null;
		}
		if(p2 != null) {
			this.p2.getPlayer().removeMetadata("gameType", xEssentials.getPlugin());
			this.p2.getPlayer().removeMetadata("game", xEssentials.getPlugin());
			this.p2.getPlayer().removeMetadata("gameScore", xEssentials.getPlugin());
			this.p2.loadInventory();
			this.p2.getPlayer().chat("/spawn");
			this.p2 = null;
		}
		if(this.isStarted) {
			this.isStarted = false;
		}
	}

}