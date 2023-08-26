package me.Vark123.EpicParty;

import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;
import me.Vark123.EpicParty.PlayerPartySystem.PartyManager;
import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;

@Getter
public final class EpicPartyAPI {

	private static final EpicPartyAPI inst = new EpicPartyAPI();
	
	private final Main epicpartyInst;
	private final PartyManager partyManager;
	private final PlayerManager playerManager;
	private final InventoryManager inventoryManager;
	
	private EpicPartyAPI() {
		epicpartyInst = Main.getInst();
		partyManager = PartyManager.get();
		playerManager = PlayerManager.get();
		inventoryManager = Main.getInst().getInventoryManager();
	}
	
	public static final EpicPartyAPI get() {
		return inst;
	}
	
}
