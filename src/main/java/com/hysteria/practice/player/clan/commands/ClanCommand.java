package com.hysteria.practice.player.clan.commands;

import com.hysteria.practice.player.clan.commands.subcommands.*;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.Locale;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;
import com.hysteria.practice.player.clan.commands.subcommands.*;

public class ClanCommand extends BaseCommand {

    public ClanCommand() {
        new ClanCreateCommand();
        new ClanDisbandCommand();
        new ClanInfoCommand();
        new ClanListCommand();
        new ClanJoinCommand();
        new ClanInviteCommand();
        new ClanRenameCommand();
        new ClanKickCommand();
        new ClanSetColorCommand();
        new ClanLeaveCommand();
        new ClanSetPointsCommand();
        new ClanChatCommand();
    }

    @Command(name = "clan")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new MessageFormat(Locale.CLAN_HELP
                .format(Profile.get(player.getUniqueId()).getLocale()))
                .send(player);
    }
}
