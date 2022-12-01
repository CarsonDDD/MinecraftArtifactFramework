package me.emprzedd.artifactframework;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PluginLogger {

    private String outputFile = "data.txt";
    private final JavaPlugin plugin;

    public PluginLogger(String outputFile, JavaPlugin plugin){
        this.outputFile = outputFile;
        this.plugin = plugin;
    }

    //logger
    public static String formatLocation(Location l) {
        String world = "\"NULL\": {";
        if(l.getWorld() != null)
            world = "\""+ l.getWorld().getName()+"\": {";

        String x = "\"X\":"+ Math.round(l.getX())+", ";
        String y = "\"Y\":"+ Math.round(l.getY())+", ";
        String z = "\"Z\":"+ Math.round(l.getZ()) + "}";

        return world+x+y+z;
    }

    public void log(String message) {
        try {
            File dataFolder = plugin.getDataFolder();

            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }

            File saveTo = new File(plugin.getDataFolder(), outputFile);

            if (!saveTo.exists()) {
                saveTo.createNewFile();
            }

            FileWriter fw = new FileWriter(saveTo, true);
            PrintWriter pw = new PrintWriter(fw);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            //System.out.println(dtf.format(now));

            pw.println("["+dtf.format(now)+"] "+message);

            pw.flush();
            pw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
