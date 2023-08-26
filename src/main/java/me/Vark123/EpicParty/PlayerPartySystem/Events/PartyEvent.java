package me.Vark123.EpicParty.PlayerPartySystem.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicParty.PlayerPartySystem.Party;

@Getter
public abstract class PartyEvent extends Event implements Cancellable {

	@Setter
	private boolean cancelled;
	
	private Party party;
	@Setter
	protected String cancelMessage;
	
	public PartyEvent(Party party) {
		this.party = party;
	}
	
}
