package rip.crystal.practice.match.command;

import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.chunk.ChunkRestorationManager;
import rip.crystal.practice.game.arena.impl.StandaloneArena;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CancelAllMatchesCommand extends BaseCommand {

    @Command(name = "cancelallmatches", permission = "cpractice.command.cancelallmatches")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        Bukkit.broadcastMessage(CC.CHAT_BAR);
        Bukkit.broadcastMessage(CC.translate("&8[&4&lMatch&8] &7All matches has been cancelled."));
        Bukkit.broadcastMessage(CC.translate("&8[&4&lMatch&8] &7Preparing for reboot"));
        Bukkit.broadcastMessage(CC.CHAT_BAR);
        Match.getMatches().forEach(Match::end);

    }
}
