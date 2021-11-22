package com.emprzedd.minecraftartifacts;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.emprzedd.minecraftartifacts.items.ArtifactItem;

public class ItemTracker implements Listener{
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		ArtifactItem artifact = ArtifactItem.convertItemToArtifact(e.getItemDrop().getItemStack());
		if(artifact != null && artifact.canTrack) {
			artifact.getLogger().logToFile("("+FileLogger.entityLocation(e.getPlayer())+")"+artifact.getRawName()+" was dropped by '" + e.getPlayer().getName() + "'.");
		}
	}
	
	@EventHandler
	public void onPickup(EntityPickupItemEvent e) {
		ArtifactItem artifact = ArtifactItem.convertItemToArtifact(e.getItem().getItemStack());
		if(artifact != null && artifact.canTrack) {
			artifact.getLogger().logToFile("("+FileLogger.entityLocation(e.getEntity())+")"+artifact.getRawName()+" was picked up by '" + e.getEntity().getName() + ".");
		}
	}
	
	//tracks interact
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
    	
    	if(ArtifactItem.artifactFullTrack) {
        	ArtifactItem[] artifacts = ArtifactItem.findAllArtifacts(e.getPlayer().getInventory());
        	for(ArtifactItem artItem : artifacts) {
        		if(artItem !=null && artItem.canTrack) {
        			artItem.getLogger().logToFile("("+FileLogger.entityLocation(e.getPlayer())+")"+artItem.getRawName()+" was in the inventory of '" + e.getPlayer().getName() + " when interacting("+e.getEventName()+")");
        		}
        	}
    	}
    	else {
    		ArtifactItem artItem = ArtifactItem.convertItemToArtifact(e.getItem());
    		if(artItem !=null && artItem.canTrack) {
    			artItem.getLogger().logToFile("("+FileLogger.entityLocation(e.getPlayer())+")"+artItem.getRawName()+" was in the hand of '" + e.getPlayer().getName() + ".");
    		}
    	}
	}
	
    //tracks iventoy use
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
    	
    	if(ArtifactItem.artifactFullTrack) {
        	ArtifactItem[] artifacts = ArtifactItem.findAllArtifacts(e.getWhoClicked().getInventory());
        	for(ArtifactItem artItem : artifacts) {
        		if(artItem !=null && artItem.canTrack) {
        			artItem.getLogger().logToFile("("+FileLogger.entityLocation(e.getWhoClicked())+")"+artItem.getRawName()+" was in the inventory of '" + e.getWhoClicked().getName() + ".");
        		}
        	}
    	}
    	else {
    		ArtifactItem artItem = ArtifactItem.convertItemToArtifact(e.getCurrentItem());
    		if(artItem !=null && artItem.canTrack) {
    			artItem.getLogger().logToFile("("+FileLogger.entityLocation(e.getWhoClicked())+")"+artItem.getRawName()+" was in the hand of '" + e.getWhoClicked().getName() + ".");
    		}
    	}
    }
    
    //tracks join
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
    	ArtifactItem[] artifacts = ArtifactItem.findAllArtifacts(e.getPlayer().getInventory());
    	for(ArtifactItem artItem : artifacts) {
    		if(artItem !=null && artItem.canTrack) {
    			artItem.getLogger().logToFile("("+FileLogger.entityLocation(e.getPlayer())+")"+e.getPlayer().getName()+" logged on with '" + artItem.getRawName() + ".");
    		}
    	}
	}
	
    //tracks leave
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
    	ArtifactItem[] artifacts = ArtifactItem.findAllArtifacts(e.getPlayer().getInventory());
    	for(ArtifactItem artItem : artifacts) {
    		if(artItem !=null && artItem.canTrack) {
    			artItem.getLogger().logToFile("("+FileLogger.entityLocation(e.getPlayer())+")"+e.getPlayer().getName()+" logged off with '" + artItem.getRawName() + ".");
    		}
    	}
	}
	
}
