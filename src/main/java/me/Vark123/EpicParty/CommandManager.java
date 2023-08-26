package me.Vark123.EpicParty;

import org.bukkit.Bukkit;

import me.Vark123.EpicParty.PlayerPartySystem.Commands.BasePartyCommand;
import me.Vark123.EpicParty.PlayerPartySystem.Commands.PartyCommandManager;
import me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl.PartyChatCommand;
import me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl.PartyInfoCommand;
import me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl.PartyInviteCommand;
import me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl.PartyJoinCommand;
import me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl.PartyKickCommand;
import me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl.PartyLeaderCommand;
import me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl.PartyLeaveCommand;

public class CommandManager {

	private CommandManager() { }
	
	public static void setExecutors() {
		Bukkit.getPluginCommand("epicparty").setExecutor(new BasePartyCommand());
		
		PartyCommandManager.get().registerSubcommand(new PartyChatCommand());
		PartyCommandManager.get().registerSubcommand(new PartyInfoCommand());
		PartyCommandManager.get().registerSubcommand(new PartyInviteCommand());
		PartyCommandManager.get().registerSubcommand(new PartyJoinCommand());
		PartyCommandManager.get().registerSubcommand(new PartyLeaderCommand());
		PartyCommandManager.get().registerSubcommand(new PartyLeaveCommand());
		PartyCommandManager.get().registerSubcommand(new PartyKickCommand());
	}
	
}
