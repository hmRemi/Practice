package com.hysteria.practice.shop.menu;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.shop.buttons.KillEffectsShopButton;
import com.hysteria.practice.shop.buttons.RankShopButton;
import com.hysteria.practice.shop.buttons.TagShopButton;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ShopMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return CC.translate("&c&lCoins Shop");
    }

    {
        setPlaceholder(true);
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

        buttons.put(11, new KillEffectsShopButton());
        buttons.put(13, new RankShopButton());
        buttons.put(15, new TagShopButton());

        return buttons;
    }
}
