package com.hysteria.practice.game.kit.menu;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.game.kit.KitLoadout;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.player.profile.hotbar.Hotbar;
import com.hysteria.practice.player.profile.meta.ProfileKitData;
import com.hysteria.practice.utilities.BukkitReflection;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.PlayerUtil;
import com.hysteria.practice.utilities.TaskUtil;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import com.hysteria.practice.utilities.menu.button.DisplayButton;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class KitEditorMenu extends Menu {

	private static final int[] ITEM_POSITIONS = new int[]{
			20, 21, 22, 23, 24, 25, 26, 29, 30, 31, 32, 33, 34, 35, 38, 39, 40, 41, 42, 43, 44, 47, 48, 49, 50, 51, 52,
			53
	};
	private static final int[] BORDER_POSITIONS = new int[]{ 1, 2, 3, 4, 5, 9, 10, 11, 12, 13, 14, 15, 16, 17, 19, 28, 37, 46 };
	private static final Button BORDER_BUTTON = Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 15, " ");

	private final int index;

	{
		setUpdateAfterClick(false);
	}

	@Override
	public String getTitle(Player player) {
		Profile profile = Profile.get(player.getUniqueId());
		return "&8Editing &8" + profile.getKitEditorData().getSelectedKit().getName();
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		for (int border : BORDER_POSITIONS) {
			buttons.put(border, BORDER_BUTTON);
		}

		buttons.put(0, new CurrentKitButton());
		buttons.put(5, new SaveButton());
		buttons.put(6, new LoadDefaultKitButton());
		buttons.put(7, new ClearInventoryButton());
		buttons.put(8, new CancelButton(index));

		Profile profile = Profile.get(player.getUniqueId());
		Kit kit = profile.getKitEditorData().getSelectedKit();
		KitLoadout kitLoadout = profile.getKitEditorData().getSelectedKitLoadout();

		buttons.put(18, new ArmorDisplayButton(kitLoadout.getArmor()[3]));
		buttons.put(27, new ArmorDisplayButton(kitLoadout.getArmor()[2]));
		buttons.put(36, new ArmorDisplayButton(kitLoadout.getArmor()[1]));
		buttons.put(45, new ArmorDisplayButton(kitLoadout.getArmor()[0]));

		List<ItemStack> items = kit.getEditRules().getEditorItems();

		if (!kit.getEditRules().getEditorItems().isEmpty()) {
			for (int i = 20; i < (kit.getEditRules().getEditorItems().size() + 20); i++) {
				buttons.put(ITEM_POSITIONS[i - 20], new InfiniteItemButton(items.get(i - 20)));
			}
		}

		return buttons;
	}

	@Override
	public void onOpen(Player player) {
		if (!isClosedByMenu()) {
			TaskUtil.run(() -> PlayerUtil.reset(player));

			Profile profile = Profile.get(player.getUniqueId());
			profile.getKitEditorData().setActive(true);

			if (profile.getKitEditorData().getSelectedKit() != null) {
				player.getInventory().setContents(profile.getKitEditorData().getSelectedKitLoadout().getContents());
			}

			player.updateInventory();
		}
	}

	@Override
	public void onClose(Player player) {
		Profile profile = Profile.get(player.getUniqueId());
		profile.getKitEditorData().setActive(false);

		if (profile.getState() != ProfileState.FIGHTING) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Hotbar.giveHotbarItems(player);
				}
			}.runTask(HyPractice.get());
		}
	}

	@AllArgsConstructor
	private class ArmorDisplayButton extends Button {

		private ItemStack itemStack;

		@Override
		public ItemStack getButtonItem(Player player) {
			if (itemStack == null || itemStack.getType() == Material.AIR) {
				return new ItemStack(Material.AIR);
			}

			return new ItemBuilder(itemStack.clone())
					.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.addItemFlag(ItemFlag.HIDE_ENCHANTS)
					.addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
					.name(CC.AQUA + BukkitReflection.getItemStackName(itemStack))
					.lore(CC.WHITE + " This is automatically equipped.")
					.build();
		}

	}

	@AllArgsConstructor
	private class CurrentKitButton extends Button {

		@Override
		public ItemStack getButtonItem(Player player) {
			Profile profile = Profile.get(player.getUniqueId());
			return new ItemBuilder(Material.MINECART)
					.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.addItemFlag(ItemFlag.HIDE_ENCHANTS)
					.addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
					.name("&6Editing: &7" + profile.getKitEditorData().getSelectedKit().getName())
					.build();
		}

	}

	@AllArgsConstructor
	private class ClearInventoryButton extends Button {

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.INK_SACK)
					.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.addItemFlag(ItemFlag.HIDE_ENCHANTS)
					.addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
					.durability(12)
					.name("&bClear Inventory")
					.lore(Arrays.asList(
							" &fThis will clear your inventory",
							" &fso you can start over."
					))
					.build();
		}

		@Override
		public void clicked(Player player, int i, ClickType clickType, int hb) {
			Button.playNeutral(player);
			player.getInventory().setContents(new ItemStack[36]);
			player.updateInventory();
		}

		@Override
		public boolean shouldUpdate(Player player, ClickType clickType) {
			return true;
		}

	}

	@AllArgsConstructor
	private class LoadDefaultKitButton extends Button {

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.INK_SACK)
					.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.addItemFlag(ItemFlag.HIDE_ENCHANTS)
					.addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
					.durability(11)
					.name(CC.YELLOW + "Load Default")
					.lore(Arrays.asList(
							CC.WHITE + " Click this to load the default kit",
							CC.WHITE + " into the kit editing menu."
					))
					.build();
		}

		@Override
		public void clicked(Player player, int i, ClickType clickType, int hb) {
			Button.playNeutral(player);

			Profile profile = Profile.get(player.getUniqueId());

			player.getInventory().setContents(profile.getKitEditorData().getSelectedKit().getKitLoadout().getContents());
			player.updateInventory();
		}

		@Override
		public boolean shouldUpdate(Player player, ClickType clickType) {
			return true;
		}

	}

	@AllArgsConstructor
	private class SaveButton extends Button {

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.INK_SACK)
					.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.addItemFlag(ItemFlag.HIDE_ENCHANTS)
					.addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
					.durability(2)
					.name("&aSave")
					.lore(" &fClick to save your kit.")
					.build();
		}

		@Override
		public void clicked(Player player, int i, ClickType clickType, int hb) {
			Button.playNeutral(player);
			player.closeInventory();

			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getKitEditorData().getSelectedKitLoadout() != null) {
				profile.getKitEditorData().getSelectedKitLoadout().setContents(player.getInventory().getContents());
			}

			Hotbar.giveHotbarItems(player);

			new KitManagementMenu(profile.getKitEditorData().getSelectedKit()).openMenu(player);
		}

	}

	@AllArgsConstructor
	private class CancelButton extends Button {

		private int index;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.INK_SACK)
					.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.addItemFlag(ItemFlag.HIDE_ENCHANTS)
					.addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
					.durability(1)
					.name("&cCancel")
					.lore(Arrays.asList(
							" &fClick this to abort editing your kit,",
							" &fand return to the kit menu."
					))
					.build();
		}

		@Override
		public void clicked(Player player, int i, ClickType clickType, int hb) {
			Button.playNeutral(player);

			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getKitEditorData().getSelectedKit() != null) {
				ProfileKitData kitData = profile.getKitData().get(profile.getKitEditorData().getSelectedKit());
				kitData.replaceKit(index, null);

				new KitManagementMenu(profile.getKitEditorData().getSelectedKit()).openMenu(player);
			}
		}

	}

	private class InfiniteItemButton extends DisplayButton {

		InfiniteItemButton(ItemStack itemStack) {
			super(itemStack, false);
		}

		@Override
		public void clicked(Player player, int slot, ClickType clickType, int hotbar) {
			Inventory inventory = player.getOpenInventory().getTopInventory();
			ItemStack itemStack = inventory.getItem(slot);

			inventory.setItem(slot, itemStack);

			player.setItemOnCursor(itemStack);
			player.updateInventory();
		}

	}

}