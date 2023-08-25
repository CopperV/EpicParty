package me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl;

import org.bukkit.command.CommandSender;

import me.Vark123.EpicParty.PlayerPartySystem.Commands.APartyCommand;

public class PartyLeaveCommand extends APartyCommand {

	public PartyLeaveCommand() {
		super("leave");
	}

	@Override
	public boolean canUse(CommandSender sender) {
		return false;
	}

	@Override
	public boolean useCommand(CommandSender sender, String... args) {
		return false;
	}

	@Override
	public void showCorrectUsage(CommandSender sender) {
		
	}

}
