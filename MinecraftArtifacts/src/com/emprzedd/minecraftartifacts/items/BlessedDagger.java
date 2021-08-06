package com.emprzedd.minecraftartifacts.items;

import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class BlessedDagger extends ArtifactItem{
	class EnchantNode{
		Enchantment enchant;
		int levelDelta;
		public EnchantNode(Enchantment e, int levelDelta) {
			this.enchant =e;
			this.levelDelta=levelDelta;
		}
	}
	
	public BlessedDagger(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	
	public BlessedDagger() {
		this(ArtifactItem.getNameFormatUniqueTemplate("Shard of The Blessed Dagger"),Material.FLINT,"&e&oThe only remaining peice of the &nB&e&olessed Dagger.");
	}
	
	Enchantment[] enchantmentArray = new Enchantment[] {
			Enchantment.MENDING,
			Enchantment.DAMAGE_ALL,
			//Enchantment.DAMAGE_UNDEAD,
			//Enchantment.DAMAGE_ARTHROPODS,
			Enchantment.LOOT_BONUS_MOBS,
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
			Enchantment.SILK_TOUCH,
			//Enchantment.DIG_SPEED,
			//Enchantment.PROTECTION_ENVIRONMENTAL,
			//bonus
			Enchantment.LUCK,
			Enchantment.LUCK,
			Enchantment.LUCK,
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
	final int ENCHANTVARIETY = 10;
	final int SOFTCAP = 30;
	
	@Override
	protected void init() {
		this.addUnsafeEnchantment(Enchantment.LUCK, 1);
		
		ItemMeta newMeta = this.getItemMeta();
		
		newMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(),"asss",6,Operation.ADD_NUMBER,EquipmentSlot.HAND));
		newMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(),"asss",-0.75,Operation.ADD_SCALAR,EquipmentSlot.HAND));
		newMeta.addAttributeModifier(Attribute.GENERIC_LUCK, new AttributeModifier(UUID.randomUUID(),"asss",69,Operation.ADD_NUMBER,EquipmentSlot.HAND));
	
		this.setItemMeta(newMeta);
		this.canSmite = false;
		this.canDropItem = true;
		this.canPlaceInItemFrame=true;
		this.canPlaceInInventory=false;
		this.canRename=false;
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
	public void onPlayerKill(PlayerInteractEvent event) {
		Player killer = event.getPlayer();
    	ItemStack item = killer.getInventory().getItemInMainHand();
    	if(this.isSelectedArtifact(item)) {
	    	/*Player player = event.getEntity();
   
	    	player.getWorld().strikeLightning(player.getTargetBlock(null, 15).getLocation());*/
	    	

	    	//adds enchant
	    	int removeBonus =0;
	    	int enchantTotal = getTotalEnchantLevels(item);
	    	if(enchantTotal>=SOFTCAP)
	    		removeBonus = enchantTotal-SOFTCAP;
	    	
	    	if(enchantTotal<=0) {
                addRandomEnchant(item,ENCHANTVARIETY/2);
                killer.sendMessage(ChatColor.LIGHT_PURPLE+"The Shard warms your hand.");
	    	}
	    	else {
		    	Random r = new Random();
		    	int killOption = r.nextInt(20)+1+removeBonus;
		    	
	            if(killOption >=1 && killOption <=11){//%55
	                addRandomEnchant(item,r.nextInt(ENCHANTVARIETY)+1);
	                killer.sendMessage(ChatColor.LIGHT_PURPLE+"The Shard sharpens");
	            }
	            else if(killOption == 20) {//%5
	            	for(Enchantment enchantment : item.getEnchantments().keySet()) {
	            		item.removeEnchantment(enchantment);
	            	}
	            	
	            	killer.sendMessage(ChatColor.LIGHT_PURPLE+"Screams from the Shard, damaging you.");
	            	killer.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,200,0));
	            	killer.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,600,3));
	            	killer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,200,1));
	            	
	            }
	            else if(killOption >=12 && killOption <=17+removeBonus){//%25
	                removeRandomEnchant(item,r.nextInt(ENCHANTVARIETY)+1+removeBonus);
	                killer.sendMessage(ChatColor.LIGHT_PURPLE+"A faint humming noise comes from the Shard.");
	            }
	            else{//%15
	            	killer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,300,0));
	            	killer.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,300,0));
			    	killer.sendMessage(ChatColor.LIGHT_PURPLE+"The Shard emits a glowing light in your hand.");
	            }
	    	}
    		
	    	//send message to the player
	    	//Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4"+player.getDisplayName()+" &6 has been banned by &4"+killer.getDisplayName()+" &6using &2\""+getDisplayName()+"&2\"&6."));
    	}
	}
	
	@EventHandler
	public void onPlayerKill(PlayerDeathEvent e) {
		if(e.getEntity().getKiller() != null && e.getEntity() instanceof Player) {
  
	    	Player killer = e.getEntity().getKiller();
	    	ItemStack item = killer.getInventory().getItemInMainHand();
	    	if(this.isSelectedArtifact(item)) {
		    	Player player = e.getEntity();
	   
		    	player.getWorld().strikeLightning(player.getTargetBlock(null, 15).getLocation());
		    	
		    	//adds enchant
		    	
		    	Random r = new Random();
		    	int killOption = r.nextInt(8)+1;
		    	
                if(killOption >0 && killOption <=4){
                    addRandomEnchant(item,r.nextInt(ENCHANTVARIETY)+1);
                    killer.sendMessage(ChatColor.LIGHT_PURPLE+"The Shard sharpens");
                }
                else if(killOption >=5 && killOption <=6){
                    removeRandomEnchant(item,r.nextInt(ENCHANTVARIETY)+1);
                    killer.sendMessage(ChatColor.LIGHT_PURPLE+"A faint humming noise comes from the Shard.");
                }
                else{
    		    	killer.sendMessage(ChatColor.LIGHT_PURPLE+"The Shard emits a glowing light in your hand.");
                }
                
		    	//send message to the player
		    	//Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4"+player.getDisplayName()+" &6 has been banned by &4"+killer.getDisplayName()+" &6using &2\""+getDisplayName()+"&2\"&6."));
	    	}
    	}
	}
	

}
