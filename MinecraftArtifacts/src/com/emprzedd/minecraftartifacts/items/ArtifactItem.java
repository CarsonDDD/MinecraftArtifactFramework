package com.emprzedd.minecraftartifacts.items;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.emprzedd.minecraftartifacts.FileLogger;

public abstract class ArtifactItem extends ItemStack implements Listener{


	public static ArrayList<ArtifactItem> ArtifactList = new ArrayList<ArtifactItem>(16);
	//private int id;
	
	private static final String ATTRIBUTE_TAG = "ass";
	private static final String LORE_FORMAT = "&e&o";
	
	private String ITEM_ID;
	
	//might need to set a custom att tag for these values to quickly check these without needing to cycle though everything
	public boolean canTrack = false;
	public boolean canPlaceInInventory = false;
	public boolean canDropItem = false;
	public boolean canPlaceInItemFrame = false;
	public boolean canPlace = false;  
	public boolean canSmite = false;
	public boolean canRename = false;
	
	public static String FORMAT_WARN = "&e&o";
	public static String FORMAT_ALLOW = "&a&o";
	
	protected abstract void init();
	
	private FileLogger logger;
	
	public ArtifactItem(String rawName,String internalId, Material type, String lore) {
		super(type);
		ITEM_ID = internalId;
		
		//used to organize items, by adding a tag to tell its an artifact and another unq one.
		ItemMeta meta = super.getItemMeta();
		meta.addAttributeModifier(Attribute.HORSE_JUMP_STRENGTH, new AttributeModifier(UUID.randomUUID(),ATTRIBUTE_TAG,69.0,Operation.ADD_NUMBER,EquipmentSlot.LEGS));
		meta.addAttributeModifier(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS, new AttributeModifier(UUID.randomUUID(),ITEM_ID,99.9,Operation.ADD_NUMBER,EquipmentSlot.LEGS));
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		this.setItemMeta(meta);
		
		//https://codepen.io/0biwan/pen/ggVemP
		//setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&l&o⚡&6&o&l"+rawName+"&r&c&l&o⚡"));
		
		/*
		 * 
		 * &c&l&o⚡&6&o&lTestItem&r&c&l&o⚡
		 * &5&l&5║&5&lT&dhe &5&lD&dragon &5&lE&dgg&5║
		 * &9&l&o&nL&b&a&9ore Wood Axe
		 * &a&o«Magic Sand»
	   	 * &7&oLock pick
		 * */
		setDisplayName(ChatColor.translateAlternateColorCodes('&', rawName));
		setArtifactLore(lore);
		
		//adds the file logger
		logger = new FileLogger(rawName+"_tracker.txt");

		ArtifactList.add(this);
		
		//loads extended init
		init();
	}
	
	
	public String getArtifactLore() {
		return super.getItemMeta().getLore().toString();
	}
	
	public void setArtifactLore(String lore) {
		ItemMeta meta = super.getItemMeta();
		
		ArrayList<String> convertedLore = new ArrayList<String>(5);
		for(String loreLine : lore.split("\n"))
			convertedLore.add(ChatColor.translateAlternateColorCodes('&', LORE_FORMAT+loreLine));
		
		meta.setLore(convertedLore);
		this.setItemMeta(meta);
		
		//this.getItemMeta().setLore(convertedLore);
	}
	
	public static String getNameFormatAdminTemplate(String name) {
		return "&c&l&o⚡&6&o&l"+name+"&r&c&l&o⚡";
	}
	
	public static String getNameFormatUniqueTemplate(String name) {
		if(name.length() >0)
			return "&a&l&o&n"+name.charAt(0)+"&a&l"+name.substring(1);
		else
			return name;
	}
	
	public String getDisplayName() {
		return this.getItemMeta().getDisplayName();
	}
	
	//yes i know you can do this in 1 pass
	public String getRawName() {
		String[] roughName = this.getItemMeta().getDisplayName().split("§");
		StringBuilder rawName = new StringBuilder();
		for(String words : roughName) {
			if(words.length() >0)
				rawName.append(words.substring(1));
		}
		
		return rawName.toString();
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
	
	
	//------------Detect code---------------//
    public static boolean isArtifact(ItemStack item) {
    	if(item.getItemMeta() != null && item.getItemMeta().getAttributeModifiers(Attribute.HORSE_JUMP_STRENGTH) != null) {
        	for(AttributeModifier att: item.getItemMeta().getAttributeModifiers(Attribute.HORSE_JUMP_STRENGTH)) {
            	if(att.getName().equals(ATTRIBUTE_TAG))
            		return true;
            }
    	}
    	//return item instanceof ArtifactItem;
    	return false;
    }
    
	public boolean isSelectedArtifact(ItemStack item) {
    	if((item != null&&item.getType() != Material.AIR) && item.getItemMeta() != null && item.getItemMeta().getAttributeModifiers() != null && item.getItemMeta().getAttributeModifiers(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS) != null) {
    		for(AttributeModifier att : item.getItemMeta().getAttributeModifiers(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS)) {
            	if(att.getName().equals(ITEM_ID))
            		return true;
            }
    	}
    	
    	return false;
    }
    
    public static ArtifactItem convertItemToArtifact(ItemStack item) {
    	for(ArtifactItem art : ArtifactList) {
    		if(art.isSelectedArtifact(item)) {
    			//change item meta to og, this should only be for event code
    			return art;
    		}
    	}
    	return null;
    }
    
    public static ArtifactItem[] findArtifacts(Inventory inv) {
    	ArrayList<ArtifactItem> artifacts = new ArrayList<ArtifactItem>();
    	for(ItemStack item : inv.getContents()) {
    		ArtifactItem artifact = convertItemToArtifact(item);
    		if(artifact!=null) {
    			artifacts.add(artifact);
    		}
    	}
    	//ArrayList<ArtifactItem> foundArtifacts = artifacts.toArray();
    	ArtifactItem[] foundArtifacts = new ArtifactItem[artifacts.size()];
    	foundArtifacts = artifacts.toArray(foundArtifacts);
    	return foundArtifacts;
    }
	
	
}
