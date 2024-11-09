package com.hysteria.practice.player.profile.menu.division;

import com.alonsoaliaga.alonsoleagues.AlonsoLeagues;
import com.alonsoaliaga.alonsoleagues.api.AlonsoLeaguesAPI;
import com.alonsoaliaga.alonsoleagues.others.LeagueData;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
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
        return "&8Divisions";
    }

    @Override
    public Map<Integer, Button> getButtons(final Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        for (LeagueData leagueData : AlonsoLeagues.getInstance().getRanksLeagueDataMap().values()) {
            buttons.put(buttons.size(), new DivisionButton(leagueData));

        }
        return buttons;
    }

    @Override
    public int getSize() {
        return 4 * 9;
    }

    private static class DivisionButton extends Button {
        private final LeagueData type;

        @Override
        public ItemStack getButtonItem(final Player player) {
            return new ItemBuilder(Material.BOOK)
                    .name(this.type.getLeagueColor() + this.type.getLeagueName())
                    .lore("")
                    .lore(" &8▪ &fPoints Required: &6" + this.type.getPointsRequired())
                    .lore(" &8▪ &fYour Points: &6" + AlonsoLeaguesAPI.getPoints(player.getUniqueId()))
                    .lore("")
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
