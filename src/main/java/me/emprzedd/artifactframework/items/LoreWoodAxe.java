package me.emprzedd.artifactframework.items;

import java.util.UUID;

import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/*
 * Lore wood axe
 * 
 * legacy item, used as a test for when first making plugin
 * 
 * Tier: good wood
 * 
 * Desc:
 * A wood axe with good stats
 * */
public class LoreWoodAxe extends ArtifactItem implements Listener{
	public LoreWoodAxe(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public LoreWoodAxe() {
		this(Rarity.formatText(Rarity.UNIQUE, "Lore Wood Axe"),Material.WOODEN_AXE,"&e&oAxe made from &nL&e&oore Wood and imbued with magic.");
	}

	
	
	PotionEffect fatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING,60,1,false,false,false);
	PotionEffect fatigue2 = new PotionEffect(PotionEffectType.SLOW_DIGGING,60,2,false,false,false);
	
	PotionEffect slow = new PotionEffect(PotionEffectType.SLOW,60,1,false,false,false);
	PotionEffect slow2 = new PotionEffect(PotionEffectType.SLOW,60,2,false,false,false);
	
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
		
		super.canDropItem = true;
		super.canPlaceInItemFrame=true;
		super.canTrack = true;
		super.canPlaceInInventory = true;
	}

	@Override
	protected void reloadConfig() {
		// TODO Auto-generated method stub
	}
	
	
	private boolean giveWeightPenelty(Player player) {
		//wrote like this so i can change formula later
		int inventorySize = player.getInventory().getSize()-5;//5 for armor slots and shld
		int penelty = inventorySize/9;

		if(penelty > 0) {
			slow.apply(player);
		}
		if(penelty > 1) {
			fatigue.apply(player);
		}
		if(penelty > 2) {
			slow2.apply(player);
		}
		
		return true;
	}
	
	
	@EventHandler
	public void attackWeightCheck(EntityDamageByEntityEvent e) {
	    if (!(e.getDamager() instanceof Player))
	    	return;
	    Player player = (Player)e.getDamager();
		if(!super.hasInInventory(player.getInventory()))
			return;
		
		giveWeightPenelty(player);
	}
	
	

}
