package me.Vark123.EpicParty.PlayerPartySystem.Commands;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;

@Getter
public final class PartyCommandManager {

	private static final PartyCommandManager inst = new PartyCommandManager();
	
	private final Map<String, APartyCommand> partySubcommands;
	
	private PartyCommandManager() {
		partySubcommands = new LinkedHashMap<>();
	}
	
	public static final PartyCommandManager get() {
		return inst;
	}
	
	public void registerSubcommand(APartyCommand subcmd) {
		partySubcommands.put(subcmd.getCmd().toLowerCase(), subcmd);
		for(String alias : subcmd.getAliases())
			partySubcommands.put(alias, subcmd);
	}
	
	public Optional<APartyCommand> getPartySubcommand(String subcmd) {
		return Optional.ofNullable(partySubcommands.get(subcmd.toLowerCase()));
	}
	
}
