package com.hysteria.practice.essentials.chat.impl.format;

import com.hysteria.practice.essentials.chat.impl.ChatFormat;
import com.hysteria.practice.utilities.chat.CC;
import org.bukkit.entity.Player;

public class DefaultChatFormat implements ChatFormat {

    @Override
    public String format(Player sender, Player receiver, String message) {
        return CC.translate(sender.getDisplayName() + "&7:&f " +
            (sender.hasPermission("cpractice.chat.color") ? CC.translate(message) : CC.strip(message)));
    }

}
