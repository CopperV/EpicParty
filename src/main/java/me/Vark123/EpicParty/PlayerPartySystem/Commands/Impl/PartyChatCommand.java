package me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicParty.Config;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;
import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;
import me.Vark123.EpicParty.PlayerPartySystem.Commands.APartyCommand;

public class PartyChatCommand extends APartyCommand {

	public PartyChatCommand() {
		super("chat");
	}

	@Override
	public boolean canUse(CommandSender sender) {
		if(!(sender instanceof Player))
			return false;
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getPartyPlayer((Player) sender).ifPresentOrElse(pp -> {
			result.setValue(pp.getParty().isPresent());
		}, () -> result.setFalse());
		return result.booleanValue();
	}

	@Override
	public boolean useCommand(CommandSender sender, String... args) {
		if(args == null || args.length < 1) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dNie mozesz wyslac pustej wiadomosci!");
			return false;
		}
		PartyPlayer pp = PlayerManager.get().getPartyPlayer((Player) sender).get();
		Party party = pp.getParty().get();
		StringBuilder msg = new StringBuilder();
		for(String arg : args) {
			msg.append(arg);
			msg.append(" ");
		}
		msg.deleteCharAt(msg.length() - 1);
		party.sendPartyMessage(pp, msg.toString());
		return true;
	}

	@Override
	public void showCorrectUsage(CommandSender sender) {
		sender.sendMessage("  §d/party chat <wiadomosc> §7- Wysyla wiadomosc na specjalny czat druzynowy");
	}

}
