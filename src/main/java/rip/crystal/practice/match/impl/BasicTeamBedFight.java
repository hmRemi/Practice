package rip.crystal.practice.match.impl;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import rip.crystal.practice.Locale;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.MatchState;
import rip.crystal.practice.match.mongo.MatchInfo;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.party.Party;
import rip.crystal.practice.player.party.classes.rogue.RogueClass;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.meta.ProfileKitData;
import rip.crystal.practice.player.profile.meta.ProfileRematchData;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.player.queue.Queue;
import rip.crystal.practice.utilities.*;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.utilities.chat.ChatComponentBuilder;
import rip.crystal.practice.utilities.elo.EloUtil;
import rip.crystal.practice.utilities.file.type.BasicConfigurationFile;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
public class BasicTeamBedFight extends BasicTeamMatch {

    public BasicTeamBedFight(Queue queue, Kit kit, Arena arena, boolean ranked, GameParticipant<MatchGamePlayer> participantA,
                               GameParticipant<MatchGamePlayer> participantB) {
        super(queue, kit, arena, ranked, participantA, participantB);
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
        Profile profile = Profile.get(player.getUniqueId());

        Party party = profile.getParty();

        if (getKit().getGameRules().isBedFight()) {
            ProfileKitData kitData = profile.getKitData().get(getKit());
            if (kitData.getKitCount() == 0) {
                player.getInventory().setContents(getKit().getKitLoadout().getContents());
                KitUtils.giveBedFightKit(player);
            }
        }

        player.updateInventory();

        // Teleport the player to their spawn point
        Location spawn = getParticipantA().containsPlayer(player.getUniqueId()) ? getArena().getSpawnA() : getArena().getSpawnB();

        //if (spawn.getBlock().getType() == Material.AIR) player.teleport(spawn);
        //else player.teleport(spawn.add(0, 2, 0));
        player.teleport(spawn);
    }

