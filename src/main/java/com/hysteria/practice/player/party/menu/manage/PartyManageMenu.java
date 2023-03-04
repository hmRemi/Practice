package com.hysteria.practice.player.party.menu.manage;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.party.menu.manage.buttons.PartyAnnounceButton;
import com.hysteria.practice.player.party.menu.manage.buttons.PartyKickButton;
import com.hysteria.practice.player.party.menu.manage.buttons.PartyPrivacyButton;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/24/2023
 */
public class PartyManageMenu extends Menu {

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public String getTitle(Player player) {
        return "&8Party";
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMainConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMainConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);

        buttons.put(11, new PartyKickButton());
        buttons.put(13, new PartyPrivacyButton());
        buttons.put(15, new PartyAnnounceButton());
        return buttons;
    }
}
