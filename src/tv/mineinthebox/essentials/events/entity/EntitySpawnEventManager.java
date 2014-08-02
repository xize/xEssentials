package tv.mineinthebox.essentials.events.entity;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Chunk;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import tv.mineinthebox.essentials.Configuration;

public class EntitySpawnEventManager implements Listener {

	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent e) {
			if(Configuration.getEntityConfig().getEntitys().containsKey(e.getEntityType().name().toLowerCase())) {
				Iterator<Entry<Boolean, String[]>> it = Configuration.getEntityConfig().getEntitys().get(e.getEntityType().name().toLowerCase()).entrySet().iterator();
				while(it.hasNext()) {
					Entry<Boolean, String[]> entry = it.next();
					if(!entry.getKey()) {
						if(e.getEntity() instanceof Wither) {
							e.setCancelled(true);
						} else {
							e.getEntity().teleport(e.getEntity().getLocation().add(0, -200, 0));
							e.getEntity().damage(e.getEntity().getMaxHealth(), e.getEntity());	
						}
					} else {
						Chunk chunk = e.getEntity().getLocation().getChunk();
						if(!Arrays.asList(entry.getValue()).contains(chunk.getWorld().getBiome(chunk.getX(), chunk.getZ()).name())) {
							if(e.getEntity() instanceof Wither) {
								e.setCancelled(true);
							} else {
								e.getEntity().teleport(e.getEntity().getLocation().add(0, -200, 0));
								e.getEntity().damage(e.getEntity().getMaxHealth(), e.getEntity());	
							}
						}
					}
					return;
				}
			}
		}
	}
