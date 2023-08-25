package me.Vark123.EpicParty.PlayerPartySystem;

import lombok.Getter;

@Getter
public final class PartyManager {

	private static final PartyManager inst = new PartyManager();
	
	private PartyManager() {
		
	}
	
	public static final PartyManager get() {
		return inst;
	}
	
}
