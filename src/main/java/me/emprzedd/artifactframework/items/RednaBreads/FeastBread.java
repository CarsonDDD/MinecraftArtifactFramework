package me.emprzedd.artifactframework.items.RednaBreads;

import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class FeastBread extends ArtifactItem {
	public FeastBread(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public FeastBread() {
		this(Rarity.formatText(Rarity.COMMON, "Feast Bread"),Material.BREAD,"Perfectly baked Rednan delight that tastes like paradise.");
	}
	
	
	
	///

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		canTrack = false;
		canPlaceInInventory = true;
		canDropItem = true;
		canPlaceInItemFrame = true;
	}
	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		if(this.isSelectedArtifact(e.getItem())) {
			Player p = e.getPlayer();

			p.setSaturation(p.getSaturation() + 6);
			p.setFoodLevel(p.getFoodLevel() + 3);
		}
	}
}