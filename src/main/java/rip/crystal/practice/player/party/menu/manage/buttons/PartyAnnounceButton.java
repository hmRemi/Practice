package rip.crystal.practice.player.party.menu.manage.buttons;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.crystal.practice.player.party.enums.PartyPrivacy;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.shop.impl.killeffects.KillEffectsShopMenu;
import rip.crystal.practice.utilities.Cooldown;
import rip.crystal.practice.utilities.ItemBuilder;
import rip.crystal.practice.utilities.TimeUtil;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.chat.Clickable;
import rip.crystal.practice.utilities.menu.Button;

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
        lore.add("&8&m--------------------------------------");
        lore.add("&7Cooldown: " + time);
        lore.add("");
        lore.add("&7&oClick here to advertise your party!");
        lore.add("&8&m--------------------------------------");


        return new ItemBuilder(Material.REDSTONE)
                .name("&c&lParty Announcement")
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

            Bukkit.broadcastMessage(CC.CHAT_BAR);
            Bukkit.broadcastMessage(CC.translate("&c&lPARTY ANNOUNCEMENT"));
            Bukkit.broadcastMessage(CC.translate("  &7* &c&l" + player.getName() + " &7is hosting a public party!"));
            Clickable clickHereToJoin = new Clickable(CC.translate("  &7* &7Click &c&lhere &7to join"), CC.translate("&7Click here to join"), "/p join " + player.getName());
            Bukkit.getOnlinePlayers().forEach(clickHereToJoin::sendToPlayer);
            Bukkit.broadcastMessage(CC.CHAT_BAR);
        } else {
            player.sendMessage(CC.CHAT_BAR);
            player.sendMessage(CC.translate("&c&lPARTY ANNOUNCEMENT"));
            player.sendMessage(CC.translate("  &7* &cYou are on cooldown."));
            player.sendMessage(CC.translate("  &7* &cRemaining time: &f" + time));
            player.sendMessage(CC.CHAT_BAR);
        }
    }
}
