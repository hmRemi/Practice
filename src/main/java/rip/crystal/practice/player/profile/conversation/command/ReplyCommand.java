package rip.crystal.practice.player.profile.conversation.command;

import rip.crystal.practice.Locale;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.player.profile.conversation.Conversation;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReplyCommand extends BaseCommand {

    @Command(name = "reply", aliases = {"r"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Please usage: /" + commandArgs.getLabel() + " (message)");
            return;
        }

        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }

        Profile playerProfile = Profile.get(player.getUniqueId());
        Conversation conversation = playerProfile.getConversations().getLastRepliedConversation();

        if(conversation == null) {
            player.sendMessage(CC.RED + "You have nobody to reply to.");
            return;
        }

        Profile targetProfile = Profile.get(conversation.getPartner(player.getUniqueId()));

        if (targetProfile.getOptions().receivingNewConversations()) {
            if (conversation.validate()) {
                conversation.sendMessage(player, Bukkit.getPlayer(conversation.getPartner(player.getUniqueId())), message.toString());
            } else {
                player.sendMessage(CC.RED + "You can no longer reply to that player.");
            }
        } else {
            if(player.hasPermission("cpractice.tpmbypass")) {
                if (conversation.validate()) {
                    conversation.sendMessage(player, Bukkit.getPlayer(conversation.getPartner(player.getUniqueId())), message.toString());

                } else {
                    player.sendMessage(CC.RED + "You can no longer reply to that player.");
                }
            } else {
                player.sendMessage(CC.RED + "That player is not receiving new conversations right now.");
            }
        }
    }
}
