package me.Vark123.EpicParty.PlayerPartySystem;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.apache.commons.lang3.mutable.MutableObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicParty.Config;
import me.Vark123.EpicParty.Main;
import me.Vark123.EpicParty.PlayerPartySystem.Events.PartyInviteEvent;
import me.Vark123.EpicParty.Tools.Pair;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

@Getter
//@EqualsAndHashCode
public class PartyPlayer {

	private Player player;
	@Setter
	private Party party;
	
	private Map<BukkitTask, Pair<PartyPlayer, Party>> partyInvitations;
	
	public PartyPlayer(Player player) {
		this.player = player;
		this.partyInvitations = new ConcurrentHashMap<>();
	}
	
	public Optional<Party> getParty() {
		return Optional.ofNullable(party);
	}
	
	public String getName() {
		return player.getName();
	}
	
	public void sendMessage(String msg) {
		player.sendMessage(msg);
	}
	
	public void sendJSONMessage(TextComponent msg) {
		player.spigot().sendMessage(ChatMessageType.CHAT, msg);
	}
	
	public void cancelInvitation(BukkitTask task) {
		if(!partyInvitations.containsKey(task))
			return;
		task.cancel();
		partyInvitations.remove(task);
	}
	
	public void sendInvitation(PartyPlayer sender, Party party) {
		PartyInviteEvent event = new PartyInviteEvent(party, sender, this);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dNie mozna zaprosic §7§o"+player.getName()+" §ddo druzyny");
			if(event.getCancelMessage() != null && !event.getCancelMessage().isEmpty())
				sender.sendMessage("§dPowod: §7§o"+event.getCancelMessage());
			return;
		}
		Pair<PartyPlayer, Party> pair = new Pair<>(sender, party);
		MutableObject<BukkitTask> task = new MutableObject<>();
		task.setValue(new BukkitRunnable() {
			@Override
			public void run() {
				if(isCancelled())
					return;
				partyInvitations.remove(task.getValue());
			}
		}.runTaskLaterAsynchronously(Main.getInst(), 20*Config.get().getInvitationDuration()));
		partyInvitations.put(task.getValue(), pair);
		
		TextComponent comp = new TextComponent("§7["+Config.get().getPrefix()+"§7] §dOtrzymales zaproszenie do druzyny §7§o"+sender.getName()+"§d. Dolacz uzywajac komendy ");
		TextComponent click = new TextComponent("§7§o/party dolacz "+sender.getName());
		click.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/epicparty join "+sender.getName()));
		comp.addExtra(click);
		sendJSONMessage(comp);
		sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dZaprosiles §7§o"+player.getName()+" §ddo swojej druzyny!");
		
		Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "+sender.getName()+" send party invitation to "+getName());
	}
	
}
