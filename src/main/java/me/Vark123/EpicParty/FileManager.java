package me.Vark123.EpicParty;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager {

	private FileManager() { }
	
	public static void init() {
		if(!Main.getInst().getDataFolder().exists())
			Main.getInst().getDataFolder().mkdir();
		
		Main.getInst().saveResource("config.yml", false);
		
		Config.get().load();
	}
	
	public static YamlConfiguration getConfig() {
		File f = new File(Main.getInst().getDataFolder(), "config.yml");
		return YamlConfiguration.loadConfiguration(f);
	}
	
}
