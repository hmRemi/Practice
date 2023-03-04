package com.hysteria.practice.player.party.command;

import com.hysteria.practice.player.party.command.subcommands.*;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.Locale;
import com.hysteria.practice.player.party.command.subcommands.*;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class PartyCommand extends BaseCommand {

    public PartyCommand() {
        super();
        new PartyChatCommand();
        new PartyCloseCommand();
        new PartyCreateCommand();
        new PartyDisbandCommand();
        new PartyInfoCommand();
        new PartyInviteCommand();
        new PartyJoinCommand();
        new PartyKickCommand();
        new PartyLeaveCommand();
        new PartyOpenCommand();
        new PartyInviteClanCommand();
        new PartyBanCommand();
        new PartyUnbanCommand();
        new PartyAnnounceCommand();
        new PartyManageCommand();
    }

    @Command(name = "party", aliases = {"p"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new MessageFormat(Locale.PARTY_HELP
                .format(Profile.get(player.getUniqueId()).getLocale()))
                .send(player);
    }
}
