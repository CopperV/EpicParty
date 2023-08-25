package me.Vark123.EpicParty;

import lombok.Getter;
import me.Vark123.EpicParty.PlayerPartySystem.PartyManager;
import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;

@Getter
public final class EpicPartyAPI {

	private static final EpicPartyAPI inst = new EpicPartyAPI();
	
	private final PartyManager partyManager;
	private final PlayerManager playerManager;
	
	private EpicPartyAPI() {
		partyManager = PartyManager.get();
		playerManager = PlayerManager.get();
	}
	
	public static final EpicPartyAPI get() {
		return inst;
	}
	
}
