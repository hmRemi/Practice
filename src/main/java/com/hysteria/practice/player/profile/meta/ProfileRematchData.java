package com.hysteria.practice.player.profile.meta;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.Locale;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.match.impl.BasicTeamLivesFight;
import com.hysteria.practice.match.impl.BasicTeamMatch;
import com.hysteria.practice.match.impl.BasicTeamRoundMatch;
import com.hysteria.practice.match.participant.MatchGamePlayer;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.player.profile.hotbar.Hotbar;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.chat.ChatComponentBuilder;
import com.hysteria.practice.utilities.chat.ChatHelper;
import com.hysteria.practice.match.impl.BasicTeamBedFight;
import com.hysteria.practice.player.profile.participant.alone.GameParticipant;
import lombok.Getter;
import lombok.var;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class ProfileRematchData {

	private final UUID key;
	private final UUID sender;
	private final UUID target;
	private final Kit kit;
	private final Arena arena;
	private boolean sent;
	private boolean receive;
	private boolean cancelled;
	private final long timestamp;

	public ProfileRematchData(UUID key, UUID sender, UUID target, Kit kit, Arena arena) {
		this.key = key;
		this.sender = sender;
		this.target = target;
		this.kit = kit;
		this.arena = arena;
		this.timestamp = System.currentTimeMillis();
	}

	public void request() {
		this.validate();

		if (cancelled) {
			return;
		}

		Player sender = Bukkit.getPlayer(this.sender);
		Player target = Bukkit.getPlayer(this.target);

		if (sender == null || target == null) {
			return;
		}

		Profile senderProfile = Profile.get(sender.getUniqueId());
		Profile targetProfile = Profile.get(target.getUniqueId());

		if (senderProfile.isBusy()) {
			sender.sendMessage(CC.RED + "You cannot duel right now.");
			return;
		}

		if (targetProfile.isBusy()) {
			sender.sendMessage(CC.RED + "You cannot duel right now.");
			return;
		}

		for (String line : new MessageFormat(Locale.REMATCH_SENT_REQUEST.format(Profile.get(sender.getUniqueId()).getLocale()))
							.add("{target_name}", target.getName())
							.add("{arena_name}", arena.getName())
							.toList()) {
			sender.sendMessage(line);
		}

		/*List<BaseComponent[]> components = new ArrayList<>();

		for (String line : new MessageFormat(Locale.REMATCH_RECEIVED_REQUEST
				.format(Profile.get(target.getUniqueId()).getLocale()))
				.add("{sender_name}", sender.getName())
				.add("{arena_name}", arena.getName())
				.toList()) {
			BaseComponent[] lineComponents = new ChatComponentBuilder("")
					.parse(line)
					.attachToEachPart(ChatHelper.hover(Locale.REMATCH_RECEIVED_REQUEST_HOVER.format(Profile.get(target.getUniqueId()).getLocale()).toString()))
					.attachToEachPart(ChatHelper.click("/rematch"))
					.create();

			components.add(lineComponents);
		}

		for (BaseComponent[] line : components) {
			target.spigot().sendMessage(line);
		}*/

		for (String msg : new MessageFormat(Locale.REMATCH_RECEIVED_REQUEST
				.format(Profile.get(target.getUniqueId()).getLocale()))
				.add("{kit_name}", kit.getDisplayName())
				.add("{sender_name}", sender.getName())
				.add("{arena_name}", arena.getName())
				.toList()) {
			if (msg.contains("%CLICKABLE%")) {
				ChatComponentBuilder builder = new ChatComponentBuilder(new MessageFormat(Locale.REMATCH_RECEIVED_CLICKABLE
						.format(Profile.get(target.getUniqueId()).getLocale()))
						.add("{sender_name}", sender.getName())
						.toString());

				builder.attachToEachPart(ChatHelper.click("/rematch " + sender.getName()));
				builder.attachToEachPart(ChatHelper.hover(new MessageFormat(Locale.REMATCH_RECEIVED_REQUEST_HOVER
						.format(Profile.get(target.getUniqueId()).getLocale()).toString())
						.toString()));

				target.spigot().sendMessage(builder.create());
			} else {
				target.sendMessage(msg);
			}
		}

		this.sent = true;

		targetProfile.getRematchData().receive = true;
	}

	public void accept() {
		this.validate();

		Player sender = HyPractice.get().getServer().getPlayer(this.sender);
		Player target = HyPractice.get().getServer().getPlayer(this.target);

		if (sender == null || target == null || !sender.isOnline() || !target.isOnline()) {
			return;
		}

		Profile senderProfile = Profile.get(sender.getUniqueId());
		Profile targetProfile = Profile.get(target.getUniqueId());

		if (senderProfile.isBusy()) {
			sender.sendMessage(CC.RED + "You cannot duel right now.");
			return;
		}

		if (targetProfile.isBusy()) {
			sender.sendMessage(target.getDisplayName() + CC.RED + " is currently busy.");
			return;
		}

		Arena arena = this.arena;

		if (arena == null || arena.isActive()) {
			arena = Arena.getRandomArena(kit);
		}

		if (arena == null) {
			sender.sendMessage(CC.RED + "Tried to start a match but there are no available arenas.");
			return;
		}

		arena.setActive(true);

		MatchGamePlayer playerA = new MatchGamePlayer(sender.getUniqueId(), sender.getName());
		MatchGamePlayer playerB = new MatchGamePlayer(target.getUniqueId(), target.getName());

		GameParticipant<MatchGamePlayer> participantA = new GameParticipant<>(playerA);
		GameParticipant<MatchGamePlayer> participantB = new GameParticipant<>(playerB);

		var match = new BasicTeamMatch(null, kit, arena, false, participantA, participantB);
		if(match.getKit().getGameRules().isBridge()){
			match = new BasicTeamRoundMatch(null, kit, arena, false, participantA, participantB, HyPractice.get().getBridgeRounds());
		} else if(match.getKit().getGameRules().isBedFight()){
			match = new BasicTeamBedFight(null, kit, arena, false, participantA, participantB);
		} else if(match.getKit().getGameRules().isLives()){
			match = new BasicTeamLivesFight(null, kit, arena, false, participantA, participantB);
		}
		match.start();
	}

	public void validate() {
		for (UUID uuid : new UUID[]{ sender, target }) {
			Player player = Bukkit.getPlayer(uuid);

			if (player != null) {
				Profile profile = Profile.get(player.getUniqueId());

				if (profile.getRematchData() == null) {
					this.cancel();
					return;
				}

				if (!profile.getRematchData().getKey().equals(this.key)) {
					this.cancel();
					return;
				}

				if (!(profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.QUEUEING)) {
					this.cancel();
					return;
				}

				if (System.currentTimeMillis() >= timestamp + 30_000L) {
					this.cancel();
					return;
				}
			}
		}
	}

	public void cancel() {
		this.cancelled = true;

		for (UUID uuid : new UUID[]{ sender, target }) {
			Player player = Bukkit.getPlayer(uuid);

			if (player != null) {
				Profile profile = Profile.get(player.getUniqueId());
				profile.setRematchData(null);

				if (profile.getState() == ProfileState.LOBBY) {
					Hotbar.giveHotbarItems(player);
				}
			}
		}
	}

}
