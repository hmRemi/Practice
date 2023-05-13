package com.hysteria.practice.player.party.menu.manage.buttons;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.Locale;
import com.hysteria.practice.player.party.enums.PartyPrivacy;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.Cooldown;
import com.hysteria.practice.utilities.ItemBuilder;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.TimeUtil;
import com.hysteria.practice.utilities.chat.ChatComponentBuilder;
import com.hysteria.practice.utilities.chat.ChatHelper;
import com.hysteria.practice.utilities.chat.Clickable;
import com.hysteria.practice.utilities.menu.Button;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.hysteria.practice.utilities.chat.CC;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/24/2023
 */
public class PartyAnnounceButton extends Button {

    @Override
    public boolean shouldUpdate(Player player, ClickType clickType) {
        return true;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Profile.get(player.getUniqueId());

        String time;

        if(profile.getPartyAnnounceCooldown().hasExpired()) {
            time = "0";
        } else {
            time = TimeUtil.millisToSeconds(profile.getPartyAnnounceCooldown().getRemaining());
        }

        List<String> lore = new ArrayList<>();
        HyPractice.get().getMenuConfig().getStringList("PARTY-MENU.ANNOUNCE.LORE").forEach(s -> lore.add(s.replace("{time}", time)));

        return new ItemBuilder(Material.REDSTONE)
                .name(HyPractice.get().getMenuConfig().getString("PARTY-MENU.ANNOUNCE.NAME"))
                .lore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.get(player.getUniqueId());
        String time;

        playNeutral(player);
        if(profile.getPartyAnnounceCooldown().hasExpired()) {
            time = "0";
        } else {
            time = TimeUtil.millisToSeconds(profile.getPartyAnnounceCooldown().getRemaining());
        }

        if (profile.getPartyAnnounceCooldown().hasExpired()) {
            if(profile.getParty().getPrivacy() != PartyPrivacy.OPEN) { // If party isn't public while command is executed, make party public.
                profile.getParty().setPrivacy(PartyPrivacy.OPEN);
            }

            profile.setPartyAnnounceCooldown(new Cooldown(60_000));

            sendAnnouncement(player);

        } else {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.translate(" &7* &fYou are on cooldown."));
            player.sendMessage(CC.translate(" &7* &fRemaining time: &6" + time));
            player.sendMessage(CC.CHAT_BAR);
        }
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
