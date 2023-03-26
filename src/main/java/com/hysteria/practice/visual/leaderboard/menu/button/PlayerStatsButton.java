package com.hysteria.practice.visual.leaderboard.menu.button;

import com.google.common.collect.Lists;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.visual.leaderboard.menu.StatisticsMenu;

import java.util.ArrayList;

public class PlayerStatsButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        ArrayList<String> lore = Lists.newArrayList();
        lore.add(CC.translate("&7&m-------------------"));
        lore.add(CC.translate("&fView your stats"));
        lore.add(CC.translate("&7&m-------------------"));
        return new ItemBuilder(Material.valueOf(String.valueOf(Material.LEASH))).lore(lore).name(CC.translate(HyPractice.get().getLeaderboardConfig().getString("INVENTORY.YOUR_STATS.TITLE"))).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        new StatisticsMenu(player).openMenu(player);
    }
}

