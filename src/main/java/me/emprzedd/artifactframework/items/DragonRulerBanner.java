package me.emprzedd.artifactframework.items;

import java.util.HashSet;
import java.util.UUID;

import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/*
 * Dragon Banner
 * 
 * I converted this from my stand alone egg plugin
 * 
 * Tier: unique?
 * 
 * Desc:
 * Place a banner and it causes a "dragon storm"
 * Dragon storm reapplies effect to all players in radius if they continue to stand in for the duration
 * When a player walks into the radius for the first time effects will get applied
 * 
 */


//todo:
/*
 * Todo:
 * save banner location and placetimes to handle server restarts when banner is placed. or not?
 * Doesnt work properly on others claimed land
 * 
 * Add anti dup which adds vanishig and smite
 * */


public class DragonRulerBanner extends ArtifactItem implements Listener{
	public DragonRulerBanner(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public DragonRulerBanner() {
		this(Rarity.formatText(Rarity.UNIQUE, "Dragon Banner"),Material.BLACK_BANNER,"&e&oBlessed with the power of a mighty Dragon.");
	}

	
	
	int EFFECT_RADIUS = 10;
	float EFFECT_NEUTRAL_BONUS = 2f;//confusing name, change later
	int EFFECT_DURATION = 20*60;//in ticks 120=2min
	
	private long bannerEffectTimer;
	
	private final long bannerCooldown = 60*60;//in seconds,14400=4hours
	private long bannerCooldownTimer=0;
	
	
	private Location bannerLocation;
	private Location defaultLocation;//temp
	private Player bannerPlayer;
	
	
	String ERROR_CANT_PLACE = "You cannot place the banner here.";
	String ERROR_EXISTING_BANNER = "You already have a banner placed at (";
	String ERROR_COOLDOWN = "The banner is not ready yet.";
	String EFFECT_MESSAGE = "&d&lA STORM SURGES!";
	String ERROR_NOHAND = "&4&lYou need an empty hand to place The Banner.";

	
	
	private HashSet<Player> bannerStorm = new HashSet<Player>();//used so players can leave and rejoin the area to spam the effect, only new players trigger it.
	
	@Override
	protected void init() {
		
		defaultLocation = new Location(Bukkit.getWorld("world"), -999,-999,-999);
		bannerLocation = new Location(defaultLocation.getWorld(),defaultLocation.getX(),defaultLocation.getY(),defaultLocation.getZ());
		
		addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
		addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 5);
		addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 5);
		
