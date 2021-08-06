package com.emprzedd.minecraftartifacts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class FileLogger {
	
	String fileName = "data.txt";
	private static Main plugin;//change main to whatever class extends javaplugin
			
	public FileLogger(String fileName) {
		this.fileName= fileName;
	}
	
	//must call this function at the beginning of the program
	static public void setPlugin(Main plugin_){
		plugin = plugin_;
	}
	
	public static String entityLocation(Entity e) {
		return entityLocation(e.getLocation());
	}
	
	static public String entityLocation(Location l) {
		String world = ""+ l.getWorld().getName();
		String x = ",X:"+ Math.round(l.getX());
		String y = ",Y:"+ Math.round(l.getY());
		String z = ",Z:"+ Math.round(l.getZ());
		
		return world+x+y+z;
	}
	
	public void logToFile(String message) {
		try {	
			File dataFolder = plugin.getDataFolder();
			
			if (!dataFolder.exists()) {
				dataFolder.mkdir();
			}

			File saveTo = new File(plugin.getDataFolder(), fileName);
			
			if (!saveTo.exists()) {
				saveTo.createNewFile();
			}

			FileWriter fw = new FileWriter(saveTo, true);
			PrintWriter pw = new PrintWriter(fw);
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  
			//System.out.println(dtf.format(now));
			   
			pw.println("["+dtf.format(now)+"]:"+message);
			
			pw.flush();
			pw.close();
		} 
		catch (IOException e) {

			e.printStackTrace();

		}

	}

}
