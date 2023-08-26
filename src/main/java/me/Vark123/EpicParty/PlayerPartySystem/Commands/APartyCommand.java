package me.Vark123.EpicParty.PlayerPartySystem.Commands;

import org.bukkit.command.CommandSender;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public abstract class APartyCommand {

	protected String cmd;
	protected String[] aliases;
	
	public abstract boolean canUse(CommandSender sender);
	public abstract boolean useCommand(CommandSender sender, String... args);
	public abstract void showCorrectUsage(CommandSender sender);
	
}