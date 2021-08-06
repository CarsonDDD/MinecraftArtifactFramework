package com.emprzedd.minecraftartifacts;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;

import com.emprzedd.minecraftartifacts.items.ArtifactItem;

public class ItemStopRename implements Listener{

	
	//--------------Stop anvil rename------------------//
	
	//https://bukkit.org/threads/any-way-to-prevent-renaming-of-items-anvil.183654/
	//https://github.com/Xemorr/StopRenaming/blob/master/src/main/java/me/xemor/stoprenaming/events/Rename.java
	
	//currently stops the ability to change item names for items containing the flag tag
    @EventHandler
    public void onAnvil(InventoryClickEvent e) {
        if (e.getClickedInventory() instanceof AnvilInventory) {
            AnvilInventory anvil = (AnvilInventory) e.getClickedInventory();
            if (e.getSlotType() == InventoryType.SlotType.RESULT) {
                if (anvil.getRenameText().isEmpty()) {
                    return;
                }
                ArtifactItem artifact = ArtifactItem.convertItemToArtifact(e.getCurrentItem());
                /*if(ArtifactItem.isArtifact(e.getCurrentItem())) {
                	e.setCancelled(true);
                }*/
                if(artifact!=null && !artifact.canRename) {
                	e.setCancelled(true);
                }
                return;
            }
        }
    }
}
