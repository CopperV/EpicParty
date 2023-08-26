package me.Vark123.EpicParty.PlayerPartySystem.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicParty.Config;
import me.Vark123.EpicParty.Main;
import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;

public class PlayerPartyChatSendListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.isCancelled())
			return;
		
		String msg = e.getMessage();
		if(msg.contains("${")) {
			e.setCancelled(true);
			return;
		}
		
		if(msg.charAt(0) != Config.get().getPartyChatPrefix())
			return;
		
		e.setCancelled(true);
		
		Player p = e.getPlayer();
		PlayerManager.get().getPartyPlayer(p).ifPresentOrElse(pp -> {
			pp.getParty().ifPresentOrElse(party -> {
				StringBuilder builder = new StringBuilder(msg);
				builder.deleteCharAt(0);
				if(builder.isEmpty()) {
					p.sendMessage("§7["+Config.get().getPrefix()+"§7] §dNie mozesz wyslac pustej wiadomosci na czat druzynowy!");
					return;
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						party.sendPartyMessage(pp, builder.toString());
					}
				}.runTask(Main.getInst());
			}, () -> {
				p.sendMessage("§7["+Config.get().getPrefix()+"§7] §dNie jestes w zadnej druzynie! Nie mozesz wysylac wiadomosci na czat druzynowy!");
			});
		}, () -> {
			p.sendMessage("§7["+Config.get().getPrefix()+"§7] §dBLAD! §dNie jestes zapisany! Zglos blad administratorowi!");
		});
	}

}