    @Override
    public void end() {
        if (getKit().getGameRules().isBedFight()) {
            for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
                for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
                    gamePlayer.setDead(true);
                    Player bukkitPlayer = gamePlayer.getPlayer();

                    if (bukkitPlayer != null) {
                        if(getWinningParticipant() != null) {
                            if (getWinningParticipant().getConjoinedNames().equals(getParticipantA().getConjoinedNames())) {
                                bukkitPlayer.sendMessage(CC.translate("&7&m----------&r&7→ &c&lMatch Ended &7&m←-----------"));
                                bukkitPlayer.sendMessage(CC.translate("&c&lMatch Results"));
                                bukkitPlayer.sendMessage(CC.translate("&7Winner: &a" + getWinningParticipant().getConjoinedNames()));
                                bukkitPlayer.sendMessage(CC.translate("&7Loser: &c" + getLosingParticipant().getConjoinedNames()));
                                bukkitPlayer.sendMessage(CC.translate("&7&m-----------------------------------'"));
                            } else if (getWinningParticipant().getConjoinedNames().equals(getParticipantB().getConjoinedNames())) {
                                bukkitPlayer.sendMessage(CC.translate("&7&m----------&r&7→ &c&lMatch Ended &7&m←-----------"));
                                bukkitPlayer.sendMessage(CC.translate("&c&lMatch Results"));
                                bukkitPlayer.sendMessage(CC.translate("&7Winner: &a" + getWinningParticipant().getConjoinedNames()));
                                bukkitPlayer.sendMessage(CC.translate("&7Loser: &c" + getLosingParticipant().getConjoinedNames()));
                                bukkitPlayer.sendMessage(CC.translate("&7&m-----------------------------------'"));
                            }
                        }
                        if (bukkitPlayer.hasMetadata("lastAttacker")) {
                            bukkitPlayer.removeMetadata("lastAttacker", cPractice.get());
                        }
                    }
                }
            }
            matches.remove(this);
        }

        if (getParticipantA().getPlayers().size() == 1 && getParticipantB().getPlayers().size() == 1) {
            UUID rematchKey = UUID.randomUUID();

            for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
                for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
                    if (!gamePlayer.isDisconnected()) {
                        Profile profile = Profile.get(gamePlayer.getUuid());

                        if (profile.getParty() == null) {
                            if (gamePlayer.getPlayer() == null) {
                                super.end();
                                return;
                            }
                            UUID opponent;

                            if (gameParticipant.equals(getParticipantA())) opponent = getParticipantB().getLeader().getUuid();
                            else opponent = getParticipantA().getLeader().getUuid();

                            if (opponent != null) {
                                ProfileRematchData rematchData = new ProfileRematchData(rematchKey, gamePlayer.getUuid(), opponent, kit, arena);
                                profile.setRematchData(rematchData);
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
        return (getParticipantA().isAllDead() && !getParticipantA().isHasBed()) || (getParticipantB().isAllDead() && !getParticipantB().isHasBed());
    }

    @Override
    public boolean canStartRound() {
        return kit.getGameRules().isBridge();
    }

    @Override
    public void onRoundEnd() {

        // Set opponents in snapshots if solo
        if (getParticipantA().getPlayers().size() == 1 && getParticipantB().getPlayers().size() == 1) {

            if (ranked) {
                int oldWinnerElo = getWinningParticipant().getLeader().getElo();
                int oldLoserElo = getLosingParticipant().getLeader().getElo();

                int newWinnerElo = EloUtil.getNewRating(oldWinnerElo, oldLoserElo, true);
                int newLoserElo = EloUtil.getNewRating(oldLoserElo, oldWinnerElo, false);

                getWinningParticipant().getLeader().setEloMod(newWinnerElo - oldWinnerElo);
                getLosingParticipant().getLeader().setEloMod(oldLoserElo - newLoserElo);

                Profile winningProfile = Profile.get(getWinningParticipant().getLeader().getUuid());
                winningProfile.getKitData().get(getKit()).setElo(newWinnerElo);
                winningProfile.getKitData().get(getKit()).incrementWon();

                Profile losingProfile = Profile.get(getLosingParticipant().getLeader().getUuid());
                losingProfile.getKitData().get(getKit()).setElo(newLoserElo);
                losingProfile.getKitData().get(getKit()).incrementLost();

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                MatchInfo matchInfo = new MatchInfo(getWinningParticipant().getConjoinedNames(),
                        getLosingParticipant().getConjoinedNames(),
                        getKit(),
                        getWinningParticipant().getLeader().getEloMod(),
                        getLosingParticipant().getLeader().getEloMod(),
                        dtf.format(now),
                        TimeUtil.millisToTimer(System.currentTimeMillis() - timeData));

                winningProfile.getMatches().add(matchInfo);
                losingProfile.getMatches().add(matchInfo);
            }
        }


        super.onRoundEnd();
    }

    @Override
    public boolean canEndRound() {
        return (getParticipantA().isAllDead() && !getParticipantA().isHasBed()) || (getParticipantB().isAllDead() && !getParticipantB().isHasBed());
    }

    @Override
    public boolean isOnSameTeam(Player first, Player second) {
        boolean[] booleans = new boolean[]{
                getParticipantA().containsPlayer(first.getUniqueId()),
                getParticipantB().containsPlayer(first.getUniqueId()),
                getParticipantA().containsPlayer(second.getUniqueId()),
                getParticipantB().containsPlayer(second.getUniqueId())
        };

        return (booleans[0] && booleans[2]) || (booleans[1] && booleans[3]);
    }

    @Override
    public List<GameParticipant<MatchGamePlayer>> getParticipants() {
        return Arrays.asList(getParticipantA(), getParticipantB());
    }

    @Override
    public ChatColor getRelationColor(Player viewer, Player target) {
        if (getKit().getGameRules().isBedFight()) {
            if (getParticipantA().containsPlayer(target.getUniqueId())) return ChatColor.RED;
            else return ChatColor.BLUE;
        }

        if (viewer.equals(target)) return ChatColor.GREEN;

        boolean[] booleans = new boolean[]{
                getParticipantA().containsPlayer(viewer.getUniqueId()),
                getParticipantB().containsPlayer(viewer.getUniqueId()),
                getParticipantA().containsPlayer(target.getUniqueId()),
                getParticipantB().containsPlayer(target.getUniqueId())
        };

        if ((booleans[0] && booleans[3]) || (booleans[2] && booleans[1])) return ChatColor.RED;
        else if ((booleans[0] && booleans[2]) || (booleans[1] && booleans[3])) return ChatColor.GREEN;
        else if (spectators.contains(viewer.getUniqueId())) return getParticipantA().containsPlayer(target.getUniqueId()) ?
                ChatColor.GREEN : ChatColor.RED;
        else return ChatColor.YELLOW;
    }

    @Override
    public List<String> getScoreboardLines(Player player) {
        List<String> lines = new ArrayList<>();
        BasicConfigurationFile config = cPractice.get().getScoreboardConfig();
        String bars = config.getString("LINES.BARS");

        if (getParticipant(player) != null) {
            if (state == MatchState.STARTING_ROUND || state == MatchState.PLAYING_ROUND || state == MatchState.ENDING_ROUND) {
                if (getParticipantA().getPlayers().size() == 1 && getParticipantB().getPlayers().size() == 1) {
                    GameParticipant<MatchGamePlayer> opponent;
                    GameParticipant<MatchGamePlayer> yours;

                    if (getParticipantA().containsPlayer(player.getUniqueId())) {
                        opponent = getParticipantB();
                        yours = getParticipantA();
                    }
                    else {
                        opponent = getParticipantA();
                        yours = getParticipantB();
                    }

                    if(opponent.getLeader().getPlayer() == null) {
                        return null;
                    }

                    if(yours.getLeader().getPlayer() == null) {
                        return null;
                    }


                    if (kit.getGameRules().isBedFight()) {
                        config.getStringList("FIGHTS.1V1.BEDFIGHT-MODE").forEach(line -> {
                            lines.add(line.replace("{bars}", bars)
                                    .replace("{duration}", getDuration())
                                    .replace("{opponent-color}", Profile.get(opponent.getLeader().getPlayer().getUniqueId()).getColor())
                                    .replace("{opponent}", opponent.getLeader().getPlayer().getName())
                                    .replace("{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getLeader().getPlayer())))
                                    .replace("{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                    .replace("{arena-author}", getArena().getAuthor())
                                    .replace("{kit}", getKit().getName())
                                    .replace("{redHasBed}", getParticipantA().isHasBed() ? "&a✔" : "&c✗")
                                    .replace("{blueHasBed}", getParticipantB().isHasBed() ? "&a✔" : "&c✗")
                            );
                        });
                        return lines;

                        /*config.getStringList("FIGHTS.1V1.BOXING-MODE").forEach(line -> lines.add(line.replace("{bars}", bars)
                                .replace("{duration}", getDuration())
                                .replace("{opponent-color}", Profile.get(opponent.getLeader().getPlayer().getUniqueId()).getColor())
                                .replace("{opponent}", opponent.getLeader().getPlayer().getName())
                                .replace("{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getLeader().getPlayer())))
                                .replace("{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                .replace("{arena-author}", getArena().getAuthor())
                                .replace("{kit}", getKit().getName())
                                .replace("{hits}", (yours.getLeader().getHits() >= opponent.getLeader().getHits() ? CC.GREEN : CC.RED) + "(" + (yours.getLeader().getHits() >= opponent.getLeader().getHits() ? "+" : "-") + (yours.getLeader().getHits() >= opponent.getLeader().getHits() ? String.valueOf(yours.getLeader().getHits() - opponent.getLeader().getHits()) : String.valueOf(opponent.getLeader().getHits() - yours.getLeader().getHits())) + ")")
                                .replace("{your-hits}", String.valueOf(yours.getLeader().getHits()))
                                .replace("{opponent-hits}", String.valueOf(opponent.getLeader().getHits()))
                                .replace("{combo}", String.valueOf(yours.getLeader().getCombo()))));*/
                    }

                    config.getStringList("FIGHTS.1V1.LINES").forEach(line -> {
                        if (line.contains("{bridge}")) return;
                        if (line.contains("{rounds}")) return;
                        lines.add(line.replace("{bars}", bars)
                                .replace("{duration}", getDuration())
                                .replace("{opponent-color}", Profile.get(opponent.getLeader().getPlayer().getUniqueId()).getColor())
                                .replace("{opponent}", opponent.getLeader().getPlayer().getName())
                                .replace("{opponent-ping}", String.valueOf(BukkitReflection.getPing(opponent.getLeader().getPlayer())))
                                .replace("{player-ping}", String.valueOf(BukkitReflection.getPing(player)))
                                .replace("{arena-author}", getArena().getAuthor())
                                .replace("{kit}", getKit().getName()));
                    });
                } else {
                    GameParticipant<MatchGamePlayer> friendly = getParticipant(player);
                    GameParticipant<MatchGamePlayer> opponent = getParticipantA().equals(friendly) ?
                            getParticipantB() : getParticipantA();

                    if (friendly.getPlayers().size() + opponent.getPlayers().size() <= 6) {
                        config.getStringList("FIGHTS.SMALL-TEAM.LINES").forEach(line -> {
                            if (line.contains("{bridge}")) {
                                if (kit.getGameRules().isBridge()) {
                                    config.getStringList("FIGHTS.BRIDGE-FORMAT.LINES").forEach(line2 -> {
                                        if (line2.contains("{points}")) return;
                                        lines.add(line2.replace("{kills}", String.valueOf(getGamePlayer(player).getKills())));
                                    });
                                }
                                return;
                            }
                            if (line.contains("{no-bridge}")) {
                                if (!kit.getGameRules().isBridge()) {
                                    config.getStringList("FIGHTS.SMALL-TEAM.NO-BRIDGE.LINES").forEach(line2 -> {
                                        if (line2.contains("{players}")) {
                                            for (MatchGamePlayer gamePlayer : friendly.getPlayers()) {
                                                lines.add(config.getString("FIGHTS.SMALL-TEAM.NO-BRIDGE.PLAYERS-FORMAT")
                                                        .replace("{player}", (gamePlayer.isDead() || gamePlayer.isDisconnected() ? "&7&m" : "") +
                                                                gamePlayer.getUsername()));
                                            }
                                            return;
                                        }
                                        if (line2.contains("{opponents}")) {
                                            for (MatchGamePlayer gamePlayer : opponent.getPlayers()) {
                                                lines.add(config.getString("FIGHTS.SMALL-TEAM.NO-BRIDGE.OPPONENTS-FORMAT")
                                                        .replace("{opponent}", (gamePlayer.isDead() || gamePlayer.isDisconnected() ? "&7&m" : "") +
                                                                gamePlayer.getUsername()));
                                            }
                                            return;
                                        }
                                        lines.add(line2.replace("{bars}", bars)
                                                .replace("{team-alive}", String.valueOf(friendly.getAliveCount()))
                                                .replace("{team-size}", String.valueOf(friendly.getPlayers().size()))
                                                .replace("{opponent-alive}", String.valueOf(opponent.getAliveCount()))
                                                .replace("{opponent-size}", String.valueOf(opponent.getPlayers().size())
                                                        .replace("{kit}", getKit().getName())));
                                    });
                                }
                                return;
                            }
                            if (line.contains("{rounds}")) return;
                            lines.add(line.replace("{bars}", bars)
                                    .replace("{duration}", getDuration())
                                    .replace("{arena-author}", getArena().getAuthor())
                                    .replace("{kit}", getKit().getName()));
                        });
                    } else {
                        config.getStringList("FIGHTS.BIG-TEAM.LINES").forEach(line -> {
                            if (line.contains("{bridge}")) {
                                if (kit.getGameRules().isBridge()) {
                                    config.getStringList("FIGHTS.BRIDGE-FORMAT.LINES").forEach(line2 -> {
                                        if (line2.contains("{points}")) return;
                                        lines.add(line2.replace("{kills}", String.valueOf(getGamePlayer(player).getKills())));
                                    });
                                }
                                return;
                            }
                            if (line.contains("{rounds}")) return;
                            lines.add(line.replace("{duration}", getDuration())
                                    .replace("{arena-author}", getArena().getAuthor())
                                    .replace("{team-alive}", String.valueOf(friendly.getAliveCount()))
                                    .replace("{team-size}", String.valueOf(friendly.getPlayers().size()))
                                    .replace("{opponent-alive}", String.valueOf(opponent.getAliveCount()))
                                    .replace("{opponent-size}", String.valueOf(opponent.getPlayers().size()))
                                    .replace("{kit}", getKit().getName()));
                        });
                    }
                }
            } else {
                config.getStringList("FIGHTS.ON-END-ROUND-FOR-NEXT").forEach(line -> lines.add(line.replace("{duration}", getDuration())
                        .replace("{arena-author}", getArena().getAuthor())
                        .replace("{kit}", getKit().getName())));
            }
        }

        return lines;
    }

    public List<String> applySpectatorScoreboard(Player spectator) {
        List<String> lines = Lists.newArrayList();
        Profile profile = Profile.get(spectator.getUniqueId());

        if (profile.getMatch() != null) {
            cPractice.get().getScoreboardConfig().getStringList("FIGHTS.SPECTATING").forEach(s ->
                    lines.add(s
                            .replace("{playerA}", String.valueOf(getParticipantA().getLeader().getUsername()))
                            .replace("{playerB}", String.valueOf(getParticipantB().getLeader().getUsername()))
                            .replace("{duration}", profile.getMatch().getDuration())
                            .replace("{kit}", profile.getMatch().getKit().getName())
                            .replace("{spectators}", String.valueOf(profile.getMatch().getSpectators().size()))
                            .replace("{arena}", profile.getMatch().getArena().getName())));
        }

        return lines;
    }

    @Override
    public void addSpectator(Player spectator, Player target) {
        super.addSpectator(spectator, target);

        ChatColor firstColor;
        ChatColor secondColor;

        if (getParticipantA().containsPlayer(target.getUniqueId())) {
            firstColor = ChatColor.GREEN;
            secondColor = ChatColor.RED;
        } else {
            firstColor = ChatColor.RED;
            secondColor = ChatColor.GREEN;
        }

        if (ranked) {
            new MessageFormat(Locale.MATCH_START_SPECTATING_RANKED.format(Profile.get(spectator.getUniqueId()).getLocale()))
                    .add("{first_color}", firstColor.toString())
                    .add("{participant_a}", getParticipantA().getConjoinedNames())
                    .add("{participant_a_elo}", String.valueOf(getParticipantA().getLeader().getElo()))
                    .add("{second_color}", secondColor.toString())
                    .add("{participant_b}", getParticipantB().getConjoinedNames())
                    .add("{participant_b_elo}", String.valueOf(getParticipantB().getLeader().getElo()))
                    .send(spectator);
        } else {
            new MessageFormat(Locale.MATCH_START_SPECTATING.format(Profile.get(spectator.getUniqueId()).getLocale()))
                    .add("{first_color}", firstColor.toString())
                    .add("{participant_a}", getParticipantA().getConjoinedNames())
                    .add("{second_color}", secondColor.toString())
                    .add("{participant_b}", getParticipantB().getConjoinedNames())
                    .send(spectator);
        }
    }
}
