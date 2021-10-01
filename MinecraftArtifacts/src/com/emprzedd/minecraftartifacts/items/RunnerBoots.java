package com.emprzedd.minecraftartifacts.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RunnerBoots extends ArtifactItem implements Listener{

	public RunnerBoots(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public RunnerBoots() {
		this(formatName(Rarity.COMMON, "Exploration Boots"),Material.LEATHER_BOOTS,"Used to travel on roads faster.\nOnly effective when healthy.");
	}
	
	
	int speedDuration = 40;//in ticks
	int speedLevel = 3;
	
	int slowDuration = 20*4;//in ticks
	int slowLevel = 4;
	
	int weakDuration= 30*20;//in ticks
	int weakLevel = 10;  
	
	int healthThreashold = 18;// The Players health needs to be above this to get the positive effects.
	
	Material[] roadMaterials = {Material.GRASS_PATH,Material.COBBLESTONE};

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.canDropItem = true;
		super.canPlaceInInventory = true;
		super.canPlaceInItemFrame = true;
		
		this.addUnsafeEnchantment(Enchantment.DIG_SPEED, 5);
		this.addEnchantment(Enchantment.DEPTH_STRIDER, 5);

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
	
	private boolean validRoadMaterial(Material target){
		for(Material mat : roadMaterials) {
			if(target.equals(mat))
				return true;
		}
		return false;
	}
	
	private boolean hasEquipped(Player p) {
		return (p.getInventory().getBoots() != null && super.isSelectedArtifact(p.getInventory().getBoots()));
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if(!hasEquipped(e.getPlayer()))
			return;
		
		Material road = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
		Player player = e.getPlayer();
		
		// If a player is doesnt meet the health req NO MATTER WHAT CIRCUMSTANCES will be granted slowness.
		// But if a player DOES meet the req AND they are on a road, they will be granted speed.
		if(player.getHealth() < healthThreashold) {
			PotionEffect slow = new PotionEffect(PotionEffectType.SLOW,slowDuration,slowLevel-1,false,false,false);	
			slow.apply(player);
		}
		else if(validRoadMaterial(road)) {
			PotionEffect speed = new PotionEffect(PotionEffectType.SPEED,speedDuration,speedLevel-1,false,false,false);	
			PotionEffect weakness = new PotionEffect(PotionEffectType.WEAKNESS,weakDuration,weakLevel-1,false,false,false);
			speed.apply(player);
			weakness.apply(player);
		}
			
	}
	
	@EventHandler
	public void onPlayerHit(EntityDamageByEntityEvent e) {
		//having then equipped then hitting a random will cause you to take the damage.
		if(e.getDamager() instanceof Player && hasEquipped((Player)e.getDamager()) && e.getEntity() instanceof Player) {
			double damage = e.getDamage();
			((Player)e.getDamager()).damage(damage);
			e.setCancelled(true);
		}
	}

	
	
}
