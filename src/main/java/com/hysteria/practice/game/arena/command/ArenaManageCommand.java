package com.hysteria.practice.game.arena.command;

import com.hysteria.practice.game.arena.menu.ArenaManagementMenu;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;


public class ArenaManageCommand extends BaseCommand {

    @Command(name = "arena.manage", permission = "cpractice.arena.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        new ArenaManagementMenu().openMenu(player);
    }
}
