package com.hysteria.practice.visual.leaderboard.menu;

import com.google.common.collect.Maps;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import org.bukkit.Material;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.meta.ProfileKitData;
import com.hysteria.practice.utilities.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.hysteria.practice.visual.scoreboard.BoardAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticsMenu extends Menu {

    private final OfflinePlayer target;

    public StatisticsMenu(OfflinePlayer target) {
        this.target = target;
    }

    @Override
    public String getTitle(Player player) {
        return HyPractice.get().getMenuConfig().getString("STATS_MENU.TITLE")
            .replace("{player}", this.target.getName());
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        int pos = 0;
        /*for (Kit kit : Kit.getKits()) {
            if (kit.getGameRules().isRanked()) buttons.put(queue.getKit().getSlot(), new KitsItems(kit));
        }*/

        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMenuConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMenuConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();

        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);
        HyPractice.get().getKitRepository().getKits().stream()
                .filter(Kit::isEnabled)
                .filter(kit -> kit.getGameRules().isRanked())
                .filter(kit -> kit.getDisplayIcon() != null)
                .forEach(kit -> {

            buttons.put(kit.getSlot(), new KitsItems(kit));
        });

        return buttons;
    }

    @AllArgsConstructor
    private class KitsItems extends Button {
        Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile data = Profile.get(target.getUniqueId());
            ProfileKitData kitData = data.getKitData().get(kit);
            List<String> lore = new ArrayList<>();
            for (String s : HyPractice.get().getMenuConfig().getStringList("STATS_MENU.DESCRIPTION")) {
                lore.add(s.replace("{bars}", CC.MENU_BAR)
                        .replace("{elo}", String.valueOf(kitData.getElo()))
                        .replace("{wins}", String.valueOf(kitData.getWon()))
                        .replace("{division}", String.valueOf(BoardAdapter.getDivision(player)))
                        .replace("{losses}", String.valueOf(kitData.getLost())));
            }
            return new ItemBuilder(kit.getDisplayIcon())
                    .name(HyPractice.get().getMenuConfig().getString("STATS_MENU.ITEM_NAME").replace("{kit}", kit.getDisplayName()))
                    .lore(lore)
                    .build();
        }
    }
}
