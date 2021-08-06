package com.emprzedd.minecraftartifacts;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.emprzedd.minecraftartifacts.items.ArtifactItem;

public class ArtifactSelectionGUI implements InventoryHolder{

	private Inventory inv;

    public ArtifactSelectionGUI(){
        //create logic to handle more then 9 items
        inv = Bukkit.createInventory(this,9*4, "§4Artifacts");

    }

    @Override
    public Inventory getInventory(){
    	for(int i =0; i < ArtifactItem.ArtifactList.size();i++)
            inv.setItem(i, ArtifactItem.ArtifactList.get(i));
    	
        return inv;
    }
}
