package com.hysteria.practice.essentials.command.player;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.player.profile.menu.division.DivisionMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Hysteria Development
 * @project HyPractice
 * @date 3/8/2023
 */
public class DivisionCommand extends BaseCommand {

    @Command(name="division", aliases = {"div", "divisions", "league", "leagues"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if(HyPractice.get().isRunningAlonsoLeagues()) {
            new DivisionMenu().openMenu(player);
        } else {
            Bukkit.getConsoleSender().sendMessage("AlonsoLeagues is required.");
        }
    }
}
