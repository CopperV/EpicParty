package me.Vark123.EpicParty.PlayerPartySystem;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicParty.Config;
import me.Vark123.EpicParty.PlayerPartySystem.Events.PartyChatEvent;
import net.md_5.bungee.api.ChatColor;

@Getter
@EqualsAndHashCode
public class Party {

	@Setter
	private PartyPlayer leader;
	private List<PartyPlayer> members;
	
	public Party(PartyPlayer leader) {
		this.leader = leader;
		
		members = new LinkedList<>();
		members.add(leader);
	}
	
	public void printInfo(Player target) {
		target.sendMessage("§7["+Config.get().getPrefix()+"§7] §dInformacje o party");
		target.sendMessage("  §bLider: §r"+Config.get().getLeaderSignature()+" §7"+leader.getName());
		StringBuilder builder = new StringBuilder("  §bCzlonkowie: §r");
		for(PartyPlayer member : members) {
			if(member.equals(leader))
				continue;
			builder.append(Config.get().getMemberSignature()+" §7"+member.getName()+", ");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.deleteCharAt(builder.length() - 1);
		target.sendMessage(builder.toString());
	}
	
	public void sendPartyMessage(PartyPlayer sender, String msg) {
		PartyChatEvent event = new PartyChatEvent(this, sender, msg);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			if(event.getCancelMessage() != null && !event.getCancelMessage().isEmpty())
				sender.sendMessage(event.getCancelMessage());
			return;
		}
		members.forEach(member -> {
			member.sendMessage("§8[§dPartyChat§8] §r"+PartyManager.get().getPartyPlayerSignature(sender)
					+" §b"+sender.getName()+"§7: §7§o"+event.getMsg());
		});
		Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] [PartyChat - "+leader.getName()+"] "+sender.getName()+": "+msg);
	}
	
	public void broadcastMessage(String msg) {
		members.forEach(member -> {
			member.sendMessage("§8[§d"+Config.get().getPrefix()+"§8] §b"+msg);
		});
		Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] [PartyChat - "+leader.getName()+"] PartyBroadcast: "+msg);
	}
	
}
