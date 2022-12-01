package me.emprzedd.artifactframework.items;

import java.util.UUID;

import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/*
 * The Shard of the Blessed Dagger
 * 
 * tbh i was way to fucked when i wrote this, i have no clue why this is like this or what it is.
 * rewrite is needed
 * 
 * Tier: 
 * 
 * Desc:
 * On player kill, change enchants on item.
 * */
public class MessengerBoots extends ArtifactItem {
	public MessengerBoots(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public MessengerBoots() {
		this(Rarity.formatText(Rarity.RARE, "Messenger's Boots"),Material.LEATHER_BOOTS,"&e&oQuick and agile.");
	}

	
	
	@Override
	protected void init() {
		this.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);

		ItemMeta newMeta = this.getItemMeta();
		newMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		LeatherArmorMeta LeatherColor = (LeatherArmorMeta) newMeta;
		LeatherColor.setColor(Color.fromBGR(16764572));
		
		newMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(),"asss",0.04,Operation.ADD_NUMBER,EquipmentSlot.FEET));
		newMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(),"asss",-20.0,Operation.ADD_NUMBER,EquipmentSlot.FEET));
		newMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(),"asss",640.0,Operation.ADD_NUMBER,EquipmentSlot.FEET));
		this.setItemMeta(newMeta);
		
		this.canSmite = false;
		this.canDropItem = true;
		this.canPlaceInItemFrame=true;
		this.canPlaceInInventory=false;
		this.canRename=false;
	}
	
	@Override
	protected void reloadConfig() {
		// TODO Auto-generated method stub
	}
}
