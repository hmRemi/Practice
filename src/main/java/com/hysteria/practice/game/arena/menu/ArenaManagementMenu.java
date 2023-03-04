package com.hysteria.practice.game.arena.menu;

import com.google.common.collect.Maps;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import com.hysteria.practice.game.arena.Arena;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ArenaManagementMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return "&c&lArena Management";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = Maps.newHashMap();
		for (Arena arena : Arena.getArenas()) {
			buttons.put(buttons.size(), new SelectArenaButton(arena));
		}
		return buttons;
	}

	@AllArgsConstructor
	private static class SelectArenaButton extends Button {

		private final Arena arena;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(arena.getDisplayIcon())
					.name("&c" + arena.getName())
					.lore("")
					.lore("&cArena Information:")
					.lore(" &c↣ &fIs Setup: &c" + arena.isSetup())
					.lore(" &c↣ &fActive: &c" + arena.isActive())
					.lore(" &c↣ &fType: &c" + arena.getType().toString())
					.lore("")
					.lore("&c&lLEFT-CLICK &cto teleport to arena.")
					.lore("&c&lRIGHT-CLICK &cto see arena status.")
					.lore("&c&lMIDDLE-CLICK &cto delete arena.")
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			// Set closed by menu
			Menu.currentlyOpenedMenus.get(player.getName()).setClosedByMenu(true);

			// Force close inventory
			player.closeInventory();

			switch (clickType) {
				case MIDDLE:
					player.performCommand("arena delete " + arena.getName());
					break;
				case LEFT:
					player.performCommand("arena teleport " + arena.getName());
					break;
				case RIGHT:
					player.performCommand("arena status " + arena.getName());
					break;
			}
		}
	}
}
