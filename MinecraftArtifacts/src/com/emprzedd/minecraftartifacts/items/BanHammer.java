package com.emprzedd.minecraftartifacts.items;

import java.util.UUID;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;

public class BanHammer extends ArtifactItem implements Listener{

	public BanHammer(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	
	public BanHammer() {
		this(ArtifactItem.getNameFormatAdminTemplate("Ban Hammer"),Material.OAK_SIGN,
				ChatColor.ITALIC+"\"The path of the righteous man is beset on all sides\n"
				+ ChatColor.ITALIC+"by the inequities of the selfish and the tyranny\n"
				+ ChatColor.ITALIC+"of evil men. Blessed is he, who in the name of charity\n"
				+ ChatColor.ITALIC+"and good will, shepherds the weak through the valley\n"
				+ ChatColor.ITALIC+"of darkness, for he is truly his brother's keeper and\n"
				+ ChatColor.ITALIC+"the finder of lost children. And I will strike down\n"
				+ ChatColor.ITALIC+"upon thee with great vengeance and furious anger those\n"
				+ ChatColor.ITALIC+"who would attempt to poison and destroy my brothers.\n"
				+ ChatColor.ITALIC+"And you will know my name is the Lord when I lay my\n"
				+ ChatColor.ITALIC+"vengeance upon thee.\"");
	}

	@Override
	protected void init() {
		this.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
		this.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1000);

		
		ItemMeta newMeta = this.getItemMeta();
		
		//move to smited code in ArtifactItem
		newMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(),"asss",10000.0,Operation.ADD_NUMBER,EquipmentSlot.HAND));
	
		this.setItemMeta(newMeta);
		this.canSmite = true;
		this.canPlace = false;
	}
	
	
	@EventHandler
	public void onPlayerKill(PlayerDeathEvent e) {
		if(e.getEntity().getKiller() != null && e.getEntity() instanceof Player) {
  
	    	Player killer = e.getEntity().getKiller();
	    	
	    	if(this.isSelectedArtifact(killer.getInventory().getItemInMainHand())) {
		    	Player player = e.getEntity();
	   
		    	player.getWorld().strikeLightning(player.getTargetBlock(null, 15).getLocation());
		    		
		    	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4"+player.getDisplayName()+" &6 has been banned by &4"+killer.getDisplayName()+" &6using &2\""+getDisplayName()+"&2\"&6."));
		    		
		        String name = player.getDisplayName();
		        player.getServer().getBanList(BanList.Type.NAME).addBan(name, "You have been struck by the \""+getDisplayName()+"\".", null, "Admin");    
		        player.getServer().getBanList(BanList.Type.IP).addBan(player.getAddress().getHostName(), "You have been struck by the \""+getDisplayName()+"\".", null, "Admin");
		            
		        player.kickPlayer("You have been struck by the \""+getDisplayName()+"\".");
	    	}
    	}
	}
	
	
}
