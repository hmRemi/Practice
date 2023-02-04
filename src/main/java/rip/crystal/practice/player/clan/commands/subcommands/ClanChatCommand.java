package rip.crystal.practice.player.clan.commands.subcommands;

import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class ClanChatCommand extends BaseCommand {

    @Command(name = "clan.chat", aliases = {"clan.c"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Please insert a valid message.");
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        StringBuilder builder = new StringBuilder();

        if (profile.getClan() == null) {
            player.sendMessage(CC.RED + "You are not in a clan.");
        }

        for (String arg : args) {
            builder.append(arg).append(" ");
        }

        if (profile.getClan() != null) {
            profile.getClan().sendChat(player, builder.toString());
        }
    }
}
