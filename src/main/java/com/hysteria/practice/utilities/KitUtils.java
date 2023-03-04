package com.hysteria.practice.utilities;

import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.match.impl.BasicTeamMatch;
import com.hysteria.practice.player.profile.Profile;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitUtils {

    public static void giveBridgeKit(Player player) {
        Profile profile = Profile.get(player.getUniqueId());
        BasicTeamMatch teamMatch = (BasicTeamMatch) profile.getMatch();
        ItemStack[] armorRed = InventoryUtil.leatherArmor(Color.RED);
        ItemStack[] armorBlue = InventoryUtil.leatherArmor(Color.BLUE);
        if(teamMatch.getParticipantA() == null) {
            return;
        }
        if(teamMatch.getParticipantB() == null) {
            return;
        }
        if(teamMatch.getKit().getGameRules().isBattlerush()) {
            if (teamMatch.getParticipantA().containsPlayer(player.getUniqueId())) {
                player.getInventory().setArmorContents(armorRed);
                player.getInventory().all(Material.WOOL).forEach((key, value) -> {
                    player.getInventory().setItem(key, new ItemBuilder(Material.WOOL).durability(14).amount(64).build());
                    player.getInventory().setItem(key, new ItemBuilder(Material.WOOL).durability(14).amount(64).build());
                });
            } else {
                player.getInventory().setArmorContents(armorBlue);
                player.getInventory().all(Material.WOOL).forEach((key, value) -> {
                    player.getInventory().setItem(key, new ItemBuilder(Material.WOOL).durability(11).amount(64).build());
                    player.getInventory().setItem(key, new ItemBuilder(Material.WOOL).durability(11).amount(64).build());
                });
            }
        } else {
            if (teamMatch.getParticipantA().containsPlayer(player.getUniqueId())) {
                player.getInventory().setArmorContents(armorRed);
                player.getInventory().all(Material.STAINED_CLAY).forEach((key, value) -> {
                    player.getInventory().setItem(key, new ItemBuilder(Material.STAINED_CLAY).durability(14).amount(64).build());
                    player.getInventory().setItem(key, new ItemBuilder(Material.STAINED_CLAY).durability(14).amount(64).build());
                });
            } else {
                player.getInventory().setArmorContents(armorBlue);
                player.getInventory().all(Material.STAINED_CLAY).forEach((key, value) -> {
                    player.getInventory().setItem(key, new ItemBuilder(Material.STAINED_CLAY).durability(11).amount(64).build());
                    player.getInventory().setItem(key, new ItemBuilder(Material.STAINED_CLAY).durability(11).amount(64).build());
                });
            }
        }
        player.updateInventory();
    }

    public static void giveBedFightKit(Player player) {
        Profile profile = Profile.get(player.getUniqueId());
        BasicTeamMatch teamMatch = (BasicTeamMatch) profile.getMatch();
        ItemStack[] armorRed = InventoryUtil.leatherArmor(Color.RED);
        ItemStack[] armorBlue = InventoryUtil.leatherArmor(Color.BLUE);
        if(teamMatch.getParticipantA() != null || teamMatch.getParticipantB() != null) {
            assert teamMatch.getParticipantA() != null;
            if (teamMatch.getParticipantA().containsPlayer(player.getUniqueId())) {
                player.getInventory().setArmorContents(armorRed);
                player.getInventory().all(Material.WOOL).forEach((key, value) -> {
                    player.getInventory().setItem(key, new ItemBuilder(Material.WOOL).durability(14).amount(64).build());
                    player.getInventory().setItem(key, new ItemBuilder(Material.WOOL).durability(14).amount(64).build());
                });
            } else {
                player.getInventory().setArmorContents(armorBlue);
                player.getInventory().all(Material.WOOL).forEach((key, value) -> {
                    player.getInventory().setItem(key, new ItemBuilder(Material.WOOL).durability(11).amount(64).build());
                    player.getInventory().setItem(key, new ItemBuilder(Material.WOOL).durability(11).amount(64).build());
                });
            }
            player.updateInventory();
        }
    }

    public static void giveLivesKit(Player player) {
        Profile profile = Profile.get(player.getUniqueId());
        BasicTeamMatch teamMatch = (BasicTeamMatch) profile.getMatch();
        ItemStack[] armorRed = InventoryUtil.leatherArmor(Color.RED);
        ItemStack[] armorBlue = InventoryUtil.leatherArmor(Color.BLUE);
        if(teamMatch.getParticipantA() != null || teamMatch.getParticipantB() != null) {
            assert teamMatch.getParticipantA() != null;
            if (teamMatch.getParticipantA().containsPlayer(player.getUniqueId())) {
                player.getInventory().setArmorContents(armorRed);
                player.getInventory().all(Material.WOOL).forEach((key, value) -> {
                    player.getInventory().setItem(key, new ItemBuilder(Material.WOOL).durability(14).amount(64).build());
                    player.getInventory().setItem(key, new ItemBuilder(Material.WOOL).durability(14).amount(64).build());
                });
            } else {
                player.getInventory().setArmorContents(armorBlue);
                player.getInventory().all(Material.WOOL).forEach((key, value) -> {
                    player.getInventory().setItem(key, new ItemBuilder(Material.WOOL).durability(11).amount(64).build());
                    player.getInventory().setItem(key, new ItemBuilder(Material.WOOL).durability(11).amount(64).build());
                });
            }
            player.updateInventory();
        }
    }

    public static void giveBaseRaidingKit(Player player) {
        Profile profile = Profile.get(player.getUniqueId());
        BasicTeamMatch teamMatch = (BasicTeamMatch) profile.getMatch();
        Kit kit;

        if(teamMatch.getParticipantA() != null || teamMatch.getParticipantB() != null) {
            if (teamMatch.getParticipantA().containsPlayer(player.getUniqueId())) {
                kit = Kit.getByName("BaseTrapper");
            } else {
                kit = Kit.getByName("BaseRaider");
            }
            assert kit != null;
            player.getInventory().setArmorContents(kit.getKitLoadout().getArmor());
            player.getInventory().setContents(kit.getKitLoadout().getContents());
            player.updateInventory();
        }
    }
}