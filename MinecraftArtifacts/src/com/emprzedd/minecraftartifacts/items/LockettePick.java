package com.emprzedd.minecraftartifacts.items;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import net.md_5.bungee.api.ChatColor;

public class LockettePick extends ArtifactItem{

	float successChance = 0.25f;
	String TARGET_SIGN = "[private]";
	String MESSAGE_SUCCESS = "lock was brocken";
	String MESSAGE_FAIL = "lock pick attemped failed";
	
	public LockettePick(String rawName, Material type, String lore) {
		super(rawName,rawName, type, lore);
	}
	
	public LockettePick() {
		this(ArtifactItem.getNameFormatUniqueTemplate("Lock picking tools"),Material.FLINT_AND_STEEL,"used to open locks");
	}

	@Override
	protected void init() {
		
		this.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
		
		
		super.canDropItem = true;
		super.canPlaceInItemFrame=true;
		super.canPlaceInInventory = true;
	}
	
	@EventHandler
	public void onUnlockAttempt(PlayerInteractEvent e) {
		
		if(isSelectedArtifact(e.getPlayer().getInventory().getItemInMainHand()) && e.getClickedBlock().getState() instanceof Sign) {
			Block block = e.getClickedBlock();
		    if(((Sign)block.getState()).getLine(0).toLowerCase().contains(TARGET_SIGN.toLowerCase())) {
				Player player = e.getPlayer();
		        double num = Math.random();
		        if(num < successChance) {
		        	block.setType(Material.AIR);
		        	player.sendMessage(MESSAGE_SUCCESS);
		        }
		        else {
		        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', FORMAT_WARN+MESSAGE_FAIL));
		        }
		        
		        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);	
		        
		    }
			
		}
		
	}
}
