package me.Vark123.EpicParty.PlayerPartySystem.Commands;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Vark123.EpicParty.Config;

public class BasePartyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("epicparty"))
			return false;
		if(args.length == 0) {
			showCorrectUsage(sender);
			return false;
		}
		MutableBoolean returnValue = new MutableBoolean(true);
		PartyCommandManager.get().getPartySubcommand(args[0])
			.ifPresentOrElse(subcmd -> {
				if(!subcmd.canUse(sender)) {
					showCorrectUsage(sender);
					returnValue.setFalse();
					return;
				}
				if(args.length > 1) {
					String[] newArgs = new String[args.length - 1];
					for(int i = 0; i < newArgs.length; ++i)
						newArgs[i] = args[i+1];
					returnValue.setValue(subcmd.useCommand(sender, newArgs));
				} else
					returnValue.setValue(subcmd.useCommand(sender));
			}, () -> {
				showCorrectUsage(sender);
				returnValue.setFalse();
			});
		return returnValue.booleanValue();
	}

	private void showCorrectUsage(CommandSender sender) {
		sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dPoprawne uzycie komendy §7§o/party");
		PartyCommandManager.get().getPartySubcommands().values().stream()
			.filter(cmd -> cmd.canUse(sender))
			.forEach(cmd -> cmd.showCorrectUsage(sender));
	}
	
}
