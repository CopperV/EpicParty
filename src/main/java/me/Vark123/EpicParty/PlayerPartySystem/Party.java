package me.Vark123.EpicParty.PlayerPartySystem;

import java.util.Collection;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Party {

	private PartyPlayer leader;
	private Collection<PartyPlayer> members;
	
}
