package com.hysteria.practice.match.command;

import com.hysteria.practice.match.menu.MatchList;
import org.bukkit.entity.Player;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/23/2023
 */
public class MatchListCommand extends BaseCommand {
    @Command(name = "matchlist")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new MatchList().openMenu(player);
    }
}
