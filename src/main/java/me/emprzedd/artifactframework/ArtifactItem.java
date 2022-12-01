package me.emprzedd.artifactframework;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class ArtifactItem extends ItemStack implements Listener{


	/**
	 * ARTIFACT_TAG & ARTIFACT_ID are nbt tags used to track an item.
	 * ARTIFACT_TAG: value stored across all ArtifactItems to verify the item is an artifact
	 * ARTIFACT_ID: value unique to artifact to specifically verify the artifact.
	 * <p>
	 * There are also non-unique magic numbers tied to each of these values. They can possibly be used to further
	 * verify if an item is an artifact.
	 * I do not use these because I believe having both "ZOMBIE_SPAWN_REINFORCEMENTS" and "HORSE_JUMP_STRENGTH"
	 * is unique enough
	 */
	private static final String ARTIFACT_TAG = "ass";
	private final static ArtifactKey ARTIFACTID = new ArtifactKey(new AttributeModifier(UUID.randomUUID(), ARTIFACT_TAG,69.0,Operation.ADD_NUMBER,EquipmentSlot.LEGS));
	private final ArtifactKey UNIQUEID;

	public static String FORMAT_WARN = "&e&o";
	public static String FORMAT_ALLOW = "&a&o";
	
	public static boolean artifactFullTrack = false;
	public static boolean artifactFullSmite = false;
	
	private final String rawName;
	
	//Artifact settings, add readonly system or move listeners here(if i want "safety")
	public boolean canTrack = false;
	public boolean canPlaceInInventory = false;
	public boolean canDropItem = false;
	public boolean canPlaceInItemFrame = false;
	public boolean canPlace = false;  
	public boolean canSmite = false;	//kills admin-permission on pickup
	public boolean canRename = false;
	
	private Sound voice = Sound.ENTITY_ENDER_DRAGON_GROWL;
	private float voiceVolume = 0.15f;
	private float voicePitch = 3f;

	//
	protected abstract void init();
	protected abstract void reloadConfig();
	
	private final PluginLogger logger;

	//change to hash table or something, every search i do is n.
	// Contains static container for every type of artifact.
	//public static ArrayList<ArtifactItem> ArtifactList = new ArrayList<ArtifactItem>(16);

	private static final Map<ArtifactKey, ArtifactItem> artifactMap = new HashMap<ArtifactKey, ArtifactItem>();
	
	private static JavaPlugin plugin;
	
	
	
	
	public ArtifactItem(String rawName, String baseID, Material type, String lore) {
		super(type);

		//adds scanning nbt tags
		ItemMeta meta = super.getItemMeta();

		UNIQUEID = new ArtifactKey(new AttributeModifier(UUID.randomUUID(), baseID,99.9,Operation.ADD_NUMBER,EquipmentSlot.LEGS));


		meta.addAttributeModifier(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS, UNIQUEID.modifier);
		meta.addAttributeModifier(Attribute.HORSE_JUMP_STRENGTH, ARTIFACTID.modifier);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		this.setItemMeta(meta);

		this.rawName = rawName;
		setDisplayName(ChatColor.translateAlternateColorCodes('&', rawName+"&r"));
		setArtifactLore(lore);


		logger = new PluginLogger(rawName+"_tracker.txt", plugin);
		
		//ArtifactList.add(this);
		artifactMap.put(UNIQUEID, this);
		
		//loads extended methods
		init();
	}

	public static void setPlugin(JavaPlugin plugin_) {plugin = plugin_;}
	protected static FileConfiguration getConfig() { return plugin.getConfig();}// easy of use function.

	public static Map<ArtifactKey, ArtifactItem> getArtifactItems(){
		return artifactMap;
	}

	public PluginLogger getLogger() {return logger;}

	protected ArtifactKey getKey(){return UNIQUEID;}
	
	public static void reloadAllArtifacts() {
		plugin.reloadConfig();
		artifactFullTrack = getConfig().getBoolean("FullItemTrack");
		artifactFullSmite = getConfig().getBoolean("FullSmiteCheck");
		
		/*for(ArtifactItem item : ArtifactList)
			item.reloadConfig();*/

		for(Map.Entry<ArtifactKey, ArtifactItem> entry : artifactMap.entrySet()){
			entry.getValue().reloadConfig();
		}
	}
	
	public List<String> getLore() {return super.getItemMeta().getLore();}
	
	public String getDisplayName() {return this.getItemMeta().getDisplayName();}


	public String getRawName() {return rawName;}

	protected void setVoice(Sound sound, float volume, float pitch){
		voice = sound;
		voiceVolume = volume;
		voicePitch = pitch;
	}
	
	
	public void setArtifactLore(String lore) {
		ArrayList<String> convertedLore = new ArrayList<String>(4);
		for(String loreLine : lore.split("\n"))
			convertedLore.add(ChatColor.translateAlternateColorCodes('&', Rarity.formatText(Rarity.LORE, loreLine)));
		
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

	public void screamAtPlayer(Player player, String message) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&0&l[&r"+getDisplayName() +"&0&l]:&r "+FORMAT_WARN+ message));
		player.playSound(player.getLocation(), voice, SoundCategory.MASTER, voiceVolume, voicePitch);
	}
	
	
//---------------------Start detect code----------------------//
//needs BIG rewrite, but thats later on
    public static boolean isAnyArtifact(ItemStack item) {
    	if(item.getItemMeta() != null && item.getItemMeta().getAttributeModifiers(Attribute.HORSE_JUMP_STRENGTH) != null)
        	for(AttributeModifier att: item.getItemMeta().getAttributeModifiers(Attribute.HORSE_JUMP_STRENGTH))
            	if(att.equals(ARTIFACTID.modifier))
            		return true;
    	//return item instanceof ArtifactItem;
    	return false;
    }
    
	public boolean isSelectedArtifact(ItemStack item) {
    	if((item != null&&item.getType() != Material.AIR) && item.getItemMeta() != null && item.getItemMeta().getAttributeModifiers() != null && item.getItemMeta().getAttributeModifiers(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS) != null)
    		for(AttributeModifier att : item.getItemMeta().getAttributeModifiers(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS))
            	if(att.equals(UNIQUEID.modifier))
					return true;
    	return false;
    }

	// I can return a normal itemstack, since it being an artifact is tied to its meta data
    /*public static ArtifactItem[] findAllArtifacts(Inventory inv) {
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
    }*/

	public static List<ArtifactItem> findAllArtifacts(Inventory inv) {
		List<ArtifactItem> artifacts = new ArrayList<ArtifactItem>();
		for(ItemStack item : inv.getContents()) {
			ArtifactItem artifact = convertItemToArtifact(item);
			if(artifact!=null) {
				artifacts.add(artifact);
			}
		}
		return artifacts;
	}
    
    /*protected boolean hasInInventory(Inventory inv) {
		boolean hasItem = false;
		for(ArtifactItem art : findAllArtifacts(inv)) {//i dont think art can be null, if the array is empty this code wont execute
			if(art.getClass() == this.getClass()) {
				hasItem=true;
				break;//i thought about adding a thing if a player has multiple, but that situation is impossible.
			}
		}
		return hasItem;
    }*/

	protected boolean hasInInventory(Inventory inv) {
		boolean hasItem = false;
		for(ItemStack item : inv.getContents()) {
			if(isSelectedArtifact(item)){
				hasItem = true;
				break;
			}
		}
		return hasItem;
	}

	// This function might be useless
	// Possible to do without loop and use typeof?
	// use its nbt + a map?
	/*public static ArtifactItem convertItemToArtifact(ItemStack item) {
    	for(ArtifactItem art : ArtifactList) {
    		if(art.isSelectedArtifact(item)) {
    			ArtifactItem newItem = (ArtifactItem) art.clone();// Does this need to be a clone?!? Since the class acts as a script applied to the tag
    			newItem.setItemMeta(item.getItemMeta());
    			
    			//allows item names to be updated all the time, ONLY if they are NOT changable
    			if(!newItem.canRename)
    				newItem.setDisplayName(art.getDisplayName());
    			
    			//ALLWAYS update itemlore
    			ItemMeta oldLore = newItem.getItemMeta();
    			oldLore.setLore(art.getLore());
    			newItem.setItemMeta(oldLore);
    			
    			return newItem;
    		}
    	}
    	return null;
    }*/

	// Takes item, looks at its nbt data and returns null if its not an artifact,
	// or returns the artifact it represents
	// Wait, should this just return the art the item is representing? and do nothing with it?
	public static ArtifactItem convertItemToArtifact(ItemStack item) {
		if(item != null && item.getItemMeta()!= null && item.getItemMeta().hasAttributeModifiers()){
			// Extremely greedy check, asserting artifact items cannot have more than 1 ZOMBIE_SPAWN_REINFORCEMENTS.
			// Which in 99% of cases this is fine because ZOMBIE_SPAWN_REINFORCEMENTS is very niche.
			Collection <AttributeModifier> modifiers = item.getItemMeta().getAttributeModifiers(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS);
			if(modifiers != null && modifiers.size() == 1){
				ArtifactKey key = new ArtifactKey(modifiers.iterator().next());
				return artifactMap.get(key);
			}
		}

		return null;
	}
//---------------------end detect code------------------------//
	
	
}