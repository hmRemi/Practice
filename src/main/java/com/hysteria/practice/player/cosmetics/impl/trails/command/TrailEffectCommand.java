package com.hysteria.practice.player.cosmetics.impl.trails.command;
/* 
   Made by cpractice Development Team
   Created on 04.11.2021
*/

import org.bukkit.entity.Player;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.player.cosmetics.impl.trails.menu.TrailsEffectsMenu;

public class TrailEffectCommand extends BaseCommand {
    @Command(name = "traileffects")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        new TrailsEffectsMenu().openMenu(player);
    }
}
