package com.hysteria.practice.game.tournament.commands.subcommands;

import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.chat.Clickable;
import com.hysteria.practice.Locale;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.game.tournament.Tournament;
import com.hysteria.practice.game.tournament.impl.TournamentSolo;
import com.hysteria.practice.game.tournament.impl.TournamentClans;
import com.hysteria.practice.game.tournament.impl.TournamentTeams;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/12/2023
 */

public class TournamentStartCommand extends BaseCommand {

    @Command(name = "tournament.start", permission = "tournament.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.RED + "Please use /tournament start (type) (kit)");
            player.sendMessage(CC.CHAT_BAR);
            return;
        }

        String type = args[0].toLowerCase();

        Kit kit = Kit.getByName(args[1]);
        if (kit == null) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.RED + "This kit doesn't exist.");
            player.sendMessage(CC.CHAT_BAR);
            return;
        }

        Tournament<?> tournament;
        switch (type) {
            case "solo":
                tournament = new TournamentSolo();
                break;
            case "clans":
                tournament = new TournamentClans();
                break;
            case "teams":
                tournament = new TournamentTeams(); // Assuming you have a TournamentTeams class
                break;
            default:
                player.sendMessage(CC.CHAT_BAR);
                player.sendMessage(CC.RED + "Invalid tournament type. Available types: solo, clans, teams");
                player.sendMessage(CC.CHAT_BAR);
                return;
        }

        if (Tournament.getTournament() != null) {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.RED + "A tournament is already in progress.");
            player.sendMessage(CC.CHAT_BAR);
            return;
        }

        tournament.setLimit(100);
        tournament.setKit(kit);
        Tournament.setTournament(tournament);

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(CC.CHAT_BAR));
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(CC.translate("&7(*) &cTournament has started, click here to join &7(*)")));
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(CC.CHAT_BAR));
    }
}
