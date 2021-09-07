package com.emprzedd.minecraftartifacts.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class LockettePick extends ArtifactItem{

	float successChance = 0.25f;
	int DURABILITY_COST =16;
	String TARGET_SIGN = "[private]";
	String MESSAGE_SUCCESS = "lock was brocken";
	String MESSAGE_FAIL = "lock pick attemped failed";
	
	public LockettePick(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	
	public LockettePick() {
		this(ArtifactItem.getNameFormatUniqueTemplate("Lock picking tools"),Material.ARROW,"Uses remaining: 10");
	}

	@Override
	protected void init() {
		
		this.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
		
		
		super.canDropItem = true;
		super.canPlaceInItemFrame=true;
		super.canPlaceInInventory = true;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onUnlockAttempt2(PlayerInteractEvent e) {
		if(isSelectedArtifact(e.getPlayer().getInventory().getItemInMainHand()) && e.getClickedBlock().getState() instanceof Sign && ((Sign)e.getClickedBlock().getState()).getLine(0).toLowerCase().contains(TARGET_SIGN.toLowerCase())) {
			e.setCancelled(true);//stops other events from interacting
			
			Player player = e.getPlayer();
			ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
			
			String[] bottomLore = item.getItemMeta().getLore().get(item.getItemMeta().getLore().size()-1).split(": ");
			
			int usesRemaining = 1;
			try {
				usesRemaining= Integer.parseInt(bottomLore[bottomLore.length-1]);
			}
			finally {
				
				//handle effect
				if(Math.random() < successChance) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', FORMAT_ALLOW+MESSAGE_SUCCESS));
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5f, 1.5f+(float)(Math.random()/2f));
					
					e.getClickedBlock().setType(Material.AIR);
				}
				else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', FORMAT_WARN+MESSAGE_FAIL));
					player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.25f, 1f+(float)Math.random());
				}
				
				
				if(Math.random() < 0.75f)
					usesRemaining-=1;
				
				if(usesRemaining <=0)
					player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);		//this is extra, lock picks should never stack, this code is redundant
				else {
					List<String> lore = item.getItemMeta().getLore();
					lore.set(lore.size()-1, bottomLore[0]+": "+usesRemaining);
					
					item.getItemMeta().setLore(lore);
				}
			}
	
		}
	}
	
	//@EventHandler(priority = EventPriority.HIGH)
	public void onUnlockAttempt(PlayerInteractEvent e) {

		//ArtifactItem item = convertItemToArtifact(e.getPlayer().getInventory().getItemInMainHand());
		
		if(isSelectedArtifact(e.getPlayer().getInventory().getItemInMainHand()) && e.getClickedBlock().getState() instanceof Sign && ((Sign)e.getClickedBlock().getState()).getLine(0).toLowerCase().contains(TARGET_SIGN.toLowerCase())) {
			e.setCancelled(true);//stops other events from interacting
			
			Player player = e.getPlayer();
			ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
			//remove dur, flint and steel has default 64				
			ItemMeta meta = item.getItemMeta();
			if(meta instanceof Damageable){
				Damageable dam = (Damageable)meta;
				int damageToAdd = (DURABILITY_COST/2) + (int)((Math.random()*DURABILITY_COST)/2);
				
				dam.setDamage(dam.getDamage()+damageToAdd);
				item.setItemMeta((ItemMeta)dam);
				
				player.sendMessage("Dur removed: " + damageToAdd);
				player.sendMessage("Dur remaining: " + damageToAdd);
			}
			else {
				player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
			}
			
			
			//handle effect
			double num = Math.random();
			if(num < successChance) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', FORMAT_ALLOW+MESSAGE_SUCCESS));
				player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5f, 1.5f+(float)(Math.random()/2f));
				
				e.getClickedBlock().setType(Material.AIR);
			}
			else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', FORMAT_WARN+MESSAGE_FAIL));
				player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.25f, 1f+(float)Math.random());
			}
			
		}
		
	}
}
