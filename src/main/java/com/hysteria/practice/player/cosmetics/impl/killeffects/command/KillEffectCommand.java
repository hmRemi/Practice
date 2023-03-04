package com.hysteria.practice.player.cosmetics.impl.killeffects.command;
/* 
   Made by cpractice Development Team
   Created on 04.11.2021
*/

import com.hysteria.practice.player.cosmetics.impl.killeffects.menu.KillEffectsMenu;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class KillEffectCommand extends BaseCommand {
    @Command(name = "killeffect")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        new KillEffectsMenu().openMenu(player);
    }
}
