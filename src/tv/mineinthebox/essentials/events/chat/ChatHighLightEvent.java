package tv.mineinthebox.essentials.events.chat;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

import tv.mineinthebox.essentials.xEssentials;
import tv.mineinthebox.essentials.events.customevents.PlayerChatHighLightEvent;
import tv.mineinthebox.essentials.interfaces.XOfflinePlayer;
import tv.mineinthebox.essentials.interfaces.XPlayer;

public class ChatHighLightEvent implements Listener {
	
	private final xEssentials pl;
	
	public ChatHighLightEvent(xEssentials pl) {
		this.pl = pl;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void OnHighLight(PlayerChatHighLightEvent e) {
		for(String name : e.getCalledPlayersAsStringArray()) {
			if(!e.getMessage().contains(pl.getConfiguration().getChatConfig().getHashTag()+name)) {
				if(pl.getManagers().getPlayerManager().isOnline(name)) {
					XPlayer xp = pl.getManagers().getPlayerManager().getPlayer(name);
					if(xp.isVanished()) {
						e.setMessage(e.getMessage().replaceAll(name, ChatColor.GRAY+"[offline]"+e.getHashTag()+name+e.getSuffix()));
					} else {
						xp.getBukkitPlayer().getWorld().playEffect(xp.getBukkitPlayer().getLocation(), Effect.MOBSPAWNER_FLAMES, 100);
						xp.getBukkitPlayer().playSound(xp.getBukkitPlayer().getLocation(), Sound.BLOCK_NOTE_HARP, 10, 100);
						xp.getBukkitPlayer().playSound(xp.getBukkitPlayer().getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 10, 100);
						xp.getBukkitPlayer().playSound(xp.getBukkitPlayer().getLocation(), Sound.BLOCK_NOTE_SNARE, 10, 100);
						e.setMessage(e.getMessage().replaceAll(name, e.getHashTag()+name+e.getSuffix()));	
					}
				} else {
					if(name.startsWith("town-")) {
						e.setMessage(e.getMessage().replaceAll(name, ChatColor.DARK_GREEN+"[Town]"+e.getHashTag()+name+e.getSuffix()));
					} else {
						e.setMessage(e.getMessage().replaceAll(name, ChatColor.GRAY+"[offline]"+e.getHashTag()+name+e.getSuffix()));	
					}
				}
			}
		}
	}

	@EventHandler
	public void onTabComplete(PlayerChatTabCompleteEvent e) {
		e.getTabCompletions().clear();
		for(XOfflinePlayer off : pl.getManagers().getPlayerManager().getOfflinePlayers()) {
				if(off.getName().toUpperCase().startsWith(e.getLastToken().toUpperCase())) {
					e.getTabCompletions().add(off.getName());
				}
		}
	}
}
