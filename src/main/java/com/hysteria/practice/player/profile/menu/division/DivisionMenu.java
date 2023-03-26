package com.hysteria.practice.player.profile.menu.division;

import com.alonsoaliaga.alonsoleagues.AlonsoLeagues;
import com.alonsoaliaga.alonsoleagues.api.AlonsoLeaguesAPI;
import com.alonsoaliaga.alonsoleagues.others.LeagueData;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.shop.impl.rank.RankShopMenu;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.TaskUtil;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import net.audidevelopment.core.api.rank.RankData;
import net.audidevelopment.core.plugin.cCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hysteria Development
 * @project HyPractice
 * @date 3/8/2023
 */
public class DivisionMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&8Divisions │ " + AlonsoLeaguesAPI.getLeague(player.getUniqueId());
    }

    @Override
    public Map<Integer, Button> getButtons(final Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();
        ItemStack PLACEHOLDER_ITEM = new ItemBuilder(Material.valueOf(HyPractice.get().getMenuConfig().getString("QUEUES.PLACEHOLDER-ITEM-MATERIAL"))).durability(HyPractice.get().getMainConfig().getInteger("QUEUES.PLACEHOLDER-ITEM-DATA")).name("&b").build();
        this.fillEmptySlots(buttons, PLACEHOLDER_ITEM);

        for (LeagueData leagueData : AlonsoLeagues.getInstance().getRanksLeagueDataMap().values()) {
            buttons.put(leagueData.getIndex(), new DivisionButton(leagueData));
        }
        return buttons;
    }

    private static class DivisionButton extends Button
    {
        private final LeagueData type;

        @Override
        public ItemStack getButtonItem(final Player player) {
            return new ItemBuilder(Material.BOOK)
                    .name(this.type.getLeagueColor() + this.type.getLeagueName())
                    .lore(CC.MENU_BAR)
                    .lore("&7Points Required: &c" + this.type.getPointsRequired())
                    .lore("&7Your Points: &c" + AlonsoLeaguesAPI.getPoints(player.getUniqueId()))
                    .lore(CC.MENU_BAR)
                    .build();
        }

        @Override
        public boolean shouldUpdate(final Player player, final ClickType clickType) {
            return true;
        }

        public DivisionButton(final LeagueData type) {
            this.type = type;
        }
    }
}
