package com.emprzedd.minecraftartifacts;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.emprzedd.minecraftartifacts.items.AdminStaff;
import com.emprzedd.minecraftartifacts.items.ArtifactItem;
import com.emprzedd.minecraftartifacts.items.BanHammer;
import com.emprzedd.minecraftartifacts.items.BlessedDagger;
import com.emprzedd.minecraftartifacts.items.DragonEgg;
import com.emprzedd.minecraftartifacts.items.DragonRulerBanner;
import com.emprzedd.minecraftartifacts.items.LockettePick;
import com.emprzedd.minecraftartifacts.items.LoreWoodAxe;
import com.emprzedd.minecraftartifacts.items.MessengerBoots;
import com.emprzedd.minecraftartifacts.items.PumpkinHead;
import com.emprzedd.minecraftartifacts.items.RunnerBoots;
import com.emprzedd.minecraftartifacts.items.WaterCrown;
import com.emprzedd.minecraftartifacts.items.RednaBreads.BlackBread;
import com.emprzedd.minecraftartifacts.items.RednaBreads.BloodBread;
import com.emprzedd.minecraftartifacts.items.RednaBreads.CeremonialBread;
import com.emprzedd.minecraftartifacts.items.RednaBreads.FeastBread;
import com.emprzedd.minecraftartifacts.items.RednaBreads.GoldenBread;
import com.emprzedd.minecraftartifacts.items.RednaBreads.MaggotBread;


public class Main extends JavaPlugin{

