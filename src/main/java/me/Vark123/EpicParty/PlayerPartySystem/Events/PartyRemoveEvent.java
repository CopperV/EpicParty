package me.Vark123.EpicParty.PlayerPartySystem.Events;

import org.bukkit.event.HandlerList;

import me.Vark123.EpicParty.PlayerPartySystem.Party;

public class PartyRemoveEvent extends PartyEvent {

	private static final HandlerList handlers = new HandlerList();

	public PartyRemoveEvent(Party party) {
		super(party);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
