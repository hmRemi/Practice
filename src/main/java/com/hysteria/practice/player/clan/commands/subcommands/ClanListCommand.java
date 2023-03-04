package com.hysteria.practice.player.clan.commands.subcommands;

import com.hysteria.practice.player.clan.Clan;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

public class ClanListCommand extends BaseCommand {

    @Command(name = "clan.list")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        player.sendMessage(CC.translate("&c&lList of all Clans"));
        Clan.getClans().values().forEach(clan ->
                player.sendMessage(CC.translate("&7- &e" + StringUtils.capitalize(clan.getName().toLowerCase()))));
    }
}
