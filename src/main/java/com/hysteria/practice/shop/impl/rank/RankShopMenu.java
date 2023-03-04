package com.hysteria.practice.shop.impl.rank;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.TaskUtil;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import net.audidevelopment.core.api.rank.RankData;
import net.audidevelopment.core.plugin.cCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RankShopMenu extends Menu
{

    {
        setUpdateAfterClick(true);
        setAutoUpdate(true);
    }

    @Override
    public Map<Integer, Button> getButtons(final Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();
        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMainConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMainConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);

        for (RankData rankData : cCore.INSTANCE.getRankManagement().getRanks()) {
            if(rankData.isPurchasable()) {
                buttons.put(rankData.getWeight(), new SettingsButton(rankData));
            }
        }
        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @Override
    public String getTitle(Player player) {
        return "&8Ranks";
    }

    private static class SettingsButton extends Button
    {
        private final RankData type;

        @Override
        public ItemStack getButtonItem(final Player player) {
            return new ItemBuilder(Material.BOOK)
                    .name((cCore.INSTANCE.getPlayerManagement().getPlayerData(player.getUniqueId()).hasRank(type) ? (CC.translate("&c&l")) : "&c&l") + this.type.getName())
                    .lore(CC.MENU_BAR)
                    .lore(cCore.INSTANCE.getPlayerManagement().getPlayerData(player.getUniqueId()).hasRank(type) ? "&7You already own this rank" : "&7You don't own this rank.")
                    .lore(cCore.INSTANCE.getPlayerManagement().getPlayerData(player.getUniqueId()).hasRank(type) ? "&7Price: None!" : "&4Price: &7" + type.getCoinsCost())
                    .lore(CC.MENU_BAR)
                    .build();
        }

        @Override
        public void clicked(final Player player, final ClickType clickType) {
            Profile profile = Profile.get(player.getUniqueId());

            if (cCore.INSTANCE.getPlayerManagement().getPlayerData(player.getUniqueId()).hasRank(type)) { // If player has permission.
                player.sendMessage(CC.translate("&7You already own this rank."));
                return;
            } else {
                if(profile.getCoins() == type.getCoinsCost() || profile.getCoins() > type.getCoinsCost()) {
                    profile.setCoins(profile.getCoins() - type.getCoinsCost());
                    player.sendMessage(CC.translate("&7You have purchased &4" + type.getName() + " &7for &4" + type.getCoinsCost() + " &7coins."));

                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "setrank " + player.getName() + " " + type.getName() + " 30d" + " global" + " Purchased with coins");
                    return;
                } else {
                    player.sendMessage(CC.translate("&cYou don't have enough funds to buy this."));
                }
            }
            player.closeInventory();
            TaskUtil.runAsync(profile::save);
        }

        @Override
        public boolean shouldUpdate(final Player player, final ClickType clickType) {
            return true;
        }

        public SettingsButton(final RankData type) {
            this.type = type;
        }
    }
}

