package com.hysteria.practice.game.kit.menu;

import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.game.kit.Kit;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class KitEditorSelectKitMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return HyPractice.get().getKitEditorConfig().getString("KITEDITOR.SELECT-KIT.TITLE");
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMenuConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMenuConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
		this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);

		HyPractice.get().getKitRepository().getKits().forEach(kit -> {
			if (kit.isEnabled()) {
				buttons.put(kit.getSlot(), new KitDisplayButton(kit));
			}
		});

		return buttons;
	}

	@Override
	public int getSize() {
		return 5/*HyPractice.get().getMainConfig().getInteger("QUEUES.SIZE")*/ * 9;
	}

	@AllArgsConstructor
	private static class KitDisplayButton extends Button {

		private Kit kit;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(kit.getDisplayIcon())
					.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.addItemFlag(ItemFlag.HIDE_ENCHANTS)
					.addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
					.name(HyPractice.get().getKitEditorConfig().getString("KITEDITOR.SELECT-KIT.NAMECOLOR") + kit.getDisplayName())
					.lore(HyPractice.get().getKitEditorConfig().getStringList("KITEDITOR.SELECT-KIT.LORE"))
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			player.closeInventory();
			Profile profile = Profile.get(player.getUniqueId());
			profile.getKitEditorData().setSelectedKit(kit);

			new KitManagementMenu(kit).openMenu(player);
		}

	}
}
