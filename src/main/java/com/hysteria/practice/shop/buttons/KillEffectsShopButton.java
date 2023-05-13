package com.hysteria.practice.shop.buttons;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.shop.impl.killeffects.KillEffectsShopMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KillEffectsShopButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Profile.get(player.getUniqueId());

        List<String> lore = new ArrayList<>();
        for (String text : HyPractice.get().getMenuConfig().getStringList("COIN-SHOP.KILL-EFFECTS.LORE")) {
            lore.add(text.replace("{coins}", profile.getCoins() + ""));
        }

        return new ItemBuilder(Material.REDSTONE)
                .name(HyPractice.get().getMenuConfig().getString("COIN-SHOP.KILL-EFFECTS.ITEM-NAME"))
                .lore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        new KillEffectsShopMenu().openMenu(player);
    }
}

