package me.emprzedd.artifactframework;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandHandler implements CommandExecutor/*, TabCompleter*/{
	
	ArtifactSelectionGUI gui;
	
	public CommandHandler(ArtifactSelectionGUI gui) {
		this.gui = gui;
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	
    	if(args.length >= 1 && args[0].equalsIgnoreCase("give")) {
    		sender.sendMessage(ChatColor.RED+"This is not implemented yet");
    	}
    	else if(args.length >= 1 && args[0].equalsIgnoreCase("gui")) {
    		execGui(sender,cmd,label,args);
    	}
    	else if(args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
    		execReload(sender,cmd,label,args);
    	}
    	// DEFAULT COMMAND TREE/HELP command
    	else {
    		sender.sendMessage("Test, This is from MinecraftArtifacts");
    	}

        return true;
    }	
	
	private void execReload(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("MinecraftArtifacts.Admin")) {
			ArtifactItem.reloadAllArtifacts();
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&lGOD&r&f: Artifacts reloaded!"));
		}
		else {
			// plebs
		}
	}
	
	private void execGui(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player && sender.hasPermission("MinecraftArtifacts.Admin")) {
        	((Player)sender).openInventory(gui.getInventory());
        }
        else if(sender.hasPermission("MinecraftArtifacts.Admin")) {
        	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&lGOD&r&f: You must be a player to use this command."));
        }
        else {
        	// plebs
        }
	}

	/*@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("artifact")) { // checking if my command is the one i'm after

            List<String> autoCompletes = new ArrayList<>(); //create a new string list for tab completion
            if(args.length == 0) {
            	autoCompletes.add("reload");
            	autoCompletes.add("give");
            	autoCompletes.add("give");
            	autoCompletes.add("help");
            }
            else if(args.length ==2 && args[0].equalsIgnoreCase("give")) {
            	for(ArtifactItem item : ArtifactItem.ArtifactList)
            		autoCompletes.add(item.getRawName());
            }

            
            return autoCompletes; // then return the list
        }

        return null;
	}*/

}
