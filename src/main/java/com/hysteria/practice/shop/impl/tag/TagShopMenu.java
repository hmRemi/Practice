package com.hysteria.practice.shop.impl.tag;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.TaskUtil;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import com.hysteria.practice.utilities.menu.pagination.PaginatedMenu;
import meth.crystal.aspirin.api.tags.Tag;
import meth.crystal.aspirin.plugin.Aspirin;
import meth.crystal.aspirin.plugin.AspirinAPI;
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

public class TagShopMenu extends Menu
{

    {
        setUpdateAfterClick(true);
        setAutoUpdate(true);
    }

    @Override
    public String getTitle(final Player player) {
        return ChatColor.DARK_GRAY + "Tags";
    }

    @Override
    public Map<Integer, Button> getButtons(final Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();
        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMenuConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMenuConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();

        for (Tag tag : Aspirin.INSTANCE.getTagManagement().getTags()) {
            buttons.put(buttons.size() + 10, new SettingsButton(tag));
        }

        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);
        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }

    private static class SettingsButton extends Button
    {
        private final Tag type;

        @Override
        public ItemStack getButtonItem(final Player player) {

            List<String> lore = new ArrayList<>();
            for (String text : HyPractice.get().getMenuConfig().getStringList("COIN-SHOP.TAG-SHOP.PURCHASE-LORE")) {
                lore.add(text
                        .replace("{price}", "500")
                        .replace("{already-own}", player.hasPermission("aspirin.tags." + type.getName()) ? HyPractice.get().getMenuConfig().getString("COIN-SHOP.TAG-SHOP.ALREADY-OWN") : HyPractice.get().getMenuConfig().getString("COIN-SHOP.TAG-SHOP.NO-PERMS")));
            }


            return new ItemBuilder(Material.NAME_TAG)
                    .name(HyPractice.get().getMenuConfig().getString("COIN-SHOP.TAG-SHOP.TAG-NAME") + this.type.getName())
                    .lore(lore)
                    .build();
        }

        @Override
        public void clicked(final Player player, final ClickType clickType) {
            Profile profile = Profile.get(player.getUniqueId());

            if (player.hasPermission("aspirin.tags." + type.getName())) { // If player has permission.
                player.sendMessage(CC.translate(HyPractice.get().getMenuConfig().getString("COIN-SHOP.TAG.ALREADY-OWN")));
                return;
            } else {
                if(profile.getCoins() == 500 || profile.getCoins() > 500) {
                    profile.setCoins(profile.getCoins() - 500);
                    player.sendMessage(CC.translate(HyPractice.get().getMenuConfig().getString("COIN-SHOP.TAG.PURCHASE").replace("{price}", "500").replace("{tag}", type.getName())));
                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "setperm " + player.getName() + " aspirin.tags." + type.getName() + " true");
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

