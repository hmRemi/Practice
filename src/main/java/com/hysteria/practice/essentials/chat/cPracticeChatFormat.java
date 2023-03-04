package com.hysteria.practice.essentials.chat;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.essentials.chat.impl.ChatFormat;
import com.hysteria.practice.player.clan.Clan;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.chat.CC;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class cPracticeChatFormat implements ChatFormat {
    @Override
    public String format(Player sender, Player receiver, String message) {
        Profile senderProfile = Profile.get(sender.getUniqueId());
        if (Clan.getByPlayer(sender) != null) {
            if (HyPractice.get().isPlaceholderAPI())
                return PlaceholderAPI.setPlaceholders(sender, CC.translate(HyPractice.get().getMainConfig().getString("CHAT.CLAN_FORMAT")
                        .replace("{prefix}", HyPractice.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                        .replace("{suffix}", HyPractice.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                        .replace("{color}", senderProfile.getColor())
                        .replace("{player}", sender.getName())
                        .replace("{message}", (sender.hasPermission("cpractice.chat.color") ? CC.translate(message) : CC.strip(message)))
                        .replace("{clan}", Clan.getByPlayer(sender).getColoredName())));
            return CC.translate(HyPractice.get().getMainConfig().getString("CHAT.CLAN_FORMAT")
                    .replace("{prefix}", HyPractice.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                    .replace("{suffix}", HyPractice.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                    .replace("{color}", senderProfile.getColor())
                    .replace("{player}", sender.getName())
                    .replace("{message}", (sender.hasPermission("cpractice.chat.color") ? CC.translate(message) : CC.strip(message)))
                    .replace("{clan}", Clan.getByPlayer(sender).getColoredName()));
        }
        if (HyPractice.get().isPlaceholderAPI())
            return PlaceholderAPI.setPlaceholders(sender, CC.translate(HyPractice.get().getMainConfig().getString("CHAT.DEFAULT_FORMAT")
                    .replace("{prefix}", HyPractice.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                    .replace("{suffix}", HyPractice.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                    .replace("{color}", senderProfile.getColor())
                    .replace("{player}", sender.getName())
                    .replace("{message}", (sender.hasPermission("cpractice.chat.color") ? CC.translate(message) : CC.strip(message)))));
        return CC.translate(HyPractice.get().getMainConfig().getString("CHAT.DEFAULT_FORMAT")
                .replace("{prefix}", HyPractice.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                .replace("{suffix}", HyPractice.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                .replace("{color}", senderProfile.getColor())
                .replace("{player}", sender.getName())
                .replace("{message}", (sender.hasPermission("cpractice.chat.color") ? CC.translate(message) : CC.strip(message))));
    }
}