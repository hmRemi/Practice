package com.hysteria.practice.player.party.menu.manage.impl;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.pagination.PaginatedMenu;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.hysteria.practice.utilities.chat.CC;

import java.util.*;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/24/2023
 */
public class PartyKickMenu extends PaginatedMenu {

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return HyPractice.get().getMenuConfig().getString("PARTY-MENU.KICK.NAME");
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Profile profile = Profile.get(player.getUniqueId());

        for (Player uuid : profile.getParty().getListOfPlayers()) {
            buttons.put(buttons.size(), new KickPlayer(uuid));
        }
        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(4, new RefreshButton());

        return buttons;
    }

    @AllArgsConstructor
    public static class RefreshButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.CARPET)
                    .name("&6Refresh")
                    .lore("&7Click here to update player list")
                    .durability(5)
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            playNeutral(player);
            new PartyKickMenu().openMenu(player);
        }
    }


    @RequiredArgsConstructor
    public static class KickPlayer extends Button {

        private final Player uuid;

        public ItemStack getButtonItem(Player player) {
            Profile profile = Profile.get(player.getUniqueId());

            List<String> lore = new ArrayList<>();
            HyPractice.get().getMenuConfig().getStringList("PARTY-MENU.KICK.MENU.LORE").forEach(s -> lore.add(s
                    .replace("{status}", (profile.getParty().containsPlayer(uuid.getUniqueId()) ? "In Party" : "Not in Party"))
                    .replace("{name}", uuid.getName())));

            return new ItemBuilder(Material.SKULL_ITEM)
                    .name(HyPractice.get().getMenuConfig().getString("PARTY-MENU.KICK.MENU.NAME").replace("{name}", uuid.getName()))
                    .lore(lore)
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            Profile profile = Profile.get(player.getUniqueId());

            if(uuid == null) return;
            if(!profile.getParty().containsPlayer(uuid.getUniqueId())) return;

            if(profile.getParty().getLeader() == uuid) {
                player.sendMessage(CC.translate("&8[" + HyPractice.get().getMenuConfig().getString("PARTY-MENU.KIT-COLOR") + "&lParty&8] &7You cannot kick yourself from your party."));
                return;
            }
            playNeutral(player);
            profile.getParty().leave(uuid, true);
        }

        @Override
        public boolean shouldUpdate(Player player, ClickType clickType) {
            return true;
        }

    }
}
