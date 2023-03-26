package com.hysteria.practice.player.profile.meta.option.button;
/* 
   Made by cpractice Development Team
   Created on 05.11.2021
*/

import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.player.profile.Profile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TrailsOptionsButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("&eClick to open Trails Menu!");

        return new ItemBuilder(Material.BOW)
                .name("&c&lTrail Effects")
                .lore(lore)
                .build();
    }
    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.get(player.getUniqueId());
    }
}
