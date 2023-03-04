package com.hysteria.practice.game.kit.command;

import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitCommand extends BaseCommand {

    public KitCommand() {
        super();
        new KitCreateCommand();
        new KitGameRuleCommand();
        new KitGetLoadoutCommand();
        new KitSetLoadoutCommand();
        new KitSetIconCommand();
        new KitToggleComand();
        new KitDeleteCommand();
        new KitStatusCommand();
        new KitRulesCommand();
        new KitSetSlotCommand();
    }

    @Command(name = "kit", permission = "cpractice.kit.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&c&lKit &7&m-&r &c&lHelp"));
        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate(" &7▢ &4/kit &8(&7&oShows this help menu&8&o)"));
        player.sendMessage(CC.translate(" &7▢ &4/kits &8(&7&oList all kits&8&o)"));
        player.sendMessage(CC.translate(" &7▢ &4/kit delete &8<&7kit&8> &8(&7&oDelete a kit&8&o)"));
        player.sendMessage(CC.translate(" &7▢ &4/kit create &8<&7kit&8> &8(&7&oCreate a kit&8&o)"));
        player.sendMessage(CC.translate(" &7▢ &4/kit toggle &8<&7kit&8> &8(&7&oToggle a kit&8&o)"));
        player.sendMessage(CC.translate(" &7▢ &4/kit seticon &8<&7kit&8> &8(&7&oSet a kit's icon&8&o)"));
        player.sendMessage(CC.translate(" &7▢ &4/kit rules &8(&7&oShows the rules of kits&8&o)"));
        player.sendMessage(CC.translate(" &7▢ &4/kit setslot &8<&7kit&8> &8<&7slot&8> &8(&7&oSet a kit's slot&8&o)"));
        player.sendMessage(CC.translate(" &7▢ &4/kit getloadout &8<&7kit&8> &8(&7&oGet a kit's loadout&8&o)"));
        player.sendMessage(CC.translate(" &7▢ &4/kit setloadout &8<&7kit&8> &8(&7&oSet a kit's loadout&8&o)"));
        player.sendMessage(CC.translate(" &7▢ &4/kit status &8<&7kit&8> &8(&7&oShows the status of kits&8&o)"));
        player.sendMessage(CC.translate(" &7▢ &4/kit setrule &8<&7kit&8> &8<&7rule&8> &8<&7value&8> >&8(&7&oSet a kit's rules&8&o)"));
        player.sendMessage(CC.CHAT_BAR);
    }
}
