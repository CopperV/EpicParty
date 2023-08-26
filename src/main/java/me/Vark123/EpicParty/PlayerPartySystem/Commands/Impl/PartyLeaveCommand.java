package me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyManager;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;
import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;
import me.Vark123.EpicParty.PlayerPartySystem.Commands.APartyCommand;

public class PartyLeaveCommand extends APartyCommand {

	public PartyLeaveCommand() {
		super("leave");
	}

	@Override
	public boolean canUse(CommandSender sender) {
		if(!(sender instanceof Player))
			return false;
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getPartyPlayer((Player) sender)
			.ifPresentOrElse(pp -> {
				result.setValue(pp.getParty().isPresent());
			}, () -> result.setFalse());
		return result.booleanValue();
	}

	@Override
	public boolean useCommand(CommandSender sender, String... args) {
		Player p = (Player) sender;
		PartyPlayer pp = PlayerManager.get().getPartyPlayer(p).get();
		Party party = pp.getParty().get();
		return PartyManager.get().leaveParty(party, pp);
	}

	@Override
	public void showCorrectUsage(CommandSender sender) {
		sender.sendMessage("  §d/party opusc §7- Opuszczasz druzyne, w ktorej obecnie sie znajdujesz");
	}

}
