package me.emprzedd.artifactframework;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;


public class ItemStopPlace implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent e) {
		/*ArtifactItem mainHand = ArtifactItem.converItemToArtifact(e.getPlayer().getInventory().getItemInMainHand());
		ArtifactItem offHand = ArtifactItem.converItemToArtifact(e.getPlayer().getInventory().getItemInOffHand());*/
		ArtifactItem item = ArtifactItem.convertItemToArtifact(e.getItemInHand());
		if(item !=null &&!item.canPlace/*(mainHand != null && !mainHand.canPlace) || (offHand != null && !offHand.canPlace)*/) {
			e.setCancelled(true);
			e.setBuild(false);	
		}
	}
}
