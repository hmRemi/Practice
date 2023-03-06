package com.hysteria.practice.match.duel.menu;

import com.google.common.collect.Maps;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import org.bukkit.Material;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
public class DuelSelectKitMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return HyPractice.get().getLangConfig().getString("DUEL.SELECT.KIT_MENU.TITLE");
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = Maps.newHashMap();
		ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMainConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMainConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
		Profile profile = Profile.get(player.getUniqueId());

		this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);
		for (Kit kit : Kit.getKits()) {
			if (kit.isEnabled()) {
				buttons.put(kit.getSlot(), new SelectKitButton(kit));
			}

			if(profile.getParty() != null) {
				buttons.put(28, new SelectKitButton(Kit.getByName("HCF")));
			}
		}

		return buttons;
	}


	@Override
	public int getSize() {
		return 5/*HyPractice.get().getMainConfig().getInteger("QUEUES.SIZE")*/ * 9;
	}

	@Override
	public void onClose(Player player) {
		if (!isClosedByMenu()) {
			Profile profile = Profile.get(player.getUniqueId());
			profile.setDuelProcedure(null);
		}
	}

	@AllArgsConstructor
	private static class SelectKitButton extends Button {

		private final Kit kit;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(kit.getDisplayIcon())
					.name(HyPractice.get().getLangConfig().getString("DUEL.SELECT.KIT_MENU.NAME").replace("{name}", kit.getDisplayName()))
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getDuelProcedure() == null) {
				player.sendMessage(CC.RED + "Could not find duel procedure.");
				return;
			}

			if(!player.hasPermission("cpractice.duel.map")) {
				// Update duel procedure
				profile.getDuelProcedure().setKit(kit);

				// Update and request the procedure
				profile.getDuelProcedure().setArena(Arena.getRandomArena(profile.getDuelProcedure().getKit()));
				profile.getDuelProcedure().send();

				// Set closed by menu
				Menu.currentlyOpenedMenus.get(player.getName()).setClosedByMenu(true);

				// Force close inventory
				player.closeInventory();
			} else {

				// Update duel procedure
				profile.getDuelProcedure().setKit(kit);

				// Set closed by menu
				Menu.currentlyOpenedMenus.get(player.getName()).setClosedByMenu(true);

				// Force close inventory
				player.closeInventory();

				// Open arena selection menu
				new DuelSelectArenaMenu().openMenu(player);
			}
		}
	}
}
