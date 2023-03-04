package com.hysteria.practice.player.profile.modmode.commands;

import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.player.profile.modmode.Modmode;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class StaffModeCommand extends BaseCommand {

    @Command(name = "staffmode", aliases = {"staff", "mod", "h"}, permission = "cpractice.staffmode")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());

        if(profile.getState() == ProfileState.EVENT || profile.getState() == ProfileState.FIGHTING || profile.getState() == ProfileState.SPECTATING || profile.getState() == ProfileState.QUEUEING || profile.getState() == ProfileState.FFA) {
            return;
        }

        if (profile.getState() == ProfileState.STAFF_MODE) Modmode.remove(player);
        else Modmode.add(player);
    }
}
