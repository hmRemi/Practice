package com.hysteria.practice.player.party.menu.manage.buttons;

import com.hysteria.practice.player.party.enums.PartyPrivacy;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/24/2023
 */
public class PartyPrivacyButton extends Button {

    @Override
    public boolean shouldUpdate(Player player, ClickType clickType) {
        return true;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Profile.get(player.getUniqueId());

        List<String> lore = new ArrayList<>();
        lore.add("&8&m--------------------------------------");
        lore.add("&7Privacy: " + profile.getParty().getPrivacy().name());
        lore.add("");
        lore.add("&7&oClick here to change Privacy mode");
        lore.add("&8&m--------------------------------------");

        return new ItemBuilder(Material.REDSTONE)
                .name("&c&lParty Privacy")
                .lore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.get(player.getUniqueId());

        playNeutral(player);
        if(profile.getParty().getPrivacy() == PartyPrivacy.OPEN) {
            profile.getParty().setPrivacy(PartyPrivacy.CLOSED);
        } else {
            profile.getParty().setPrivacy(PartyPrivacy.OPEN);
        }
    }
}
