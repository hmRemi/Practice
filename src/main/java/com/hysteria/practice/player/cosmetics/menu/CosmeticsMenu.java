package com.hysteria.practice.player.cosmetics.menu;
/* 
   Made by hypractice Development Team
   Created on 30.11.2021
*/

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.profile.meta.option.button.DeathAnimationsOptionsButton;
import com.hysteria.practice.player.profile.meta.option.button.ShopOptionsButton;
import com.hysteria.practice.player.profile.meta.option.button.TagsOptionsButton;
import com.hysteria.practice.player.profile.meta.option.button.TrailsOptionsButton;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CosmeticsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return CC.translate(HyPractice.get().getMenuConfig().getString("COSMETICS.TITLE"));
    }

    {
        setPlaceholder(true);
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMenuConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMenuConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);

        buttons.put(10, new TagsOptionsButton());
        buttons.put(11, new DeathAnimationsOptionsButton());
        buttons.put(16, new ShopOptionsButton());

        return buttons;
    }
}
