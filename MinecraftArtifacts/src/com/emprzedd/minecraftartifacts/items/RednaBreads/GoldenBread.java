package com.emprzedd.minecraftartifacts.items.RednaBreads;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.emprzedd.minecraftartifacts.items.ArtifactItem;

public class GoldenBread extends ArtifactItem{
	public GoldenBread(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public GoldenBread() {
		this(formatName(Rarity.COMMON, "Golden Bread"),Material.BREAD,"Preserved only for the purest of the pure.");
	}
	
	
	
	///

	@Override
	protected void init() {
		this.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
		ItemMeta newMeta = this.getItemMeta();
		newMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		this.setItemMeta(newMeta);
		
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
			p.setSaturation(p.getSaturation()+6);
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,20*30, 4));
		}
	}
}