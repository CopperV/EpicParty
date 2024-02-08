package me.Vark123.EpicParty.PlayerPartySystem.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.Runes.PoteznaRunaDomisia;
import me.Vark123.EpicRPG.RuneSystem.Runes.PoteznaRunaDomisia_M;
import me.Vark123.EpicRPG.RuneSystem.Runes.ZadzaKrwi;
import me.Vark123.EpicRPG.RuneSystem.Runes.Events.AllyRangedRuneUseEvent;

public class AllyRuneUseListener implements Listener {

	@EventHandler
	public void onUse(AllyRangedRuneUseEvent e) {
		Player caster = e.getCaster();
		
		ARune rune = e.getRune();
		if(!(rune instanceof PoteznaRunaDomisia 
				|| rune instanceof PoteznaRunaDomisia_M 
				|| rune instanceof ZadzaKrwi))
			return;
		
		PlayerManager.get().getPartyPlayer(caster).ifPresent(pp -> {
			pp.getParty().ifPresent(party -> {
				party.getMembers().stream()
					.filter(_pp -> !_pp.equals(pp))
					.map(_pp -> _pp.getPlayer())
					.filter(p -> !e.getAffectedPlayers().contains(p))
					.filter(tmp -> {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(tmp.getLocation()));
						State flag = set.queryValue(null, Flags.PVP);
						if(flag != null && flag.equals(State.ALLOW)
								&& !tmp.getWorld().getName().toLowerCase().contains("dungeon"))
							return false;
						return true;
					})
					.forEach(e.getAffectedPlayers()::add);
			});
		});
	}
	
}
