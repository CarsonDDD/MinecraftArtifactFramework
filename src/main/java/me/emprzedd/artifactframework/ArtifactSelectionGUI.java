package me.emprzedd.artifactframework;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Map;

public class ArtifactSelectionGUI implements InventoryHolder{

	private final Inventory inv;

    public ArtifactSelectionGUI(){
        //create logic to handle more then 9 items
        inv = Bukkit.createInventory(this,9*4, "�4Artifacts");
    }

    @Override
    public Inventory getInventory(){
        int i =0;
        for (Map.Entry<ArtifactKey, ArtifactItem> entry : ArtifactItem.getArtifactItems().entrySet()) {
            inv.setItem(i++, entry.getValue());
        }
        return inv;
    }
}
