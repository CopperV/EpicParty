package me.Vark123.EpicParty.PlayerPartySystem;

import java.util.Optional;

import org.bukkit.entity.Player;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PartyPlayer {

	private Player player;
	private Party party;
	
	public PartyPlayer(Player player) {
		this.player = player;
	}
	
	public Optional<Party> getParty() {
		return Optional.ofNullable(party);
	}
	
}
