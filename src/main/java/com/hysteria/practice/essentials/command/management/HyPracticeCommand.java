package com.hysteria.practice.essentials.command.management;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import lombok.val;
import org.bukkit.command.CommandSender;

public class HyPracticeCommand extends BaseCommand {

    @Command(name = "hypractice", aliases = {"practice"}, inGameOnly = false, permission = "cpractice.owner")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        CommandSender sender = commandArgs.getSender();
        String[] args = commandArgs.getArgs();

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            val start = System.currentTimeMillis();
            HyPractice.get().getMainConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getLang().reload();
            sender.sendMessage(CC.translate("&7▢ &bLang reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getLangConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bLangConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getHotbarConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bHotbarConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getAbilityConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bAbilityConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getArenasConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bArenasConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getEventsConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bEventsConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getKitsConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bKitsConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getKiteditorConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bKiteditorConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getLeaderboardConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bLeaderboardConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getScoreboardConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bScoreboardConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getTabLobbyConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bTabLobbyConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getTabSingleFFAFightConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bTabSingleFFAFightConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getTabSingleTeamFightConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bTabSingleTeamFightConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getTabPartyFFAFightConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bTabPartyFFAFightConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getTabPartyTeamFightConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bTabPartyTeamFightConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getTabEventConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bTabEventConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getTabFFAConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bTabFFAConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getMenuConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &bMenuConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getEssentials().setMotd(CC.translate(HyPractice.get().getLangConfig().getStringList("MOTD")));
            sender.sendMessage(CC.translate("&7▢ &bMOTD reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            val finish = System.currentTimeMillis();
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage(CC.translate(" &7▢ &bHyPractice &fhas been reloaded. &8(&7" + (finish - start) + "&8) &7▢"));
            sender.sendMessage(CC.CHAT_BAR);
            return;
        }

        if(args.length > 0 && args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage(CC.translate("&b&lSetup &7&m-&r &b&lHelp"));
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage(CC.translate(" &7▢ &b/arena &8(&7&oSetup arenas&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &b/event &8(&7&oSetup events&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &b/kit &8(&7&oSetup kits&8&o)"));
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage(CC.translate("&b&lCommands &7&m-&r &b&lHelp"));
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage(CC.translate(" &7▢ &b/leaderboard &8(&7&oView leaderboard&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &b/settings &8(&7&oView player settings&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &b/party &8(&7&oParty commands&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &b/clan &8(&7&oClan commands&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &b/stats &8(&7&oView player stats&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &b/fly &8(&7&oToggle flight&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &b/ffa &8(&7&oFFA commands&8&o)"));
            sender.sendMessage(CC.CHAT_BAR);
            return;
        }
        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.translate("&b&lHYPRACTICE &7made by &bziue"));
        sender.sendMessage(CC.translate("&7Version: &b" + HyPractice.get().getDescription().getVersion()));
        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.translate("&b&lAdmin &7&m-&r &b&lHelp"));
        sender.sendMessage(CC.translate(" &7▢ &b/hypractice reload &8(&7&oReload configs&8&o)"));
        sender.sendMessage(CC.translate(" &7▢ &b/hypractice help &8(&7&oView help command&8&o)"));
        sender.sendMessage(CC.CHAT_BAR);
    }
}