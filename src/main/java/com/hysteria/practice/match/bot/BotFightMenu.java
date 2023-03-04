package com.hysteria.practice.match.bot;

import com.google.common.collect.Maps;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.match.participant.MatchGamePlayer;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.match.Match;
import com.hysteria.practice.match.impl.BotMatch;
import com.hysteria.practice.player.profile.participant.alone.GameParticipant;

import java.util.Map;

public class BotFightMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&4Select Difficulty";
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMainConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMainConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);
        for (Bot.BotDifficulty difficulty : Bot.BotDifficulty.values()) {
            buttons.put(difficulty.getSlot(), new DifficultyButton(difficulty));
        }
        return buttons;
    }

    @RequiredArgsConstructor
    private static class DifficultyButton extends Button {

        private final Bot.BotDifficulty difficulty;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                    .lore("")
                    .lore("&4Difficulty&7:&7 " + difficulty)
                    .lore("&4Reach&7:&7 " + difficulty.getReach())
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            handleTrainMapClick(player, difficulty);
        }
    }

    private static void handleTrainMapClick(final Player player, final Bot.BotDifficulty difficulty) {
        player.closeInventory();
        Match match;
        Kit kit = Kit.getByName("NoDebuff");
        Arena arena = Arena.getRandomArena(kit);

        MatchGamePlayer playerA = new MatchGamePlayer(player.getUniqueId(), Profile.get(player.getUniqueId()).getColor() + player.getName());
        GameParticipant<MatchGamePlayer> participantA = new GameParticipant<>(playerA);

        match = new BotMatch(null, kit, arena, false, difficulty, participantA);
        match.start();
    }

}