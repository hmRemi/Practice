package com.hysteria.practice.player.clan.commands.subcommands;

import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.chat.Clickable;
import com.hysteria.practice.Locale;
import com.hysteria.practice.player.clan.Clan;
import com.hysteria.practice.player.clan.ClanInvite;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClanInviteCommand extends BaseCommand {

    @Command(name = "clan.invite")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "A player with that name could not be found.");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        if (player.getUniqueId().equals(target.getUniqueId())) {
            player.sendMessage(CC.RED + "You cannot invite yourself.");
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        Profile profileTarget = Profile.get(target.getUniqueId());
        if (profile.getClan() == null) {
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_NOT_FOUND
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        Clan clan = profile.getClan();
        if (clan.getMembers().size() == 15) {
            new MessageFormat(Locale.CLAN_ERROR_MEMBERS_LIMIT
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        if (profileTarget.getClan() != null) {
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_ALREADY_IN_CLAN_OTHER.format(profile.getLocale()))
                    .add("{target_name}", target.getName())
                    .send(player);
            return;
        }

        if (!clan.getLeader().equals(player.getUniqueId())) {
            new MessageFormat(Locale.CLAN_ERROR_ONLY_OWNER
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        profileTarget.getInvites().put(profile.getClan().getName(), new ClanInvite(player.getUniqueId(), target.getUniqueId()));
        new MessageFormat(Locale.CLAN_INVITE_SENDER.format(profile.getLocale()))
                .add("{target_name}", target.getName())
                .send(player);
        Clickable clickable = new Clickable();
        clickable.add(new MessageFormat(Locale.CLAN_INVITE_RECEIVER.format(profile.getLocale()))
                .add("{clan_name}", profile.getClan().getColoredName())
                .toString());
        clickable.add(" &4C&4l&4i&4c&4k &4h&4e&4r&4e to enter", CC.translate("&4Click to enter"), "/clan join " +  profile.getClan().getName());
        clickable.sendToPlayer(target);
    }
}