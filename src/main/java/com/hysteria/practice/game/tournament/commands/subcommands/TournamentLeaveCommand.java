package com.hysteria.practice.game.tournament.commands.subcommands;

import com.hysteria.practice.HyPractice;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.game.tournament.Tournament;
import com.hysteria.practice.game.tournament.TournamentState;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/12/2023
 */

public class TournamentLeaveCommand extends BaseCommand {

    @Command(name = "tournament.leave")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        Tournament<?> tournament = Tournament.getTournament();
        if (tournament == null || tournament.getState() == TournamentState.ENDED || !profile.isInTournament()) {
            player.sendMessage(ChatColor.RED + "No tournament found.");
            return;
        }

        HyPractice.get().getTournamentManager().handleLeave(player);
    }
}
