package me.Vark123.EpicParty.PlayerPartySystem.Commands.Impl;

import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicParty.Config;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyManager;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;
import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;
import me.Vark123.EpicParty.PlayerPartySystem.Commands.APartyCommand;
import me.Vark123.EpicParty.Tools.Pair;

public class PartyJoinCommand extends APartyCommand {

	public PartyJoinCommand() {
		super("join");
	}

	@Override
	public boolean canUse(CommandSender sender) {
		if(!(sender instanceof Player))
			return false;
		if(PlayerManager.get().getPartyPlayer((Player) sender).isEmpty())
			return false;
		return PlayerManager.get().getPartyPlayer((Player) sender).get()
				.getPartyInvitations().keySet()
				.stream()
				.filter(task -> !task.isCancelled())
				.count() > 0;
	}

	@Override
	public boolean useCommand(CommandSender sender, String... args) {
		if(args == null || args.length < 1) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dMusisz podac gracza, do ktorego druzyny chcesz dolaczyc");
			return false;
		}
		
		Player p = (Player) sender;
		PartyPlayer pp = PlayerManager.get().getPartyPlayer(p).get();
		MutableBoolean result = new MutableBoolean(true);
		pp.getPartyInvitations().keySet().stream()
			.filter(task -> !task.isCancelled()
					&& pp.getPartyInvitations().get(task).getKey().getName().equalsIgnoreCase(args[0]))
			.findFirst()
			.ifPresentOrElse(task -> {
				Pair<PartyPlayer, Party> pair = pp.getPartyInvitations().get(task);
				PartyPlayer pSender = pair.getKey();
				Party party = pair.getValue();
				if(!pSender.getPlayer().isOnline()) {
					sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §7§o"+pSender.getName()+" §djest offline. Nie mozesz dolaczyc do jego druzyny");
					pp.cancelInvitation(task);
					result.setFalse();
					return;
				}
				
				Optional<PartyPlayer> oPSender = PlayerManager.get().getPartyPlayer(pSender.getPlayer());
				if(oPSender.isEmpty() || !oPSender.get().equals(pSender)) {
					sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §7§o"+pSender.getName()+" §dwychodzil w miedzyczasie z serwera. To zaproszenie jest juz niewazne");
					pp.cancelInvitation(task);
					result.setFalse();
					return;
				}
				
				if(party == null) {
					if(pSender.getParty().isPresent()) {
						if(!pSender.getParty().get().getLeader().equals(pSender)) {
							sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §7§o"+pSender.getName()+" §dznajduje sie w druzynie, ktorej nie jest liderem. Nie mozesz do niego dolaczyc");
							pp.cancelInvitation(task);
							result.setFalse();
							return;
						} else 
							party = pSender.getParty().get();
					}
				} else {
					if(pp.getParty().isPresent() && pp.getParty().get().equals(party)) {
						sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dJestes juz w druzynie §7§o"+pSender.getName()+" §d. Nie mozesz jeszcze raz do niego dolaczyc.");
						pp.cancelInvitation(task);
						result.setFalse();
						return;
					}
					if(pSender.getParty().isPresent()) {
						if(party.equals(pSender.getParty().get())) {
							if(!party.getLeader().equals(pSender)) {
								sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dW druzynie §7§o"+pSender.getName()+" §dzmienil sie lider. To zaproszenie stalo sie nieauktualne");
								pp.cancelInvitation(task);
								result.setFalse();
								return;
							}
						} else {
							sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §7§o"+pSender.getName()+" §dw miedzyczasie zmienil druzyne. To zaproszenie stalo sie nieauktualne");
							pp.cancelInvitation(task);
							result.setFalse();
							return;
						}
					} else {
						sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §7§o"+pSender.getName()+" §dw miedzyczasie opuscil swoja druzyne. To zaproszenie stalo sie nieauktualne");
						pp.cancelInvitation(task);
						result.setFalse();
						return;
					}
				}
				if(result.isTrue()) {
					pp.cancelInvitation(task);
					if(party != null)
						result.setValue(PartyManager.get().jointToParty(party, pp));
					else
						result.setValue(PartyManager.get().createParty(pSender, pp) != null);
				}
			}, () -> {
				sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dNie masz zadnego zaproszenia do druzyny §7§o"+args[0]);
				result.setFalse();
			});
		return result.booleanValue();
	}

	@Override
	public void showCorrectUsage(CommandSender sender) {
		sender.sendMessage("  §d/party info <gracz> §7- Dolacz do czyjejsc druzyny (o ile wyslali Tobie zaproszenie)");
	}

}
