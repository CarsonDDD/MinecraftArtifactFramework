package me.emprzedd.artifactframework;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
		
		if(!e.getPlayer().hasPermission("MinecraftArtifacts.Admin") && e.getItem() != null) {
			ArtifactItem artifact;
			if(ArtifactItem.artifactFullSmite) {
				// Scans inventory for item. More accurate, but requires more resources
				for(ItemStack item : e.getPlayer().getInventory()) {
					artifact = ArtifactItem.convertItemToArtifact(item);				
					if(artifact != null && artifact.canSmite) {
						smite(e.getPlayer(), artifact);
						e.setCancelled(true);
						return;
					}
				}
			}
			else {
				// Only scans item used. Less accurate, but requires less resources
				artifact = ArtifactItem.convertItemToArtifact(e.getItem());				
				if(artifact != null && artifact.canSmite) {
					smite(e.getPlayer(), artifact);
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onAttack(EntityDamageByEntityEvent e){
		if(!e.getDamager().hasPermission("MinecraftArtifacts.Admin") && e.getDamager() instanceof HumanEntity) {
				// Only scans item used. Less accurate, but requires less resources
			HumanEntity attacker = (HumanEntity)e.getDamager();
			ArtifactItem artifact = ArtifactItem.convertItemToArtifact(attacker.getInventory().getItemInMainHand());
			if(artifact != null && artifact.canSmite) {
				smite(attacker, artifact);
				e.setCancelled(true);
			}

		}
	}
	
	private void smite(HumanEntity player, ArtifactItem artifact) {
		Bukkit.broadcastMessage(player.getName() +ChatColor.RED+ " was not strong enough to wield the power of the " + artifact.getDisplayName()+ChatColor.RED+".");
		player.getWorld().strikeLightning(player.getLocation());
		player.setLastDamageCause(new EntityDamageEvent(player, EntityDamageEvent.DamageCause.MAGIC, 999));
		player.setHealth(0.0);
	}
}

