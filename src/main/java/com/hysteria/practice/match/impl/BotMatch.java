package com.hysteria.practice.match.impl;

import com.hysteria.practice.Locale;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.match.MatchState;
import com.hysteria.practice.match.bot.Bot;
import com.hysteria.practice.match.bot.BotManager;
import com.hysteria.practice.match.participant.MatchGamePlayer;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.queue.Queue;
import com.hysteria.practice.utilities.BukkitReflection;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.chat.ChatComponentBuilder;
import com.hysteria.practice.utilities.file.type.BasicConfigurationFile;
import lombok.Getter;
import lombok.Setter;
import net.citizensnpcs.api.CitizensAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.match.Match;
import com.hysteria.practice.player.profile.participant.alone.GameParticipant;

import net.citizensnpcs.api.npc.NPC;

import java.util.*;

@Getter
public class BotMatch extends Match {

	private final GameParticipant<MatchGamePlayer> participantA;
	private @Setter GameParticipant<MatchGamePlayer> winningParticipant;
	private @Setter GameParticipant<MatchGamePlayer> losingParticipant;

	private final Bot.BotDifficulty difficulty;

	public BotMatch(Queue queue, Kit kit, Arena arena, boolean ranked, Bot.BotDifficulty difficulty, GameParticipant<MatchGamePlayer> participantA) {
		super(queue, kit, arena, ranked);

		this.difficulty = difficulty;
		this.participantA = participantA;
	}

	/**
	 * Set up the player according to {@link Kit},
	 * {@link Arena}
	 * <p>
	 * This also teleports the player to the specified arena,
	 * gives special potion effects if specified
	 *
	 * @param player {@link Player} being setup
	 */

	@Override
	public void setupPlayer(Player player) {
		super.setupPlayer(player);
		// Teleport the player to their spawn point
		HyPractice.get().getBotManager().createMatch(player, Kit.getByName("NoDebuff"), difficulty, Objects.requireNonNull(Arena.getRandomArena(Kit.getByName("NoDebuff"))));
	}

