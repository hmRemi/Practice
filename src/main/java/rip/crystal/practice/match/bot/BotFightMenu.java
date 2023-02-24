package rip.crystal.practice.match.bot;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.arena.menu.ArenaManagementMenu;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.impl.BasicTeamMatch;
import rip.crystal.practice.match.impl.BotMatch;
import rip.crystal.practice.match.mongo.MatchInfo;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.file.language.Lang;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

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
        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(cPractice.get().getMainConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(cPractice.get().getMainConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
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