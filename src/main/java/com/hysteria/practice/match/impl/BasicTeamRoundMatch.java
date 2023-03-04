package com.hysteria.practice.match.impl;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.chunk.ChunkRestorationManager;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.match.MatchState;
import com.hysteria.practice.match.mongo.MatchInfo;
import com.hysteria.practice.match.participant.MatchGamePlayer;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.queue.Queue;
import com.hysteria.practice.utilities.*;
import com.hysteria.practice.utilities.elo.EloUtil;
import com.hysteria.practice.utilities.file.type.BasicConfigurationFile;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.game.arena.impl.StandaloneArena;
import com.hysteria.practice.player.profile.participant.alone.GameParticipant;
import com.hysteria.practice.utilities.chat.CC;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BasicTeamRoundMatch extends BasicTeamMatch {

    private final int roundsToWin;

    public BasicTeamRoundMatch(Queue queue, Kit kit, Arena arena, boolean ranked, GameParticipant<MatchGamePlayer> participantA,
                               GameParticipant<MatchGamePlayer> participantB, int roundsToWin) {
        super(queue, kit, arena, ranked, participantA, participantB);
        this.roundsToWin = roundsToWin;
    }

    @Override
    public boolean canEndMatch() {
        return this.getParticipantA().getRoundWins() == roundsToWin || this.getParticipantB().getRoundWins() == roundsToWin;
    }

    @Override
    public boolean canStartRound() {
        return !(getParticipantA().getRoundWins() == roundsToWin || getParticipantB().getRoundWins() == roundsToWin);
    }

    @Override
    public boolean canEndRound() {
        if(getKit().getGameRules().isBridge()){
            return this.getParticipantA().isEliminated() || this.getParticipantB().isEliminated();
        }
        return this.getParticipantA().isAllDead() || this.getParticipantB().isAllDead();
    }

    @Override
    public void end() {
        super.end();
        if (getKit().getGameRules().isBridge()) {
            for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
                for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
                    gamePlayer.setDead(true);
                    Player bukkitPlayer = gamePlayer.getPlayer();

                    if (bukkitPlayer != null) {
                        if(getWinningParticipant() != null) {
                            if (getWinningParticipant().getConjoinedNames().equals(getParticipantA().getConjoinedNames())) {
                                bukkitPlayer.sendMessage(CC.translate("&7&m----------&r&7→ &c&lMatch Ended &7&m←-----------"));
                                bukkitPlayer.sendMessage(CC.translate("&c&lMatch Results"));
                                bukkitPlayer.sendMessage("");
                                bukkitPlayer.sendMessage(CC.translate("&cRed Points&7:&7 " + StringUtils.getStringPoint(getParticipantA().getRoundWins(), org.bukkit.ChatColor.RED, getRoundsToWin())));
                                bukkitPlayer.sendMessage(CC.translate("&9Blue Points&7:&7 " + StringUtils.getStringPoint(getParticipantB().getRoundWins(), org.bukkit.ChatColor.BLUE, getRoundsToWin())));
                                bukkitPlayer.sendMessage("");
                                bukkitPlayer.sendMessage(CC.translate("&7Winner: &a" + getWinningParticipant().getConjoinedNames()));
                                bukkitPlayer.sendMessage(CC.translate("&7Loser: &c" + getLosingParticipant().getConjoinedNames()));
                                bukkitPlayer.sendMessage(CC.translate("&7&m-----------------------------------'"));
                            } else if (getWinningParticipant().getConjoinedNames().equals(getParticipantB().getConjoinedNames())) {
                                bukkitPlayer.sendMessage(CC.translate("&7&m----------&r&7→ &c&lMatch Ended &7&m←-----------"));
                                bukkitPlayer.sendMessage(CC.translate("&c&lMatch Results"));
                                bukkitPlayer.sendMessage("");
                                bukkitPlayer.sendMessage(CC.translate("&9Blue Points&7:&7 " + StringUtils.getStringPoint(getParticipantB().getRoundWins(), ChatColor.BLUE, getRoundsToWin())));
                                bukkitPlayer.sendMessage(CC.translate("&cRed Points&7:&7 " + StringUtils.getStringPoint(getParticipantA().getRoundWins(), org.bukkit.ChatColor.RED, getRoundsToWin())));
                                bukkitPlayer.sendMessage("");
                                bukkitPlayer.sendMessage(CC.translate("&7Winner: &a" + getWinningParticipant().getConjoinedNames()));
                                bukkitPlayer.sendMessage(CC.translate("&7Loser: &c" + getLosingParticipant().getConjoinedNames()));
                                bukkitPlayer.sendMessage(CC.translate("&7&m-----------------------------------'"));
                            }
                        }
                        if (bukkitPlayer.hasMetadata("lastAttacker")) {
                            bukkitPlayer.removeMetadata("lastAttacker", HyPractice.get());
                        }
                    }
                }
            }
            matches.remove(this);
        }
    }

    @Override
    public void onRoundEnd() {
        // Store winning participant
        setWinningParticipant(getParticipantA().isAllDead() ? getParticipantB() : getParticipantA());
        getWinningParticipant().setRoundWins(getWinningParticipant().getRoundWins() + 1);

        // Store losing participant
        setLosingParticipant(getParticipantA().isAllDead() ? getParticipantA() : getParticipantB());

        if (!canEndMatch()) {
            if (!getKit().getGameRules().isBridge()) cleanup();
            //int roundsToWin = (ranked ? 3 : 1) - winningParticipant.getRoundWins();
            if (HyPractice.get().getMainConfig().getBoolean("MATCH.REMOVE_BLOCKS_ON_ROUND_END_BRIDGE")) {
                ChunkRestorationManager.getIChunkRestoration().reset(getArena());
            }

            if(getKit().getGameRules().isBattlerush()) {
                if(getArena() instanceof StandaloneArena) {
                    ChunkRestorationManager.getIChunkRestoration().reset(getArena());
                }
            }

            state = MatchState.ENDING_ROUND;
            logicTask.setNextAction(3);
            this.getParticipants().forEach(participant ->
                participant.getPlayers().forEach(gamePlayer -> {
                    Player player = gamePlayer.getPlayer();
                    player.setVelocity(new Vector());
                    gamePlayer.setDead(false);
                    Location spawn = getParticipantA().containsPlayer(player.getUniqueId()) ? getArena().getSpawnA() : getArena().getSpawnB();

                    if (spawn.getBlock().getType() == Material.AIR) player.teleport(spawn);
                    else player.teleport(spawn.add(0, 2, 0));

                    if(getKit().getGameRules().isSumo()){
                        PlayerUtil.denyMovement(player);
                        return;
                    }

                    TaskUtil.runLater(() -> {
                        PlayerUtil.reset(player);
                        Profile profile = Profile.get(player.getUniqueId());
                        if (getKit().getGameRules().isBridge()) {
                            if (profile.getSelectedKit() == null) {
                                player.getInventory().setContents(getKit().getKitLoadout().getContents());
                            } else {
                                player.getInventory().setContents(profile.getSelectedKit().getContents());
                            }
                            KitUtils.giveBridgeKit(player);
                            PlayerUtil.denyMovement(player);
                        } else {
                            if (profile.getSelectedKit() == null) {
                                player.getInventory().setArmorContents(getKit().getKitLoadout().getArmor());
                                player.getInventory().setContents(getKit().getKitLoadout().getContents());
                            } else {
                                player.getInventory().setArmorContents(profile.getSelectedKit().getArmor());
                                player.getInventory().setContents(profile.getSelectedKit().getContents());
                            }
                        }
                        player.updateInventory();
                    }, 5L);
                }));
            return;
        }

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
    public List<String> getScoreboardLines(Player player) {
        List<String> lines = new ArrayList<>();
        BasicConfigurationFile config = HyPractice.get().getScoreboardConfig();
        String bars = config.getString("LINES.BARS");

        if (getParticipant(player) != null) {
            if (state == MatchState.STARTING_ROUND || state == MatchState.PLAYING_ROUND || state == MatchState.ENDING_ROUND) {
                if (getParticipantA().getPlayers().size() == 1 && getParticipantB().getPlayers().size() == 1) {
                    GameParticipant<MatchGamePlayer> opponent;

                    if (getParticipantA().containsPlayer(player.getUniqueId())) opponent = getParticipantB();
                    else opponent = getParticipantA();

                    config.getStringList("FIGHTS.1V1.LINES").forEach(line -> {
                        if (line.contains("{bridge}")) {
                            if (kit.getGameRules().isBridge()) {
                                config.getStringList("FIGHTS.BRIDGE-FORMAT.LINES").forEach(line2 -> {
                                    if (line2.contains("{points}")) {
                                        if (this.getParticipantA().containsPlayer(player.getUniqueId())) {
                                            lines.add(config.getString("FIGHTS.BRIDGE-FORMAT.RED")
                                                    .replace("{string-points}", StringUtils.getStringPoint(getParticipantA().getRoundWins(), org.bukkit.ChatColor.RED, getRoundsToWin())));
                                            lines.add(config.getString("FIGHTS.BRIDGE-FORMAT.BLUE")
                                                    .replace("{string-points}", StringUtils.getStringPoint(getParticipantB().getRoundWins(), org.bukkit.ChatColor.BLUE, getRoundsToWin())));
                                        } else {
                                            lines.add(config.getString("FIGHTS.BRIDGE-FORMAT.BLUE")
                                                    .replace("{string-points}", StringUtils.getStringPoint(getParticipantB().getRoundWins(), org.bukkit.ChatColor.BLUE, getRoundsToWin())));
                                            lines.add(config.getString("FIGHTS.BRIDGE-FORMAT.RED")
                                                    .replace("{string-points}", StringUtils.getStringPoint(getParticipantA().getRoundWins(), org.bukkit.ChatColor.RED, getRoundsToWin())));
                                        }
                                        return;
                                    }
                                    lines.add(line2.replace("{kills}", String.valueOf(getGamePlayer(player).getKills())));
                                });
                            }
                            return;
                        }
                        if (line.contains("{rounds}")) {
                            if (!kit.getGameRules().isBridge()) {
                                lines.add(config.getString("FIGHTS.ROUND-START")
                                        .replace("{round}", StringUtils.getStringPoint(getParticipant(player).getRoundWins(), ChatColor.LIGHT_PURPLE, roundsToWin)));
                            }
                            return;
                        }
                        lines.add(line.replace("{bars}", bars)
                                .replace("{duration}", getDuration())
                                .replace("{opponent-color}", Profile.get(opponent.getLeader().getUuid()).getColor())
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
                                config.getStringList("FIGHTS.BRIDGE-FORMAT.LINES").forEach(line2 -> {
                                    if (line2.contains("{points}")) {
                                        if (this.getParticipantA().containsPlayer(player.getUniqueId())) {
                                            lines.add(config.getString("FIGHTS.BRIDGE-FORMAT.RED")
                                                    .replace("{string-points}", StringUtils.getStringPoint(getParticipantA().getRoundWins(), org.bukkit.ChatColor.RED, getRoundsToWin())));
                                            lines.add(config.getString("FIGHTS.BRIDGE-FORMAT.BLUE")
                                                    .replace("{string-points}", StringUtils.getStringPoint(getParticipantB().getRoundWins(), org.bukkit.ChatColor.BLUE, getRoundsToWin())));
                                        } else {
                                            lines.add(config.getString("FIGHTS.BRIDGE-FORMAT.BLUE")
                                                    .replace("{string-points}", StringUtils.getStringPoint(getParticipantB().getRoundWins(), org.bukkit.ChatColor.BLUE, getRoundsToWin())));
                                            lines.add(config.getString("FIGHTS.BRIDGE-FORMAT.RED")
                                                    .replace("{string-points}", StringUtils.getStringPoint(getParticipantA().getRoundWins(), org.bukkit.ChatColor.RED, getRoundsToWin())));
                                        }
                                        return;
                                    }
                                    lines.add(line2.replace("{kills}", String.valueOf(getGamePlayer(player).getKills())));
                                });
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
                                                lines.add(config.getString("FIGHTS.SMALL-TEAM.NO-BRIDGE.OPPONENT-FORMAT")
                                                        .replace("{opponent}", (gamePlayer.isDead() || gamePlayer.isDisconnected() ? "&7&m" : "") +
                                                                gamePlayer.getUsername()));
                                            }
                                            return;
                                        }
                                        lines.add(line2.replace("{bars}", bars)
                                                .replace("{team-alive}", String.valueOf(friendly.getAliveCount()))
                                                .replace("{team-size}", String.valueOf(friendly.getPlayers().size()))
                                                .replace("{opponent-alive}", String.valueOf(opponent.getAliveCount()))
                                                .replace("{opponent-size}", String.valueOf(opponent.getPlayers().size()))
                                                .replace("{kit}", getKit().getName()));
                                    });
                                }
                                return;
                            }
                            if (line.contains("{rounds}")) {
                                if (!kit.getGameRules().isBridge()) {
                                    lines.add(line
                                            .replace("{round}", StringUtils.getStringPoint(getParticipant(player).getRoundWins(), ChatColor.LIGHT_PURPLE, roundsToWin))
                                            .replace("{kit}", getKit().getName()));
                                }
                                return;
                            }
                            lines.add(line.replace("{bars}", bars)
                                    .replace("{duration}", getDuration())
                                    .replace("{arena-author}", getArena().getAuthor())
                                    .replace("{kit}", getKit().getName()));
                        });
                    } else {
                        config.getStringList("FIGHTS.BIG-TEAM.LINES").forEach(line -> {
                            if (line.contains("{bridge}")) {
                                config.getStringList("FIGHTS.BRIDGE-FORMAT.LINES").forEach(line2 -> {
                                    if (line2.contains("{points}")) {
                                        if (this.getParticipantA().containsPlayer(player.getUniqueId())) {
                                            lines.add(config.getString("FIGHTS.BRIDGE-FORMAT.RED")
                                                    .replace("{string-points}",
                                                            StringUtils.getStringPoint(getParticipantA().getRoundWins(), org.bukkit.ChatColor.RED, getRoundsToWin())));
                                            lines.add(config.getString("FIGHTS.BRIDGE-FORMAT.BLUE")
                                                    .replace("{string-points}",
                                                            StringUtils.getStringPoint(getParticipantB().getRoundWins(), org.bukkit.ChatColor.BLUE, getRoundsToWin())));
                                        } else {
                                            lines.add(config.getString("FIGHTS.BRIDGE-FORMAT.BLUE")
                                                    .replace("{string-points}",
                                                            StringUtils.getStringPoint(getParticipantB().getRoundWins(), org.bukkit.ChatColor.BLUE, getRoundsToWin())));
                                            lines.add(config.getString("FIGHTS.BRIDGE-FORMAT.RED")
                                                    .replace("{string-points}",
                                                            StringUtils.getStringPoint(getParticipantA().getRoundWins(), org.bukkit.ChatColor.RED, getRoundsToWin())));
                                        }
                                        return;
                                    }
                                    lines.add(line2.replace("{kills}", String.valueOf(getGamePlayer(player).getKills())));
                                });
                                return;
                            }
                            if (line.contains("{rounds}")) {
                                if (!kit.getGameRules().isBridge()) {
                                    lines.add(line
                                            .replace("{round}",
                                                    StringUtils.getStringPoint(getParticipant(player).getRoundWins(), ChatColor.LIGHT_PURPLE, roundsToWin)));
                                }
                                return;
                            }
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
}