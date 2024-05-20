package com.hysteria.practice.match;

import com.alonsoaliaga.alonsoleagues.api.AlonsoLeaguesAPI;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.Locale;
import com.hysteria.practice.chunk.ChunkRestorationManager;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.game.arena.impl.StandaloneArena;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.game.kit.meta.KitGameRules;
import com.hysteria.practice.game.knockback.Knockback;
import com.hysteria.practice.game.tournament.Tournament;
import com.hysteria.practice.match.events.MatchEndEvent;
import com.hysteria.practice.match.events.MatchStartEvent;
import com.hysteria.practice.match.impl.BasicTeamBedFight;
import com.hysteria.practice.match.impl.BasicTeamLivesFight;
import com.hysteria.practice.match.impl.BasicTeamMatch;
import com.hysteria.practice.match.impl.BasicTeamRoundMatch;
import com.hysteria.practice.match.participant.MatchGamePlayer;
import com.hysteria.practice.match.task.*;
import com.hysteria.practice.player.cosmetics.impl.killeffects.KillEffectType;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.player.profile.hotbar.Hotbar;
import com.hysteria.practice.player.profile.meta.ProfileKitData;
import com.hysteria.practice.player.profile.participant.GamePlayer;
import com.hysteria.practice.player.profile.participant.alone.GameParticipant;
import com.hysteria.practice.player.profile.visibility.VisibilityLogic;
import com.hysteria.practice.player.queue.Queue;
import com.hysteria.practice.utilities.*;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.chat.ChatComponentBuilder;
import com.hysteria.practice.utilities.chat.ChatHelper;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import org.github.paperspigot.Title;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class Match {
    @Getter protected static List<Match> matches = new ArrayList<>();

    protected MatchState state = MatchState.STARTING_ROUND;
    private final UUID matchId = UUID.randomUUID();

    private final Queue queue;
    protected boolean ranked;
    protected Arena arena;
    protected Kit kit;

    private final Set<Entity> entitiesToRemove = new HashSet<>();
    private final List<BlockState> changedBlocks;
    private final List<Location> placedBlocks;
    protected List<MatchSnapshot> snapshots;
    protected List<Item> droppedItems;
    protected List<UUID> spectators;

    protected MatchLogicTask logicTask;
    protected long timeData;

    /**
     * Construct a match using the given details
     *
     * @param queue {@link Queue} if match is started from queue, then we provide it
     * @param kit   {@link Kit} The kit that will be given to all players in the match
     * @param arena {@link Arena} The arena that will be used in the match
     */
    public Match(Queue queue, Kit kit, Arena arena, boolean ranked) {
        this.queue = queue;
        this.kit = kit;
        this.arena = arena;
        this.ranked = ranked;
        this.snapshots = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.droppedItems = new ArrayList<>();
        this.placedBlocks = new ArrayList<>();
        this.changedBlocks = new ArrayList<>();

        matches.add(this);
    }

    /**
     * Set up the player according to {@link Kit},
     * {@link KitGameRules}
     * <p>
     * This also sets the players knock-back profile,
     * set's the players damage ticks,
     * applies kit if there is no custom kits.
     *
     * @param player {@link Player} being setup
     */
    public void setupPlayer(Player player) {
        // Set the player as alive
        MatchGamePlayer gamePlayer = getGamePlayer(player);
        gamePlayer.setDead(false);

        // If the player disconnected, skip any operations for them
        if (gamePlayer.isDisconnected()) return;

        Profile profile = Profile.get(player.getUniqueId());

        // Reset the player's inventory
        PlayerUtil.reset(player);

        // Deny movement if the kit is sumo , bridge or bed fight
        if (getKit().getGameRules().isSumo() || getKit().getGameRules().isBridge()) {
            PlayerUtil.denyMovement(player);
        }

        // Set the player's max damage ticks and knock-back
        player.setMaximumNoDamageTicks(getKit().getGameRules().getHitDelay());
        player.setNoDamageTicks(getKit().getGameRules().getHitDelay());

        Knockback.getKnockbackProfiler().setKnockback(player.getPlayer(), getKit().getGameRules().getKbProfile());

        // If the player has no kits, apply the default kit, otherwise
        // give the player a list of kit books to choose from
        if (!getKit().getGameRules().isSumo()) {
            ProfileKitData kitData = profile.getKitData().get(getKit());

            if (kitData.getKitCount() > 0) {
                profile.getKitData().get(getKit()).giveBooks(player);
            } else {
                player.getInventory().setArmorContents(getKit().getKitLoadout().getArmor());
                player.getInventory().setContents(getKit().getKitLoadout().getContents());
                //player.sendMessage(Locale.MATCH_GIVE_KIT.format("Default"));
                new MessageFormat(com.hysteria.practice.Locale.MATCH_GIVE_KIT.format(profile.getLocale()))
                        .add("{kit_name}", "Default")
                        .send(player);
            }
        }
    }

    /**
     * Initiate and start the {@link Match}
     * This method sets the {@link Arena} as active
     * Sets up the player and handles visibility
     */
    public void start() {
        // Set state
        state = MatchState.STARTING_ROUND;

        // Start logic task
        logicTask = new MatchLogicTask(this);
        logicTask.runTaskTimer(HyPractice.get(), 0L, 20L);

        // Set arena as active
        arena.setActive(true);

        // Send arena message
        if (getArena().getAuthor() != null && !getArena().getAuthor().isEmpty()) {
            sendMessage(com.hysteria.practice.Locale.MATCH_PLAYING_ARENA_AUTHOR, new MessageFormat()
                    .add("{arena_name}", arena.getName())
                    .add("{author}", arena.getAuthor()));
        } else
            sendMessage(com.hysteria.practice.Locale.MATCH_PLAYING_ARENA_NO_AUTHOR, new MessageFormat().add("{arena_name}", arena.getName()));

        // Setup players
        for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
            for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
                Player player = gamePlayer.getPlayer();

                if (player != null) {
                    Profile profile = Profile.get(player.getUniqueId());
                    profile.setState(ProfileState.FIGHTING);
                    profile.setMatch(this);

                    TaskUtil.run(() -> setupPlayer(player));
                    if (getKit().getGameRules().isShowHealth()) {
                        for (GameParticipant<MatchGamePlayer> gameParticipantOther : getParticipants()) {
                            for (MatchGamePlayer gamePlayerOther : gameParticipantOther.getPlayers()) {
                                Player other = gamePlayerOther.getPlayer();
                                Scoreboard scoreboard = player.getScoreboard();
                                Objective objective = scoreboard.getObjective(DisplaySlot.BELOW_NAME);

                                if (objective == null) {
                                    objective = scoreboard.registerNewObjective("showhealth", "health");
                                }

                                objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
                                objective.setDisplayName(ChatColor.RED + StringEscapeUtils.unescapeJava("❤")); // ❤ \u2764
                                objective.getScore(other.getName()).setScore((int) Math.floor(other.getHealth() / 2));
                            }
                        }
                    }

                }
            }
        }

        // Handle player visibility
        for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
            for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
                Player player = gamePlayer.getPlayer();

                if (player != null) {
                    VisibilityLogic.handle(player);
                }
            }
        }

        timeData = System.currentTimeMillis();
        new MatchStartEvent(this).call();
    }

    /**
     * Call {@link Player}
     * This method resets the ability cooldowns
     * and the lunar client cooldowns for the player.
     */
    private void clearCooldowns(Player player) {
        Profile profile = Profile.get(player.getUniqueId());
        List<Consumer<Player>> cooldownAppliers = Arrays.asList(
                profile.getPartneritem()::cooldownRemove,
                profile.getAntitrapper()::cooldownRemove,
                profile.getLuckyingot()::cooldownRemove,
                profile.getCombo()::cooldownRemove,
                profile.getCookie()::cooldownRemove,
                profile.getEffectdisabler()::cooldownRemove,
                profile.getGuardianangel()::cooldownRemove,
                profile.getNinjastar()::cooldownRemove,
                profile.getPocketbard()::cooldownRemove,
                profile.getRocket()::cooldownRemove,
                profile.getScrambler()::cooldownRemove,
                profile.getStrength()::cooldownRemove,
                profile.getSwapperaxe()::cooldownRemove,
                profile.getSwitcher()::cooldownRemove,
                profile.getTankingot()::cooldownRemove,
                profile.getTimewarp()::cooldownRemove
        );

        cooldownAppliers.forEach(applier -> applier.accept(player));
    }

    /**
     * Call {@link MatchEndEvent}
     * This method resets the players profile,
     * also clears all cool-downs for abilities.
     */
    public void end() {
        new MatchEndEvent(this).call();

        for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
            for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
                if (!gamePlayer.isDisconnected()) {
                    Player player = gamePlayer.getPlayer();

                    if (player != null) {
                        player.setFireTicks(0);
                        player.updateInventory();

                        Profile profile = Profile.get(player.getUniqueId());
                        profile.setFishHit(0);
                        profile.setState(ProfileState.LOBBY);
                        profile.setMatch(null);
                        profile.setEnderpearlCooldown(new Cooldown(0));
                        profile.setSelectedKit(null);

                        this.clearCooldowns(player);

                        if (getKit().getGameRules().isShowHealth()) {
                            Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);

                            if (objective != null) objective.unregister();
                        }
                    }
                }
            }
        }

        for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
            for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
                if (!gamePlayer.isDisconnected()) {
                    Player player = gamePlayer.getPlayer();

                    if (player != null) {
                        VisibilityLogic.handle(player);
                        Hotbar.giveHotbarItems(player);
                        HyPractice.get().getEssentials().teleportToSpawn(player);
                    }
                }
            }
        }

        for (Player player : getSpectatorsAsPlayers()) {
            removeSpectator(player);
        }


        getEntitiesToRemove().forEach(Entity::remove);
        droppedItems.forEach(Entity::remove);
        new MatchResetTask(this).runTask(HyPractice.get());
        if (getArena() instanceof StandaloneArena) {
            ChunkRestorationManager.getIChunkRestoration().reset(getArena());
        }

        matches.remove(this);
        logicTask.cancel();
    }

    public abstract boolean canEndMatch();

    public void setEffects() {
        TaskUtil.run(() ->
                getParticipants().forEach(gameParticipant ->
                        gameParticipant.getPlayers().forEach(gamePlayer -> {
                            Player player = gamePlayer.getPlayer();
                            if (player != null) {
                                kit.getGameRules().getEffects().forEach(player::addPotionEffect);
                            }
                        })
                )
        );
    }


    public void onRoundStart() {
        // Reset snapshots
        snapshots.clear();

        // Reset each game participant
        for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
            gameParticipant.reset();
            gameParticipant.getPlayers().forEach(gamePlayer -> {
                // Allow movement if the kit is sumo, bridge or bed fight
                if (getKit().getGameRules().isSumo() || getKit().getGameRules().isBridge())
                    PlayerUtil.allowMovement(gamePlayer.getPlayer());
            });
        }

        TaskUtil.run(() ->
                getParticipants().forEach(gameParticipant ->
                        gameParticipant.getPlayers().forEach(gamePlayer -> {
                            Player player = gamePlayer.getPlayer();
                            if (player != null) {
                                for (PotionEffect effect : kit.getGameRules().getEffects()) {
                                    player.addPotionEffect(effect);
                                }
                            }
                        })));

        // Set time data
        timeData = System.currentTimeMillis();
    }

    /**
     * Returns true if the round is ready to start
     *
     * @return {@link Boolean} Where the round is ready to start
     */
    public abstract boolean canStartRound();

    public void onRoundEnd() {
        timeData = System.currentTimeMillis() - timeData;
        // Snapshot alive players' inventories
        for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
            for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
                if (!gamePlayer.isDisconnected()) {
                    Player player = gamePlayer.getPlayer();

                    if (player != null) {
                        if (!gamePlayer.isDead()) {
                            MatchSnapshot snapshot = new MatchSnapshot(player, false);
                            snapshot.setPotionsThrown(gamePlayer.getPotionsThrown());
                            snapshot.setPotionsMissed(gamePlayer.getPotionsMissed());
                            snapshot.setLongestCombo(gamePlayer.getLongestCombo());
                            snapshot.setTotalHits(gamePlayer.getHits());

                            snapshots.add(snapshot);
                        }
                    }
                }
            }
        }

        if (this instanceof BasicTeamMatch) {
            BasicTeamMatch match = (BasicTeamMatch) this;
            // Set opponents in snapshots if solo
            if (match.getParticipantA().getPlayers().size() == 1 && match.getParticipantB().getPlayers().size() == 1) {
                for (MatchSnapshot snapshot : snapshots) {
                    if (snapshot.getUuid().equals(match.getParticipantA().getLeader().getUuid())) {
                        snapshot.setOpponent(match.getParticipantB().getLeader().getUuid());
                    } else if (snapshot.getUuid().equals(match.getParticipantB().getLeader().getUuid())) {
                        snapshot.setOpponent(match.getParticipantA().getLeader().getUuid());
                    }
                }
            }
        }

        // Make all snapshots available
        for (MatchSnapshot snapshot : snapshots) {
            snapshot.setCreatedAt(System.currentTimeMillis());
            MatchSnapshot.getSnapshots().put(snapshot.getUuid(), snapshot);
        }

        // Send ending messages to game participants
        for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
            for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
                if (!gamePlayer.isDisconnected()) {
                    Player player = gamePlayer.getPlayer();


                    if (player != null) {
                        for (BaseComponent[] components : generateEndComponents(player)) {
                            if (!getKit().getGameRules().isBedFight() && !getKit().getGameRules().isLives()) {
                                player.spigot().sendMessage(components);
                            }
                        }
                    }
                }
            }
        }

        // Send ending messages to spectators
        for (Player player : getSpectatorsAsPlayers()) {
            for (BaseComponent[] components : generateEndComponents(player)) {
                player.spigot().sendMessage(components);
            }

            removeSpectator(player);
        }
    }

    public abstract boolean canEndRound();

    /**
     * This method is called when a player disconnects
     * It handles the player's disconnection, and sends the disconnection message
     * to all players in the match.
     *
     * @param dead {@link Player} The player that disconnected
     */
    public void onDisconnect(Player dead) {
        if (getKit().getGameRules().isBridge()) {
            BasicTeamRoundMatch match = (BasicTeamRoundMatch) this;
            if (match.getParticipantA().containsPlayer(dead.getUniqueId()))
                match.setWinningParticipant(match.getParticipantB());
            else match.setWinningParticipant(match.getParticipantA());
            end();
            return;
        }

        if (getKit().getGameRules().isBedFight()) {
            BasicTeamBedFight match = (BasicTeamBedFight) this;
            if (match.getParticipantA().containsPlayer(dead.getUniqueId()))
                match.setWinningParticipant(match.getParticipantB());
            else match.setWinningParticipant(match.getParticipantA());
            end();
            return;
        }

        if (getKit().getGameRules().isLives()) {
            BasicTeamLivesFight match = (BasicTeamLivesFight) this;
            if (match.getParticipantA().containsPlayer(dead.getUniqueId()))
                match.setWinningParticipant(match.getParticipantB());
            else match.setWinningParticipant(match.getParticipantA());
            end();
            return;
        }

        // Don't continue if the match is already ending
        if (!(state == MatchState.STARTING_ROUND || state == MatchState.PLAYING_ROUND)) return;

        MatchGamePlayer deadGamePlayer = getGamePlayer(dead);

        if (deadGamePlayer != null) {
            deadGamePlayer.setDisconnected(true);

            if (!deadGamePlayer.isDead()) {
                onDeath(dead);
            }
        }
    }

    /**
     * This method is called when a player dies
     * It handles the player's death, and sends the death message
     * to all players in the match.
     *
     * @param dead {@link Player} The player that died
     */
    public void onDeath(Player dead) {
        // Don't continue if the match is already ending
        if (!(state == MatchState.STARTING_ROUND || state == MatchState.PLAYING_ROUND)) return;

        MatchGamePlayer deadGamePlayer = getGamePlayer(dead);

        // Don't continue if the player is already dead
        if (deadGamePlayer.isDead()) return;
        Profile profile = Profile.get(dead.getUniqueId());

        // Get killer
        Player killer = PlayerUtil.getLastAttacker(dead);

        getParticipant(dead).setLives(getParticipant(dead).getLives() - 1);

        // Set player as dead
        if (getKit().getGameRules().isBridge()) {
            getParticipant(dead).getPlayers().forEach(gamePlayer -> gamePlayer.setDead(false));
        } else if (getKit().getGameRules().isBedFight()) {
            // Check if the participant has a bed
            if (getParticipant(dead).isHasBed()) {
                //				// Set player as alive if a bed
                getParticipant(dead).getPlayers().forEach(gamePlayer -> gamePlayer.setDead(false));
            } else {
                // Otherwise set the player as dead
                deadGamePlayer.setDead(true);
            }
        } else if (getKit().getGameRules().isLives()) {
            // Check if the participant has 0 lives
            if (getParticipant(dead).getLives() <= 0) {
                // Set the player as dead
                deadGamePlayer.setDead(true);
            } else {
                // Set the player as alive if they still have lives
                getParticipant(dead).getPlayers().forEach(gamePlayer -> gamePlayer.setDead(false));
            }
        } else {
            deadGamePlayer.setDead(true);
        }


        if (killer != null) {
            Profile winner = Profile.get(killer.getUniqueId());

            KillEffectType effect = winner.getKillEffectType();
            if (effect != null && effect.getCallable() != null) {
                effect.getCallable().call(dead.getPlayer().getLocation().clone().add(0.0, 1.0, 0.0));
            }

            if (PlayerUtil.getLastAttacker(dead) != null) {
                MatchGamePlayer matchGamePlayer = getGamePlayer(killer);
                if (matchGamePlayer != null) {
                    matchGamePlayer.incrementKills();
                }
            }
        }

        dead.setVelocity(new Vector());

        MatchSnapshot snapshot = new MatchSnapshot(dead, true); // Save snapshot of player. (Match information)
        snapshot.setPotionsMissed(deadGamePlayer.getPotionsMissed());
        snapshot.setPotionsThrown(deadGamePlayer.getPotionsThrown());
        snapshot.setLongestCombo(deadGamePlayer.getLongestCombo());
        snapshot.setTotalHits(deadGamePlayer.getHits());

        snapshots.add(snapshot);

        for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
            for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
                if (!gamePlayer.isDisconnected()) {
                    Player player = gamePlayer.getPlayer();

                    if (player != null) {
                        if (!getKit().getGameRules().isSumo() && !getKit().getGameRules().isBridge() && !getKit().getGameRules().isBedFight() && !getKit().getGameRules().isLives()) {
                            VisibilityLogic.handle(player, dead);
                        }
                        if (!getKit().getGameRules().isBridge() && !getKit().getGameRules().isBedFight() && !getKit().getGameRules().isLives()) {
                            sendDeathMessage(player, dead, killer);
                        }
                    }
                }
            }
        }

        // Handle visibility for spectators
        // Send death message
        for (Player player : getSpectatorsAsPlayers()) {
            if (!getKit().getGameRules().isSumo() && !getKit().getGameRules().isBridge() && !getKit().getGameRules().isBedFight() && !getKit().getGameRules().isLives()) {
                VisibilityLogic.handle(player, dead);
            }
            if (!getKit().getGameRules().isBridge() && !getKit().getGameRules().isBedFight() && !getKit().getGameRules().isLives()) {
                sendDeathMessage(player, dead, killer);
            }
        }

        if (canEndRound()) {
            state = MatchState.ENDING_ROUND;
            onRoundEnd();

            if (canEndMatch()) {
                state = MatchState.ENDING_MATCH;
                if (killer != null) {
                    Profile winner = new Profile(killer.getUniqueId());

                    //killer.sendTitle(new Title(CC.translate("&a&lVICTORY!"), CC.translate("&aYou &7won against &a" + dead.getName()), 1, 100, 0));

                    if (HyPractice.get().isRunningAlonsoLeagues()) {
                        AlonsoLeaguesAPI.addPoints(killer.getUniqueId(), 10);
                    }
                    int coins = getRandomNumber(10, 50);
                    int rankedCoins = getRandomNumber(50, 100);

                    if (isRanked()) {
                        winner.addCoins(rankedCoins);
                    } else {
                        winner.addCoins(coins);
                    }
                }

                if (killer != null) {
                    //dead.sendTitle(new Title(CC.translate("&c&lDEFEAT!"), CC.translate("&cYou &7lost to &c" + killer.getName()), 1, 100, 0));
                }

            }
            logicTask.setNextAction(4);
        } else {
            if (!(this instanceof BasicTeamRoundMatch) && (!(this instanceof BasicTeamBedFight) && (!(this instanceof BasicTeamLivesFight)))) {
                TaskUtil.runLater(() -> {
                    PlayerUtil.reset(dead);
                    addSpectator(dead, killer);
                }, 10L);
            } else {
                if (getKit().getGameRules().isBridge()) {
                    assert this instanceof BasicTeamRoundMatch;
                    BasicTeamRoundMatch teamRoundMatch = (BasicTeamRoundMatch) this;
                    Location spawn = teamRoundMatch.getParticipantA().containsPlayer(dead.getUniqueId()) ? teamRoundMatch.getArena().getSpawnA() : teamRoundMatch.getArena().getSpawnB();

                    TaskUtil.runLater(() -> {
                        dead.spigot().respawn();
                        PlayerUtil.reset(dead);
                        if (profile.getSelectedKit() == null) {
                            dead.getInventory().setContents(getKit().getKitLoadout().getContents());
                        } else {
                            dead.getInventory().setContents(profile.getSelectedKit().getContents());
                        }
                        dead.teleport(spawn.add(0, 2, 0));
                        KitUtils.giveBridgeKit(dead);
                    }, 1L);
                }
                if (getKit().getGameRules().isBedFight()) {
                    assert this instanceof BasicTeamBedFight;
                    BasicTeamBedFight teamRoundMatch = (BasicTeamBedFight) this;
                    Location spawn = teamRoundMatch.getParticipantA().containsPlayer(dead.getUniqueId()) ? teamRoundMatch.getArena().getSpawnA() : teamRoundMatch.getArena().getSpawnB();
                    GameParticipant<MatchGamePlayer> opposingTeam = teamRoundMatch.getParticipantA().containsPlayer(dead.getUniqueId()) ? teamRoundMatch.getParticipantA() : teamRoundMatch.getParticipantB();

                    if (opposingTeam.isHasBed() && teamRoundMatch.getState() != MatchState.ENDING_MATCH) {
                        PotionEffect weakness = new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0);
                        new BukkitRunnable() {
                            int respawn = 4;

                            @Override
                            public void run() {
                                if (respawn <= 1) {
                                    if (profile.getState() != ProfileState.FIGHTING) {
                                        cancel();
                                        return;
                                    }
                                    dead.removePotionEffect(PotionEffectType.WEAKNESS);
                                    dead.teleport(spawn.add(0, 2, 0));

                                    Player player = teamRoundMatch.getParticipantB().containsPlayer(dead.getUniqueId()) ? teamRoundMatch.getParticipantA().getLeader().getPlayer() : teamRoundMatch.getParticipantB().getLeader().getPlayer();
                                    player.showPlayer(dead);

                                    dead.setFallDistance(50);
                                    dead.setAllowFlight(false);
                                    dead.setFlying(false);

                                    dead.setHealth(dead.getMaxHealth());
                                    dead.setFoodLevel(20);

                                    dead.sendMessage(CC.translate("&aYou have respawned!"));
                                    dead.playSound(dead.getLocation(), Sound.ORB_PICKUP, 10, 1);

                                    Bukkit.getScheduler().runTaskLater(HyPractice.get(), () -> {
                                        dead.resetMaxHealth();
                                        dead.setHealth(dead.getMaxHealth());
                                        dead.setFoodLevel(20);

                                        PlayerUtil.reset(dead);
                                        if (profile.getSelectedKit() == null) {
                                            dead.getInventory().setContents(getKit().getKitLoadout().getContents());
                                        } else {
                                            dead.getInventory().setContents(profile.getSelectedKit().getContents());
                                        }
                                        KitUtils.giveBedFightKit(dead);
                                        //dead.sendTitle(new Title(CC.translate("&aRespawning..."), "", 1, 20, 0));
                                        cancel();
                                    }, 2L);
                                }

                                if (respawn == 4) {
                                    dead.spigot().respawn();
                                    PlayerUtil.reset(dead);

                                    dead.teleport(spawn.add(0, 2, 0));
                                    dead.addPotionEffect(weakness);

                                    Player player = teamRoundMatch.getParticipantB().containsPlayer(dead.getUniqueId()) ? teamRoundMatch.getParticipantA().getLeader().getPlayer() : teamRoundMatch.getParticipantB().getLeader().getPlayer();
                                    player.hidePlayer(dead);

                                    dead.getInventory().clear();
                                    dead.getInventory().setArmorContents(null);
                                    dead.updateInventory();

                                    dead.setHealth(dead.getMaxHealth());
                                    dead.setFoodLevel(20);

                                    dead.setVelocity(dead.getVelocity().add(new org.bukkit.util.Vector(0, 0.25, 0)));
                                    dead.setAllowFlight(true);
                                    dead.setFlying(true);
                                    dead.setVelocity(dead.getVelocity().add(new Vector(0, 0.15, 0)));
                                    dead.setAllowFlight(true);
                                    dead.setFlying(true);
                                }

                                respawn--;
                                //dead.sendTitle(new Title(CC.translate("&a") + respawn, "", 1, 20, 0));
                                dead.playSound(dead.getLocation(), Sound.NOTE_PLING, 10, 1);
                            }
                        }.runTaskTimer(HyPractice.get(), 1L, 20L);
                    } else {
                        TaskUtil.runLater(() -> {
                            dead.spigot().respawn();
                            PlayerUtil.reset(dead);
                            addSpectator(dead, killer);
                        }, 10L);
                    }
                }
                if (getKit().getGameRules().isLives()) {
                    assert this instanceof BasicTeamLivesFight;
                    BasicTeamLivesFight teamRoundMatch = (BasicTeamLivesFight) this;
                    Location spawn = teamRoundMatch.getParticipantA().containsPlayer(dead.getUniqueId()) ? teamRoundMatch.getArena().getSpawnA() : teamRoundMatch.getArena().getSpawnB();

                    if (teamRoundMatch.getState() != MatchState.ENDING_MATCH) {
                        PotionEffect weakness = new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0);
                        new BukkitRunnable() {
                            int respawn = 4;

                            @Override
                            public void run() {
                                if (respawn <= 1) {
                                    if (profile.getState() != ProfileState.FIGHTING) {
                                        cancel();
                                        return;
                                    }
                                    dead.removePotionEffect(PotionEffectType.WEAKNESS);
                                    dead.teleport(spawn.add(0, 2, 0));

                                    Player player = teamRoundMatch.getParticipantB().containsPlayer(dead.getUniqueId()) ? teamRoundMatch.getParticipantA().getLeader().getPlayer() : teamRoundMatch.getParticipantB().getLeader().getPlayer();
                                    player.showPlayer(dead);

                                    dead.setFallDistance(50);
                                    dead.setAllowFlight(false);
                                    dead.setFlying(false);

                                    dead.setHealth(dead.getMaxHealth());
                                    dead.setFoodLevel(20);

                                    dead.sendMessage(CC.translate("&aYou have respawned!"));
                                    dead.playSound(dead.getLocation(), Sound.ORB_PICKUP, 10, 1);

                                    Bukkit.getScheduler().runTaskLater(HyPractice.get(), () -> {
                                        dead.resetMaxHealth();
                                        dead.setHealth(dead.getMaxHealth());
                                        dead.setFoodLevel(20);

                                        PlayerUtil.reset(dead);
                                        if (profile.getSelectedKit() == null) {
                                            dead.getInventory().setContents(getKit().getKitLoadout().getContents());
                                        } else {
                                            dead.getInventory().setContents(profile.getSelectedKit().getContents());
                                        }
                                        KitUtils.giveLivesKit(dead);
                                        //.sendTitle(new Title(CC.translate("&aRespawning..."), "", 1, 20, 0));
                                        cancel();
                                    }, 2L);
                                }

                                if (respawn == 4) {
                                    dead.spigot().respawn();
                                    PlayerUtil.reset(dead);

                                    dead.teleport(teamRoundMatch.getParticipantB().containsPlayer(dead.getUniqueId()) ? teamRoundMatch.getParticipantA().getLeader().getPlayer().getLocation() : teamRoundMatch.getParticipantB().getLeader().getPlayer().getLocation());
                                    dead.addPotionEffect(weakness);

                                    Player player = teamRoundMatch.getParticipantB().containsPlayer(dead.getUniqueId()) ? teamRoundMatch.getParticipantA().getLeader().getPlayer() : teamRoundMatch.getParticipantB().getLeader().getPlayer();
                                    player.hidePlayer(dead);

                                    dead.getInventory().clear();
                                    dead.getInventory().setArmorContents(null);
                                    dead.updateInventory();

                                    dead.setHealth(dead.getMaxHealth());
                                    dead.setFoodLevel(20);

                                    dead.setVelocity(dead.getVelocity().add(new org.bukkit.util.Vector(0, 0.25, 0)));
                                    dead.setAllowFlight(true);
                                    dead.setFlying(true);
                                    dead.setVelocity(dead.getVelocity().add(new Vector(0, 0.15, 0)));
                                    dead.setAllowFlight(true);
                                    dead.setFlying(true);
                                }

                                respawn--;
                                //dead.sendTitle(new Title(CC.translate("&a") + respawn, "", 1, 20, 0));
                                dead.playSound(dead.getLocation(), Sound.NOTE_PLING, 10, 1);
                            }
                        }.runTaskTimer(HyPractice.get(), 1L, 20L);
                    } else {
                        TaskUtil.runLater(() -> {
                            dead.spigot().respawn();
                            PlayerUtil.reset(dead);
                            addSpectator(dead, killer);
                        }, 10L);
                    }
                }
            }
        }

        if (Tournament.getTournament() != null) {
            if (Tournament.getTournament().getPlayers().contains(dead.getUniqueId())) {
                profile.setInTournament(false);
            }
        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public abstract boolean isOnSameTeam(Player first, Player second);

    public abstract List<GameParticipant<MatchGamePlayer>> getParticipants();

    public GameParticipant<MatchGamePlayer> getParticipant(Player player) {
        for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
            if (gameParticipant.containsPlayer(player.getUniqueId())) {
                return gameParticipant;
            }
        }

        return null;
    }

    public MatchGamePlayer getGamePlayer(Player player) {
        for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
            for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
                if (gamePlayer.getUuid().equals(player.getUniqueId())) {
                    return gamePlayer;
                }
            }
        }

        return null;
    }

    public abstract ChatColor getRelationColor(Player viewer, Player target);

    public abstract List<String> getScoreboardLines(Player player);

    public void addSpectator(Player spectator, Player target) {
        Profile profile = Profile.get(spectator.getUniqueId());
        profile.setMatch(this);

        if (profile.getParty() == null) {
            spectator.teleport(target.getLocation().clone().add(0, 2, 0));
        }

        if (profile.getState() != ProfileState.STAFF_MODE) {
            profile.setState(ProfileState.SPECTATING);
            Hotbar.giveHotbarItems(spectator);
            spectator.updateInventory();
        }

        spectators.add(spectator.getUniqueId());

        VisibilityLogic.handle(spectator);

        for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
            for (GamePlayer gamePlayer : gameParticipant.getPlayers()) {
                if (!gamePlayer.isDisconnected()) {
                    Player bukkitPlayer = gamePlayer.getPlayer();

                    if (bukkitPlayer != null && !(profile.getState() == ProfileState.STAFF_MODE)) {
                        VisibilityLogic.handle(bukkitPlayer);
                        new MessageFormat(com.hysteria.practice.Locale.MATCH_NOW_SPECTATING.format(Profile.get(bukkitPlayer.getUniqueId()).getLocale()))
                                .add("{spectator_name}", spectator.getName())
                                .send(bukkitPlayer);
                    }
                }
            }
        }
        spectator.spigot().setCollidesWithEntities(false);
        TaskUtil.runLater(() -> {
            spectator.setGameMode(GameMode.CREATIVE);
            spectator.setAllowFlight(true);
            spectator.setFlying(true);
        }, 5L);
    }

    public void removeSpectator(Player spectator) {
        spectators.remove(spectator.getUniqueId());

        Profile profile = Profile.get(spectator.getUniqueId());

        if (profile.getState() != ProfileState.STAFF_MODE) {
            profile.setState(ProfileState.LOBBY);
            profile.setMatch(null);

            PlayerUtil.reset(spectator);
            Hotbar.giveHotbarItems(spectator);
            HyPractice.get().getEssentials().teleportToSpawn(spectator);
            VisibilityLogic.handle(spectator);
        } else {
            profile.setMatch(null);
            HyPractice.get().getEssentials().teleportToSpawn(spectator);
        }
        for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
            for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
                if (!gamePlayer.isDisconnected()) {
                    Player bukkitPlayer = gamePlayer.getPlayer();

                    if (bukkitPlayer != null && !(profile.getState() == ProfileState.STAFF_MODE)) {
                        VisibilityLogic.handle(bukkitPlayer);

                        if (state != MatchState.ENDING_MATCH) {
                            new MessageFormat(com.hysteria.practice.Locale.MATCH_NO_LONGER_SPECTATING.format(Profile.get(bukkitPlayer.getUniqueId()).getLocale()))
                                    .add("{spectator_name}", spectator.getName())
                                    .send(bukkitPlayer);
                        }
                    }
                }
            }
        }
    }

    public String getDuration() {
        if (state == MatchState.STARTING_ROUND) return "Starting";
        if (state == MatchState.ENDING_MATCH) return "Ending";
        else return TimeUtil.millisToTimer(System.currentTimeMillis() - timeData);
    }

    /**
     * Send a {@link String} message to all match participants
     *
     * @param message {@link String}
     */

    public void sendMessage(String message) {
        for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
            gameParticipant.sendMessage(CC.translate(message));
        }

        for (Player player : getSpectatorsAsPlayers()) {
            player.sendMessage(CC.translate(message));
        }
    }

    /**
     * Send a {@link String} message to all match participants
     *
     * @param lang          {@link com.hysteria.practice.Locale}
     * @param messageFormat {@link String}
     */

    public void sendMessage(com.hysteria.practice.Locale lang, MessageFormat messageFormat) {
        for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
            gameParticipant.sendMessage(lang, messageFormat);
        }

        for (Player player : getSpectatorsAsPlayers()) {
            messageFormat.setMessage(lang.format(Profile.get(player.getUniqueId()).getLocale()));
            messageFormat.send(player);
        }
    }


    public void broadcastTitle(String message, String subMessage, int stay) {
        //Title title = new Title(CC.translate(message), CC.translate(subMessage), 1, stay, 0);
        //getParticipants().forEach(gameParticipant -> gameParticipant.sendTitle(title));
    }


    /**
     * Send a {@link Sound} to all match participants
     *
     * @param sound {@link Sound}
     */

    public void sendSound(Sound sound, float volume, float pitch) {
        for (GameParticipant<MatchGamePlayer> gameParticipant : getParticipants()) {
            gameParticipant.sendSound(sound, volume, pitch);
        }

        for (Player player : getSpectatorsAsPlayers()) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    protected List<Player> getSpectatorsAsPlayers() {
        return spectators.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public abstract List<BaseComponent[]> generateEndComponents(Player player);

    public void sendDeathMessage(Player player, Player dead, Player killer) {
        String deathMessage;
        Profile profile = Profile.get(player.getUniqueId());

        if (killer == null) {
            deathMessage = new MessageFormat(com.hysteria.practice.Locale.MATCH_PLAYER_DIED.format(profile.getLocale()))
                    .add("{dead_name}", getRelationColor(player, dead) + dead.getName())
                    .toString();
        } else {
            deathMessage = new MessageFormat(com.hysteria.practice.Locale.MATCH_PLAYER_KILLED.format(profile.getLocale()))
                    .add("{dead_name}", getRelationColor(player, dead) + dead.getName())
                    .add("{killer_name}", getRelationColor(player, killer) + killer.getName())
                    .toString();
        }

        player.sendMessage(deathMessage);
    }

    public void sendDeathMessage(Player dead, Player killer) {
        String deathMessage;
        // Send death message
        for (GameParticipant<MatchGamePlayer> gameParticipant : this.getParticipants()) {
            for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
                if (!gamePlayer.isDisconnected()) {
                    Player other = gamePlayer.getPlayer();
                    if (other == null) return;
                    Profile profile = Profile.get(other.getUniqueId());

                    if (killer == null) {
                        deathMessage = new MessageFormat(com.hysteria.practice.Locale.MATCH_PLAYER_DIED.format(profile.getLocale()))
                                .add("{dead_name}", getRelationColor(other, dead) + dead.getName())
                                .toString();
                    } else {
                        deathMessage = new MessageFormat(com.hysteria.practice.Locale.MATCH_PLAYER_KILLED.format(profile.getLocale()))
                                .add("{dead_name}", getRelationColor(other, dead) + dead.getName())
                                .add("{killer_name}", getRelationColor(other, killer) + killer.getName())
                                .toString();
                    }
                    other.sendMessage(deathMessage);
                }
            }
        }

        // Handle visibility for spectators
        // Send death message
        for (Player other : this.getSpectatorsAsPlayers()) {
            Profile profile = Profile.get(other.getUniqueId());
            if (killer == null) {
                deathMessage = new MessageFormat(com.hysteria.practice.Locale.MATCH_PLAYER_DIED.format(profile.getLocale()))
                        .add("{dead_name}", getRelationColor(other, dead) + dead.getName())
                        .toString();
            } else {
                deathMessage = new MessageFormat(com.hysteria.practice.Locale.MATCH_PLAYER_KILLED.format(profile.getLocale()))
                        .add("{dead_name}", getRelationColor(other, dead) + dead.getName())
                        .add("{killer_name}", getRelationColor(other, killer) + killer.getName())
                        .toString();
            }
            other.sendMessage(deathMessage);
        }
    }

    public static void init() {
        new MatchPearlCooldownTask().runTaskTimer(HyPractice.get(), 2L, 2L);
        new MatchSnapshotCleanupTask().runTaskTimer(HyPractice.get(), 20L * 5, 20L * 5);
        HyPractice.get().getServer().getScheduler().runTaskTimer(HyPractice.get(), new MatchLiquidTask(), 20L, 8L);
    }

    public static void cleanup() {
        matches.forEach(match -> {
            match.getDroppedItems().forEach(Entity::remove);
            if (match.getArena() instanceof StandaloneArena) {
                ChunkRestorationManager.getIChunkRestoration().reset(match.getArena());
            }
        });
    }

    public static int getInFightsCount(Queue queue) {
        return matches.stream()
                .filter(match -> match.getQueue() != null && (match.getState() == MatchState.STARTING_ROUND || match.getState() == MatchState.PLAYING_ROUND)).flatMap(match -> match.getParticipants().stream())
                .mapToInt(participant -> participant.getPlayers().size())
                .sum();
    }

    public static BaseComponent[] generateInventoriesComponents(String prefix, GameParticipant<MatchGamePlayer> participant) {
        return generateInventoriesComponents(prefix, Collections.singletonList(participant));
    }

    public static BaseComponent[] generateInventoriesComponents(String prefix, List<GameParticipant<MatchGamePlayer>> participants) {
        ChatComponentBuilder builder = new ChatComponentBuilder(prefix);

        int totalPlayers = 0;
        int processedPlayers = 0;

        for (GameParticipant<MatchGamePlayer> gameParticipant : participants) {
            totalPlayers += gameParticipant.getPlayers().size();
        }

        for (GameParticipant<MatchGamePlayer> gameParticipant : participants) {
            for (MatchGamePlayer gamePlayer : gameParticipant.getPlayers()) {
                processedPlayers++;

                ChatComponentBuilder current = new ChatComponentBuilder(
                        CC.translate(new MessageFormat(com.hysteria.practice.Locale.MATCH_CLICK_TO_VIEW_NAME
                                .format(Profile.get(gamePlayer.getUuid()).getLocale()))
                                .add("{name}", gamePlayer.getUsername()).toString()))
                        .attachToEachPart(ChatHelper.hover(CC.translate(
                                new MessageFormat(Locale.MATCH_CLICK_TO_VIEW_HOVER
                                        .format(Profile.get(gamePlayer.getUuid()).getLocale()))
                                        .add("{name}", gamePlayer.getUsername()).toString())))
                        .attachToEachPart(ChatHelper.click("/viewinv " + gamePlayer.getUuid().toString()));

                builder.append(current.create());

                if (processedPlayers != totalPlayers) {
                    builder.append(", ");
                    builder.getCurrent().setClickEvent(null);
                    builder.getCurrent().setHoverEvent(null);
                }
            }
        }

        return builder.create();
    }

    public void addEntityToRemove(Entity entity) {
        this.entitiesToRemove.add(entity);
    }

    public void removeEntityToRemove(Entity entity) {
        this.entitiesToRemove.remove(entity);
    }
}
