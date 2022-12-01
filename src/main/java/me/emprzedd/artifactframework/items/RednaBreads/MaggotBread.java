package me.emprzedd.artifactframework.items.RednaBreads;

import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class MaggotBread extends ArtifactItem {
	public MaggotBread(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public MaggotBread() {
		this(Rarity.formatText(Rarity.COMMON, "Maggot Bread"),Material.BREAD,"Something inside it is moving.");
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

	@Override
	protected void reloadConfig() {
		// TODO Auto-generated method stub
		
	}
	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		if(this.isSelectedArtifact(e.getItem())) {
			Player p = e.getPlayer();
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,20*120, 0));
			p.addPotionEffect(new PotionEffect(PotionEffectType.POISON,20*120, 0));
		}
	}
}