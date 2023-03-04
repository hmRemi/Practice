package com.hysteria.practice.visual.leaderboard.commands;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.Locale;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class EloCommand extends BaseCommand {

    @Command(name = "elo")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length > 0) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (target == null || !target.hasPlayedBefore()) {
                new MessageFormat(Locale.PLAYER_NOT_FOUND
                        .format(Profile.get(player.getUniqueId()).getLocale()))
                        .send(player);
                return;
            }
            for (String s : HyPractice.get().getLangConfig().getStringList("ELO.VIEW_OTHER")) {
                if (s.contains("{format}")) {
                    Kit.getKits().stream().filter(Kit::isEnabled).filter(kit -> kit.getGameRules().isRanked()).forEach(kit ->
                            player.sendMessage(CC.translate(HyPractice.get().getLangConfig().getString("ELO.VIEW_FORMAT")
                                    .replace("{kit}", kit.getName())
                                    .replace("{elo}", String.valueOf(Profile.get(target.getUniqueId()).getKitData().get(kit).getElo())))));
                    continue;
                }
                player.sendMessage(CC.translate(s
                        .replace("{bars}", CC.CHAT_BAR)
                        .replace("{color}", Profile.get(target.getUniqueId()).getColor())
                        .replace("{player}", target.getName())));
            }
            return;
        }

        for (String s : HyPractice.get().getLangConfig().getStringList("ELO.VIEW_YOUR")) {
            if (s.contains("{format}")) {
                Kit.getKits().stream().filter(Kit::isEnabled).filter(kit -> kit.getGameRules().isRanked()).forEach(kit ->
                        player.sendMessage(CC.translate(HyPractice.get().getLangConfig().getString("ELO.VIEW_FORMAT")
                                .replace("{kit}", kit.getName())
                                .replace("{elo}", String.valueOf(Profile.get(player.getUniqueId()).getKitData().get(kit).getElo())))));
                continue;
            }
            player.sendMessage(CC.translate(s.replace("{bars}", CC.CHAT_BAR)));
        }
    }
}
