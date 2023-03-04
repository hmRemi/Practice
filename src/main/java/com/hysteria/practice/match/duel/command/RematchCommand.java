package com.hysteria.practice.match.duel.command;

import com.hysteria.practice.Locale;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.meta.ProfileRematchData;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.chat.CC;
import net.audidevelopment.core.plugin.cCore;
import org.bukkit.entity.Player;

public class RematchCommand extends BaseCommand {

	@Command(name = "rematch")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		Profile profile = Profile.get(player.getUniqueId());
		ProfileRematchData rematchData = profile.getRematchData();

		if(cCore.INSTANCE.getServerManagement().isServerRebooting()) {
			player.sendMessage(CC.translate("&7You cannot rematch during a reboot"));
			return;
		}

		if (rematchData == null) {
			new MessageFormat(Locale.REMATCH_DO_NOT_HAVE_ANYONE.format(profile.getLocale()))
					.send(player);
			return;
		}

		rematchData.validate();

		if (rematchData.isCancelled()) {
			new MessageFormat(Locale.REMATCH_CANCELLED.format(profile.getLocale()))
					.send(player);
			return;
		}

		if (rematchData.isReceive()) {
			rematchData.accept();
		} else {
			if (rematchData.isSent()) {
				new MessageFormat(Locale.REMATCH_IS_SENT.format(profile.getLocale()))
						.send(player);
				return;
			}

			rematchData.request();
		}
	}
}
