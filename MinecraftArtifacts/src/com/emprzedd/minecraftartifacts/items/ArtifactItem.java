package com.emprzedd.minecraftartifacts.items;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.emprzedd.minecraftartifacts.FileLogger;
import com.emprzedd.minecraftartifacts.Main;

public abstract class ArtifactItem extends ItemStack implements Listener{
	
	
	//nbt tags
	private static final String ATTRIBUTE_TAG = "ass";	//stored hidden in nbt, used to check if ItemStack is an artifact
	private String ITEM_ID;		//used to differentiate artifacts
	

	private static String FORMAT_LORE = "&e&o";
	public static String FORMAT_WARN = "&e&o";
	public static String FORMAT_ALLOW = "&a&o";
	
	
	//Artifact settings, add readonly system or move listeners here(if i want "safety")
	public boolean canTrack = false;
	public boolean canPlaceInInventory = false;
	public boolean canDropItem = false;
	public boolean canPlaceInItemFrame = false;
	public boolean canPlace = false;  
	public boolean canSmite = false;	//kills non-op on pickup
	public boolean canRename = false;
	
	
	//
	protected abstract void init();
	protected abstract void reloadConfig();
	
	private FileLogger logger;
	public static ArrayList<ArtifactItem> ArtifactList = new ArrayList<ArtifactItem>(16);	//change to hash table or something, every search i do is n.
	
	private static Main plugin;
	
	
	
	
	public ArtifactItem(String rawName,String internalId, Material type, String lore) {
		super(type);
		//adds scanning tags
		ItemMeta meta = super.getItemMeta();
		ITEM_ID = internalId;
		meta.addAttributeModifier(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS, new AttributeModifier(UUID.randomUUID(),ITEM_ID,99.9,Operation.ADD_NUMBER,EquipmentSlot.LEGS));
		meta.addAttributeModifier(Attribute.HORSE_JUMP_STRENGTH, new AttributeModifier(UUID.randomUUID(),ATTRIBUTE_TAG,69.0,Operation.ADD_NUMBER,EquipmentSlot.LEGS));
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		this.setItemMeta(meta);

		setDisplayName(ChatColor.translateAlternateColorCodes('&', rawName));
		setArtifactLore(lore);
		
		logger = new FileLogger(rawName+"_tracker.txt");
		
		ArtifactList.add(this);
		
		//loads extended methods
		init();
	}
	
	public static void setPlugin(Main plugin_) {
		plugin = plugin_;
	}
	
	protected FileConfiguration getConfig() {
		return plugin.getConfig();
	}
	
	
	//---------------------Start name template--------------------//
	public enum Rarity{
		ADMIN,
		DIVINE,
		UNIQUE,
		RARE,
		COMMON
	}
	
	//text formatting https://codepen.io/0biwan/pen/ggVemP
	public static String formatName(Rarity format, String name ) {
		String formattedName = "&4&lERROR";
		if(format == Rarity.COMMON) {//?????
			formattedName= "&8&l//&7&o"+name+"&8&l//";
		}
		else if(format == Rarity.RARE) {//green
			formattedName= "&a&o«"+name+"»";
			//&a&o«Magic Sand»
		}
		else if(format == Rarity.UNIQUE) {//blue
			formattedName = "&9&l&o&n"+name.charAt(0)+"&b&9"+name.substring(1);//i dont care for this error checking, your dumb if you wanted a blank name and the jar should throw an outofbounds exeception you trash.			
		}
		else if(format == Rarity.DIVINE) {//yellow? red?? purple?? ╒ ë¡!║║
			//&5&l&k0&r&5&l║ T H E  ·  D R A G O N  ·  E G G ║&k0
			//&e&o]&r&l&e║&r &l&5P&r &l&dO P&r &o&e·&r &l&5R&r &l&dO C K&r &l&e║&r&o&e&o] 
			//"&e&k]&r&l&e║&r&l&5" + FIRST_WORD_LETTER + "&l&dO"+ FIRST_LEFTOVER  ?SPACE=("&o&e·&r") + " &l&e║&r&o&e&k]"
			StringBuilder sb = new StringBuilder();
			String[] words = name.split(" ");
			
			sb.append("&e&k]&r&l&e║&r&l&5");
			
			
			for(int wordPos=0; wordPos<words.length;wordPos++) {
				
				
				sb.append("&5&l"+words[wordPos].charAt(0) +"&d&o");
				
				for(int charPos=1; charPos < words[wordPos].length();charPos++) {
						sb.append(" "+words[wordPos].charAt(charPos));
				}
				
				//doesnt add space after last word
				if(wordPos != words.length-1)
					sb.append("&r  &o&e·  &r");
			}
			sb.append("&r&l&e║&r&e&k]");
			
			formattedName = sb.toString();
		}
		else if(format == Rarity.ADMIN) {//gold
			formattedName = "&c&l&o⚡&6&o&l"+name+"&r&c&l&o⚡";
		}
		
		return formattedName;
	}
	/*public static String getNameFormatAdminTemplate(String name) {
		return "&c&l&o⚡&6&o&l"+name+"&r&c&l&o⚡";
	}
	
	public static String getNameFormatUniqueTemplate(String name) {
		if(name.length() >0)
			return "&a&l&o&n"+name.charAt(0)+"&a&l"+name.substring(1);
		else
			return name;
	}*/
	//---------------------End name template----------------------//
	
	
	public static void reloadAllArtifacts() {//not implemented yet
		plugin.reloadConfig();
		for(ArtifactItem item : ArtifactList)
			item.reloadConfig();
	}
	
