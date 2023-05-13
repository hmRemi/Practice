package com.hysteria.practice.visual.leaderboard.menu;

import com.google.common.collect.Maps;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import com.hysteria.practice.visual.leaderboard.menu.button.GlobalStatsButton;
import com.hysteria.practice.visual.leaderboard.menu.button.KitButton;
import com.hysteria.practice.visual.leaderboard.menu.button.PlayerStatsButton;
import com.hysteria.practice.visual.leaderboard.menu.button.StatsButton;
import com.hysteria.practice.game.kit.Kit;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class LeaderBoardMenu extends Menu {

    private final Player target;

    @Override
    public String getTitle(Player player) {
        return HyPractice.get().getLeaderboardConfig().getString("INVENTORY.TITLE");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player){
        Map<Integer, Button> buttons = Maps.newHashMap();

        //buttons.put(2, new StatsButton(target));
        buttons.put(3, new PlayerStatsButton());
        buttons.put(5, new GlobalStatsButton());

        AtomicInteger pos = new AtomicInteger();
        Kit.getKits().stream()
                .filter(Kit::isEnabled)
                .filter(kit -> kit.getGameRules().isRanked())
                .filter(kit -> kit.getDisplayIcon() != null)
                .forEach(kit -> {
            pos.getAndIncrement();

            ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMenuConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMenuConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
            this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);
            buttons.put(kit.getSlot() + 9, new KitButton(kit));
        });

        return buttons;
    }

    @Override
    public int getSize() {
        return HyPractice.get().getMenuConfig().getInteger("STATS_MENU.SIZE") * 9;
    }
}