	 @Override
	    public void onEnable(){
	        //getConfig().options().copyDefaults(true);
	        saveDefaultConfig();
		 	FileLogger.setPlugin(this);
		 	ArtifactItem.setPlugin(this);
		 	
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
		 	getServer().getPluginManager().registerEvents(new DragonEgg(), this);
		 	//getServer().getPluginManager().registerEvents(new LockettePick(), this);
		 	//getServer().getPluginManager().registerEvents(new DragonRulerBanner(), this);
		 	getServer().getPluginManager().registerEvents(new PumpkinHead(), this);
		 	getServer().getPluginManager().registerEvents(new RunnerBoots(), this);
		 	getServer().getPluginManager().registerEvents(new MaggotBread(), this);
		 	getServer().getPluginManager().registerEvents(new BlackBread(), this);
		 	getServer().getPluginManager().registerEvents(new CeremonialBread(), this);
		 	getServer().getPluginManager().registerEvents(new BloodBread(), this);
		 	getServer().getPluginManager().registerEvents(new FeastBread(), this);
		 	getServer().getPluginManager().registerEvents(new GoldenBread(), this);
		 	getServer().getPluginManager().registerEvents(new WaterCrown(), this);
		 	
		 	
		 	
		 	getCommand("artifact").setExecutor(new CommandHandler(new ArtifactSelectionGUI()));
		 	//getCommand("artifact").setTabCompleter(new CommandHandler(new ArtifactSelectionGUI()));
		 	
		 	
	        //getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Minecraft Artifacts loaded");
	        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&f█&f█&f█&1╗&b░&b░&b░&f█&f█&f█&1╗&f█&f█&1╗&f█&f█&f█&1╗&b░&b░&f█&f█&1╗&f█&f█&f█&f█&f█&f█&f█&1╗&b░&f█&f█&f█&f█&f█&1╗&b░&f█&f█&f█&f█&f█&f█&1╗&b░&b░&f█&f█&f█&f█&f█&1╗&b░&f█&f█&f█&f█&f█&f█&f█&f█&1╗"));
	        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&f█&f█&f█&f█&1╗&b░&f█&f█&f█&f█&1║&f█&f█&1║&f█&f█&f█&f█&1╗&b░&f█&f█&1║&f█&f█&1╔&1═&1═&1═&1═&1╝&f█&f█&1╔&1═&1═&f█&f█&1╗&f█&f█&1╔&1═&1═&f█&f█&1╗&f█&f█&1╔&1═&1═&f█&f█&1╗&1╚&1═&1═&f█&f█&1╔&1═&1═&1╝"));
	        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&f█&f█&1╔&f█&f█&f█&f█&1╔&f█&f█&1║&f█&f█&1║&f█&f█&1╔&f█&f█&1╗&f█&f█&1║&f█&f█&f█&f█&f█&1╗&b░&b░&f█&f█&1║&b░&b░&1╚&1═&1╝&f█&f█&f█&f█&f█&f█&1╔&1╝&f█&f█&f█&f█&f█&f█&f█&1║&b░&b░&b░&f█&f█&1║&b░&b░&b░"));
	        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&f█&f█&1║&1╚&f█&f█&1╔&1╝&f█&f█&1║&f█&f█&1║&f█&f█&1║&1╚&f█&f█&f█&f█&1║&f█&f█&1╔&1═&1═&1╝&b░&b░&f█&f█&1║&b░&b░&f█&f█&1╗&f█&f█&1╔&1═&1═&f█&f█&1╗&f█&f█&1╔&1═&1═&f█&f█&1║&b░&b░&b░&f█&f█&1║&b░&b░&b░"));
	        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&f█&f█&1║&b░&1╚&1═&1╝&b░&f█&f█&1║&f█&f█&1║&f█&f█&1║&b░&1╚&f█&f█&f█&1║&f█&f█&f█&f█&f█&f█&f█&1╗&1╚&f█&f█&f█&f█&f█&1╔&1╝&f█&f█&1║&b░&b░&f█&f█&1║&f█&f█&1║&b░&b░&f█&f█&1║&b░&b░&b░&f█&f█&1║&b░&b░&b░"));
	        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&1╚&1═&1╝&b░&b░&b░&b░&b░&1╚&1═&1╝&1╚&1═&1╝&1╚&1═&1╝&b░&b░&1╚&1═&1═&1╝&1╚&1═&1═&1═&1═&1═&1═&1╝&b░&1╚&1═&1═&1═&1═&1╝&b░&1╚&1═&1╝&b░&b░&1╚&1═&1╝&1╚&1═&1╝&b░&b░&1╚&1═&1╝&b░&b░&b░&1╚&1═&1╝&b░&b░&b░"));
	        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',""));
	        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&b░&f█&f█&f█&f█&f█&1╗&b░&f█&f█&f█&f█&f█&f█&1╗&b░&f█&f█&f█&f█&f█&f█&f█&f█&1╗&f█&f█&1╗&f█&f█&f█&f█&f█&f█&f█&1╗&b░&f█&f█&f█&f█&f█&1╗&b░&b░&f█&f█&f█&f█&f█&1╗&b░&f█&f█&f█&f█&f█&f█&f█&f█&1╗&b░&f█&f█&f█&f█&f█&f█&1╗&f█&f█&1╗"));
	        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&f█&f█&1╔&1═&1═&f█&f█&1╗&f█&f█&1╔&1═&1═&f█&f█&1╗&1╚&1═&1═&f█&f█&1╔&1═&1═&1╝&f█&f█&1║&f█&f█&1╔&1═&1═&1═&1═&1╝&f█&f█&1╔&1═&1═&f█&f█&1╗&f█&f█&1╔&1═&1═&f█&f█&1╗&1╚&1═&1═&f█&f█&1╔&1═&1═&1╝&f█&f█&1╔&1═&1═&1═&1═&1╝&f█&f█&1║"));
	        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&f█&f█&f█&f█&f█&f█&f█&1║&f█&f█&f█&f█&f█&f█&1╔&1╝&b░&b░&b░&f█&f█&1║&b░&b░&b░&f█&f█&1║&f█&f█&f█&f█&f█&1╗&b░&b░&f█&f█&f█&f█&f█&f█&f█&1║&f█&f█&1║&b░&b░&1╚&1═&1╝&b░&b░&b░&f█&f█&1║&b░&b░&b░&1╚&f█&f█&f█&f█&f█&1╗&b░&f█&f█&1║"));
	        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&f█&f█&1╔&1═&1═&f█&f█&1║&f█&f█&1╔&1═&1═&f█&f█&1╗&b░&b░&b░&f█&f█&1║&b░&b░&b░&f█&f█&1║&f█&f█&1╔&1═&1═&1╝&b░&b░&f█&f█&1╔&1═&1═&f█&f█&1║&f█&f█&1║&b░&b░&f█&f█&1╗&b░&b░&b░&f█&f█&1║&b░&b░&b░&b░&1╚&1═&1═&1═&f█&f█&1╗&1╚&1═&1╝"));
	        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&f█&f█&1║&b░&b░&f█&f█&1║&f█&f█&1║&b░&b░&f█&f█&1║&b░&b░&b░&f█&f█&1║&b░&b░&b░&f█&f█&1║&f█&f█&1║&b░&b░&b░&b░&b░&f█&f█&1║&b░&b░&f█&f█&1║&1╚&f█&f█&f█&f█&f█&1╔&1╝&b░&b░&b░&f█&f█&1║&b░&b░&b░&f█&f█&f█&f█&f█&f█&1╔&1╝&f█&f█&1╗"));
	        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&1╚&1═&1╝&b░&b░&1╚&1═&1╝&1╚&1═&1╝&b░&b░&1╚&1═&1╝&b░&b░&b░&1╚&1═&1╝&b░&b░&b░&1╚&1═&1╝&1╚&1═&1╝&b░&b░&b░&b░&b░&1╚&1═&1╝&b░&b░&1╚&1═&1╝&b░&1╚&1═&1═&1═&1═&1╝&b░&b░&b░&b░&1╚&1═&1╝&b░&b░&b░&1╚&1═&1═&1═&1═&1═&1╝&b░&1╚&1═&1╝"));
	        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&bMinecraft Artifacts Loaded"));
	        
	    }

	    @Override
	    public void onDisable(){
	        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Minecraft Artifacts Stopped");
	    }
}
