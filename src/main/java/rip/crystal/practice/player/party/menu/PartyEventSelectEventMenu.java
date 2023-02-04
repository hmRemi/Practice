package rip.crystal.practice.player.party.menu;

import rip.crystal.practice.player.party.enums.PartyEvent;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PartyEventSelectEventMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return "&4Select an event";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();
		buttons.put(3, new SelectEventButton(PartyEvent.FFA));
		buttons.put(5, new SelectEventButton(PartyEvent.SPLIT));
		return buttons;
	}

	@AllArgsConstructor
	private static class SelectEventButton extends Button {

		private final PartyEvent partyEvent;

		@Override
		public ItemStack getButtonItem(Player player) {
			if(partyEvent == PartyEvent.FFA) {
				return new ItemBuilder(Material.QUARTZ)
					.name("&4"+ partyEvent.getName())
					.lore(CC.SB_BAR)
					.lore("&7A fight all against all")
					.lore("&7And the last one to stay alive wins")
					.lore(CC.SB_BAR)
					.build();
			}

			return new ItemBuilder(Material.REDSTONE)
					.name("&4" + partyEvent.getName())
					.lore(CC.SB_BAR)
					.lore("&7The party is divided into two")
					.lore("&7Teams and they fight")
					.lore(CC.SB_BAR)
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getParty() == null) {
				player.sendMessage(CC.RED + "You are not in a party.");
				return;
			}

			for (Player member : profile.getParty().getListOfPlayers()) {
				Profile profileMember = Profile.get(member.getUniqueId());
				if (profileMember.getState() != ProfileState.LOBBY) {
					player.sendMessage(CC.translate("&4All player of the party have to be in spawn for you to start another event"));
					player.closeInventory();
					return;
				}
			}

			new PartyEventSelectKitMenu(partyEvent).openMenu(player);
		}
	}
}
