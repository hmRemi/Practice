package com.hysteria.practice.player.queue.menus;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.match.Match;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.queue.Queue;
import com.hysteria.practice.player.queue.menus.buttons.FFAButton;
import com.hysteria.practice.player.queue.menus.buttons.RankedButton;
import com.hysteria.practice.player.queue.menus.buttons.UnrankedButton;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.file.type.BasicConfigurationFile;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class QueueSelectKitMenu extends Menu {

	private final boolean ranked;

	@Override
	public String getTitle(Player player) {
		if (ranked) return HyPractice.get().getMainConfig().getString("QUEUE.RANKED.INVENTORY_TITLE");
		else return HyPractice.get().getMainConfig().getString("QUEUE.UNRANKED.INVENTORY_TITLE");
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		HashMap<Integer, Button> buttons = new HashMap<>();

		ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMainConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMainConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
		this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);

		buttons.put(48, new FFAButton());

		for (Queue queue : Queue.getQueues()) {
			if(queue.isRanked() == ranked) {
				buttons.put(50, new UnrankedButton());
			} else {
				buttons.put(50, new RankedButton());
			}

			if (queue.isRanked() == ranked) buttons.put(queue.getKit().getSlot(), new SelectKitButton(queue));
		}
		return buttons;
	}

	@Override
	public int getSize() {
		return HyPractice.get().getMainConfig().getInteger("QUEUES.SIZE") * 9;
	}

	@AllArgsConstructor
	private static class SelectKitButton extends Button {

		private final Queue queue;

		@Override
		public ItemStack getButtonItem(Player player) {
			List<String> lore = new ArrayList<>();

			Profile profile = Profile.get(player.getUniqueId());
			int pos = 0;

			BasicConfigurationFile config = HyPractice.get().getMainConfig();
			config.getStringList("QUEUE." + (queue.isRanked() ? "RANKED" : "UNRANKED") + ".LORE").forEach(s ->
					lore.add(s.replace("{bars}", CC.SB_BAR)
							.replace("{in-fight}", String.valueOf(Match.getInFightsCount(queue)))
							.replace("{winstreak}", String.valueOf(profile.getKitData().get(queue.getKit()).getKillstreak()))
							.replace("{elo}", String.valueOf(profile.getKitData().get(queue.getKit()).getElo()))
							.replace("{in-queue}", String.valueOf(queue.getPlayers().size()))));

			ChatColor color = ChatColor.valueOf(config.getString("QUEUE." + (queue.isRanked() ? "RANKED" : "UNRANKED") + ".NAME_COLOR"));
			boolean amount = config.getBoolean("QUEUE.AMOUNT_PER_FIGHTS");

			return new ItemBuilder(queue.getKit().getDisplayIcon())
					.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.addItemFlag(ItemFlag.HIDE_ENCHANTS)
					.addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
					.name(CC.translate(color + queue.getKit().getDisplayName()))
					.amount(amount ? Match.getInFightsCount(queue) + 1 : 1)
					.lore(lore)
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Profile profile = Profile.get(player.getUniqueId());

			if (profile.isBusy()) {
				player.sendMessage(CC.RED + "You cannot queue right now.");
				return;
			}

			player.closeInventory();
			queue.addPlayer(player, queue.isRanked() ? profile.getKitData().get(queue.getKit()).getElo() : 0);
		}
	}

	@AllArgsConstructor
	private static class Painting extends Button {
		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.STAINED_GLASS)
					.name(CC.translate(""))
					.build();
		}
	}
}
