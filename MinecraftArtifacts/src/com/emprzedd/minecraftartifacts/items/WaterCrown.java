package com.emprzedd.minecraftartifacts.items;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class WaterCrown extends ArtifactItem implements Listener{

	public WaterCrown(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public WaterCrown() {
		this(formatName(Rarity.COMMON, "Water hat"),Material.DIAMOND_HELMET,"Test");
	}
	
	int airAmount = 10;
	Map<UUID, Long> playerCooldown = new HashMap<>();

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.canDropItem = true;
		super.canPlaceInInventory = true;
		super.canPlaceInItemFrame = true;
		
		/*this.addUnsafeEnchantment(Enchantment.DIG_SPEED, 5);
		this.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 3);

		ItemMeta newMeta = this.getItemMeta();
		//newMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		LeatherArmorMeta LeatherColor = (LeatherArmorMeta) newMeta;
		LeatherColor.setColor(Color.fromRGB(255, 50, 100));

		this.setItemMeta(newMeta);*/
	}

	@Override
	protected void reloadConfig() {
		// TODO Auto-generated method stub
		/*speedDuration = getConfig().getInt("RunnerBoots.SpeedDuration");
		speedLevel = getConfig().getInt("RunnerBoots.SpeedLevel"); 
		
		weakDuration = getConfig().getInt("RunnerBoots.WeakDuration");
		weakLevel = getConfig().getInt("RunnerBoots.WeakLevel"); 
		
		slowDuration = getConfig().getInt("RunnerBoots.SlowDuration");
		slowLevel = getConfig().getInt("RunnerBoots.SlowLevel"); */
		airAmount = getConfig().getInt("WaterCrown.time");
	}
	
	
	private boolean hasEquipped(Player p) {
		return (p.getInventory().getHelmet() != null && super.isSelectedArtifact(p.getInventory().getHelmet()));
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if(!hasEquipped(e.getPlayer()))
			return;
		
		Player player = e.getPlayer();
		player.setRemainingAir(airAmount);
		
			
	}
	
	@EventHandler
	public void onPlayerHit(EntityDamageByEntityEvent e) {
		//having then equipped then hitting a random will cause you to take the damage.
		if(e.getDamager() instanceof Player && hasEquipped((Player)e.getDamager()) && e.getEntity() instanceof Player) {
			/*Player player = (Player)e.getDamager();
			double damage = e.getDamage();
			player.damage(damage);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', WARN_ATTACK));
			e.setCancelled(true);*/
		}
	}
	
}
