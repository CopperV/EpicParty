package me.Vark123.EpicParty;

import org.bukkit.Bukkit;

import me.Vark123.EpicParty.PlayerPartySystem.Listeners.PlayerJoinListener;
import me.Vark123.EpicParty.PlayerPartySystem.Listeners.PlayerQuitListener;

public class ListenerManager {

	private ListenerManager() { }
	
	public static void registerListeners() {
		Main inst = Main.getInst();
	
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), inst);
	}
	
}
