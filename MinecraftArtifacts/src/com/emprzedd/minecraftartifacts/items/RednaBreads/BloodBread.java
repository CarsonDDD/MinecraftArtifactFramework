package com.emprzedd.minecraftartifacts.items.RednaBreads;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.emprzedd.minecraftartifacts.items.ArtifactItem;

public class BloodBread extends ArtifactItem{
	public BloodBread(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public BloodBread() {
		this(formatName(Rarity.COMMON, "Blood Bread"),Material.BREAD,"Eternal Fire.");
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
			p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,20*20, 0));
			p.setFireTicks(Integer.MAX_VALUE-2);
		}
	}
}