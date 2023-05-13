package com.hysteria.practice.player.profile.meta.option.button;
/* 
   Made by hypractice Development Team
   Created on 05.11.2021
*/

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.cosmetics.impl.killeffects.menu.KillEffectsMenu;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.shop.menu.ShopMenu;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ShopOptionsButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {

        return new ItemBuilder(Material.MINECART)
                .name(HyPractice.get().getMenuConfig().getString("COSMETICS.SHOP.ITEM-NAME"))
                .lore(HyPractice.get().getMenuConfig().getStringList("COSMETICS.SHOP.LORE"))
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.get(player.getUniqueId());
        new ShopMenu().openMenu(player);
    }
}
