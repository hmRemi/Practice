package com.hysteria.practice.player.cosmetics.impl.killeffects.menu;
/* 
   Made by hypractice Development Team
   Created on 30.11.2021
*/

import com.google.common.collect.Maps;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.TaskUtil;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import com.hysteria.practice.utilities.menu.pagination.PaginatedMenu;
import com.hysteria.practice.player.cosmetics.impl.killeffects.KillEffectType;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KillEffectsMenu extends Menu
{

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public String getTitle(final Player player) {
        return CC.translate(HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.TITLE"));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
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
            final Profile profile = Profile.get(player.getUniqueId());
            List<String> lore = HyPractice.get().getMenuConfig().getStringList("DEATH-EFFECTS.LORE");

            lore.replaceAll(s ->
                    CC.translate(s
                            .replace("{effect}", this.type.getName())
                            .replace("{permission}", (this.type.hasPermission(player) ? HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.PERMISSION") : HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.NO-PERMISSION")))
                            .replace("{playerEffect}", ((profile.getKillEffectType() != null) ? profile.getKillEffectType().getName() : "&7No Effect"))
                            .replace("{viewStatus}", (profile.getKillEffectType() == this.type) ? HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.ALREADY-SELECTED") : (this.type.hasPermission(player) ? HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.PERMISSION") : HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.NO-PERMISSION")))
                    )
            );

            return new ItemBuilder(type.getMaterial())
                    .name(HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.NAME").replace("{effect}", type.getName()))
                    .lore(lore)
                    .build();
        }

        @Override
        public void clicked(final Player player, final ClickType clickType) {
            final Profile profile = Profile.get(player.getUniqueId());
            if (!this.type.hasPermission(player)) {
                player.sendMessage(HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.NO-PERMS"));
            }
            else if (profile.getKillEffectType() == this.type) {
                player.sendMessage(HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.ALREADY-SELECTED"));
            }
            else {
                profile.setKillEffectType(this.type);
                player.sendMessage(HyPractice.get().getMenuConfig().getString("DEATH-EFFECTS.SELECTED").replace("{effect}", this.type.getName()));
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

