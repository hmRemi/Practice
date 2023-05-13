package com.hysteria.practice.player.party.command.subcommands;
/* 
   Made by hypractice Development Team
   Created on 07.11.2021
*/

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.Locale;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.Cooldown;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.TimeUtil;
import com.hysteria.practice.utilities.chat.ChatComponentBuilder;
import com.hysteria.practice.utilities.chat.ChatHelper;
import com.hysteria.practice.utilities.chat.Clickable;
import com.hysteria.practice.player.party.enums.PartyPrivacy;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class PartyAnnounceCommand extends BaseCommand {

    private boolean sendMessage;

    @Command(name = "party.announce", aliases = {"p.announce"}, permission = "hypractice.party.announce")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        Profile profile = Profile.get(player.getUniqueId());

        if(profile.getParty() == null) { // If player isn't in party, return.
            player.sendMessage(ChatColor.RED + "You are not in a party.");
            return;
        }

        if (!profile.getParty().getLeader().equals(player)) { // If the executor isn't the party leader, return.
            player.sendMessage(ChatColor.RED + "You are not the leader of this party.");
            return;
        }

        if(profile.getParty().getPrivacy() != PartyPrivacy.OPEN) { // If party isn't public while command is executed, make party public.
            profile.getParty().setPrivacy(PartyPrivacy.OPEN);
        }

        if(!profile.getPartyAnnounceCooldown().hasExpired()) { // If the cooldown hasn't expired, send message then return.
            String time = TimeUtil.millisToSeconds(profile.getPartyAnnounceCooldown().getRemaining()); // Get the remaining time of cooldown and init to String.
            // Message to broadcast
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.translate("&c&lPARTY ANNOUNCEMENT"));
            player.sendMessage(CC.translate("  &7* &cYou are on cooldown."));
            player.sendMessage(CC.translate("  &7* &cRemaining time: &f" + time));
            player.sendMessage(CC.CHAT_BAR);
            return;
        }

        profile.setPartyAnnounceCooldown(new Cooldown(60_000)); // Set the party announce cooldown to 60 seconds (1 minute)


        sendAnnouncement(player);

    }

    public void sendAnnouncement(Player player) {
        Profile profile = Profile.get(player.getUniqueId());

        for (String msg : new MessageFormat(Locale.PARTY_HOST
                .format(profile.getLocale()))
                .add("{player}", player.getName())
                .toList()) {
            if (msg.contains("%CLICKABLE%")) {
                ChatComponentBuilder builder = new ChatComponentBuilder(new MessageFormat(Locale.PARTY_HOST_CLICKABLE
                        .format(profile.getLocale()))
                        .add("{sender_name}", player.getName())
                        .toString());
                builder.attachToEachPart(ChatHelper.click("/party join " + player.getName()));
                builder.attachToEachPart(ChatHelper.hover(new MessageFormat(Locale.PARTY_HOST_CLICKABLE
                        .format(profile.getLocale()))
                        .toString()));

                player.spigot().sendMessage(builder.create());
            } else {
                player.sendMessage(msg);
            }
        }
    }
}
