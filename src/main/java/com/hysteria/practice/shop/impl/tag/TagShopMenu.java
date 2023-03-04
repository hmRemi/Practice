package com.hysteria.practice.shop.impl.tag;

import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.TaskUtil;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.pagination.PaginatedMenu;
import net.audidevelopment.core.api.tags.Tag;
import net.audidevelopment.core.plugin.cCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TagShopMenu extends PaginatedMenu
{

    {
        setUpdateAfterClick(true);
        setAutoUpdate(true);
    }

    @Override
    public String getPrePaginatedTitle(final Player player) {
        return ChatColor.DARK_GRAY + "Tags";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(final Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        for (Tag tag : cCore.INSTANCE.getTagManagement().getTags()) {
            buttons.put(buttons.size(), new SettingsButton(tag));
        }
        return buttons;
    }

    private static class SettingsButton extends Button
    {
        private final Tag type;

        @Override
        public ItemStack getButtonItem(final Player player) {
            return new ItemBuilder(Material.NAME_TAG)
                    .name((player.hasPermission("aqua.tags." + type.getName()) ? (CC.translate("&c&l")) : "&c&l") + this.type.getName())
                    .lore(CC.MENU_BAR)
                    .lore(player.hasPermission("aqua.tags." + type.getName()) ? "&7You already own this tag" : "&7You don't own this tag.")
                    .lore(player.hasPermission("aqua.tags." + type.getName()) ? "&7Price: None!" : "&4Price: &7" + "500")
                    .lore(CC.MENU_BAR)
                    .build();
        }

        @Override
        public void clicked(final Player player, final ClickType clickType) {
            Profile profile = Profile.get(player.getUniqueId());

            if (player.hasPermission("aqua.tags." + type.getName())) { // If player has permission.
                player.sendMessage(CC.translate("&7You already own this tag."));
                return;
            } else {
                if(profile.getCoins() == 500 || profile.getCoins() > 500) {
                    profile.setCoins(profile.getCoins() - 500);
                    player.sendMessage(CC.translate("&7You have purchased &4" + type.getName() + " &7for &4500 &7coins."));

                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "setperm " + player.getName() + " aqua.tags." + type.getName() + " true");
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

        public SettingsButton(final Tag type) {
            this.type = type;
        }
    }
}

