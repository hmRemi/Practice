package rip.crystal.practice.shop.command;

import org.bukkit.entity.Player;
import rip.crystal.practice.api.chat.ChatUtil;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.chat.CC;

public class CoinsCommand extends BaseCommand {

    @Command(name="coins", aliases = {"coin"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());

        CC.sender(player, ChatUtil.NORMAL_LINE);
        CC.sender(player, " &4&lCoins");
        CC.sender(player, "");
        CC.sender(player, "&4Your coins: &7" + profile.getCoins());
        CC.sender(player, ChatUtil.NORMAL_LINE);
    }
}
