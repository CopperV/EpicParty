package me.Vark123.EpicParty.PlayerPartySystem.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Vark123.EpicParty.PlayerPartySystem.PartyManager;
import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;

public class PlayerQuitListener implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		playerCleaner(e.getPlayer());
	}
	
	public void onKick(PlayerKickEvent e) {
		playerCleaner(e.getPlayer());
	}
	
	private void playerCleaner(Player p) {
		PlayerManager.get().unregisterPlayer(p).ifPresent(pp -> {
			pp.getParty().ifPresent(party -> {
				PartyManager.get().leaveParty(party, pp);
			});
		});
	}
	
}
