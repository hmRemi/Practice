package com.hysteria.practice.essentials.command.player;

import com.hysteria.practice.Locale;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.hotbar.Hotbar;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.PlayerUtil;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ResetCommand extends BaseCommand {

    @Command(name = "reset", permission = "hypractice.command.reset")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            PlayerUtil.reset(player);
            player.teleport(HyPractice.get().getEssentials().getSpawn());
            Hotbar.giveHotbarItems(player);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        PlayerUtil.reset(target);
        target.teleport(HyPractice.get().getEssentials().getSpawn());
        Hotbar.giveHotbarItems(player);
    }
}
