package com.emprzedd.minecraftartifacts.items;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

public class LoreWoodAxe extends ArtifactItem{

	public LoreWoodAxe(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	
	public LoreWoodAxe() {
		this(ArtifactItem.getNameFormatUniqueTemplate("Lore Wood Axe"),Material.WOODEN_AXE,"&e&oAxe made from &nL&e&oore Wood and imbued with magic.");
	}

	@Override
	protected void init() {
		this.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, 5);
		this.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 5);
		this.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
		this.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
		this.addUnsafeEnchantment(Enchantment.MENDING, 1);
		this.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
		this.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 5);
		this.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
		this.addUnsafeEnchantment(Enchantment.DIG_SPEED, 6);
		this.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);

		
		ItemMeta newMeta = this.getItemMeta();
		
		newMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		newMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(),"asss",10.0,Operation.ADD_NUMBER,EquipmentSlot.HAND));
		newMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(),"asss",6.0,Operation.ADD_NUMBER,EquipmentSlot.HAND));
		newMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, new AttributeModifier(UUID.randomUUID(),"asss",10.0,Operation.ADD_NUMBER,EquipmentSlot.HAND));
		newMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(),"asss",4.0,Operation.ADD_NUMBER,EquipmentSlot.HAND));
	
		this.setItemMeta(newMeta);
		
		this.canSmite = false;
		this.canDropItem = true;
		this.canPlaceInItemFrame=true;
		this.canPlaceInInventory=false;
		this.canRename=false;
	}

}
