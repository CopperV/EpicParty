package me.Vark123.EpicParty.PlayerPartySystem.Events;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;

@Getter
public class PartyInviteEvent extends PartyEvent {

	private static final HandlerList handlers = new HandlerList();

	private final PartyPlayer sender;
	private final PartyPlayer receiver;
	
	public PartyInviteEvent(Party party, PartyPlayer sender, PartyPlayer receiver) {
		super(party);
		this.sender = sender;
		this.receiver = receiver;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
