package me.Vark123.EpicParty.PlayerPartySystem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.entity.Player;

import lombok.Getter;

@Getter
public final class PlayerManager {

	private static final PlayerManager inst = new PlayerManager();
	
	private final Map<Player, PartyPlayer> players;
	
	private PlayerManager() {
		players = new LinkedHashMap<>();
	}
	
	public static final PlayerManager get() {
		return inst;
	}
	
	public PartyPlayer registerPlayer(Player p) {
		return players.put(p, new PartyPlayer(p));
	}
	
	public Optional<PartyPlayer> getPartyPlayer(Player p) {
		return Optional.ofNullable(players.get(p));
	}
	
	public Optional<PartyPlayer> unregisterPlayer(Player p) {
		return Optional.ofNullable(players.remove(p));
	}
	
}
