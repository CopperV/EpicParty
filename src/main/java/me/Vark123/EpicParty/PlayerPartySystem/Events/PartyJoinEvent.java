package me.Vark123.EpicParty.PlayerPartySystem.Events;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;

@Getter
public class PartyJoinEvent extends PartyEvent {

	private static final HandlerList handlers = new HandlerList();

	private PartyPlayer newMember;
	
	public PartyJoinEvent(Party party, PartyPlayer newMember) {
		super(party);
		this.newMember = newMember;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
