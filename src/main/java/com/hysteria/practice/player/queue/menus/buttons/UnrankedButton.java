package com.hysteria.practice.player.queue.menus.buttons;
/* 
   Made by cpractice Development Team
   Created on 30.11.2021
*/

import com.google.common.collect.Lists;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.file.type.BasicConfigurationFile;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.queue.Queue;
import com.hysteria.practice.utilities.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

@AllArgsConstructor
public class UnrankedButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        BasicConfigurationFile config = HyPractice.get().getMainConfig();
        ArrayList<String> lore = Lists.newArrayList();
        for (String text : config.getStringList("QUEUES.TYPES.UNRANKED.LORE")) {
            lore.add(text.replace("<kit>", HyPractice.get().getMainConfig().getString("FFA.KIT")));
        }
        return new ItemBuilder(Material.valueOf(HyPractice.get().getMainConfig().getString("QUEUES.TYPES.UNRANKED.ICON"))).lore(CC.translate(lore)).amount(1).name(CC.translate(HyPractice.get().getMainConfig().getString("QUEUES.TYPES.UNRANKED.NAME"))).durability(HyPractice.get().getMainConfig().getInteger("QUEUES.TYPES.UNRANKED.DATA")).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        Queue.getUnRankedMenu().openMenu(player);
    }
}

