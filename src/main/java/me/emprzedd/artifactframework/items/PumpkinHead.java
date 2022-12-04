package me.emprzedd.artifactframework.items;

import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;

public class PumpkinHead extends ArtifactItem implements Listener{

	public PumpkinHead(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public PumpkinHead() {
		this(Rarity.formatText(Rarity.COMMON, "Spooky Head"),Material.CARVED_PUMPKIN,"Be carful, its a tight fit!");
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

}
