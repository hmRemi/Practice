package rip.crystal.practice.player.party.menu.manage.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.impl.BasicTeamMatch;
import rip.crystal.practice.player.party.Party;
import rip.crystal.practice.player.party.menu.manage.buttons.PartyAnnounceButton;
import rip.crystal.practice.player.party.menu.manage.buttons.PartyKickButton;
import rip.crystal.practice.player.party.menu.manage.buttons.PartyPrivacyButton;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.file.IProfile;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;
import rip.crystal.practice.utilities.menu.pagination.PaginatedMenu;

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
        return "&8Party Kick";
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
                    .name("&4Refresh")
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
            lore.add("&8&m--------------------------------------");
            lore.add("&4Party Status: &7" + (profile.getParty().containsPlayer(uuid.getUniqueId()) ? "In Party" : "Not in Party"));
            lore.add("");
            lore.add("&7&oClick to kick " + uuid.getName() + "!");
            lore.add("&8&m--------------------------------------");

            return new ItemBuilder(Material.SKULL_ITEM)
                    .name("&4&l" + uuid.getName())
                    .lore(lore)
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            Profile profile = Profile.get(player.getUniqueId());

            if(uuid == null) return;
            if(!profile.getParty().containsPlayer(uuid.getUniqueId())) return;

            if(profile.getParty().getLeader() == uuid) {
                player.sendMessage(CC.translate("&8[&4&lParty&8] &7You cannot kick yourself from your party."));
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
