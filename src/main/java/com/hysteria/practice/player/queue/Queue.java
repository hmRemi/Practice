package com.hysteria.practice.player.queue;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.player.profile.hotbar.Hotbar;
import com.hysteria.practice.player.queue.task.QueueTask;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.TaskUtil;
import com.hysteria.practice.utilities.menu.Menu;
import com.hysteria.practice.Locale;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.player.queue.menus.QueueSelectKitMenu;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public class Queue {

	@Getter private static final List<Queue> queues = new ArrayList<>();
	@Getter public static Menu unRankedMenu = new QueueSelectKitMenu(false);
	@Getter public static Menu rankedMenu = new QueueSelectKitMenu(true);

	private final UUID uuid = UUID.randomUUID();
	private final Kit kit;
	private final boolean ranked;
	private final LinkedList<QueueProfile> players = new LinkedList<>();

	public Queue(Kit kit, boolean ranked) {
		this.kit = kit;
		this.ranked = ranked;

		queues.add(this);
	}

	public String getQueueName() {
		return (ranked ? "Ranked" : "Unranked") + " " + kit.getName();
	}

	public void addPlayer(Player player, int elo) {
		QueueProfile queueProfile = new QueueProfile(this, player.getUniqueId());
		queueProfile.setElo(elo);

		Profile profile = Profile.get(player.getUniqueId());
		profile.setQueueProfile(queueProfile);
		profile.setState(ProfileState.QUEUEING);

		players.add(queueProfile);

		Hotbar.giveHotbarItems(player);

		if (ranked) {
			//player.sendMessage(Locale.QUEUE_JOIN_RANKED.format(kit.getName(), elo));
			new MessageFormat(Locale.QUEUE_JOIN_RANKED.format(profile.getLocale()))
				.add("{kit_name}", kit.getName())
				.add("{elo}", String.valueOf(elo))
				.send(player);
		} else {
			//player.sendMessage(Locale.QUEUE_JOIN_UNRANKED.format(kit.getName()));

			//for (String message : HyPractice.get().getLang().getStringList("QUEUE.JOIN_UNRANKED")) {
			new MessageFormat(Locale.QUEUE_JOIN_UNRANKED.format(profile.getLocale())).add("{kit_name}", kit.getName()).add("{pingrange}", "" + (profile.getPingRange() == -1 ? "Unrestricted" : Integer.valueOf(profile.getPingRange()))).send(player);
			//}
			//new MessageFormat(Locale.QUEUE_JOIN_UNRANKED.format(profile.getLocale())).add("{kit_name}", kit.getName()).send(player);
		}
	}

	public void removePlayer(QueueProfile queueProfile) {
		players.remove(queueProfile);

		Profile profile = Profile.get(queueProfile.getPlayerUuid());
		profile.setQueueProfile(null);
		profile.setState(ProfileState.LOBBY);

		Player player = Bukkit.getPlayer(queueProfile.getPlayerUuid());

		if (player != null) {
			Hotbar.giveHotbarItems(player);

			if (ranked) {
				//player.sendMessage(Locale.QUEUE_LEAVE_RANKED.format(kit.getName()));
				new MessageFormat(Locale.QUEUE_LEAVE_RANKED.format(profile.getLocale()))
					.add("{kit_name}", kit.getName())
					.send(player);
			} else {
				//player.sendMessage(Locale.QUEUE_LEAVE_UNRANKED.format(kit.getName()));
				new MessageFormat(Locale.QUEUE_LEAVE_UNRANKED.format(profile.getLocale()))
					.add("{kit_name}", kit.getName())
					.send(player);
			}
		}

	}

	public static Queue getByUuid(UUID uuid) {
		for (Queue queue : queues) {
			if (queue.getUuid().equals(uuid)) {
				return queue;
			}
		}

		return null;
	}

	public static void init() {
		TaskUtil.runTimer(new QueueTask(), 10L, 10L);
		TaskUtil.runTimer(() -> {
			HyPractice.get().inQueues = (int) Profile.getProfiles().values()
					.stream()
					.filter(profile -> profile.getPlayer() != null && profile.getPlayer().isOnline())
					.filter(profile -> profile.getState() == ProfileState.QUEUEING)
					.count();
			HyPractice.get().inFights = (int) Profile.getProfiles().values()
					.stream()
					.filter(profile -> profile.getPlayer() != null && profile.getPlayer().isOnline())
					.filter(profile -> profile.getState() == ProfileState.FIGHTING || profile.getState() == ProfileState.EVENT)
					.count();
		}, 2L, 2L);
	}
}
