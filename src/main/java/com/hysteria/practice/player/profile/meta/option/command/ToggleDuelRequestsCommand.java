package com.hysteria.practice.player.profile.meta.option.command;

import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.Locale;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class ToggleDuelRequestsCommand extends BaseCommand {

    @Command(name = "toggleduels", aliases = {"tgr", "tgd"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        profile.getOptions().receiveDuelRequests(!profile.getOptions().receiveDuelRequests());

        if (profile.getOptions().receiveDuelRequests()) {
            new MessageFormat(Locale.OPTIONS_RECEIVE_DUEL_REQUESTS_ENABLED
                    .format(profile.getLocale()))
                    .send(player);
        } else {
            new MessageFormat(Locale.OPTIONS_RECEIVE_DUEL_REQUESTS_DISABLED
                    .format(profile.getLocale()))
                    .send(player);
        }
    }
}
