package rip.crystal.practice.game.tournament.commands.subcommands;

import rip.crystal.practice.cPractice;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/12/2023
 */

public class TournamentStopCommand extends BaseCommand {

    @Command(name = "tournament.stop", permission = "tournament.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        cPractice.get().getTournamentManager().cancelTournament(player);
    }
}