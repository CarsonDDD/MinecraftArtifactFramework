package me.emprzedd.artifactframework.items;

import java.util.Random;
import java.util.UUID;

import me.emprzedd.artifactframework.ArtifactItem;
import me.emprzedd.artifactframework.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
public class BlessedDagger extends ArtifactItem implements Listener {
	public BlessedDagger(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	public BlessedDagger() {
		this(Rarity.formatText(Rarity.UNIQUE, "Shard of The Blessed Dagger"),Material.FLINT,"&e&oSaid to be the only remaining peice of the &nB&e&olessed Dagger.");
	}
	
	
	
	Enchantment[] enchantmentArray = new Enchantment[] {
			//12
			Enchantment.FIRE_ASPECT,
			Enchantment.FIRE_ASPECT,
			Enchantment.FIRE_ASPECT,
			Enchantment.FIRE_ASPECT,
			Enchantment.FIRE_ASPECT,
			Enchantment.KNOCKBACK,
			Enchantment.KNOCKBACK,
			Enchantment.KNOCKBACK,
			Enchantment.KNOCKBACK,
			Enchantment.KNOCKBACK,
			Enchantment.KNOCKBACK,
			Enchantment.KNOCKBACK,
			
			//6
			Enchantment.MENDING,
			Enchantment.SILK_TOUCH,
			Enchantment.VANISHING_CURSE,
			Enchantment.LUCK,
			Enchantment.LOOT_BONUS_MOBS,
			Enchantment.LOOT_BONUS_MOBS,

			//12
			Enchantment.DAMAGE_ALL,
			Enchantment.DAMAGE_ALL,
			Enchantment.DAMAGE_ALL,	
			Enchantment.DAMAGE_ALL,
			Enchantment.DAMAGE_ALL,
			Enchantment.DAMAGE_ALL,	
			Enchantment.DAMAGE_ALL,
			Enchantment.DAMAGE_ALL,
			Enchantment.DAMAGE_ALL,
			Enchantment.DAMAGE_ALL,
			Enchantment.DAMAGE_ALL,
			Enchantment.DAMAGE_ALL,
	};
	
	final int MAXLEVEL = 10;
	final int ENCHANTVARIETY = 5;
	final int SOFTCAP = 30;
	
	@Override
	protected void init() {
		this.addUnsafeEnchantment(Enchantment.LUCK, 1);
		
		ItemMeta newMeta = this.getItemMeta();
		newMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(),"asss",6,Operation.ADD_NUMBER,EquipmentSlot.HAND));
		newMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(),"asss",-0.75,Operation.ADD_SCALAR,EquipmentSlot.HAND));
		newMeta.addAttributeModifier(Attribute.GENERIC_LUCK, new AttributeModifier(UUID.randomUUID(),"asss",69,Operation.ADD_NUMBER,EquipmentSlot.HAND));
		this.setItemMeta(newMeta);
		
		super.canDropItem = true;
		super.canPlaceInItemFrame=true;
		super.canTrack = true;
		super.canPlaceInInventory = true;
	}

	
	private void removeRandomEnchant(ItemStack item, int amount) {
		Object[] enchantList = item.getEnchantments().keySet().toArray();
		
		if(enchantList.length <=0)
			return;
		
		Enchantment randomEnchant = (Enchantment)enchantList[new Random().nextInt(enchantList.length)];
		
		if(randomEnchant == Enchantment.LUCK)
			return;
		
		int newLevel = item.getEnchantmentLevel(randomEnchant)-amount;
		if(newLevel <=0)
			item.removeEnchantment(randomEnchant);
		else
			item.addUnsafeEnchantment(randomEnchant, newLevel);
		//item.addUnsafeEnchantment(randomEnchant, (newLevel>0) ? newLevel : 0);
		/*int newLevel = item.getEnchantmentLevel(randomEnchant)+amount;
		item.addUnsafeEnchantment(randomEnchant, (newLevel>0) ? newLevel : 0);*/
	}
	
	private void addRandomEnchant(ItemStack item, int amount) {
		Enchantment[] enchants = enchantmentArray;
		
		Enchantment randomEnchant = enchants[new Random().nextInt(enchants.length)];
		
		if(item.containsEnchantment(randomEnchant)) {
			int newLevel = item.getEnchantmentLevel(randomEnchant)+amount;
			if(newLevel > MAXLEVEL)
				newLevel = MAXLEVEL;
			item.addUnsafeEnchantment(randomEnchant, newLevel);
		}
		else {
			item.addUnsafeEnchantment(randomEnchant, amount);
		}
		
		//item.addUnsafeEnchantment(randomEnchant, item.containsEnchantment(randomEnchant) ? item.getEnchantmentLevel(randomEnchant)+amount : amount);
	}
	
	private int getTotalEnchantLevels(ItemStack item) {
		int total = 0;
    	for(Enchantment enchantment : item.getEnchantments().keySet()) {
    		total+= item.getEnchantmentLevel(enchantment);
    	}
    	return total;
	}
	
	@EventHandler
	public void onPlayerKill(PlayerDeathEvent e) {
		
		if(!(e.getEntity().getKiller() instanceof Player))
			return;
		
		Player killer = e.getEntity().getKiller();
    	ItemStack item = killer.getInventory().getItemInMainHand();
    	if(item != null && this.isSelectedArtifact(item)) {
   
	    	e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation());

	    	//adds enchant
	    	int removeBonus =0;
	    	int enchantTotal = getTotalEnchantLevels(item);
	    	if(enchantTotal>SOFTCAP)
	    		removeBonus = enchantTotal-SOFTCAP;
	    	
	    	if(enchantTotal<=0) {
                addRandomEnchant(item,ENCHANTVARIETY/2);
                killer.sendMessage(ChatColor.LIGHT_PURPLE+"The Shard warms your hand.");
	    	}
	    	else {
		    	Random r = new Random();
		    	int killOption = r.nextInt(20)+1+(removeBonus*2);
		    	
	            if(killOption >=1 && killOption <=11){//%55
	                addRandomEnchant(item,r.nextInt(ENCHANTVARIETY)+1);
	                killer.sendMessage(ChatColor.LIGHT_PURPLE+"The Shard sharpens");
	                killer.playSound(killer.getLocation(), Sound.BLOCK_GRINDSTONE_USE, SoundCategory.MASTER,1f,1.5f);
	            }
	            else if(killOption == 20) {//%5
	            	for(Enchantment enchantment : item.getEnchantments().keySet()) {
	            		item.removeEnchantment(enchantment);
	            	}
	            	
	            	killer.sendMessage(ChatColor.LIGHT_PURPLE+"Screams from the Shard, damaging you.");
	            	killer.playSound(killer.getLocation(), Sound.ENTITY_GHAST_SCREAM, SoundCategory.MASTER,1f,0.5f);
	            	killer.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,200,0));
	            	killer.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,600,3));
	            	killer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,200,1));
	            	
	            }
	            else if(killOption >=12 && killOption <=17+(removeBonus*2)){//%25 and scaling with bonus
	                removeRandomEnchant(item,r.nextInt(ENCHANTVARIETY)+1+(removeBonus*2));
	                killer.sendMessage(ChatColor.LIGHT_PURPLE+"A faint humming noise comes from the Shard.");//e
	                killer.playSound(killer.getLocation(), Sound.ENTITY_ZOMBIE_HURT, SoundCategory.MASTER,1f,2f);
	            }
	            else{//%15
	            	killer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,300,0));
	            	killer.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,300,0));
			    	killer.sendMessage(ChatColor.LIGHT_PURPLE+"The Shard emits a glowing light in your hand.");
			    	killer.playSound(killer.getLocation(), Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD, SoundCategory.MASTER,1f,1.5f);
	            }
	    	}
    		
	    	//send message to the player
	    	//Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4"+player.getDisplayName()+" &6 has been banned by &4"+killer.getDisplayName()+" &6using &2\""+getDisplayName()+"&2\"&6."));
    	}
	}
	

}
