package com.emprzedd.minecraftartifacts.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;

public class PumpkinHead extends ArtifactItem implements Listener{

	public PumpkinHead(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public PumpkinHead() {
		this(formatName(Rarity.COMMON, "Spooky Head"),Material.CARVED_PUMPKIN,"Be carful, its a tight fit!");
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.canDropItem = true;
		super.canPlaceInInventory = true;
		super.canPlaceInItemFrame = true;
		super.canPlace = false;
		
		this.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
		this.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
		this.addUnsafeEnchantment(Enchantment.OXYGEN, 5);
	}

	@Override
	protected void reloadConfig() {
		// TODO Auto-generated method stub
		
	}

}
