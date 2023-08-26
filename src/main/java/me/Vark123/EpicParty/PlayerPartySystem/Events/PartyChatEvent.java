package me.Vark123.EpicParty.PlayerPartySystem.Events;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;

@Getter
public class PartyChatEvent extends PartyEvent {

	private static final HandlerList handlers = new HandlerList();

	private PartyPlayer sender;
	@Setter
	private String msg;
	
	public PartyChatEvent(Party party, PartyPlayer sender, String msg) {
		super(party);
		this.sender = sender;
		this.msg = msg;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
