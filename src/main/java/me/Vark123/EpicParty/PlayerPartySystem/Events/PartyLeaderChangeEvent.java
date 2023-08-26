package me.Vark123.EpicParty.PlayerPartySystem.Events;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;

@Getter
public class PartyLeaderChangeEvent extends PartyEvent {

	private static final HandlerList handlers = new HandlerList();

	private final PartyPlayer oldLeader;
	private final PartyPlayer newLeader;
	
	public PartyLeaderChangeEvent(Party party, PartyPlayer newLeader, PartyPlayer oldLeader) {
		super(party);
		this.oldLeader = oldLeader;
		this.newLeader = newLeader;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