		ItemMeta newMeta = getItemMeta();
		newMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(),"Dragon power", 4, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HEAD));
		setItemMeta(newMeta);
		
		super.canPlace = true;
		super.canTrack = true;
		super.canDropItem = true;
		super.canPlaceInItemFrame = true;
	}
	
	@Override
	protected void reloadConfig() {
		// TODO Auto-generated method stub
	}
	
	private void givePotionEffect(LivingEntity entity, PotionEffectType effect, int durationTicks, int strength) {
		PotionEffect potionEffect = new PotionEffect(effect,durationTicks,strength-1,false,true,true);
		potionEffect.apply(entity);
	}
	
	
	private int giveAOEEffects(Location centerPoint, Player user) {
		
		centerPoint.getWorld().setStorm(true);
		centerPoint.getWorld().setWeatherDuration(EFFECT_DURATION);
		
		centerPoint.getWorld().playSound(centerPoint, Sound.AMBIENT_CAVE, 10f, 0.5f);
		
		centerPoint.getWorld().strikeLightningEffect(centerPoint);
		
		int entitiesEffected = 0;
		
		for(Entity entity : centerPoint.getWorld().getNearbyEntities(centerPoint, EFFECT_RADIUS*EFFECT_NEUTRAL_BONUS, EFFECT_RADIUS*EFFECT_NEUTRAL_BONUS, EFFECT_RADIUS*EFFECT_NEUTRAL_BONUS)) {
			if(entity instanceof LivingEntity) {
				LivingEntity effectTarget = (LivingEntity)entity;
				givePotionEffect(effectTarget,PotionEffectType.GLOWING,EFFECT_DURATION/8,1);
			}
			
		}
		
		for(Entity entity : centerPoint.getWorld().getNearbyEntities(centerPoint, EFFECT_RADIUS, EFFECT_RADIUS, EFFECT_RADIUS)) {
			if(entity instanceof LivingEntity) {
				entitiesEffected++;
				LivingEntity effectTarget = (LivingEntity)entity;
				if(effectTarget.getName().equals(user.getName()))
					givePositiveRewards(effectTarget);
				else
					giveNegativeRewards(effectTarget);
				
				giveNeutralRewards(effectTarget);
			}	
		}
		return entitiesEffected;
	}
	
	//all hard coded. il change this when i make the switch to custom configs, but for now no one needs to see this.
	//make custom later
	private void givePositiveRewards(LivingEntity entity) {
		if(entity.getType() == EntityType.PLAYER) {
			Player player = (Player)entity;
			player.getWorld().strikeLightningEffect(player.getLocation());//doesnt do damage
			player.removePotionEffect(PotionEffectType.GLOWING);
		}
		
		givePotionEffect(entity,PotionEffectType.FIRE_RESISTANCE,EFFECT_DURATION,3);
		
		givePotionEffect(entity,PotionEffectType.INCREASE_DAMAGE,EFFECT_DURATION/2,2);
		
		
		givePotionEffect(entity,PotionEffectType.BLINDNESS,EFFECT_DURATION/8,1);
		givePotionEffect(entity,PotionEffectType.INVISIBILITY,EFFECT_DURATION/6,1);
	}
	
	//make custom later
	private void giveNeutralRewards(LivingEntity entity) {
		givePotionEffect(entity,PotionEffectType.SATURATION,EFFECT_DURATION,10);
		//givePotionEffect(entity,PotionEffectType.LEVITATION,3*20,2);
		givePotionEffect(entity,PotionEffectType.SLOW_FALLING,5*20,1);
		givePotionEffect(entity,PotionEffectType.DAMAGE_RESISTANCE,2*20,2);
		
		givePotionEffect(entity,PotionEffectType.BAD_OMEN,EFFECT_DURATION,1);
		
		givePotionEffect(entity,PotionEffectType.SLOW,EFFECT_DURATION/10,1);
		givePotionEffect(entity,PotionEffectType.SLOW_DIGGING,EFFECT_DURATION/10,1);
		
		
		if(entity.getType() == EntityType.PLAYER) {
			((Player)entity).playSound(entity.getLocation(), Sound.AMBIENT_CAVE, 10f, 1.1f);//makes things trippy
			((Player)entity).playSound(entity.getLocation(), Sound.AMBIENT_CAVE, 10f, 0.5f);//makes things trippy
			((Player)entity).playSound(entity.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 10f, 0.75f);//makes things trippy
			((Player)entity).playSound(entity.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 10f, 1f);//makes things trippy	
		}

		entity.sendMessage(ChatColor.translateAlternateColorCodes('&', EFFECT_MESSAGE));
	}
	
	//make custom later
	private void giveNegativeRewards(LivingEntity entity) {
		if(entity.getType() == EntityType.PLAYER) {
			Player player = (Player)entity;
			player.getWorld().strikeLightning(player.getLocation());//does damage
			player.setFireTicks(20*5);
			player.removePotionEffect(PotionEffectType.GLOWING);
		}
		
		
		givePotionEffect(entity,PotionEffectType.INCREASE_DAMAGE,EFFECT_DURATION/2,1);
		
		givePotionEffect(entity,PotionEffectType.BLINDNESS,EFFECT_DURATION/6,1);
		givePotionEffect(entity,PotionEffectType.GLOWING,EFFECT_DURATION/6,1);
	}
	
	private boolean insideRange(Location loc, Location area, int range) {
		return (area.getX()+range > loc.getX() && loc.getX() > area.getX()-range)&&
		(area.getY()+range > loc.getY() && loc.getY() > area.getY()-range)&&
		(area.getZ()+range > loc.getZ() && loc.getZ() > area.getZ()-range);
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlace(BlockPlaceEvent e) {
		if(!isSelectedArtifact(e.getItemInHand()))
			return;
		
		//protected land work around, i assume towny either sets build to false or just cancels the event
		if(!e.canBuild() || e.isCancelled()) {
			e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', FORMAT_WARN +ERROR_CANT_PLACE));
			e.setBuild(false);
			e.setCancelled(true);
			return;
		}
		
		//stops from placing if another banner already exists
		if(!bannerLocation.equals(defaultLocation)) {
			e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', FORMAT_WARN + ERROR_EXISTING_BANNER+"x:"+bannerLocation.getBlockX() + " y:" + bannerLocation.getBlockY() + " z:" + bannerLocation.getBlockZ()+")"));
			e.setBuild(false);
			e.setCancelled(true);
			return;
		}
		

		//makes banner get placed, but still has the item in inv. The placed banner will not drop items
		//If you place a banner, one of your hands needs to be empty, this item is unquie and cannot stack
		if(e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR && e.getPlayer().getInventory().getItemInOffHand().getType() != Material.AIR) {
			e.setBuild(false);
			e.setCancelled(true);
			e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', FORMAT_WARN +ERROR_NOHAND)));
			return;
		}
		
		
		//handles cooldown
		if(System.currentTimeMillis() < bannerCooldownTimer+(bannerCooldown*1000)) {
			e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', FORMAT_WARN +ERROR_COOLDOWN));
			int remaining = (int)(bannerCooldownTimer+(bannerCooldown*1000) - System.currentTimeMillis())/1000;
			e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&a&l" + remaining + " seconds remaining.")));
			e.setBuild(false);
			e.setCancelled(true);
			return;
		}
		else {
			bannerCooldownTimer = System.currentTimeMillis();
		}
		
		
		
		
		Player player = e.getPlayer();
		bannerPlayer=player;
		bannerLocation = e.getBlock().getLocation();
		bannerEffectTimer = System.currentTimeMillis();
		
		
		//makes banner get placed, but still has the item in inv. The placed banner will not drop items
		//If you place a banner, one of your hands needs to be empty, this item is unquie and cannot stack
		if(player.getInventory().getItemInMainHand().getType() == Material.AIR)
			player.getInventory().setItemInMainHand(this);
		else if(player.getInventory().getItemInOffHand().getType() == Material.AIR)
			player.getInventory().setItemInOffHand(this);
		else
			player.sendMessage("ERROR");
		

		//give cooldown
		player.sendMessage("Placed banner");
		
		
		
		
		//giveAOEEffects(player.getLocation(),player);	
	}
	
	@EventHandler
	public void onBannerBreak(BlockBreakEvent e) {
		if(bannerLocation !=null && bannerLocation != defaultLocation && e.getBlock().getLocation().equals(bannerLocation)) {
			bannerLocation=defaultLocation;
			e.getBlock().setType(Material.AIR);
			e.setCancelled(true);
			bannerStorm = new HashSet<Player>();
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(bannerLocation == null || bannerLocation == defaultLocation || !insideRange(e.getPlayer().getLocation(),bannerLocation,EFFECT_RADIUS))
			return;
		
		
		//handle banner effect upkeep
		if(System.currentTimeMillis() >= bannerEffectTimer+(EFFECT_DURATION* 1000L)) {
			giveAOEEffects(bannerLocation,bannerPlayer);
			bannerEffectTimer = System.currentTimeMillis();
		}
		
		//new player
		if(!bannerStorm.contains(e.getPlayer())) {
			bannerStorm.add(e.getPlayer());
			giveAOEEffects(bannerLocation,bannerPlayer);
		}
	}
	

}
