package me.Vark123.EpicParty;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;

@Getter
public class Main extends JavaPlugin {

	@Getter
	private static Main inst;
	
	private EpicPartyAPI api;
	private InventoryManager inventoryManager;
	
	@Override
	public void onEnable() {
		inst = this;
		
		inventoryManager = new InventoryManager(inst);
		inventoryManager.invoke();
		
		api = EpicPartyAPI.get();
		
		CommandManager.setExecutors();
		ListenerManager.registerListeners();
		FileManager.init();
	}

	@Override
	public void onDisable() {
		
	}

}
