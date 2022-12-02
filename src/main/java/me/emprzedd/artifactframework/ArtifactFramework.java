package me.emprzedd.artifactframework;

import me.emprzedd.artifactframework.items.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

import me.emprzedd.artifactframework.items.RednaBreads.BlackBread;
import me.emprzedd.artifactframework.items.RednaBreads.BloodBread;
import me.emprzedd.artifactframework.items.RednaBreads.CeremonialBread;
import me.emprzedd.artifactframework.items.RednaBreads.FeastBread;
import me.emprzedd.artifactframework.items.RednaBreads.GoldenBread;
import me.emprzedd.artifactframework.items.RednaBreads.MaggotBread;


public class ArtifactFramework extends JavaPlugin{

    private PluginLogger logger;
    @Override
    public void onEnable(){
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        logger = new PluginLogger("output.txt",this);
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
        getServer().getPluginManager().registerEvents(new FlameOfCreation(), this);
        //getServer().getPluginManager().registerEvents(new LockettePick(), this);
        // test
        //getServer().getPluginManager().registerEvents(new DragonRulerBanner(), this);
        getServer().getPluginManager().registerEvents(new PumpkinHead(), this);
        getServer().getPluginManager().registerEvents(new RunnerBoots(), this);
        getServer().getPluginManager().registerEvents(new MaggotBread(), this);
        getServer().getPluginManager().registerEvents(new BlackBread(), this);
        getServer().getPluginManager().registerEvents(new CeremonialBread(), this);
        getServer().getPluginManager().registerEvents(new BloodBread(), this);
        getServer().getPluginManager().registerEvents(new FeastBread(), this);
        getServer().getPluginManager().registerEvents(new GoldenBread(), this);



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
