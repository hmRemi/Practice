package com.hysteria.practice.player.cosmetics.command;
/* 
   Made by cpractice Development Team
   Created on 30.11.2021
*/

import com.hysteria.practice.player.cosmetics.menu.CosmeticsMenu;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class CosmeticsCommand extends BaseCommand {

    @Command(name="cosmetic", aliases = {"cosmetics", "cosm"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        new CosmeticsMenu().openMenu(player);
    }
}
