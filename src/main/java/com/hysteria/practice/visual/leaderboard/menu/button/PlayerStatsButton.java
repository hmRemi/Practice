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
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class PlayerStatsButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        ArrayList<String> lore = Lists.newArrayList();
        lore.add(CC.translate("&8&m----------------------"));
        lore.add(CC.translate(" &fYour own wins, losses,"));
        lore.add(CC.translate(" &fand ranked statistics."));
        lore.add(CC.translate(""));
        lore.add(CC.translate("&aClick to view."));
        lore.add(CC.translate("&8&m----------------------"));

        ItemStack item = new ItemBuilder(Material.SKULL_ITEM)
                .durability(3)
                .name(CC.translate(HyPractice.get().getLeaderboardConfig().getString("INVENTORY.YOUR_STATS.TITLE")))
                .lore(lore)
                .build();

        SkullMeta itemMeta = (SkullMeta)item.getItemMeta();
        itemMeta.setOwner(player.getName());
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        new StatisticsMenu(player).openMenu(player);
    }
}

