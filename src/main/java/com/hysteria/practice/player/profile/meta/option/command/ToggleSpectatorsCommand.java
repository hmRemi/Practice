package com.hysteria.practice.player.profile.meta.option.command;

import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.Locale;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class ToggleSpectatorsCommand extends BaseCommand {

    @Command(name = "togglespectators", aliases = {"togglespecs", "tgs"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        profile.getOptions().allowSpectators(!profile.getOptions().allowSpectators());

        if (profile.getOptions().allowSpectators()) {
            new MessageFormat(Locale.OPTIONS_SPECTATORS_ENABLED
                    .format(profile.getLocale()))
                    .send(player);
        } else {
            new MessageFormat(Locale.OPTIONS_SPECTATORS_DISABLED
                    .format(profile.getLocale()))
                    .send(player);
        }
    }
}
