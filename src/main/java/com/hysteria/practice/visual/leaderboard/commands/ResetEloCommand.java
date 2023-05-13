package com.hysteria.practice.visual.leaderboard.commands;

import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.Locale;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ResetEloCommand extends BaseCommand {

    @Command(name = "resetelo", permission = "hypractice.resetelo")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Please usage: /resetelo (player)");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null || !target.hasPlayedBefore()) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        Profile profile = Profile.get(target.getUniqueId());
        profile.getKitData().forEach((kit, profileKitData) -> profileKitData.setElo(1000));
        player.sendMessage(CC.translate("&a" + target.getName() + " reset elo successfully"));
    }
}
