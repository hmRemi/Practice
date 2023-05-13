package com.hysteria.practice.player.queue.menus.buttons;
/* 
   Made by hypractice Development Team
   Created on 30.11.2021
*/

import com.google.common.collect.Lists;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.game.event.game.menu.EventHostMenu;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.file.type.BasicConfigurationFile;
import com.hysteria.practice.utilities.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class EventHostButton extends Button {

    BasicConfigurationFile config = HyPractice.get().getMenuConfig();

    @Override
    public ItemStack getButtonItem(Player player) {
        ArrayList<String> lore = Lists.newArrayList();
        for (String text : this.config.getStringList("QUEUES.TYPES.EVENT.LORE")) {
            lore.add(text.replace("<kit>", HyPractice.get().getMainConfig().getString("FFA.KIT")));
        }
        return new ItemBuilder(Material.valueOf(HyPractice.get().getMenuConfig().getString("QUEUES.TYPES.EVENT.ICON"))).lore(CC.translate(lore)).amount(1).name(CC.translate(HyPractice.get().getMenuConfig().getString("QUEUES.TYPES.EVENT.NAME"))).durability(HyPractice.get().getMenuConfig().getInteger("QUEUES.TYPES.EVENT.DATA")).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        new EventHostMenu().openMenu(player);
    }
}

