package me.Vark123.EpicParty.PlayerPartySystem;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.mutable.MutableObject;
import org.bukkit.Bukkit;

import lombok.Getter;
import me.Vark123.EpicParty.Config;
import me.Vark123.EpicParty.PlayerPartySystem.Events.PartyCreateEvent;
import me.Vark123.EpicParty.PlayerPartySystem.Events.PartyEvent;
import me.Vark123.EpicParty.PlayerPartySystem.Events.PartyJoinEvent;
import me.Vark123.EpicParty.PlayerPartySystem.Events.PartyLeaderChangeEvent;
import me.Vark123.EpicParty.PlayerPartySystem.Events.PartyLeaveEvent;
import me.Vark123.EpicParty.PlayerPartySystem.Events.PartyRemoveEvent;

@Getter
public final class PartyManager {

	private static final PartyManager inst = new PartyManager();
	
	private final Collection<Party> parties;
	
	private PartyManager() {
		parties = new HashSet<>();
	}
	
	public static final PartyManager get() {
		return inst;
	}
	
	public Party createParty(PartyPlayer leader, PartyPlayer member) {
		if(member.getParty().isPresent()) {
			Party memberParty = member.getParty().get();
			if(!leaveParty(memberParty, member))
				return null;
		}
		
		Party party = new Party(leader);
		party.getMembers().add(member);
		
		PartyEvent event = new PartyCreateEvent(party, leader, member);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			leader.sendMessage("§7["+Config.get().getPrefix()+"§7] §dNie mozesz stworzyc party z §7"+member.getName());
			member.sendMessage("§7["+Config.get().getPrefix()+"§7] §dNie mozesz dolaczyc do party §7"+leader.getName());
			if(event.getCancelMessage() != null && !event.getCancelMessage().isEmpty()) {
				leader.sendMessage("§dPowod: §r"+event.getCancelMessage());
				member.sendMessage("§dPowod: §r"+event.getCancelMessage());
			}
			return null;
		}
		party.broadcastMessage("§7§o"+member.getName()+" §bdolaczyl do party");
		member.sendMessage("§7["+Config.get().getPrefix()+"§7] §dDolaczyles do party §7"+leader.getName());
		
		parties.add(party);
		leader.setParty(party);
		member.setParty(party);
		
		return party;
	}
	
	public boolean jointToParty(Party party, PartyPlayer newMember) {
		if(newMember.getParty().isPresent()) {
			Party memberParty = newMember.getParty().get();
			if(!leaveParty(memberParty, newMember))
				return false;
		}
		
		PartyEvent event = new PartyJoinEvent(party, newMember);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			PartyPlayer leader = party.getLeader();
			leader.sendMessage("§7["+Config.get().getPrefix()+"§7] §7"+newMember.getName()+" §dnie moze dolaczyc do druzyny");
			newMember.sendMessage("§7["+Config.get().getPrefix()+"§7] §dNie mozesz dolaczyc do druzyny §7"+leader.getName());
			if(event.getCancelMessage() != null && !event.getCancelMessage().isEmpty()) {
				leader.sendMessage("§dPowod: §r"+event.getCancelMessage());
				newMember.sendMessage("§dPowod: §r"+event.getCancelMessage());
			}
			return false;
		}
		party.broadcastMessage("§7§o"+newMember.getName()+" §bdolaczyl do party");
		newMember.sendMessage("§7["+Config.get().getPrefix()+"§7] §dDolaczyles do party §7"+party.getLeader().getName());
		
		party.getMembers().add(newMember);
		newMember.setParty(party);
		return true;
	}
	
	public boolean leaveParty(Party party, PartyPlayer oldMember) {
		PartyEvent leaveEvent = new PartyLeaveEvent(party, oldMember);
		Bukkit.getPluginManager().callEvent(leaveEvent);
		if(leaveEvent.isCancelled()) {
			oldMember.sendMessage("§7["+Config.get().getPrefix()+"§7] §dNie mozesz opuscic druzyny §7"+party.getLeader().getName());
			if(leaveEvent.getCancelMessage() != null && !leaveEvent.getCancelMessage().isEmpty())
				oldMember.sendMessage("§dPowod: §r"+leaveEvent.getCancelMessage());
			return false;
		}

		oldMember.sendMessage("§7["+Config.get().getPrefix()+"§7] §dOpusciles druzyne §7"+party.getLeader().getName());
		oldMember.setParty(null);
		party.getMembers().remove(oldMember);
		if(party.getMembers().size() < 3) {
			return removeParty(party);
		}
		
		party.broadcastMessage("§7§o"+oldMember.getName()+" §dopuscil druzyne");
		if(oldMember.equals(party.getLeader())) {
			Random rand = new Random();
			PartyPlayer newLeader = party.getMembers()
					.get(rand.nextInt(party.getMembers().size()));
			changePartyLeader(party, newLeader);
		}
		return true;
	}
	
	public boolean removeParty(Party party) {
		PartyEvent event = new PartyRemoveEvent(party);
		Bukkit.getPluginManager().callEvent(event);
		party.getMembers().forEach(pp -> {
			pp.sendMessage("§7["+Config.get().getPrefix()+"§7] §dDruzyna §7"+party.getLeader().getName()+" §dzostala rozwiazana");
			pp.setParty(null);
		});
		party.getMembers().clear();
		party.setLeader(null);
		parties.remove(party);
		return true;
	}
	
	public boolean changePartyLeader(Party party, PartyPlayer newLeader) {
		PartyPlayer oldLeader = party.getLeader();
		
		PartyEvent event = new PartyLeaderChangeEvent(party, newLeader, oldLeader);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			oldLeader.sendMessage("§7["+Config.get().getPrefix()+"§7] §dNie mozesz mianowac §7"+newLeader.getName()+" §dnowym liderem druzyny");
			if(event.getCancelMessage() != null && !event.getCancelMessage().isEmpty())
				oldLeader.sendMessage("§dPowod: §r"+event.getCancelMessage());
			return false;
		}

		party.broadcastMessage("§7§o"+oldLeader.getName()+" §bmianowal §7§o"+newLeader.getName()+" §bnowym liderem druzyny");
		party.setLeader(newLeader);
		return true;
	}
	
	public Optional<Party> getPartyByLeader(PartyPlayer leader) {
		return parties.stream()
				.filter(party -> party.getLeader().equals(leader))
				.findFirst();
	}
	
	public String getPartyPlayerSignature(PartyPlayer pp) {
		MutableObject<String> toReturn = new MutableObject<>(Config.get().getMemberSignature());
		pp.getParty().ifPresent(party -> {
			if(party.getLeader().equals(pp))
				toReturn.setValue(Config.get().getLeaderSignature());
		});
		return toReturn.getValue();
	}
	
}
