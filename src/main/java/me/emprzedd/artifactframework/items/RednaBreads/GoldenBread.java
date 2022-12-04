package me.emprzedd.artifactframework.items.RednaBreads;

import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class GoldenBread extends ArtifactItem {
	public GoldenBread(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public GoldenBread() {
		this(Rarity.formatText(Rarity.COMMON, "Golden Bread"),Material.BREAD,"Preserved only for the purest of the pure.");
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

	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		if(this.isSelectedArtifact(e.getItem())) {
			Player p = e.getPlayer();
			p.setSaturation(p.getSaturation()+6);
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,20*30, 4));
		}
	}
}