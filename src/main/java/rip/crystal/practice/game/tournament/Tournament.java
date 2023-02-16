package rip.crystal.practice.game.tournament;

import com.google.common.collect.Lists;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.utilities.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/12/2023
 */

@Getter @Setter
public abstract class Tournament<T>{

    @Getter @Setter private static Tournament<?> tournament;

    private List<GameParticipant<MatchGamePlayer>> teams = Lists.newArrayList();

    private final List<Match> matches = Lists.newArrayList();
    private final List<UUID> players = Lists.newArrayList();

    private TournamentState state = TournamentState.WAITING;
    private GameParticipant<MatchGamePlayer> winner;

    private boolean started = false;
    private int size, limit = 5;
    private boolean clans;
    private int round = 0;
    private Kit kit;

    public abstract void join(T type);

    public abstract void start();

    public abstract void nextRound();

    public abstract void eliminatedTeam(GameParticipant<MatchGamePlayer> teamEliminated);

    public abstract void end(GameParticipant<MatchGamePlayer> winner);

    public void removePlayer(UUID uuid) {
        this.players.remove(uuid);
    }

    public void broadcast(String msg){
        teams.forEach(team -> team.getPlayers().forEach(matchGamePlayer -> matchGamePlayer.getPlayer().sendMessage(CC.translate(msg))));
    }

    public List<Player> getOnlinePlayers(){
        return players.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public GameParticipant<MatchGamePlayer> getParticipant(Player player) {
        for (GameParticipant<MatchGamePlayer> gameParticipant : this.teams) {
            for (MatchGamePlayer matchGamePlayer : gameParticipant.getPlayers()) {
                if (!matchGamePlayer.getPlayer().getUniqueId().equals(player.getUniqueId())) continue;
                return gameParticipant;
            }
        }
        return null;
    }

    public abstract List<String> getTournamentScoreboard();
}