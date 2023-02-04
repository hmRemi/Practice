package rip.crystal.practice.game.arena.command;

import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import org.bukkit.command.CommandSender;


public class ArenaCommand extends BaseCommand {

    public ArenaCommand() {
        super();
        new ArenaAddKitCommand();
        new ArenaCreateCommand();
        new ArenaDeleteCommand();
        new ArenaGenerateCommand();
        new ArenaGenHelperCommand();
        new ArenaRemoveKitCommand();
        new ArenaSaveCommand();
        new ArenaManageCommand();
        new ArenaSelectionCommand();
        new ArenaSetSpawnCommand();
        new ArenaStatusCommand();
        new ArenaSetAuthorCommand();
        new ArenaTeleportCommand();
        new ArenaSetIconCommand();
    }

    @Command(name = "arena", permission = "cpractice.arena.admin", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs commandArgs) {
        CommandSender sender = commandArgs.getSender();

        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.translate("&4&lArena &7&m-&r &4&lHelp"));
        sender.sendMessage(CC.translate(" &7▢ &4/arena create &8<&7arena&8> &8<&7SHARED/STANDALONE&8>  &8(&7&oCreate an arena&8&o)"));
        sender.sendMessage(CC.translate(" &7▢ &4/arena setspawn &8<&7arena&8> &8<&7(a/b/[red/blue])&8>  &8(&7&oSet the spawns for an arena&8&o)"));
        sender.sendMessage(CC.translate(" &7▢ &4/arena setauthor &8<&7arena&8> &8<&7author&8>  &8(&7&oSet the author of an arena&8&o)"));
        sender.sendMessage(CC.translate(" &7▢ &4/arena removekit &8<&7arena&8> &8<&7kit&8>  &8(&7&oRemove a kit from an arena&8&o)"));
        sender.sendMessage(CC.translate(" &7▢ &4/arena addkit &8<&7arena&8> &8<&7kit&8>  &8(&7&oAdd a kit to an arena&8&o)"));
        sender.sendMessage(CC.translate(" &7▢ &4/arena teleport &8<&7arena&8> &8(&7&oTeleport to an arena&8&o)"));
        sender.sendMessage(CC.translate(" &7▢ &4/arena seticon &8<&7arena&8> &8(&7&oSet an icon for the arena&8&o)"));
        sender.sendMessage(CC.translate(" &7▢ &4/arena status &8<&7arena&8> &8(&7&oView status of all arenas&8&o)"));
        sender.sendMessage(CC.translate(" &7▢ &4/arena delete &8<&7arena&8> &8(&7&oDelete an arena&8&o)"));
        sender.sendMessage(CC.translate(" &7▢ &4/arena manage &8(&7&oOpen a gui to manage arenas&8&o)"));
        sender.sendMessage(CC.translate(" &7▢ &4/arena wand &8(&7&oGet the arena selection tool&8&o)"));
        sender.sendMessage(CC.translate(" &7▢ &4/arenas &8(&7&oList all arenas&8&o)"));
        sender.sendMessage(CC.CHAT_BAR);
    }
}
