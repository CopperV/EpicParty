package me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl;

import org.bukkit.command.CommandSender;

import me.Vark123.EpicParty.PlayerPartySystem.Commands.APartyCommand;

public class PartyInviteCommand extends APartyCommand {

	public PartyInviteCommand() {
		super("invite");
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
