package me.emprzedd.artifactframework.items;

import java.util.UUID;

import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;

/*
 * Admin staff
 * 
 * Tier: Admin
 * 
 * Desc:
 * one hit KO everything.
 * */
public class AdminStaff extends ArtifactItem {
	public AdminStaff(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public AdminStaff() {
		this(Rarity.formatText(Rarity.ADMIN, "Admin Staff"),Material.BLAZE_ROD,"A powerful staff which can only\nbe wielded by a higher power.");
	}
	
	@Override
	protected void init() {
		this.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
		this.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1000);
		this.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 256);
		this.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, 256);
		
		ItemMeta newMeta = this.getItemMeta();
		newMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(),"ass",60.0,Operation.ADD_NUMBER,EquipmentSlot.HAND));
		this.setItemMeta(newMeta);
		
		this.canSmite = true;
	}

	@Override
	protected void reloadConfig() {
		// TODO Auto-generated method stub
	}
}
