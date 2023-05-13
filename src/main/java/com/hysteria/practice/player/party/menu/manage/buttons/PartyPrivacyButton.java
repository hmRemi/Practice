package com.hysteria.practice.player.party.menu.manage.buttons;

import com.hysteria.practice.HyPractice;
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
        HyPractice.get().getMenuConfig().getStringList("PARTY-MENU.PRIVACY.LORE").forEach(s -> lore.add(s.replace("{privacy}", profile.getParty().getPrivacy().name())));

        return new ItemBuilder(Material.REDSTONE)
                .name(HyPractice.get().getMenuConfig().getString("PARTY-MENU.PRIVACY.NAME"))
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
