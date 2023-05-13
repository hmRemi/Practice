package com.hysteria.practice.shop.impl.rank;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.TaskUtil;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import meth.crystal.aspirin.api.rank.RankData;
import meth.crystal.aspirin.plugin.Aspirin;
import meth.crystal.aspirin.plugin.AspirinAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMenuConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMenuConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);

        for (RankData rankData : Aspirin.INSTANCE.getRankManagement().getRanks()) {
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
            List<String> lore = new ArrayList<>();
            for (String text : HyPractice.get().getMenuConfig().getStringList("COIN-SHOP.RANK-SHOP.PURCHASE-LORE")) {
                lore.add(text
                        .replace("{price}", type.getCoinsCost() + "")
                        .replace("{already-own}", AspirinAPI.INSTANCE.getPlayerData(player.getUniqueId()).hasRank(type) ? HyPractice.get().getMenuConfig().getString("COIN-SHOP.RANK-SHOP.ALREADY-OWN") : HyPractice.get().getMenuConfig().getString("COIN-SHOP.RANK-SHOP.NO-PERMS")));
            }

            return new ItemBuilder(Material.BOOK)
                    .name(HyPractice.get().getMenuConfig().getString("COIN-SHOP.RANK-SHOP.RANK-NAME") + this.type.getName())
                    .lore(lore)
                    .build();
        }

        @Override
        public void clicked(final Player player, final ClickType clickType) {
            Profile profile = Profile.get(player.getUniqueId());

            if (AspirinAPI.INSTANCE.getPlayerData(player.getUniqueId()).hasRank(type)) { // If player has permission.
                player.sendMessage(CC.translate(HyPractice.get().getMenuConfig().getString("COIN-SHOP.RANK.ALREADY-OWN")));
                return;
            } else {
                if(profile.getCoins() == type.getCoinsCost() || profile.getCoins() > type.getCoinsCost()) {
                    profile.setCoins(profile.getCoins() - type.getCoinsCost());
                    player.sendMessage(CC.translate(HyPractice.get().getMenuConfig().getString("COIN-SHOP.RANK.PURCHASE").replace("{price}", type.getCoinsCost() + "").replace("{rank}", type.getName())));
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

