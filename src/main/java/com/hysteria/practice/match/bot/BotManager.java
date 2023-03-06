package com.hysteria.practice.match.bot;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.player.profile.Profile;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Hysteria Development
 * @project HyPractice
 * @date 3/5/2023
 */
public class BotManager {
    private final HashMap<UUID, Bot> npcRegistry;

    public BotManager() {
        this.npcRegistry = new HashMap<>();
    }

    public void createMatch(Player player, Kit kit, Bot.BotDifficulty difficulty, Arena arena) {
        final NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Zeus");
        npc.data().set("player-skin-name", "ziue");
        npc.spawn(arena.getSpawnA());
        npc.setProtected(false);
        player.teleport(arena.getSpawnB());

        final Bot bot = new Bot();
        bot.setBotDifficulty(difficulty);
        bot.setKit(kit);
        bot.setArena(arena);
        bot.setDestroyed(false);
        bot.setNpc(npc);
        bot.startMechanics(Collections.singletonList(player.getUniqueId()), difficulty);

        this.npcRegistry.put(player.getUniqueId(), bot);
    }

    public void removeMatch(final Player player, final boolean won) {
        if (!this.isTraining(player)) {
            return;
        }
        final Bot bot = this.npcRegistry.get(player.getUniqueId());
        if (bot.getBotMechanics() != null) {
            bot.getBotMechanics().cancel();
        }
        bot.destroy();
        this.npcRegistry.remove(player.getUniqueId());
    }

    public void removeMatch(final NPC npc) {
        final UUID uuid = this.getPlayerMatch(npc);
        if (uuid == null) {
            return;
        }
        if (!this.npcRegistry.containsKey(uuid)) {
            return;
        }
        final Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            final Player player2;
            Bukkit.getScheduler().runTaskLater(HyPractice.get(), () -> {
                return;
            }, 10L);
        }
        final Bot bot = this.npcRegistry.get(uuid);
        if (bot.getBotMechanics() != null) {
            bot.getBotMechanics().cancel();
        }
        bot.destroy();
        this.npcRegistry.remove(uuid);
    }

    public boolean isTraining(final Player player) {
        return this.npcRegistry.containsKey(player.getUniqueId());
    }

    public UUID getPlayerMatch(final NPC npcMatching) {
        for (final Map.Entry<UUID, Bot> map : this.npcRegistry.entrySet()) {
            if (map.getValue().getNpc().getUniqueId() == npcMatching.getUniqueId()) {
                return map.getKey();
            }
        }
        return null;
    }

    public Bot getBotFromNPC(final NPC npc) {
        for (final Map.Entry<UUID, Bot> map : this.npcRegistry.entrySet()) {
            if (map.getValue().getNpc().getUniqueId() == npc.getUniqueId()) {
                return map.getValue();
            }
        }
        return null;
    }

    public Bot getBotFromPlayer(final Player player) {
        for (final Map.Entry<UUID, Bot> map : this.npcRegistry.entrySet()) {
            if (map.getKey() == player.getUniqueId()) {
                return map.getValue();
            }
        }
        return null;
    }

    public HashMap<UUID, Bot> getNpcRegistry() {
        return this.npcRegistry;
    }
}