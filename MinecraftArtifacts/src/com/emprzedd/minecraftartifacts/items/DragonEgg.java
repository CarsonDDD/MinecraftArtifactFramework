package com.emprzedd.minecraftartifacts.items;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.emprzedd.minecraftartifacts.FileLogger;

/*
 * The Dragon Egg
 * 
 * I converted this from my stand alone egg plugin
 * 
 * Tier: Highest?
 * 
 * Desc:
 * onShift: player "goes up" and gets wither
 * Damage numbers are different
 * onChat: plays rawrr sound
 * onJoin: special join announcement + everyone gets a sound
 * onDeath: egg is forced out and is summoned in another location
 * onMove: player gains speed and other effects
 * 
 */
public class DragonEgg extends ArtifactItem implements Listener {
	public DragonEgg(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public DragonEgg() {
		this(ArtifactItem.getNameFormatAdminTemplate("The Dragon Egg"),Material.DRAGON_EGG,"&e&oMake lore tag later");
	}

	
	
	@Override
	protected void init() {
		addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
		addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
		addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
		addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 4);
		
		ItemMeta newMeta = getItemMeta();
		newMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(),"Egg Power", 4, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HEAD));
		newMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(),"Egg Power", 4, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HEAD));
		newMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(),"Egg Power", 2, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HEAD));
		setItemMeta(newMeta);
		
		super.canDropItem = true;
		super.canPlace = true;
		super.canTrack = true;
	}
	
	@Override
	protected void reloadConfig() {
		// TODO Auto-generated method stub
	}
	
	
	private boolean isEggHolder(Player p) {
		return (p.getInventory().getHelmet() != null && isSelectedArtifact(p.getInventory().getHelmet()));
	}
	
	private boolean isEggInInventory(Inventory inv) {
		return inv.contains(Material.DRAGON_EGG);
    	/*for(ItemStack item : inv.getContents()) {
    		ArtifactItem artifact = convertItemToArtifact(item);
    		if(artifact !=null && artifact instanceof DragonEgg) {
    			return true;
    		}
    	}
    	return false;*/
	}
	
	//reduces damage delt, and increase damage taken
	@EventHandler
	public void damageModifier(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player && isEggHolder((Player)e.getEntity())) {
			((Player) e.getEntity()).playSound(e.getEntity().getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1f, 1.5f);
			e.setDamage(e.getDamage()*1.35);
		}
		else if(e.getDamager() instanceof Player && isEggHolder((Player)e.getDamager())) {
			((Player) e.getDamager()).playSound(e.getDamager().getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1f, 1.5f);
			e.setDamage(e.getDamage()*0.75);
		}
	}
	
	//adds a particle trail following the player
	@EventHandler
	public void playerTrails(PlayerMoveEvent e) {
		if(!isEggHolder(e.getPlayer()))
			return;
		
		Player p = e.getPlayer();
		
		DustOptions dustOptions = new DustOptions(Color.fromRGB(255, 0, 225), 1);
		p.spawnParticle(Particle.REDSTONE, p.getLocation(), 2, dustOptions);
		
		PotionEffect speed = new PotionEffect(PotionEffectType.SPEED,10,1,false,false,false);
		PotionEffect bad = new PotionEffect(PotionEffectType.BAD_OMEN,10,10,false,false,false);
		PotionEffect hunger = new PotionEffect(PotionEffectType.HUNGER,10,1,false,false,false);
		
		speed.apply(p);
		bad.apply(p);
		hunger.apply(p);
		
		/*if(isEggHolder(e.getPlayer())) {
			Player p = e.getPlayer();
			
			DustOptions dustOptions = new DustOptions(Color.fromRGB(255, 0, 225), 1);
			p.spawnParticle(Particle.REDSTONE, p.getLocation(), 2, dustOptions);
			
			PotionEffect speed = new PotionEffect(PotionEffectType.SPEED,10,1,false,false,false);
			PotionEffect bad = new PotionEffect(PotionEffectType.BAD_OMEN,10,10,false,false,false);
			PotionEffect hunger = new PotionEffect(PotionEffectType.HUNGER,10,1,false,false,false);
			
			speed.apply(p);
			bad.apply(p);
			hunger.apply(p);
		}*/
	}
	
	//Player roawrrrr
	@EventHandler
	public void playerRAWRRRSXXXDDDD(AsyncPlayerChatEvent e) {
		if(!isEggHolder(e.getPlayer()))
			return;
		
		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 5f, 1.5f);
		e.setMessage(ChatColor.ITALIC+e.getMessage());
	}
	
	//allows player to "fly" when sneaking
	@EventHandler
	public void sneakFly(PlayerToggleSneakEvent e) {
		if(!isEggHolder(e.getPlayer()))
			return;
		
		PotionEffect levitate = new PotionEffect(PotionEffectType.LEVITATION,3,15,false,false,false);
		PotionEffect glow = new PotionEffect(PotionEffectType.GLOWING,1200,0,false,false,false);
		PotionEffect invis = new PotionEffect(PotionEffectType.INVISIBILITY,40,1,false,false,false);
		PotionEffect wither = new PotionEffect(PotionEffectType.WITHER,40,2,false,false,false);
		
		Player player = e.getPlayer();
		invis.apply(player);
		levitate.apply(player);
		glow.apply(player);
		wither.apply(player);
		
		DustOptions dustOptions = new DustOptions(Color.fromRGB(254, 254, 224), 1);
		player.spawnParticle(Particle.REDSTONE, player.getEyeLocation(), 10, dustOptions);	
		
		player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 2, 1.25f);
		/*if(isEggHolder(e.getPlayer())) {
			Player p = e.getPlayer();
			
			PotionEffect levitate = new PotionEffect(PotionEffectType.LEVITATION,3,15,false,false,false);
			PotionEffect glow = new PotionEffect(PotionEffectType.GLOWING,1200,0,false,false,false);
			PotionEffect invis = new PotionEffect(PotionEffectType.INVISIBILITY,40,1,false,false,false);
			PotionEffect wither = new PotionEffect(PotionEffectType.WITHER,40,2,false,false,false);
			
			invis.apply(p);
			levitate.apply(p);
			glow.apply(p);
			wither.apply(p);
			
			DustOptions dustOptions = new DustOptions(Color.fromRGB(254, 254, 224), 1);
			p.spawnParticle(Particle.REDSTONE, p.getEyeLocation(), 10, dustOptions);	
			
			p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 2, 1.25f);
		}*/
	}
	
	//custom death message for if the player withers while flying
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if(!(isEggHolder(e.getEntity()) && e.getDeathMessage().contains("withered")))
			return;
		
		e.setDeathMessage(ChatColor.RESET + e.getEntity().getDisplayName() + " was slain by the Dragon Egg.");
	}
	
	//displays who has the egg
	@EventHandler
	public void holderJoinMessage(PlayerJoinEvent e) {
		if(!isEggHolder(e.getPlayer()))
			return;
		
		e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', "&e&lThe Dragon Egg holder &a&n"+e.getPlayer().getDisplayName()+"&r&e&l joined the game."));
		
		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD, 5f, 0.5f);
		for(Player p : Bukkit.getOnlinePlayers())
			p.playSound(p.getPlayer().getLocation(), Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD, 5f, 0.5f);
	}
	
	//converts ALL instances of the dragon egg into the artifact
	@EventHandler
	public void convertItemPickup(EntityPickupItemEvent e) {
		if(e.getEntity() instanceof Player && e.getItem().getItemStack().getType().equals(Material.DRAGON_EGG)) {
			e.getItem().setItemStack(this);
		}
	}
	
	@EventHandler
	public void onEggDeath(EntityDamageByEntityEvent e) {
		if(!(e.getEntity() instanceof Player && (isEggInInventory(((Player)e.getEntity()).getInventory()) || isEggHolder((Player)e.getEntity())))) 
			return;
		
		Player player = (Player)e.getEntity();
		if(e.getFinalDamage() < player.getHealth())
			return;
		
		//handles totem logic, totems dont protect the egg. change later?
		if(player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING) || player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
			return;
		}
		
		Bukkit.broadcastMessage(ChatColor.DARK_RED+""+ChatColor.BOLD+"The Egg holder has fallen in battle.");
		
		Random r = new Random();
		int x = player.getLocation().getBlockX()+r.nextInt(16)-9;
		int z = player.getLocation().getBlockZ()-r.nextInt(16)+9;
		int y = Math.min(player.getWorld().getHighestBlockYAt(x, z)+16, 255);	
		
		Location eggLoc = new Location(player.getWorld(),x,y,z);
		
		player.getInventory().remove(Material.DRAGON_EGG);//this line might cause an error
		
		player.getWorld().strikeLightning(new Location(player.getWorld(),x,player.getLocation().getY(),z));
		
		eggLoc.getBlock().setType(Material.DRAGON_EGG);
		
		getLogger().logToFile("Egg holder '+" + player.getDisplayName()+"' has died. Egg summoned at '" +FileLogger.entityLocation(eggLoc)+"'.");
			
	}
	
	
	
	

}
