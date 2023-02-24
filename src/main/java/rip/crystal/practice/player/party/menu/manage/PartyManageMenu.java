package rip.crystal.practice.player.party.menu.manage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.party.menu.manage.buttons.PartyAnnounceButton;
import rip.crystal.practice.player.party.menu.manage.buttons.PartyKickButton;
import rip.crystal.practice.player.party.menu.manage.buttons.PartyPrivacyButton;
import rip.crystal.practice.player.queue.menus.buttons.FFAButton;
import rip.crystal.practice.player.queue.menus.buttons.RankedButton;
import rip.crystal.practice.player.queue.menus.buttons.UnrankedButton;
import rip.crystal.practice.shop.buttons.KillEffectsShopButton;
import rip.crystal.practice.shop.buttons.RankShopButton;
import rip.crystal.practice.shop.buttons.TagShopButton;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.Menu;

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

        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(cPractice.get().getMainConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(cPractice.get().getMainConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);

        buttons.put(11, new PartyKickButton());
        buttons.put(13, new PartyPrivacyButton());
        buttons.put(15, new PartyAnnounceButton());
        return buttons;
    }
}
