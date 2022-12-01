package me.emprzedd.artifactframework;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemInventoryHandler implements Listener{
    //Main plugin;
    
    //NOT IMPLEMENTED YET  
    public ItemInventoryHandler(JavaPlugin plugin) {
    	//this.plugin = plugin;
    }

    //blocks hoppers
	@EventHandler
	public void onHopperMove(InventoryMoveItemEvent e) {
		ArtifactItem artifact = ArtifactItem.convertItemToArtifact(e.getItem());
		if(artifact != null && !artifact.canPlaceInInventory) {
			Location location = e.getInitiator().getLocation();
			Collection<Entity> players = location.getWorld().getNearbyEntities(location, 501, 501, 501, p -> p.getType() == EntityType.PLAYER);
			Player firstPlayer = null;
			for(Entity playerEntity : players) {
				//gets first player entity, might be redundant if filter actually works
				if(playerEntity instanceof Player) {
					Player player = (Player)playerEntity;
	
					if(firstPlayer == null)
						firstPlayer = player; 
					
					artifact.screamAtPlayer(player,"This Artifact is to powerful to be stored... in a hopper...");
				}	
			}
			
			e.getInitiator().remove(e.getItem());
			e.getSource().remove(e.getItem());
			
			if(firstPlayer != null)
				firstPlayer.getInventory().addItem(e.getItem());
			else {
				//plugin.logToFile("(Look for last drop)Egg might be lost in a hopper? 'firstPlayer' was null when trying to return the egg to a player");
			}

			e.setCancelled(true);
		}
		
	}
	
    //blocks shift clicking
    @EventHandler
    public void onInventoryShiftClick(InventoryClickEvent e) {
    	if(e.getWhoClicked().isOp()) return;
    	if(e.getInventory() != null &&(e.getInventory().getType() == InventoryType.ANVIL||e.getInventory().getType() == InventoryType.PLAYER)) return;
    	
        if (e.getClick().isShiftClick()) {
            Inventory clicked = e.getClickedInventory();
            if (clicked == e.getWhoClicked().getInventory()) {
                // The item is being shift clicked from the bottom to the top
                //ItemStack clickedOn = e.getCurrentItem();
        		ArtifactItem artifact = ArtifactItem.convertItemToArtifact(e.getCurrentItem());

                if (artifact != null && !artifact.canPlaceInInventory) {
                    artifact.screamAtPlayer((Player)e.getWhoClicked(),"This Artifact is to powerful to be stored...");
                    e.setCancelled(true);
                }
            }
        }
    }
    
    //blocks normal
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
    	if(e.getWhoClicked().isOp()) return;
    	if(e.getClickedInventory() != null &&e.getClickedInventory().getType() == InventoryType.ANVIL) return;
    	
        Inventory clicked = e.getClickedInventory();
        if (clicked != e.getWhoClicked().getInventory()) { // Note: !=
            // The cursor item is going into the top inventory
            //ItemStack onCursor = e.getCursor();
            ArtifactItem artifact = ArtifactItem.convertItemToArtifact(e.getCursor());
            if (e.getClickedInventory() != null && artifact != null && !artifact.canPlaceInInventory){
                artifact.screamAtPlayer((Player)e.getWhoClicked(),"This Artifact is to powerful to be stored...");
                e.setCancelled(true);     	
            }  
        }
    }
    
    //blocks drag
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
    	if(e.getWhoClicked().isOp()) return;
    	if(e.getInventory() != null && e.getInventory().getType() == InventoryType.ANVIL) return;
        //ItemStack dragged = e.getOldCursor(); // This is the item that is being dragged
        ArtifactItem artifact = ArtifactItem.convertItemToArtifact(e.getOldCursor());
        if (artifact != null && !artifact.canPlaceInInventory) {
            int inventorySize = e.getInventory().getSize(); // The size of the inventory, for reference

            // Now we go through all the slots and check if the slot is inside our inventory (using the inventory size as reference)
            for (int i : e.getRawSlots()) {
                if (i < inventorySize) {
                    artifact.screamAtPlayer((Player)e.getWhoClicked(),"This Artifact is to powerful to be stored...");
                    e.setCancelled(true);
                    break;
                }
            }
        }
    }
    
    
    
    //blocks itemframe placement if artifact has the proper tag
    @EventHandler
    public void onItemFrameClick(PlayerInteractEntityEvent e) {

    	if(e.getRightClicked() instanceof ItemFrame) {
    		ArtifactItem mainHand = ArtifactItem.convertItemToArtifact(e.getPlayer().getInventory().getItemInMainHand());
    		ArtifactItem offHand = ArtifactItem.convertItemToArtifact(e.getPlayer().getInventory().getItemInOffHand());
    		ItemFrame frame = (ItemFrame)e.getRightClicked();

            if(mainHand != null && !mainHand.canPlaceInItemFrame){
                mainHand.screamAtPlayer(e.getPlayer(),"This Artifact is to powerful to be stored... In an item frame...");
                e.setCancelled(true);
            }
            else if (offHand != null && !offHand.canPlaceInItemFrame && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) {
                offHand.screamAtPlayer(e.getPlayer(),"This Artifact is to powerful to be stored... In an item frame...");
                e.setCancelled(true);
            }


        }
    	   
    	
    }
    
    
    //if players inv is full when the e is canceled the item vanishes
    /*
     * 
     * NEEDS ITEM LOSE FIX...later
     * 
     * */
    //blocks drops
    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
    	ArtifactItem artifact = ArtifactItem.convertItemToArtifact(e.getItemDrop().getItemStack());
		if(artifact != null && !artifact.canDropItem) {
			Player player = e.getPlayer();
			if(player.getInventory().getSize() != player.getInventory().getMaxStackSize()) {
                artifact.screamAtPlayer(e.getPlayer(),"This Artifact is bound to you...");
				e.setCancelled(true);
			}
		}
    }
    
}
