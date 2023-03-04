package com.hysteria.practice.player.profile.conversation.command;

import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.Locale;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.player.profile.conversation.Conversation;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageCommand extends BaseCommand {

    @Command(name = "message", aliases = {"msg", "whisper", "tell", "t"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.RED + "Please usage: /" + commandArgs.getLabel() + " (player) (message)");
            return;
        }

        StringBuilder message = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (player.equals(target)) {
            player.sendMessage(CC.RED + "You cannot message yourself!");
            return;
        }

        if (target == null) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        Profile playerProfile = Profile.get(player.getUniqueId());
        Profile targetProfile = Profile.get(target.getUniqueId());

        if (targetProfile.getState() == ProfileState.STAFF_MODE) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        if (targetProfile.getOptions().receivingNewConversations()) {
            Conversation conversation = playerProfile.getConversations().getOrCreateConversation(target);

            if (conversation.validate()) {
                conversation.sendMessage(player, target, message.toString());
            } else {
                player.sendMessage(CC.RED + "That player is not receiving new conversations right now.");
            }
        } else {
            if(player.hasPermission("cpractice.tpmbypass")) {
                Conversation conversation = playerProfile.getConversations().getOrCreateConversation(target);

                if (conversation.validate()) {
                    conversation.sendMessage(player, target, message.toString());
                } else {
                    player.sendMessage(CC.RED + "That player is not receiving new conversations right now.");
                }
            } else {
                player.sendMessage(CC.RED + "That player is not receiving new conversations right now.");
            }
        }
    }
}
