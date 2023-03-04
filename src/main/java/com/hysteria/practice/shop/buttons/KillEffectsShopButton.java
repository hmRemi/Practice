package com.hysteria.practice.shop.buttons;

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
        lore.add("&8&m--------------------------------------");
        lore.add("&7Your coins: " + profile.getCoins());
        lore.add("");
        lore.add("&7&oClick here to purchase Kill Effects!");
        lore.add("&8&m--------------------------------------");


        return new ItemBuilder(Material.REDSTONE)
                .name("&c&lKill Effects")
                .lore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        new KillEffectsShopMenu().openMenu(player);
    }
}

