package com.emprzedd.minecraftartifacts;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.emprzedd.minecraftartifacts.items.AdminStaff;
import com.emprzedd.minecraftartifacts.items.BanHammer;
import com.emprzedd.minecraftartifacts.items.BlessedDagger;
import com.emprzedd.minecraftartifacts.items.LoreWoodAxe;
import com.emprzedd.minecraftartifacts.items.MessengerBoots;


public class Main extends JavaPlugin{

	 @Override
	    public void onEnable(){
	        //getConfig().options().copyDefaults(true);
	        saveDefaultConfig();
		 	FileLogger.setPlugin(this);
		 	
		 	//----------Events-------------------------//
		 	getServer().getPluginManager().registerEvents(new ItemInventoryHandler(this), this);
		 	getServer().getPluginManager().registerEvents(new ItemStopRename(), this);
		 	getServer().getPluginManager().registerEvents(new ItemTracker(), this);
		 	getServer().getPluginManager().registerEvents(new ItemSmite(), this);
		 	getServer().getPluginManager().registerEvents(new ItemStopPlace(), this);
		 	
		 	
		 	//--------Artifact Items------------------//
		 	getServer().getPluginManager().registerEvents(new AdminStaff(), this);
		 	getServer().getPluginManager().registerEvents(new BanHammer(), this);
		 	getServer().getPluginManager().registerEvents(new LoreWoodAxe(), this);
		 	getServer().getPluginManager().registerEvents(new MessengerBoots(), this);
		 	getServer().getPluginManager().registerEvents(new BlessedDagger(), this);
		 	
		 	
		 	getCommand("artifact").setExecutor(new CommandHandler(new ArtifactSelectionGUI()));
	        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Minecraft Artifacts loaded");
	    }

	    @Override
	    public void onDisable(){
	        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Minecraft Artifacts Stopped");
	    }
	    
	    private void loadConfig(){
	    	
	    }
	    
}
