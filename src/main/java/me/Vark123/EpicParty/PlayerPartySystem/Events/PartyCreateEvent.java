package me.Vark123.EpicParty.PlayerPartySystem.Events;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;

@Getter
public class PartyCreateEvent extends PartyEvent {

	private static final HandlerList handlers = new HandlerList();

	private PartyPlayer leader;
	private PartyPlayer member;
	
	public PartyCreateEvent(Party party, PartyPlayer leader, PartyPlayer member) {
		super(party);
		
		this.leader = leader;
		this.member = member;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
