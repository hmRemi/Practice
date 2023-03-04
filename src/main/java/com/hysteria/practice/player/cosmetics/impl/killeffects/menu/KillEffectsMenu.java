package com.hysteria.practice.player.cosmetics.impl.killeffects.menu;
/* 
   Made by cpractice Development Team
   Created on 30.11.2021
*/

import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.TaskUtil;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.pagination.PaginatedMenu;
import com.hysteria.practice.player.cosmetics.impl.killeffects.KillEffectType;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class KillEffectsMenu extends PaginatedMenu
{

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public String getPrePaginatedTitle(final Player player) {
        return CC.translate("&c&lDeath Effects");
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(final Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();
        //Stream.of(KillEffectType.values()).forEach(type -> buttons.put(KillEffectType.values().length, new SettingsButton(type)));
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
            final Profile profile = Profile.get(player.getUniqueId());
            return new ItemBuilder(type.getMaterial())
                    .name((profile.getKillEffectType() == this.type) ? "&c&l" + this.type.getName() : (this.type.hasPermission(player) ? (CC.translate("&c&l")) : "&c&l") + this.type.getName())
                    .lore(CC.MENU_BAR)
                    .lore("&7Left click to change your")
                    .lore("&7death effect to to " + "&4" + this.type.getName() + "&7.")
                    .lore("")
                    .lore("&7Selected Death Effect: " + "&4" + ((profile.getKillEffectType() != null) ? profile.getKillEffectType().getName() : "&4None"))
                    .lore((profile.getKillEffectType() == this.type) ? "&7That death effect is already selected." : (this.type.hasPermission(player) ? "&7Click to select this death effect." : "&4You don't own this death effect."))
                    .lore(CC.MENU_BAR)
                    .build();
        }

        @Override
        public void clicked(final Player player, final ClickType clickType) {
            final Profile profile = Profile.get(player.getUniqueId());
            if (!this.type.hasPermission(player)) {
                player.sendMessage(CC.translate("&7You don't have the &4" + this.type.getName() + "&7 death effect. Purchase it at &4store.hy-pvp.net" + "&7."));
            }
            else if (profile.getKillEffectType() == this.type) {
                player.sendMessage(CC.translate("&4" + this.type.getName() + "&7 death effect is already selected."));
            }
            else {
                profile.setKillEffectType(this.type);
                player.sendMessage(CC.translate("&4" + this.type.getName() + "&7 is now set as your death effect."));
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

