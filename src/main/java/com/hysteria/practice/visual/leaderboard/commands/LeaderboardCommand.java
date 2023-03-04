package com.hysteria.practice.visual.leaderboard.commands;

import com.hysteria.practice.visual.leaderboard.menu.LeaderBoardMenu;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class LeaderboardCommand extends BaseCommand {

    @Command(name = "topelo", aliases = {"leaderboard"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new LeaderBoardMenu(player).openMenu(player);
    }
}
