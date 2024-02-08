package me.Vark123.EpicParty.PlayerPartySystem.Listeners;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;

public class PlayerPvPProtectionListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(damager instanceof AbstractArrow)
			damager = (Entity) ((AbstractArrow) damager).getShooter();
		Entity victim = e.getEntity();
		if(!(damager instanceof Player)
				|| !(victim instanceof Player))
			return;
		
		Player p1 = (Player) damager;
		Player p2 = (Player) victim;
		PlayerManager.get().getPartyPlayer(p1).ifPresent(pp1 -> {
			pp1.getParty().ifPresent(party -> {
				PlayerManager.get().getPartyPlayer(p2).ifPresent(pp2 -> {
					if(party.getMembers().contains(pp2))
						e.setCancelled(true);
				});
			});
		});
	}
	
}
