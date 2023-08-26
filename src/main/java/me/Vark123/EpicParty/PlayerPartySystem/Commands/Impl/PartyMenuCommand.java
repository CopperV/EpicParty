package me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;
import me.Vark123.EpicParty.PlayerPartySystem.Commands.APartyCommand;
import me.Vark123.EpicParty.PlayerPartySystem.MenuSystem.PartyMenuManager;

public class PartyMenuCommand extends APartyCommand {

	public PartyMenuCommand() {
		super("menu", new String[] {"gui"});
	}

	@Override
	public boolean canUse(CommandSender sender) {
		if(!(sender instanceof Player))
			return false;
		if(PlayerManager.get().getPartyPlayer((Player) sender).isEmpty())
			return false;
		return true;
	}

	@Override
	public boolean useCommand(CommandSender sender, String... args) {
		PartyMenuManager.get().openDefaultMenu((Player) sender);
		return true;
	}

	@Override
	public void showCorrectUsage(CommandSender sender) {
		sender.sendMessage("  §d/party menu §7- Otwiera menu z przydatnymi dla gracza informacjami");
		
	}

	
	
}
