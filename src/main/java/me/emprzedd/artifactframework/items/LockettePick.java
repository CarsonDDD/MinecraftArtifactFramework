package me.emprzedd.artifactframework.items;

import java.util.Hashtable;

import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;


import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/*
 * Lockette Pick
 * 
 * Still in dev phase, paused since 2021/09/10
 * 
 * Tier: lowest?
 * 
 * Desc:
 * "locks" have health.
 * tool used to break private signs, either by picking it or by force
 * */
public class LockettePick extends ArtifactItem {
	public LockettePick(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public LockettePick() {
		this(Rarity.formatText(Rarity.COMMON, "Theif Tools"),Material.FLINT_AND_STEEL,"Right click to open a lock.");
	}
	
	
	
	float successChance = 0.01f;
	int startingDurability = 64;
	int costDurability = 64/8;
	
	int MAX_HEALTH = 500;
	int baseDamage = 5;
	
	//Main main;
	
	String TARGET_SIGN = "[private]";
	String MESSAGE_SUCCESS = "lock was brocken";
	String MESSAGE_FAIL = "lock pick attemped failed";
	
	Hashtable<Location,Integer> lockHealth;

	@Override
	protected void init() {
		lockHealth = new Hashtable<Location, Integer>();
		
		this.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
		
		super.canDropItem = true;
		super.canPlaceInItemFrame=true;
		super.canPlaceInInventory = true;
	}
	
	@Override
	protected void reloadConfig() {
		// TODO Auto-generated method stub
	}
	
	
	@EventHandler
	public void onUnlockAttempt(PlayerInteractEvent e) {
		if(!(isSelectedArtifact(e.getPlayer().getInventory().getItemInMainHand()) && e.getClickedBlock().getState() instanceof Sign && ((Sign)e.getClickedBlock().getState()).getLine(0).toLowerCase().contains(TARGET_SIGN.toLowerCase())))
			return;
		
		e.setCancelled(true);
		
		Player player = e.getPlayer();
		ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
		Location signLocation = e.getClickedBlock().getLocation();
		
		//Get and set health TODO:its always a new sign
		float startingHealth = (int)((Math.random() * (5000 - 1000)) +1000);
		int roundNum=100;
		int currentHealth = (int)(roundNum*(Math.ceil((Math.abs(startingHealth/roundNum)))));
		
		if(lockHealth.containsKey(signLocation))
			currentHealth = lockHealth.get(signLocation);
		else {
			lockHealth.put(signLocation, currentHealth);
		}
		

		//do stuff with health
		ItemMeta meta = item.getItemMeta();
		int itemDurability=0;
		if(meta instanceof Damageable){
			Damageable dam = (Damageable)meta;
			itemDurability = 64-dam.getDamage();

		}
		
		double durFactor = 0.45+ (itemDurability/64.0);																//*1.45
		double levelFactor = (e.getPlayer().getLevel()/30.0)*10;													//+10
		double hungerFactor = (e.getPlayer().getSaturation()/20.0)*5;												//+5
		double healthFactor = (e.getPlayer().getHealth()/20.0)+hungerFactor*2;										//+10
		double dirtBonus = e.getPlayer().getInventory().contains(Material.DIRT) ? 1.25 : 1;							//*1.2
		double levelBonus = (e.getPlayer().getLevel()/30.0);														//*1
		double leatherBonus = e.getPlayer().getInventory().contains(Material.LEATHER_BOOTS) ? 1.15 : 1;				//*1.1
		double leatherBonus2 = e.getPlayer().getInventory().contains(Material.LEATHER_HELMET) ? 1.15 : 1;			//*1.1
		double leatherBonus3 = e.getPlayer().getInventory().contains(Material.LEATHER_CHESTPLATE) ? 1.15 : 1;		//*1.1
		double leatherBonus4 = e.getPlayer().getInventory().contains(Material.LEATHER_LEGGINGS) ? 1.15 : 1;			//*1.1
		//double ybonus = 1+(e.getPlayer().getLocation().getY()/100.0)*0.25;
		
		double luckBonus = e.getPlayer().hasPotionEffect(PotionEffectType.JUMP) ? 1: 0.5+(0.35*Math.random());
																													// normal bouns=1.452 first big hit=52
		
		
		double damage = (levelFactor+hungerFactor+healthFactor)*dirtBonus*levelBonus*leatherBonus*leatherBonus2*leatherBonus3*leatherBonus4*luckBonus*durFactor;
		currentHealth-=damage;
		
		if(currentHealth <=0) {
			lockHealth.remove(signLocation);
			e.getClickedBlock().setType(Material.AIR);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&a&lLOCK BROKEN")));
		}
		else {
			lockHealth.replace(signLocation, currentHealth);

			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&4&lLock Health: &r&c"+currentHealth)));
		}
			
		
		//remove durability
		int removeAmount = (costDurability/2)+(int)(costDurability*Math.random());
		removeAttempts(item,removeAmount,player);
		
		//handle effect
		double chance = successChance;//Double.parseDouble(item.getItemMeta().getLore().get(0));
		if(Math.random() < chance) {
			sendNotification(player,FORMAT_ALLOW+MESSAGE_SUCCESS,Sound.BLOCK_ANVIL_PLACE, 0.5f, 1.5f+(float)(Math.random()/2f));
			e.getClickedBlock().setType(Material.AIR);
		}
		else
			sendNotification(player,FORMAT_WARN+MESSAGE_FAIL,Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR,0.25f,1f+(float)Math.random());//pickLose(player);
	
		
	}
	
	private void sendNotification(Player player, String message, Sound sound, float vol, float pitch) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		player.playSound(player.getLocation(), sound, vol, pitch);
	}
	
	//remove player later, only here for testing
	private void removeAttempts(ItemStack item, int amount, Player player) {
		//remove dur, flint and steel has default 64				
		ItemMeta meta = item.getItemMeta();
		if(meta instanceof Damageable){
			Damageable dam = (Damageable)meta;
			int total = dam.getDamage()+amount;
			dam.setDamage(total);
			item.setItemMeta(dam);
			
			if(total >= startingDurability)
				item.setAmount(item.getAmount() - 1);
			
			player.sendMessage("Dur removed: " + amount);
			player.sendMessage("Dur remaining: " + (startingDurability -dam.getDamage()));
		}
	}
}
