package com.hysteria.practice.shop.impl.killeffects;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.cosmetics.impl.killeffects.KillEffectType;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.TaskUtil;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import com.hysteria.practice.utilities.menu.pagination.PaginatedMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KillEffectsShopMenu extends Menu
{

    {
        setUpdateAfterClick(true);
        setAutoUpdate(true);
    }

    @Override
    public String getTitle(final Player player) {
        return CC.translate(HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.TITLE"));
    }

    @Override
    public Map<Integer, Button> getButtons(final Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();
        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMenuConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMenuConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);

        for (KillEffectType killEffectTypes : KillEffectType.values()) {
            buttons.put(killEffectTypes.getSlot(), new SettingsButton(killEffectTypes));
        }
        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }

    private static class SettingsButton extends Button
    {
        private final KillEffectType type;

        @Override
        public ItemStack getButtonItem(final Player player) {
            List<String> lore = new ArrayList<>();
            for (String text : HyPractice.get().getMenuConfig().getStringList("DEATH-EFFECTS.PURCHASE-LORE")) {
                lore.add(text
                    .replace("{price}", type.getPrice() + "")
                    .replace("{already-own}", this.type.hasPermission(player) ? HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.ALREADY-OWN") : HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.NO-PERMS")));
            }

            return new ItemBuilder(type.getMaterial())
                    .name(HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.NAME").replace("{effect}", type.getName()))
                    .lore(lore)
                    .build();
        }

        @Override
        public void clicked(final Player player, final ClickType clickType) {
            Profile profile = Profile.get(player.getUniqueId());

            if (this.type.hasPermission(player)) {
                player.sendMessage(CC.translate(HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.ALREADY-OWN")));
                return;
            } else {
                if(profile.getCoins() == type.getPrice() || profile.getCoins() > type.getPrice()) {
                    profile.setCoins(profile.getCoins() - type.getPrice());
                    player.sendMessage(CC.translate(HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.PURCHASED").replace("{effect}", this.type.getName()).replace("{coins}", this.type.getPrice() + "")));

                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), HyPractice.get().getMainConfig().getString("PURCHASE-COSMETICS-CMD").replace("{player}", player.getName()).replace("{effect}", type.getName()));
                    return;
                } else {
                    player.sendMessage(CC.translate(HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.NO-FUNDS")));
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

