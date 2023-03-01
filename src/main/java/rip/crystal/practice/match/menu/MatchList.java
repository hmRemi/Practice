package rip.crystal.practice.match.menu;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.impl.BasicFreeForAllMatch;
import rip.crystal.practice.match.impl.BasicTeamMatch;
import rip.crystal.practice.player.party.menu.manage.impl.PartyKickMenu;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.menu.Button;
import rip.crystal.practice.utilities.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/23/2023
 */
public class MatchList extends PaginatedMenu {

    {
        this.setUpdateAfterClick(true);
        this.setAutoUpdate(true);
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return CC.translate(cPractice.get().getMenuConfig().getString("MATCH-LIST.TITLE").replace("{amount}", String.valueOf(Match.getMatches().size())));
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new ConcurrentHashMap<>();

        int slot = 0;

        for (Match match : Match.getMatches()) {
            if(match instanceof BasicTeamMatch) {
                buttons.put(slot, new MatchButton(match));
            }
           slot++;
        }
        return buttons;
    }

    @AllArgsConstructor
    public static class RefreshButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.CARPET)
                    .name("&4Refresh")
                    .lore("&7Click here to update player list")
                    .durability(5)
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            playNeutral(player);
            new MatchList().openMenu(player);
        }
    }


    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(4, new RefreshButton());

        return buttons;
    }

    @RequiredArgsConstructor
    public static class MatchButton extends Button {

        private final Match match;

        public ItemStack getButtonItem(Player player) {
            List<String> lore = cPractice.get().getMenuConfig().getStringList("MATCH-LIST.LORE");
            lore.replaceAll(s ->
                    CC.translate(s
                            .replace("{arena}", match.getArena().getName())
                            .replace("{duration}", match.getDuration())
                            .replace("{spectators}", String.valueOf(match.getSpectators().size()))
                            .replace("{kit}", match.getKit().getName())
                            .replace("{type}", match.isRanked() ? "Ranked" : "Un-Ranked")
                    )
            );

            if(match instanceof BasicTeamMatch) {
                return new ItemBuilder(match.getKit().getDisplayIcon().clone())
                    .name(CC.translate(cPractice.get().getMenuConfig().getString("MATCH-LIST.NAME")
                                    .replace("{p1}", String.valueOf(((BasicTeamMatch) match).getParticipantA().getLeader().getPlayer().getName()))
                                    .replace("{p2}", String.valueOf(((BasicTeamMatch) match).getParticipantB().getLeader().getPlayer().getName()))
                            )
                    )
                    .lore(lore)
                    .build();
            } else {
                return new ItemBuilder(match.getKit().getDisplayIcon().clone())
                    .name(CC.translate("&c&lFree For All: " + match.getParticipants().size()))
                    .lore(lore)
                    .build();
            }
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            player.performCommand("spectate " + ((BasicTeamMatch) match).getParticipantA().getLeader().getPlayer().getName());
        }
    }

}
