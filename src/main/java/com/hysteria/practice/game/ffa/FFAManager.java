package com.hysteria.practice.game.ffa;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.Locale;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.game.arena.cuboid.Cuboid;
import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.game.knockback.Knockback;
import com.hysteria.practice.match.task.FFAVisibilityTask;
import com.hysteria.practice.match.task.RefillCooldownTask;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.utilities.MessageFormat;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class FFAManager {

    private Cuboid ffaSafezone;

    public void initTasks() {
        new RefillCooldownTask().runTaskTimer(HyPractice.get(), 2L, 2L);
        new FFAVisibilityTask().runTaskTimer(HyPractice.get(), 2L, 2L);
    }

    public void handleFirstFFAJoin(Player player, Arena arena) {
        Profile profile = Profile.get(player.getUniqueId());

        new MessageFormat(Locale.FFA_JOIN_MESSAGES.format(profile.getLocale()))
                .add("{kit}", getKit().getName())
                .add("{players}", "" + getFFAPlayers().size())
                .send(player);

        this.broadcastMessage("&b" + player.getName() + " &fhas joined FFA.");

        joinFFA(player, arena);
    }

    public void joinFFA(Player player, Arena ffaArena) {
        Kit kit = HyPractice.get().getKitRepository().getKitByName(HyPractice.get().getMainConfig().getString("FFA.KIT")); // Initialize kit from config.

        Profile profile = Profile.get(player.getUniqueId());
        profile.setState(ProfileState.FFA); // Set player state to FFA

        Knockback.getKnockbackProfiler().setKnockback(player, "default");
        player.setMaximumNoDamageTicks(HyPractice.get().getKitRepository().getKitByName("NoDebuff").getGameRules().getHitDelay());
        player.setNoDamageTicks(HyPractice.get().getKitRepository().getKitByName("NoDebuff").getGameRules().getHitDelay());

        player.setGameMode(GameMode.SURVIVAL); // Set player gamemode to survival
        player.setFlying(false);
        player.setAllowFlight(false);

        // Give kit loadout
        player.getInventory().setArmorContents(kit.getKitLoadout().getArmor());
        player.getInventory().setContents(kit.getKitLoadout().getContents());

        player.teleport(ffaArena.getSpawnA()); // Teleport player to arena (FFA Arena)

        for (Profile target : this.getFFAPlayers()) {
            Player targetPlayer = target.getPlayer();
            if (targetPlayer == null || profile.getState() != ProfileState.FFA || target.getState() != ProfileState.FFA)
                continue;
            player.showPlayer(targetPlayer);
            targetPlayer.showPlayer(player);
        }
    }

    public Kit getKit() {
        return HyPractice.get().getKitRepository().getKitByName(HyPractice.get().getMainConfig().getString("FFA.KIT"));
    }

    private void broadcastMessage(String message) {
        getFFAPlayers().forEach(player -> player.msg(message));
    }

    public Set<Profile> getFFAPlayers() {
        HashSet<Profile> ffaPlayers = new HashSet<>();
        for (Profile profile : Profile.getProfiles().values()) {
            if (profile.getState() != ProfileState.FFA) continue;
            ffaPlayers.add(profile);
        }
        return ffaPlayers;
    }
}

