package com.hysteria.practice.visual.leaderboard.commands;

import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.Locale;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.regex.Pattern;

public class SetEloCommand extends BaseCommand {

    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    @Command(name = "setelo", permission = "cpractice.setelo")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (Arrays.asList(0,1,2).contains(args.length)) {
            player.sendMessage(CC.RED + "Please usage: /setelo (kit) (player) (integer)");
            return;
        }

        Kit kit = Kit.getByName(args[0]);
        if (kit == null) {
            player.sendMessage(CC.RED + "Please insert a valid Kit.");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (target == null || !target.hasPlayedBefore()) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        if (!pattern.matcher(args[2]).matches()) {
            player.sendMessage(CC.RED + "Please insert a valid Integer.");
            return;
        }

        //int integer = Integer.getInteger(args[2]);

        Profile profile = Profile.get(target.getUniqueId());
        profile.getKitData().get(kit).setElo(Integer.parseInt(args[2]));
        player.sendMessage(CC.translate("&cPlayer " + target.getName() + " new elo from " + kit.getName() + " is " + args[2]));
    }
}
