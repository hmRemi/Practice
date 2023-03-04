package com.hysteria.practice.player.party.command.subcommands;

import com.hysteria.practice.player.clan.Clan;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.Locale;
import com.hysteria.practice.player.party.enums.PartyPrivacy;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class PartyInviteClanCommand extends BaseCommand {

    @Command(name = "party.inviteclan", aliases = {"p.inviteclan"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());

        if(profile.getClan() == null) {
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_NOT_FOUND
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        if (profile.getParty() == null) {
            player.sendMessage(CC.RED + "You do not have a party.");
            return;
        }

        if (!profile.getParty().getLeader().equals(player)) {
            player.sendMessage(CC.RED + "You are not the leader of your party.");
            return;
        }

        if (profile.getParty().getPrivacy() == PartyPrivacy.OPEN) {
            player.sendMessage(CC.RED + "The party state is Open. You do not need to invite players.");
            return;
        }

        Clan clan = profile.getClan();

        clan.getOnPlayers().forEach(target -> {
            if (profile.getParty().getInvite(target.getUniqueId()) != null) {
                return;
            }

            if (profile.getParty().containsPlayer(target.getUniqueId())) {
                return;
            }

            Profile targetData = Profile.get(target.getUniqueId());

            if (targetData.isBusy()) {
                return;
            }

            profile.getParty().invite(target);
        });
    }
}