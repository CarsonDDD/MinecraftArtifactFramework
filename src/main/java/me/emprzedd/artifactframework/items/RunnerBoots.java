package me.emprzedd.artifactframework.items;

import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
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

import net.md_5.bungee.api.ChatColor;

public class RunnerBoots extends ArtifactItem implements Listener{

	public RunnerBoots(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public RunnerBoots() {
		this(Rarity.formatText(Rarity.COMMON, "Exploration Boots"),Material.LEATHER_BOOTS,"Used to travel on roads faster.\nOnly effective when healthy and friendly.");
	}
	
	
	int speedDuration = 20;//in ticks
	int speedLevel = 5;
	
	int slowDuration = 10*4;//in ticks
	int slowLevel = 4;
	
	int weakDuration = 15*20;//in ticks
	int weakLevel = 10;  
	
	int healthThreashold = 18;// The Players health needs to be above this to get the positive effects.
	
	Material[] roadMaterials = {Material.DIRT_PATH, Material.COBBLESTONE, Material.COBBLESTONE_SLAB, Material.COBBLESTONE_STAIRS, Material.STONE_BRICKS, Material.STONE_BRICK_STAIRS, Material.STONE_BRICK_STAIRS, Material.GRAVEL, Material.BLACKSTONE, Material.ANDESITE, Material.ANDESITE_SLAB, Material.ANDESITE_STAIRS, Material.NETHER_BRICK, Material.NETHER_BRICK_SLAB, Material.NETHER_BRICK_STAIRS,Material.BRICK, Material.BRICK_SLAB, Material.BRICK_STAIRS };
	
	String WARN_ATTACK = "&2Attack others while wearing the boots will cause you to take the damage.";

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.canDropItem = true;
		super.canPlaceInInventory = true;
		super.canPlaceInItemFrame = true;
		
		this.addUnsafeEnchantment(Enchantment.DIG_SPEED, 5);
		this.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 3);

		ItemMeta newMeta = this.getItemMeta();
		//newMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		LeatherArmorMeta LeatherColor = (LeatherArmorMeta) newMeta;
		LeatherColor.setColor(Color.fromRGB(255, 50, 100));

		this.setItemMeta(newMeta);
	}

	@Override
	protected void reloadConfig() {
		// TODO Auto-generated method stub
		speedDuration = getConfig().getInt("RunnerBoots.SpeedDuration");
		speedLevel = getConfig().getInt("RunnerBoots.SpeedLevel"); 
		
		weakDuration = getConfig().getInt("RunnerBoots.WeakDuration");
		weakLevel = getConfig().getInt("RunnerBoots.WeakLevel"); 
		
		slowDuration = getConfig().getInt("RunnerBoots.SlowDuration");
		slowLevel = getConfig().getInt("RunnerBoots.SlowLevel"); 
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
		Material legs = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.SELF).getType();
		Player player = e.getPlayer();
		
		// If a player is doesnt meet the health req NO MATTER WHAT CIRCUMSTANCES will be granted slowness.
		// But if a player DOES meet the req AND they are on a road, they will be granted speed.
		if(player.getHealth() < healthThreashold) {
			PotionEffect slow = new PotionEffect(PotionEffectType.SLOW,slowDuration,slowLevel-1,false,false,false);	
			slow.apply(player);
		}
		else if(validRoadMaterial(road) || validRoadMaterial(legs)) {
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
			Player player = (Player)e.getDamager();
			double damage = e.getDamage();
			player.damage(damage);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', WARN_ATTACK));
			e.setCancelled(true);
		}
	}
	
}
