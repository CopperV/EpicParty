package me.Vark123.EpicParty.PlayerPartySystem.MenuSystem;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import me.Vark123.EpicParty.Config;
import me.Vark123.EpicParty.EpicPartyAPI;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;
import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;

@Getter
public final class PartyMenuManager {

	private static final PartyMenuManager inst = new PartyMenuManager();
	
	private final String title;
	
	private final ItemStack back;
	
	private final ItemStack playerInfo;
	private final ItemStack invitations;
	private final ItemStack partyInfo;

	private final ItemStack invitation;
	private final ItemStack head;
	
	private PartyMenuManager() {
		title = "§d§lDRUZYNA §r- §7§lINFORMACJE";
		
		playerInfo = new ItemStack(Material.WRITTEN_BOOK);
		playerInfo.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		invitations = new ItemStack(Material.PAPER);{
			ItemMeta im = invitations.getItemMeta();
			im.setDisplayName("§bZaproszenia do druzyn");
			invitations.setItemMeta(im);
		}
		partyInfo = new ItemStack(Material.PLAYER_HEAD);{
			ItemMeta im = partyInfo.getItemMeta();
			im.setDisplayName("§bInformacje o druzynie");
			partyInfo.setItemMeta(im);
		}
		
		invitation = new ItemStack(Material.PAPER);
		head = new ItemStack(Material.PLAYER_HEAD);

		back = new ItemStack(Material.BARRIER);{
			ItemMeta im = back.getItemMeta();
			im.setDisplayName("§cPowrot");
			back.setItemMeta(im);
		}
		
		
	}
	
	public static final PartyMenuManager get() {
		return inst;
	}
	
	public void openDefaultMenu(Player p) {
		Optional<PartyPlayer> oPartyPlayer = PlayerManager.get().getPartyPlayer(p);
		if(oPartyPlayer.isEmpty())
			return;
		RyseInventory.builder()
			.title(title)
			.rows(3)
			.disableUpdateTask()
			.listener(PartyMenuEvents.get().getDefaultMenuClickEvent())
			.provider(getDefaultMenuProvider(p))
			.build(EpicPartyAPI.get().getEpicpartyInst())
			.open(p);
	}
	
	public void openInvitationsMenu(Player p) {
		Optional<PartyPlayer> oPartyPlayer = PlayerManager.get().getPartyPlayer(p);
		if(oPartyPlayer.isEmpty())
			return;
		PartyPlayer pp = oPartyPlayer.get();
		int rows = pp.getPartyInvitations().size()/9 + 2;
		RyseInventory.builder()
			.title(title)
			.rows(rows)
			.disableUpdateTask()
			.listener(PartyMenuEvents.get().getInvitationsMenuClickEvent())
			.provider(getInvitationsMenuProvider(p))
			.build(EpicPartyAPI.get().getEpicpartyInst())
			.open(p);
	}
	
	public void openPartyInfoMenu(Player p) {
		Optional<PartyPlayer> oPartyPlayer = PlayerManager.get().getPartyPlayer(p);
		if(oPartyPlayer.isEmpty())
			return;
		PartyPlayer pp = oPartyPlayer.get();
		Optional<Party> oParty = pp.getParty();
		if(oParty.isEmpty())
			return;
		Party party = oParty.get();
		int rows = party.getMembers().size()/9 + 2;
		RyseInventory.builder()
			.title(title)
			.rows(rows)
			.disableUpdateTask()
			.listener(PartyMenuEvents.get().getPartyInfoMenuClickEvent())
			.provider(getPartyInfoMenuProvider(p))
			.build(EpicPartyAPI.get().getEpicpartyInst())
			.open(p);
	}
	
	private InventoryProvider getDefaultMenuProvider(Player p) {
		PartyPlayer pp = PlayerManager.get().getPartyPlayer(p).get();
		ItemStack pInfo = playerInfo.clone();
		ItemMeta im = pInfo.getItemMeta();
		im.setDisplayName("§dNick: §7§o"+pp.getName());
		pp.getParty().ifPresentOrElse(party -> {
			im.setLore(Arrays.asList(
					"§dParty: §7§o"+party.getLeader().getName(),
					"§dCzlonkowie: §7§l"+party.getMembers().size()));
		}, () -> {
			im.setLore(Arrays.asList("§dParty: §7BRAK"));
		});
		im.addItemFlags(
				ItemFlag.HIDE_ATTRIBUTES,
				ItemFlag.HIDE_DESTROYS,
				ItemFlag.HIDE_DYE,
				ItemFlag.HIDE_ENCHANTS,
				ItemFlag.HIDE_PLACED_ON,
				ItemFlag.HIDE_POTION_EFFECTS,
				ItemFlag.HIDE_UNBREAKABLE);
		pInfo.setItemMeta(im);
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				contents.set(4, pInfo);
				contents.set(11, invitations);
				contents.set(15, partyInfo);
			}
		};
	}
	
	private InventoryProvider getInvitationsMenuProvider(Player p) {
		PartyPlayer pp = PlayerManager.get().getPartyPlayer(p).get();
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				contents.set(0, back);
				MutableInt slot = new MutableInt();
				pp.getPartyInvitations().values().forEach(pair -> {
					ItemStack _invitation = invitation.clone();
					ItemMeta im = _invitation.getItemMeta();
					im.setDisplayName("§dZaproszenie do druzyny §7§o"+pair.getKey().getName());
					_invitation.setItemMeta(im);
					NBTItem nbt = new NBTItem(_invitation);
					nbt.setString("epicparty.command", "epicparty join "+pair.getKey().getName());
					nbt.applyNBT(_invitation);
					contents.set(slot.getAndIncrement()+9, _invitation);
				});
			}
		};
	}
	
	private InventoryProvider getPartyInfoMenuProvider(Player p) {
		PartyPlayer pp = PlayerManager.get().getPartyPlayer(p).get();
		Party party = pp.getParty().get();
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				contents.set(0, back);
				
				Player leader = party.getLeader().getPlayer();
				ItemStack leaderHead = head.clone();
				SkullMeta im = (SkullMeta) leaderHead.getItemMeta();
				String name = "§e"+leader.getName();
				if(leader.equals(p))
					name += " §7[§b§lTY§7]";
				im.setDisplayName(Config.get().getLeaderSignature()+" §r"+name+" §r"+Config.get().getLeaderSignature());
				im.setOwningPlayer(leader);
				leaderHead.setItemMeta(im);
				contents.set(9, leaderHead);
				
				MutableInt slot = new MutableInt(1);
				party.getMembers().stream()
					.filter(pp -> !party.getLeader().equals(pp))
					.forEach(member -> {
						Player pMember = member.getPlayer();
						ItemStack memberHead = head.clone();
						SkullMeta _im = (SkullMeta) memberHead.getItemMeta();
						String _name = "§7"+pMember.getName();
						if(pMember.equals(p))
							_name += " §7[§b§lTY§7]";
						_im.setDisplayName(_name);
						_im.setOwningPlayer(pMember);
						memberHead.setItemMeta(_im);
						contents.set(slot.getAndIncrement()+9, memberHead);
					});
			}
		};
	}
	
}
