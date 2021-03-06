package com.emprzedd.minecraftartifacts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.emprzedd.minecraftartifacts.items.ArtifactItem;

import net.md_5.bungee.api.ChatColor;

public class ItemSmite implements Listener{

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		/*if(!e.getPlayer().hasPermission("MinecraftArtifacts.Admin")) {
			for(ItemStack item : e.getPlayer().getInventory()) {
				ArtifactItem artifact = ArtifactItem.convertItemToArtifact(item);				
				if(artifact != null && artifact.canSmite) {
					Bukkit.broadcastMessage(e.getPlayer().getDisplayName() +ChatColor.RED+ " was not strong enough to wield the power of the " + artifact.getDisplayName()+ChatColor.DARK_RED+".");
					e.getPlayer().setHealth(0.0);
					return;
				}
			}
		}*/
		
		if(!e.getPlayer().hasPermission("MinecraftArtifacts.Admin")) {
			ArtifactItem artifact;
			if(ArtifactItem.artifactFullSmite) {
				// Scans inventory for item. More accurate, but requires more resources
				for(ItemStack item : e.getPlayer().getInventory()) {
					artifact = ArtifactItem.convertItemToArtifact(item);				
					if(artifact != null && artifact.canSmite) {
						smite(e.getPlayer(), artifact);
						return;
					}
				}
			}
			else {
				// Only scans item used. Less accurate, but requires less resources
				artifact = ArtifactItem.convertItemToArtifact(e.getItem());				
				if(artifact != null && artifact.canSmite) {
					smite(e.getPlayer(), artifact);
					return;
				}
			}
		}
	}
	
	private void smite(Player player, ArtifactItem artifact) {
		Bukkit.broadcastMessage(player.getDisplayName() +ChatColor.RED+ " was not strong enough to wield the power of the " + artifact.getDisplayName()+ChatColor.DARK_RED+".");
		player.setHealth(0.0);
	}
}

