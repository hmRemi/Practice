package com.hysteria.practice.player.profile.meta.option.button;
/* 
   Made by hypractice Development Team
   Created on 05.11.2021
*/

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import meth.crystal.aspirin.menus.tags.TagsMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class TagsOptionsButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {

        return new ItemBuilder(Material.NAME_TAG)
                .name(HyPractice.get().getMenuConfig().getString("COSMETICS.TAGS.ITEM-NAME"))
                .lore(HyPractice.get().getMenuConfig().getStringList("COSMETICS.TAGS.LORE"))
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        new TagsMenu().open(player);
    }
}
