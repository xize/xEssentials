package tv.mineinthebox.essentials.events.blocks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BedrockPlaceEvent implements Listener {
	
	@EventHandler
	public void onBedrockPlace(BlockPlaceEvent e) {
		if(e.getBlock().getType() == Material.BEDROCK) {
			e.getPlayer().sendMessage(ChatColor.RED + "you are not allowed to place bedrock!");
			e.setCancelled(true);
		}
	}

}
