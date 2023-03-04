package com.hysteria.practice.match.duel;

import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.chat.ChatComponentBuilder;
import com.hysteria.practice.utilities.chat.ChatHelper;
import com.hysteria.practice.Locale;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.utilities.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter @Setter
public class DuelProcedure {

	private Player sender;
	private UUID target;
	private boolean party;
	private Kit kit;
	private Arena arena;
	private int rounds;

	public DuelProcedure(Player sender, Player target, boolean party) {
		this.sender = sender;
		this.target = target.getUniqueId();
		this.party = party;
	}

	public void send() {
		Player target = Bukkit.getPlayer(this.target);

		if (!sender.isOnline() || target == null || !target.isOnline()) return;

		DuelRequest duelRequest = new DuelRequest(sender.getUniqueId(), target.getUniqueId(), party);
		duelRequest.setRounds(rounds);
		duelRequest.setKit(kit);
		duelRequest.setArena(arena);

		Profile senderProfile = Profile.get(sender.getUniqueId());
		senderProfile.setDuelProcedure(null);

		Profile targetProfile = Profile.get(target.getUniqueId());
		targetProfile.getDuelRequests().add(duelRequest);

		if (party) {
			new MessageFormat(Locale.DUEL_SENT_PARTY
				.format(senderProfile.getLocale()))
				.add("{kit_name}", kit.getName())
				.add("{target_name}", target.getName())
				.add("{arena_name}", arena.getName())
				.add("{party_size}", String.valueOf(targetProfile.getParty().getPlayers().size()))
				.send(sender);

			for (String msg : new MessageFormat(Locale.DUEL_RECEIVED_PARTY
								.format(targetProfile.getLocale()))
								.add("{kit_name}", kit.getName())
								.add("{sender_name}", sender.getName())
								.add("{arena_name}", arena.getName())
								.add("{party_size}", String.valueOf(targetProfile.getParty().getPlayers().size()))
								.toList()) {
				if (msg.contains("%CLICKABLE%")) {
					ChatComponentBuilder builder = new ChatComponentBuilder(new MessageFormat(Locale.DUEL_RECEIVED_CLICKABLE
						.format(targetProfile.getLocale()))
						.add("{sender_name}", sender.getName())
						.toString());
					builder.attachToEachPart(ChatHelper.click("/duel accept " + sender.getName()));
					builder.attachToEachPart(ChatHelper.hover(new MessageFormat(Locale.DUEL_RECEIVED_HOVER
						.format(targetProfile.getLocale()))
						.toString()));

					target.spigot().sendMessage(builder.create());
				} else {
					target.sendMessage(msg);
				}
			}
		} else {
			//sender.sendMessage(Locale.DUEL_SENT.format(kit.getName(), target.getName(), arena.getName()));

			if(arena.getName() != null) {
				new MessageFormat(Locale.DUEL_SENT
						.format(senderProfile.getLocale()))
						.add("{kit_name}", kit.getName())
						.add("{target_name}", target.getName())
						.add("{arena_name}", arena.getName())
						.send(sender);

				for (String msg : new MessageFormat(Locale.DUEL_RECEIVED
						.format(targetProfile.getLocale()))
						.add("{kit_name}", kit.getName())
						.add("{sender_name}", sender.getName())
						.add("{arena_name}", arena.getName())
						.toList()) {
					if (msg.contains("%CLICKABLE%")) {
						ChatComponentBuilder builder = new ChatComponentBuilder(new MessageFormat(Locale.DUEL_RECEIVED_CLICKABLE
								.format(targetProfile.getLocale()))
								.add("{sender_name}", sender.getName())
								.toString());
						builder.attachToEachPart(ChatHelper.click("/duel accept " + sender.getName()));
						builder.attachToEachPart(ChatHelper.hover(new MessageFormat(Locale.DUEL_RECEIVED_HOVER
								.format(targetProfile.getLocale()))
								.toString()));

						target.spigot().sendMessage(builder.create());
					} else {
						target.sendMessage(msg);
					}
				}
			} else {
				sender.sendMessage(CC.translate("&7No arenas found"));
			}
		}
	}

}
