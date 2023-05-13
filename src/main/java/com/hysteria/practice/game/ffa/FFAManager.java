package com.hysteria.practice.game.ffa;
/* 
   Made by hypractice Development Team
   Created on 27.11.2021
*/

import com.hysteria.practice.Locale;
import com.hysteria.practice.game.arena.cuboid.Cuboid;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.UUIDFetcher;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.game.knockback.Knockback;
import com.hysteria.practice.match.task.FFAVisiblityTask;
import com.hysteria.practice.match.task.RefillCooldownTask;
import org.bukkit.entity.Player;

import java.util.*;

public class FFAManager {

    @Getter @Setter private Cuboid ffaSafezone;

    public void init() {
        new RefillCooldownTask().runTaskTimer(HyPractice.get(), 2L, 2L);
        new FFAVisiblityTask().runTaskTimer(HyPractice.get(), 2L, 2L);
    }

    public void firstJoinFfa(Player player, Arena arena) {
        Profile profile = Profile.get(player.getUniqueId());

        new MessageFormat(Locale.FFA_JOIN_MESSAGES.format(profile.getLocale()))
                .add("{kit}", getKit().getName())
                .add("{players}", "" + getFFAPlayers().size())
                .send(player);

        this.broadcastMessage("&b" + player.getName() + " &fhas joined FFA.");

        joinFFA(player, arena);
    }

    public void joinFFA(Player player, Arena ffaArena) {
        Kit kit = Kit.getByName(HyPractice.get().getMainConfig().getString("FFA.KIT")); // Initialize kit from config.

        Profile profile = Profile.get(player.getUniqueId());

        profile.setState(ProfileState.FFA); // Set player state to FFA

        Knockback.getKnockbackProfiler().setKnockback(player, "default");
        // Set the player's max damage ticks and knock-back
        player.setMaximumNoDamageTicks(Kit.getByName("NoDebuff").getGameRules().getHitDelay());
        player.setNoDamageTicks(Kit.getByName("NoDebuff").getGameRules().getHitDelay());

        player.setGameMode(GameMode.SURVIVAL); // Set player gamemode to survival
        player.setFlying(false);
        player.setAllowFlight(false);

        // Give kit loadout
        player.getInventory().setArmorContents(kit.getKitLoadout().getArmor());
        player.getInventory().setContents(kit.getKitLoadout().getContents());

        player.teleport(ffaArena.getSpawnA()); // Teleport player to arena (FFA Arena)

        for (Profile target : this.getFFAPlayers()) {
            Player targetPlayer = target.getPlayer();
            if(targetPlayer == null) {
                return;
            }
            if(profile.getState() != ProfileState.FFA) {
                return;
            }
            if(target.getState() != ProfileState.FFA) {
                return;
            }
            player.showPlayer(targetPlayer);
            targetPlayer.showPlayer(player);
        }
    }

    public Kit getKit() {
        return Kit.getByName(HyPractice.get().getMainConfig().getString("FFA.KIT"));
    }

    private void broadcastMessage(String message) {
        for (Profile ffaPlayer : HyPractice.get().getFfaManager().getFFAPlayers()) {
            ffaPlayer.msg(message);
        }
    }

    public Set<Profile> getFFAPlayers() {
        HashSet<Profile> set = new HashSet<>();
        for (Profile profile : Profile.getProfiles().values()) {
            if (profile.getState() != ProfileState.FFA) continue;
            set.add(profile);
        }
        return set;
    }
}

