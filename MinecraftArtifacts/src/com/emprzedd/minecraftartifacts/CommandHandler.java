package com.emprzedd.minecraftartifacts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.emprzedd.minecraftartifacts.items.ArtifactItem;

public class CommandHandler implements CommandExecutor,TabCompleter{
	
	ArtifactSelectionGUI gui;
	
	public CommandHandler(ArtifactSelectionGUI gui) {
		this.gui = gui;
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)) {
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "You must be a player to use this command.");
        	return true;
        }
        
       
        Player player = (Player)sender;
        
        if(!player.hasPermission("MinecraftArtifacts.Usage")){
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if(cmd.getName().equalsIgnoreCase("artifact")){
        	
        	if(args.length ==0) {
        		player.openInventory(gui.getInventory());
        		return true;
        	}
        	else if(args.length>=2) {
        		if(args[0].equalsIgnoreCase("give")) {
        			player.sendMessage(ChatColor.RED+"This is not implemented yet");
        			return true;
        		}
        		
        		player.sendMessage(ChatColor.RED+"uh oh, you used this command wrong");
        	}

        }


        return true;
    }

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("artifact")) { // checking if my command is the one i'm after

            List<String> autoCompletes = new ArrayList<>(); //create a new string list for tab completion

            if (args.length == 1) { //only interested in the first sub command, if you wanted to cover more deeper sub commands, you could have multiple if statements or a switch statement
            	autoCompletes.add("give");
            }
            else if(args.length ==2 && args[0].equalsIgnoreCase("give")) {
            	for(ArtifactItem item : ArtifactItem.ArtifactList)
            		autoCompletes.add(item.getRawName());
            }

            
            return autoCompletes; // then return the list
        }

        return null; // this will return nothing if it wasn't the disguise command I have
	}

}
