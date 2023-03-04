package com.hysteria.practice.player.queue.menus;
/* 
   Made by cpractice Development Team
   Created on 30.11.2021
*/

import com.hysteria.practice.HyPractice;
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
        return CC.translate(HyPractice.get().getMainConfig().getString("QUEUES.TITLE"));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMainConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMainConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();

        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);
        buttons.put(HyPractice.get().getMainConfig().getInteger("QUEUES.TYPES.UNRANKED.SLOT"), new UnrankedButton());
        buttons.put(HyPractice.get().getMainConfig().getInteger("QUEUES.TYPES.RANKED.SLOT"), new RankedButton());
        buttons.put(HyPractice.get().getMainConfig().getInteger("QUEUES.TYPES.FFA.SLOT"), new FFAButton());

        return buttons;
    }

    @Override
    public int getSize() {
        return HyPractice.get().getMainConfig().getInteger("QUEUES.SIZE") * 9;
    }
}