	public String getArtifactLoreString() {
		return super.getItemMeta().getLore().toString();
	}
	
	public List<String> getArtifactLore() {
		return super.getItemMeta().getLore();
	}
	
	public String getDisplayName() {
		return this.getItemMeta().getDisplayName();
	}
	
	//change later
	public String getRawName() {
		String[] roughName = this.getItemMeta().getDisplayName().split("§");
		StringBuilder rawName = new StringBuilder();
		for(String words : roughName) {
			if(words.length() >0)
				rawName.append(words.substring(1));
		}	
		return rawName.toString();
	}
	
	
	public void setArtifactLore(String lore) {
		ArrayList<String> convertedLore = new ArrayList<String>(5);
		for(String loreLine : lore.split("\n"))
			convertedLore.add(ChatColor.translateAlternateColorCodes('&', FORMAT_LORE+loreLine));
		
		ItemMeta meta = super.getItemMeta();
		meta.setLore(convertedLore);
		this.setItemMeta(meta);
		//this.getItemMeta().setLore(convertedLore);
	}
	
	public void setDisplayName(String name) {
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(name);
		this.setItemMeta(meta);
		//this.getItemMeta().setDisplayName(name);
	}
	
	public FileLogger getLogger() {
		return logger;
	}
	
	
//---------------------Start detect code----------------------//
//needs BIG rewrite, but thats later on
    public static boolean isArtifact(ItemStack item) {
    	if(item.getItemMeta() != null && item.getItemMeta().getAttributeModifiers(Attribute.HORSE_JUMP_STRENGTH) != null)
        	for(AttributeModifier att: item.getItemMeta().getAttributeModifiers(Attribute.HORSE_JUMP_STRENGTH))
            	if(att.getName().equals(ATTRIBUTE_TAG))
            		return true;
    	//return item instanceof ArtifactItem;
    	return false;
    }
    
	public boolean isSelectedArtifact(ItemStack item) {
    	if((item != null&&item.getType() != Material.AIR) && item.getItemMeta() != null && item.getItemMeta().getAttributeModifiers() != null && item.getItemMeta().getAttributeModifiers(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS) != null)
    		for(AttributeModifier att : item.getItemMeta().getAttributeModifiers(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS))
            	if(att.getName().equals(ITEM_ID))
            		return true;
    	return false;
    }
    
    public static ArtifactItem[] findAllArtifacts(Inventory inv) {
    	ArrayList<ArtifactItem> artifacts = new ArrayList<ArtifactItem>();
    	for(ItemStack item : inv.getContents()) {
    		ArtifactItem artifact = convertItemToArtifact(item);
    		if(artifact!=null) {
    			artifacts.add(artifact);
    		}
    	}
    	ArtifactItem[] foundArtifacts = new ArtifactItem[artifacts.size()];
    	foundArtifacts = artifacts.toArray(foundArtifacts);
    	return foundArtifacts;
    }
    
    protected boolean hasInInventory(Inventory inv) {
		boolean hasItem = false;
		for(ArtifactItem art : findAllArtifacts(inv)) {//i dont think art can be null, if the array is empty this code wont execute
			if(art.getClass() == this.getClass()) {
				hasItem=true;
				break;//i thought about adding a thing if a player has multiple, but that situation is impossible.
			}
		}
		return hasItem;
    }
    
    public static boolean findAftifact(Inventory inv, ArtifactItem artifact) {
		return artifact.hasInInventory(inv);
    }
    
	public static ArtifactItem convertItemToArtifact(ItemStack item) {
    	for(ArtifactItem art : ArtifactList) {
    		if(art.isSelectedArtifact(item)) {
    			ArtifactItem newItem = (ArtifactItem) art.clone();
    			newItem.setItemMeta(item.getItemMeta());
    			
    			//allows item names to be updated all the time, ONLY if they are NOT changable
    			if(!newItem.canRename)
    				newItem.setDisplayName(art.getDisplayName());
    			
    			//ALLWAYS update itemlore
    			ItemMeta oldLore = newItem.getItemMeta();
    			oldLore.setLore(art.getArtifactLore());
    			newItem.setItemMeta(oldLore);
    			
    			return newItem;
    		}
    	}
    	return null;
    }
//---------------------end detect code------------------------//
	
	
}