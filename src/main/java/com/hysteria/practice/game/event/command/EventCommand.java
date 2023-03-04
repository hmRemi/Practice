package com.hysteria.practice.game.event.command;

import com.hysteria.practice.Locale;
import com.hysteria.practice.game.event.game.command.*;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.game.event.game.map.command.EventMapCommand;
import com.hysteria.practice.game.event.game.map.command.EventMapsCommand;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;
import com.hysteria.practice.game.event.game.command.*;

public class EventCommand extends BaseCommand {

	public EventCommand() {
		super();
		new EventAddMapCommand();
		new EventAdminCommand();
		new EventRemoveMapCommand();
		new EventSetLobbyCommand();
		new EventCancelCommand();
		new EventClearCooldownCommand();
		new EventForceStartCommand();
		new EventInfoCommand();
		new EventJoinCommand();
		new EventLeaveCommand();
		new EventMapCommand();
		new EventMapsCommand();
	}

	@Command(name = "event")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		new MessageFormat(Locale.EVENT_HELP.format(Profile.get(player.getUniqueId()).getLocale())).send(player);
	}
}
