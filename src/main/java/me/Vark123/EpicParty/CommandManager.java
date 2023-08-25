package me.Vark123.EpicParty;

import org.bukkit.Bukkit;

import me.Vark123.EpicParty.PlayerPartySystem.Commands.BasePartyCommand;

public class CommandManager {

	private CommandManager() { }
	
	public static void setExecutors() {
		Bukkit.getPluginCommand("epicparty").setExecutor(new BasePartyCommand());
	}
	
}
