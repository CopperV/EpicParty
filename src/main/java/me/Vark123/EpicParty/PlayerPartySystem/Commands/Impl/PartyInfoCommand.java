package me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicParty.Config;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;
import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;
import me.Vark123.EpicParty.PlayerPartySystem.Commands.APartyCommand;

public class PartyInfoCommand extends APartyCommand {

	public PartyInfoCommand() {
		super("info");
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
		Player p = (Player) sender;
		
		MutableBoolean result = new MutableBoolean(true);
		if(args != null && args.length > 0) {
			Player target = Bukkit.getPlayerExact(args[0]);
			if(target == null) {
				sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dGracz §7§o"+args[0]+" §djest offline!");
				return false;
			}
			PlayerManager.get().getPartyPlayer(target).ifPresentOrElse(pTarget -> {
				pTarget.getParty().ifPresentOrElse(party -> {
					party.printInfo(p);
				}, () -> {
					sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §7§o"+args[0]+" §dnie jest w zadnym party");
					result.setFalse();
				});
			}, () -> {
				sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dBLAD! §7§o"+args[0]+" §dnie jest zapisany! Zglos blad administratorowi!");
				result.setFalse();
			});
			return result.booleanValue();
		}

		PartyPlayer pp = PlayerManager.get().getPartyPlayer(p).get();
		pp.getParty().ifPresentOrElse(party -> {
			party.printInfo(p);
		}, () -> {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dNie znajdujesz sie w zadnym party!");
			result.setFalse();
		});
		
		return result.booleanValue();
	}

	@Override
	public void showCorrectUsage(CommandSender sender) {
		sender.sendMessage("  §d/party info (gracz) §7- Wyswietla informacje o swoim party badz innego gracza");
	}

}
