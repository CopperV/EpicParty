package me.Vark123.EpicParty;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

@Getter
public class Main extends JavaPlugin {

	@Getter
	private static Main inst;
	
	private EpicPartyAPI api;
	
	@Override
	public void onEnable() {
		inst = this;
		api = EpicPartyAPI.get();
		
		CommandManager.setExecutors();
		ListenerManager.registerListeners();
		FileManager.init();
	}

	@Override
	public void onDisable() {
		
	}

}
