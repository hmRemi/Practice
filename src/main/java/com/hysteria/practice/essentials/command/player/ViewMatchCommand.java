package com.hysteria.practice.essentials.command.player;

import com.hysteria.practice.Locale;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.menu.match.ViewMatchMenu;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ViewMatchCommand extends BaseCommand {

    @Command(name = "viewmatch", aliases = {"matches"}, permission = "cpractice.viewmatch")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            Profile profile = Profile.get(player.getUniqueId());
            new ViewMatchMenu(profile).openMenu(player);
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        Profile profile = Profile.get(target.getUniqueId());
        new ViewMatchMenu(profile).openMenu(player);
    }
}