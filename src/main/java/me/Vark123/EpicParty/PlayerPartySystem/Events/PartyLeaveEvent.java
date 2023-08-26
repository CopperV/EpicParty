package me.Vark123.EpicParty.PlayerPartySystem.Events;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;

@Getter
public class PartyLeaveEvent extends PartyEvent {

	private static final HandlerList handlers = new HandlerList();

	private PartyPlayer oldMember;
	
	public PartyLeaveEvent(Party party, PartyPlayer oldMember) {
		super(party);
		this.oldMember = oldMember;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
