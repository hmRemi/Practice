package com.hysteria.practice.player.party.menu;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.match.Match;
import com.hysteria.practice.match.impl.*;
import com.hysteria.practice.match.participant.MatchGamePlayer;
import com.hysteria.practice.player.party.Party;
import com.hysteria.practice.player.party.enums.PartyEvent;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.participant.team.TeamGameParticipant;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import org.bukkit.Material;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.player.profile.participant.alone.GameParticipant;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@AllArgsConstructor
public class PartyEventSelectKitMenu extends Menu {

	private PartyEvent partyEvent;

	@Override
	public String getTitle(Player player) {
		return HyPractice.get().getMenuConfig().getString("PARTY-MENU.KIT-TITLE");
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		Party party = Profile.get(player.getUniqueId()).getParty();

		ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMenuConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMenuConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();

		this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);
		for (Kit kit : HyPractice.get().getKitRepository().getKits()) {
			if ((partyEvent == PartyEvent.SPLIT /*||*/ /*!kit.getGameRules().isBridge() &&*/ /*party.getListOfPlayers().size() <= 8*/)) {
				if (kit.isEnabled()) {
					buttons.put(kit.getSlot(), new SelectKitButton(partyEvent, kit));
				}
				buttons.put(Objects.requireNonNull(HyPractice.get().getKitRepository().getKitByName("HCF")).getSlot(), new SelectKitButton(partyEvent, HyPractice.get().getKitRepository().getKitByName("HCF")));

			} else if ((partyEvent == PartyEvent.FFA)) {
				if (kit.isEnabled() &&
					!kit.getGameRules().isHcftrap() &&
					!kit.getGameRules().isHcf() &&
					!kit.getGameRules().isBattlerush() &&
					!kit.getGameRules().isBridge() &&
					!kit.getGameRules().isBedFight() &&
					!kit.getGameRules().isLives()
				) {
					buttons.put(kit.getSlot(), new SelectKitButton(partyEvent, kit));
				}
			} else {
				buttons.put(kit.getSlot(), new SelectKitButton(partyEvent, kit));
			}
		}
		return buttons;
	}


	@Override
	public int getSize() {
		return 5 * 9;
	}

	@AllArgsConstructor
	private static class SelectKitButton extends Button {

		private PartyEvent partyEvent;
		private Kit kit;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(kit.getDisplayIcon())
					.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.addItemFlag(ItemFlag.HIDE_ENCHANTS)
					.addItemFlag(ItemFlag.HIDE_POTION_EFFECTS)
					.name(HyPractice.get().getMenuConfig().getString("PARTY-MENU.KIT-COLOR") + kit.getDisplayName())
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			Menu.currentlyOpenedMenus.get(player.getName()).setClosedByMenu(true);

			player.closeInventory();

			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getParty() == null) {
				player.sendMessage(CC.translate("&8[" + HyPractice.get().getMenuConfig().getString("PARTY-MENU.KIT-COLOR") + "&lParty&8] &7You are not in a party."));
				return;
			}

			if (profile.getParty().getPlayers().size() <= 1) {
				player.sendMessage(CC.translate("&8[" + HyPractice.get().getMenuConfig().getString("PARTY-MENU.KIT-COLOR") + "&lParty&8] &7You do not have enough players in your party to start an event."));
				return;
			}

			Party party = profile.getParty();
			Arena arena = Arena.getRandomArena(kit);

			if (arena == null) {
				player.sendMessage(CC.translate("&8[" + HyPractice.get().getMenuConfig().getString("PARTY-MENU.KIT-COLOR") + "&lParty&8] &7There are no available arenas."));
				return;
			}

			arena.setActive(true);
			Match match;

			if (partyEvent == PartyEvent.FFA) {
				List<GameParticipant<MatchGamePlayer>> participants = new ArrayList<>();

				for (Player partyPlayer : party.getListOfPlayers()) {
					participants.add(new GameParticipant<>(new MatchGamePlayer(partyPlayer.getUniqueId(), partyPlayer.getName())));
				}

				match = new BasicFreeForAllMatch(null, kit, arena, participants);
			} else {
				Player partyLeader = party.getLeader();
				Player randomLeader = Bukkit.getPlayer(party.getPlayers().get(1));

				MatchGamePlayer leaderA = new MatchGamePlayer(partyLeader.getUniqueId(), partyLeader.getName());
				MatchGamePlayer leaderB = new MatchGamePlayer(randomLeader.getUniqueId(), randomLeader.getName());

				GameParticipant<MatchGamePlayer> participantA = new TeamGameParticipant<>(leaderA);
				GameParticipant<MatchGamePlayer> participantB = new TeamGameParticipant<>(leaderB);

				List<Player> players = new ArrayList<>(party.getListOfPlayers());
				Collections.shuffle(players);

				for (Player otherPlayer : players) {
					if (participantA.containsPlayer(otherPlayer.getUniqueId()) ||
					    participantB.containsPlayer(otherPlayer.getUniqueId())) {
						continue;
					}

					MatchGamePlayer gamePlayer = new MatchGamePlayer(otherPlayer.getUniqueId(), otherPlayer.getName());

					if (participantA.getPlayers().size() > participantB.getPlayers().size()) {
						participantB.getPlayers().add(gamePlayer);
					} else {
						participantA.getPlayers().add(gamePlayer);
					}
				}

				// Create match
				if (kit.getGameRules().isBridge()) {
					match = new BasicTeamRoundMatch(null, kit, arena, false, participantA, participantB, 3);
				} else if (kit.getGameRules().isBedFight()) {
					match = new BasicTeamBedFight(null, kit, arena, false, participantA, participantB);
				} else if (kit.getGameRules().isLives()) {
					match = new BasicTeamLivesFight(null, kit, arena, false, participantA, participantB);
				} else {
					match = new BasicTeamMatch(null, kit, arena, false, participantA, participantB);
				}
			}
			// Start match
			match.start();
		}
	}
}
