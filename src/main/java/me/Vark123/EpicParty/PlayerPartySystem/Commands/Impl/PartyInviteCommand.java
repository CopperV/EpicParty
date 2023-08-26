package me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableObject;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicParty.Config;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;
import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;
import me.Vark123.EpicParty.PlayerPartySystem.Commands.APartyCommand;
import me.Vark123.EpicParty.Tools.Pair;

public class PartyInviteCommand extends APartyCommand {

	public PartyInviteCommand() {
		super("invite", new String[]{"zapros"});
	}

	@Override
	public boolean canUse(CommandSender sender) {
		if(!(sender instanceof Player))
			return false;
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getPartyPlayer((Player) sender)
			.ifPresentOrElse(pp -> {
				pp.getParty().ifPresent(party -> {
					result.setValue(party.getLeader().equals(pp));
				});
			}, () -> {
				result.setFalse();
			});
		return result.booleanValue();
	}

	@Override
	public boolean useCommand(CommandSender sender, String... args) {
		if(args == null || args.length < 1) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dMusisz podac gracza, ktorego chcesz zaprosic");
			return false;
		}
		
		Player p = (Player) sender;
		PartyPlayer pp = PlayerManager.get().getPartyPlayer(p).get();
		MutableObject<Party> party = new MutableObject<>();
		if(pp.getParty().isPresent())
			party.setValue(pp.getParty().get());
		if(party.getValue() != null && !party.getValue().getLeader().equals(pp)) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dNie jestes liderem party! Nie mozesz zapraszac graczy!");
			return false;
		}
		
		Player target = Bukkit.getPlayerExact(args[0]);
		if(target == null) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dGracz §7§o"+args[0]+" §djest offline!");
			return false;
		}
		if(target.equals(p)) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dNie mozesz zaprosic samego siebie do druzyny!");
			return false;
		}

		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getPartyPlayer(target).ifPresentOrElse(pTarget -> {
			if(pTarget.getParty().isPresent() 
					&& party.getValue() != null
					&& party.getValue().equals(pTarget.getParty().get())) {
				sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §7§o"+args[0]+" §djest juz w Twojej druzynie!");
				result.setFalse();
				return;
			}
			pTarget.getPartyInvitations().keySet().stream()
				.filter(task -> !task.isCancelled()
						&& pTarget.getPartyInvitations().get(task).getKey().equals(pp))
				.findFirst()
				.ifPresent(task -> {
					Pair<PartyPlayer, Party> pair = pTarget.getPartyInvitations().get(task);
					if((party.getValue() == null && pair.getValue() == null)
							|| party.getValue().equals(pair.getValue())) {
						sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dJuz zaprosiles §7§o"+args[0]+" §ddo druzyny!");
						result.setFalse();
						return;
					} else {
						pTarget.cancelInvitation(task);
						return;
					}
				});
			if(result.isTrue()){
				pTarget.sendInvitation(pp, party.getValue());
			}
		}, () -> {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dBLAD! §7§o"+args[0]+" §dnie jest zapisany! Zglos blad administratorowi!");
			result.setFalse();
		});
		return result.booleanValue();
	}

	@Override
	public void showCorrectUsage(CommandSender sender) {
		sender.sendMessage("  §d/party zapros <gracz> §7- Wysyla graczowi zaproszenie do Twojej druzyny");
	}

}
