package com.hysteria.practice.essentials.command.management;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import lombok.val;
import org.bukkit.command.CommandSender;

public class cPracticeCommand extends BaseCommand {

    @Command(name = "cpractice", aliases = {"cpractice"}, inGameOnly = false, permission = "cpractice.owner")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        CommandSender sender = commandArgs.getSender();
        String[] args = commandArgs.getArgs();

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            val start = System.currentTimeMillis();
            HyPractice.get().getMainConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4Config reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getLang().reload();
            sender.sendMessage(CC.translate("&7▢ &4Lang reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getLangConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4NPCs reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getNpcConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4Pearls reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getPearlConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4LangConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getHotbarConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4HotbarConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getAbilityConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4AbilityConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getArenasConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4ArenasConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getEventsConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4EventsConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getKitsConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4KitsConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getKiteditorConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4KiteditorConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getLeaderboardConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4LeaderboardConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getScoreboardConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4ScoreboardConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getTabLobbyConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4TabLobbyConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getTabSingleFFAFightConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4TabSingleFFAFightConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getTabSingleTeamFightConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4TabSingleTeamFightConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getTabPartyFFAFightConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4TabPartyFFAFightConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getTabPartyTeamFightConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4TabPartyTeamFightConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getTabEventConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4TabEventConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getTabFFAConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4TabFFAConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getMenuConfig().reload();
            sender.sendMessage(CC.translate("&7▢ &4MenuConfig reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            HyPractice.get().getEssentials().setMotd(CC.translate(HyPractice.get().getLangConfig().getStringList("MOTD")));
            sender.sendMessage(CC.translate("&7▢ &4MOTD reloaded in &8(&7" + (System.currentTimeMillis() - start) + "ms &8) &7▢"));
            val finish = System.currentTimeMillis();
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage(CC.translate(" &7▢ &4cPractice &fhas been reloaded. &8(&7" + (finish - start) + "&8) &7▢"));
            sender.sendMessage(CC.CHAT_BAR);
            return;
        }

        if(args.length > 0 && args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage(CC.translate("&c&lSetup &7&m-&r &c&lHelp"));
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage(CC.translate(" &7▢ &4/arena &8(&7&oSetup arenas&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &4/event &8(&7&oSetup events&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &4/kit &8(&7&oSetup kits&8&o)"));
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage(CC.translate("&c&lCommands &7&m-&r &c&lHelp"));
            sender.sendMessage(CC.CHAT_BAR);
            sender.sendMessage(CC.translate(" &7▢ &4/cpractice &8(&7&oMain commands&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &4/leaderboard &8(&7&oView leaderboard&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &4/settings &8(&7&oView player settings&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &4/party &8(&7&oParty commands&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &4/clan &8(&7&oClan commands&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &4/stats &8(&7&oView player stats&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &4/fly &8(&7&oToggle flight&8&o)"));
            sender.sendMessage(CC.translate(" &7▢ &4/ffa &8(&7&oFFA commands&8&o)"));
            sender.sendMessage(CC.CHAT_BAR);
            return;
        }
        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.translate("&c&lcPRACTICE &7made by &4ziue"));
        sender.sendMessage(CC.translate("&7Version: &4" + HyPractice.get().getDescription().getVersion()));
        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.translate("&c&lAdmin &7&m-&r &c&lHelp"));
        sender.sendMessage(CC.translate(" &7▢ &4/cpractice reload &8(&7&oReload configs&8&o)"));
        sender.sendMessage(CC.translate(" &7▢ &4/cpractice help &8(&7&oView help command&8&o)"));
        sender.sendMessage(CC.CHAT_BAR);
    }
}