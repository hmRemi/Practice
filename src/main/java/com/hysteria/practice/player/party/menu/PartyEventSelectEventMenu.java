package com.hysteria.practice.player.party.menu;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.party.enums.PartyEvent;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
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
		return HyPractice.get().getMenuConfig().getString("PARTY-MENU.TITLE");
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();
		ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMenuConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMenuConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
		this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);

		buttons.put(HyPractice.get().getMenuConfig().getInteger("PARTY-MENU.TYPES.FFA.SLOT"), new SelectEventButton(PartyEvent.FFA));
		buttons.put(HyPractice.get().getMenuConfig().getInteger("PARTY-MENU.TYPES.SPLIT.SLOT"), new SelectEventButton(PartyEvent.SPLIT));
		buttons.put(HyPractice.get().getMenuConfig().getInteger("PARTY-MENU.TYPES.REDROVER.SLOT"), new SelectEventButton(PartyEvent.REDROVER));
		return buttons;
	}

	@Override
	public int getSize() {
		return 3 * 9;
	}

	@AllArgsConstructor
	private static class SelectEventButton extends Button {

		private final PartyEvent partyEvent;

		@Override
		public ItemStack getButtonItem(Player player) {
			if(partyEvent == PartyEvent.FFA) {
				return new ItemBuilder(Material.QUARTZ)
					.name(HyPractice.get().getMenuConfig().getString("PARTY-MENU.TYPES.FFA.NAME"))
					.lore(HyPractice.get().getMenuConfig().getStringList("PARTY-MENU.TYPES.FFA.LORE"))
					.build();
			} else if (partyEvent == PartyEvent.SPLIT) {
				return new ItemBuilder(Material.REDSTONE)
						.name(HyPractice.get().getMenuConfig().getString("PARTY-MENU.TYPES.SPLIT.NAME"))
						.lore(HyPractice.get().getMenuConfig().getStringList("PARTY-MENU.TYPES.SPLIT.LORE"))
						.build();
			} else {
				return new ItemBuilder(Material.DIAMOND_AXE)
						.name(HyPractice.get().getMenuConfig().getString("PARTY-MENU.TYPES.REDROVER.NAME"))
						.lore(HyPractice.get().getMenuConfig().getStringList("PARTY-MENU.TYPES.REDROVER.LORE"))
						.build();
			}
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
					player.sendMessage(CC.translate("&7All players must be in spawn to start a party"));
					player.closeInventory();
					return;
				}
			}

			if(partyEvent == PartyEvent.REDROVER) return;

			new PartyEventSelectKitMenu(partyEvent).openMenu(player);
		}
	}
}
