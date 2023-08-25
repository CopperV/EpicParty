package me.Vark123.EpicParty;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;

@Getter
public final class Config {

	private static final Config inst = new Config();
	
	private String prefix;
	
	private String leaderSignature;
	private String memberSignature;
	
	private Config() {
		
	}
	
	public static final Config get() {
		return inst;
	}
	
	public void load() {
		YamlConfiguration fYml = FileManager.getConfig();
		
		this.prefix = ChatColor.translateAlternateColorCodes('&', fYml.getString("prefix"));
		this.leaderSignature = ChatColor.translateAlternateColorCodes('&', fYml.getString("party.leader-signature"));
		this.memberSignature = ChatColor.translateAlternateColorCodes('&', fYml.getString("party.member-signature"));
	}
	
}
