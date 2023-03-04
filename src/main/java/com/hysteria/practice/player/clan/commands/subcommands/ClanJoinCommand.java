package com.hysteria.practice.player.clan.commands.subcommands;

import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.Locale;
import com.hysteria.practice.player.clan.Clan;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class ClanJoinCommand extends BaseCommand {

    @Command(name = "clan.join")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        Clan clan = Clan.getByName(args[0]);
        if (clan == null) {
            player.sendMessage(CC.RED + "Please insert a valid Clan.");
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        if (profile.getClan() != null) {
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_ALREADY_IN_CLAN
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        if (!profile.getInvites().containsKey(clan.getName())) {
            new MessageFormat(Locale.CLAN_ERROR_NOT_INVITATION
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }
        if (profile.getInvites().get(clan.getName()).isExpired()) {
            new MessageFormat(Locale.CLAN_ERROR_INVITATION_EXPIRED
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        clan.join(player);
    }
}
