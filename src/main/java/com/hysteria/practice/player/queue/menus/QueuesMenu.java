package com.hysteria.practice.player.queue.menus;
/* 
   Made by hypractice Development Team
   Created on 30.11.2021
*/

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.queue.menus.buttons.EventHostButton;
import com.hysteria.practice.player.queue.menus.buttons.FFAButton;
import com.hysteria.practice.player.queue.menus.buttons.RankedButton;
import com.hysteria.practice.player.queue.menus.buttons.UnrankedButton;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import com.hysteria.practice.utilities.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class QueuesMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return CC.translate(HyPractice.get().getMenuConfig().getString("QUEUES.TITLE"));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMenuConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMenuConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();

        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);
        if(HyPractice.get().getMenuConfig().getBoolean("QUEUES.TYPES.UNRANKED.ENABLED")) {
            buttons.put(HyPractice.get().getMenuConfig().getInteger("QUEUES.TYPES.UNRANKED.SLOT"), new UnrankedButton());
        }
        if(HyPractice.get().getMenuConfig().getBoolean("QUEUES.TYPES.RANKED.ENABLED")) {
            buttons.put(HyPractice.get().getMenuConfig().getInteger("QUEUES.TYPES.RANKED.SLOT"), new RankedButton());
        }
        if(HyPractice.get().getMenuConfig().getBoolean("QUEUES.TYPES.EVENT.ENABLED")) {
            buttons.put(HyPractice.get().getMenuConfig().getInteger("QUEUES.TYPES.EVENT.SLOT"), new EventHostButton());
        }
        if(HyPractice.get().getMenuConfig().getBoolean("QUEUES.TYPES.FFA.ENABLED")) {
            buttons.put(HyPractice.get().getMenuConfig().getInteger("QUEUES.TYPES.FFA.SLOT"), new FFAButton());
        }
        return buttons;
    }

    @Override
    public int getSize() {
        return HyPractice.get().getMenuConfig().getInteger("QUEUES.SIZE") * 9;
    }
}
