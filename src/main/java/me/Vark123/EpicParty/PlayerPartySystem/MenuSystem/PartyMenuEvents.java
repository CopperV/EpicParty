package me.Vark123.EpicParty.PlayerPartySystem.MenuSystem;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import lombok.Getter;
import me.Vark123.EpicParty.Config;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;
import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;

@Getter
public final class PartyMenuEvents {

	private static final PartyMenuEvents inst = new PartyMenuEvents();
	
	private final EventCreator<InventoryClickEvent> defaultMenuClickEvent;
	private final EventCreator<InventoryClickEvent> invitationsMenuClickEvent;
	private final EventCreator<InventoryClickEvent> partyInfoMenuClickEvent;
	
	private PartyMenuEvents() {
		this.defaultMenuClickEvent = defaultMenuClickEventCreator();
		this.invitationsMenuClickEvent = invitationsMenuClickEventCreator();
		this.partyInfoMenuClickEvent = partyInfoMenuClickEventCreator();
	}
	
	public static final PartyMenuEvents get() {
		return inst;
	}
	
	private EventCreator<InventoryClickEvent> defaultMenuClickEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			ItemStack it = e.getCurrentItem();
			if(it == null)
				return;
			
			Player p = (Player) e.getWhoClicked();
			if(it.equals(PartyMenuManager.get().getInvitations())) {
				PartyMenuManager.get().openInvitationsMenu(p);
				return;
			}
			
			if(!it.equals(PartyMenuManager.get().getPartyInfo()))
				return;
			
			PartyPlayer pp = PlayerManager.get().getPartyPlayer(p).get();
			if(pp.getParty().isEmpty()) {
				p.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie jestes w zadnej druzynie! Nie ma tutaj zadnych informacji");
				p.closeInventory();
				return;
			}
			
			PartyMenuManager.get().openPartyInfoMenu(p);
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryClickEvent> invitationsMenuClickEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			ItemStack it = e.getCurrentItem();
			if(it == null)
				return;
			
			if(it.equals(PartyMenuManager.get().getBack())) {
				PartyMenuManager.get().openDefaultMenu((Player) e.getWhoClicked());
				return;
			}
			
			NBTItem nbt = new NBTItem(it);
			if(!nbt.hasTag("epicparty.command"))
				return;
			
			String cmd = nbt.getString("epicparty.command");
			Bukkit.dispatchCommand(e.getWhoClicked(), cmd);
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryClickEvent> partyInfoMenuClickEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			ItemStack it = e.getCurrentItem();
			if(it == null)
				return;
			
			if(!it.equals(PartyMenuManager.get().getBack()))
				return;
			
			PartyMenuManager.get().openDefaultMenu((Player) e.getWhoClicked());
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
}
