package com.hysteria.practice.shop.impl.killeffects;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.cosmetics.impl.killeffects.KillEffectType;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.TaskUtil;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.pagination.PaginatedMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class KillEffectsShopMenu extends PaginatedMenu
{

    {
        setUpdateAfterClick(true);
        setAutoUpdate(true);
    }

    @Override
    public String getPrePaginatedTitle(final Player player) {
        return ChatColor.DARK_GRAY + "Death Effects";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(final Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        for (KillEffectType killEffectTypes : KillEffectType.values()) {
            buttons.put(buttons.size(), new SettingsButton(killEffectTypes));
        }
        return buttons;
    }

    private static class SettingsButton extends Button
    {
        private final KillEffectType type;

        @Override
        public ItemStack getButtonItem(final Player player) {
            return new ItemBuilder(type.getMaterial())
                    .name((this.type.hasPermission(player) ? (CC.translate("&c&l")) : "&c&l") + this.type.getName())
                    .lore(CC.MENU_BAR)
                    .lore(this.type.hasPermission(player) ? "&7You already own this death effect!" : "&7You don't own this death effect.")
                    .lore(this.type.hasPermission(player) ? "&7Price: None!" : "&4Price: &7" + this.type.getPrice())
                    .lore(CC.MENU_BAR)
                    .build();
        }

        @Override
        public void clicked(final Player player, final ClickType clickType) {
            Profile profile = Profile.get(player.getUniqueId());

            if (this.type.hasPermission(player)) {
                player.sendMessage(CC.translate("&7You already own this death effect."));
                return;
            } else {
                if(profile.getCoins() == type.getPrice() || profile.getCoins() > type.getPrice()) {
                    profile.setCoins(profile.getCoins() - type.getPrice());
                    player.sendMessage(CC.translate("&7You have purchased &4" + type.getName() + " &7for &4" + type.getPrice() + " &7coins."));

                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), HyPractice.get().getMainConfig().getString("PURCHASE-COSMETICS-CMD").replace("{player}", player.getName()).replace("{effect}", type.getName()));
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

        public SettingsButton(final KillEffectType type) {
            this.type = type;
        }
    }
}