	@Override
	public void end() {
		if (participantA.getPlayers().size() == 1) {
			for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
				for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
					if (!gamePlayer.isDisconnected()) {

						if (gamePlayer.getPlayer() == null) {
							super.end();
							HyPractice.get().getBotManager().removeMatch(gamePlayer.getPlayer(), true);
							return;
						}
					}
				}
			}
		} else {
			if(HyPractice.get().getBotManager().getNpcRegistry().size() == 1) {
				for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
					for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
						if (!gamePlayer.isDisconnected()) {

							if (gamePlayer.getPlayer() == null) {
								super.end();
								HyPractice.get().getBotManager().removeMatch(gamePlayer.getPlayer(), false);
								return;
							}
						}
					}
				}
			}
		}

		super.end();
	}

	@Override
	public boolean canEndMatch() {
		return participantA.isAllDead() || HyPractice.get().getBotManager().getBotFromPlayer(participantA.getLeader().getPlayer()).getBukkitEntity().isDead();
	}

	@Override
	public boolean canStartRound() {
		return kit.getGameRules().isBridge();
	}

	@Override
	public void onRoundEnd() {
		super.onRoundEnd();
	}

	@Override
	public boolean canEndRound() {
		return participantA.isAllDead() || HyPractice.get().getBotManager().getBotFromPlayer(participantA.getLeader().getPlayer()).isDestroyed();
	}

	@Override
	public boolean isOnSameTeam(Player first, Player second) {
		return false;
	}

	@Override
	public List<GameParticipant<MatchGamePlayer>> getParticipants() {
		return Arrays.asList(participantA);
	}

	@Override
	public ChatColor getRelationColor(Player viewer, Player target) {
		return ChatColor.YELLOW;
	}

	@Override
	public List<String> getScoreboardLines(Player player) {
		List<String> lines = new ArrayList<>();
		BasicConfigurationFile config = HyPractice.get().getScoreboardConfig();
		String bars = config.getString("LINES.BARS");

		if (getParticipant(player) != null) {
			if (state == MatchState.STARTING_ROUND || state == MatchState.PLAYING_ROUND || state == MatchState.ENDING_ROUND) {
				if (participantA.getPlayers().size() == 1 && HyPractice.get().getBotManager().getNpcRegistry().size() == 1) {
					final Bot bot = HyPractice.get().getBotManager().getNpcRegistry().get(player.getUniqueId());

					config.getStringList("FIGHTS.1V1.LINES").forEach(line -> {
						if (line.contains("{bridge}")) return;
						if (line.contains("{rounds}")) return;
						lines.add(line.replace("{bars}", bars)
								.replace("{duration}", getDuration())
								.replace("{opponent}", bot.getBukkitEntity().getName())
								.replace("{opponent-ping}", String.valueOf(BukkitReflection.getPing(bot.getBukkitEntity().getPlayer())))
								.replace("{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
								.replace("{arena-author}", getArena().getAuthor())
								.replace("{kit}", getKit().getName()));
					});
				}
			}
		}

		return lines;
	}
	@Override
	public void addSpectator(Player spectator, Player target) {
		super.addSpectator(spectator, target);

		ChatColor firstColor;
		ChatColor secondColor;

		if (participantA.containsPlayer(target.getUniqueId())) {
			firstColor = ChatColor.GREEN;
			secondColor = ChatColor.RED;
		} else {
			firstColor = ChatColor.RED;
			secondColor = ChatColor.GREEN;
		}


		new MessageFormat(com.hysteria.practice.Locale.MATCH_START_SPECTATING.format(Profile.get(spectator.getUniqueId()).getLocale()))
			.add("{first_color}", firstColor.toString())
			.add("{participant_a}", participantA.getConjoinedNames())
			.add("{second_color}", secondColor.toString())
			.send(spectator);
	}

	@Override
	public List<BaseComponent[]> generateEndComponents(Player player) {
		List<BaseComponent[]> componentsList = new ArrayList<>();
		Profile profile = Profile.get(player.getUniqueId());

		for (String line : com.hysteria.practice.Locale.MATCH_END_DETAILS.getStringList(profile.getLocale())) {
			if (line.equalsIgnoreCase("%INVENTORIES%")) {

				BaseComponent[] winners = generateInventoriesComponents(
					new MessageFormat(com.hysteria.practice.Locale.MATCH_END_WINNER_INVENTORY.format(profile.getLocale()))
						.add("{context}", participantA.getPlayers().size() == 1 ? "" : "s")
						.toString(), winningParticipant);

				BaseComponent[] losers = generateInventoriesComponents(
					new MessageFormat(com.hysteria.practice.Locale.MATCH_END_LOSER_INVENTORY.format(profile.getLocale()))
						.add("{context}", HyPractice.get().getBotManager().getNpcRegistry().size() > 1 ? "s" : "").toString(), losingParticipant);


				if (participantA.getPlayers().size() == 1 && HyPractice.get().getBotManager().getNpcRegistry().size() == 1) {
					ChatComponentBuilder builder = new ChatComponentBuilder("");

					for (BaseComponent component : winners) {
						builder.append((TextComponent) component);
					}

					builder.append(new ChatComponentBuilder("&7 - ").create());

					for (BaseComponent component : losers) {
						builder.append((TextComponent) component);
					}

					componentsList.add(builder.create());
				} else {
					componentsList.add(winners);
					componentsList.add(losers);
				}

				continue;
			}

			if (line.equalsIgnoreCase("%ELO_CHANGES%")) {
				if (participantA.getPlayers().size() == 1 && HyPractice.get().getBotManager().getNpcRegistry().size() == 1 && ranked) {
					List<String> sectionLines = new MessageFormat(Locale.MATCH_ELO_CHANGES.getStringList(profile.getLocale()))
						.add("{winning_name}", winningParticipant.getConjoinedNames())
						.add("{winning_elo_mod}", String.valueOf(winningParticipant.getLeader().getEloMod()))
						.add("{winning_elo_mod_elo}",
							String.valueOf((winningParticipant.getLeader().getElo() + winningParticipant.getLeader().getEloMod())))
						.add("{losser_name}", losingParticipant.getConjoinedNames())
						.add("{losser_elo_mod}", String.valueOf(losingParticipant.getLeader().getEloMod()))
						.add("{losser_elo_mod_elo}",
							String.valueOf((losingParticipant.getLeader().getElo() - winningParticipant.getLeader().getEloMod())))
						.toList();


					for (String sectionLine : sectionLines) {
						componentsList.add(new ChatComponentBuilder("").parse(sectionLine).create());
					}
				}

				continue;
			}

			componentsList.add(new ChatComponentBuilder("").parse(line).create());
		}

		return componentsList;
	}

}
