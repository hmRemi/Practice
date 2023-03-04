package com.hysteria.practice.player.profile.meta.option.command;

import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.Locale;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class ToggleScoreboardCommand extends BaseCommand {

    @Command(name = "togglescoreboard", aliases = {"tsb"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        profile.getOptions().showScoreboard(!profile.getOptions().showScoreboard());

        if (profile.getOptions().showScoreboard()) {
            new MessageFormat(Locale.OPTIONS_SCOREBOARD_ENABLED
                    .format(profile.getLocale()))
                    .send(player);
        } else {
            new MessageFormat(Locale.OPTIONS_SCOREBOARD_DISABLED
                    .format(profile.getLocale()))
                    .send(player);
        }
    }
}
