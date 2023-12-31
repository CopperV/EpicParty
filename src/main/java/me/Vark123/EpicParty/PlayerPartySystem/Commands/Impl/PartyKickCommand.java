package me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicParty.Config;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyManager;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;
import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;
import me.Vark123.EpicParty.PlayerPartySystem.Commands.APartyCommand;

public class PartyKickCommand extends APartyCommand {

	public PartyKickCommand() {
		super("kick", new String[] {"wyrzuc"});
	}

	@Override
	public boolean canUse(CommandSender sender) {
		if(!(sender instanceof Player))
			return false;
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getPartyPlayer((Player) sender)
			.ifPresentOrElse(pp -> {
				pp.getParty().ifPresentOrElse(
						party -> result.setValue(party.getLeader().equals(pp)),
						() -> result.setFalse());
			}, () -> result.setFalse());
		return result.booleanValue();
	}

	@Override
	public boolean useCommand(CommandSender sender, String... args) {
		if(args == null || args.length < 1) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dMusisz podac gracza, ktorego chcesz wyrzucic");
			return false;
		}
		
		Player p = (Player) sender;
		PartyPlayer pp = PlayerManager.get().getPartyPlayer(p).get();
		Party party = pp.getParty().get();
		
		Player target = Bukkit.getPlayerExact(args[0]);
		if(target == null) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dGracz §7§o"+args[0]+" §djest offline!");
			return false;
		}
		if(target.equals(p)) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dNie mozesz wyrzucic samego siebie z druzyny!");
			return false;
		}

		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getPartyPlayer(target).ifPresentOrElse(pTarget -> {
			pTarget.getParty().ifPresentOrElse(targetParty -> {
				if(!targetParty.equals(party)) {
					sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §7§o"+args[0]+" §dnie jest w Twojej druzynie!");
					result.setFalse();
					return;
				}
				PartyManager.get().kickFromParty(party, pTarget);
			}, () -> {
				sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §7§o"+args[0]+" §dnie jest w Twojej druzynie!");
				result.setFalse();
			});
		}, () -> {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dBLAD! §7§o"+args[0]+" §dnie jest zapisany! Zglos blad administratorowi!");
			result.setFalse();
		});
		return result.getValue();
	}

	@Override
	public void showCorrectUsage(CommandSender sender) {
		sender.sendMessage("  §d/party wyrzuc <gracz> §7- Wyrzuca gracza z Twojej druzyny");
	}

}
