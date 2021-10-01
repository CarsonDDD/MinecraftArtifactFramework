package com.emprzedd.minecraftartifacts.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class PumpkinHead extends ArtifactItem implements Listener{

	public PumpkinHead(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public PumpkinHead() {
		this(formatName(Rarity.COMMON, "Spooky Head"),Material.PUMPKIN,"A tight fit, be careful.");
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.canDropItem = true;
		super.canPlaceInInventory = true;
		super.canPlaceInItemFrame = true;
		super.canPlace = false;
		
		this.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
		this.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		this.addEnchantment(Enchantment.OXYGEN, 5);
		this.addEnchantment(Enchantment.LUCK, 1);

		ItemMeta newMeta = this.getItemMeta();
		//newMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		LeatherArmorMeta LeatherColor = (LeatherArmorMeta) newMeta;
		LeatherColor.setColor(Color.fromRGB(255, 50, 100));

		this.setItemMeta(newMeta);
		
	}

	@Override
	protected void reloadConfig() {
		// TODO Auto-generated method stub
		
	}

}
