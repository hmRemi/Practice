package rip.crystal.practice.shop.buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.shop.impl.rank.RankShopMenu;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.menu.Button;

import java.util.ArrayList;
import java.util.List;

public class RankShopButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Profile.get(player.getUniqueId());

        List<String> lore = new ArrayList<>();
        lore.add("&8&m--------------------------------------");
        lore.add("&7Your coins: " + profile.getCoins());
        lore.add("");
        lore.add("&7&oClick here to purchase Ranks!");
        lore.add("&8&m--------------------------------------");


        return new ItemBuilder(Material.NETHER_STAR)
                .name("&c&lRanks")
                .lore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        new RankShopMenu().openMenu(player);
    }
}

