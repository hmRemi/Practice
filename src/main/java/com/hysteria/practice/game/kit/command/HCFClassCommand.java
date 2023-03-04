package com.hysteria.practice.game.kit.command;
/* 
   Made by cpractice Development Team
   Created on 11.10.2021
*/

import com.hysteria.practice.player.party.menu.HCFClassMenu;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class HCFClassCommand extends BaseCommand {

    @Command(name = "hcfclass")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());

        if(profile.getParty().getLeader().getUniqueId().equals(player.getUniqueId())) {
            new HCFClassMenu().openMenu(player);
        } else {
            player.sendMessage(CC.translate("&cYou must be in a party and be the leader to execute this command!"));
        }
    }
}
