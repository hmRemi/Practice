package rip.crystal.practice.game.ffa;
/* 
   Made by cpractice Development Team
   Created on 27.11.2021
*/

import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.cuboid.Cuboid;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.game.knockback.Knockback;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;
import rip.crystal.practice.utilities.chat.CC;
import org.bukkit.entity.Player;

import java.util.*;

public class FFAManager {

    @Getter @Setter private Cuboid ffaSafezone;

    public int killstreak;

    public void joinFFA(Player player, Arena ffaArena) {
        Kit kit = Kit.getByName(cPractice.get().getMainConfig().getString("FFA.KIT")); // Initialize kit from config.

        Profile profile = Profile.get(player.getUniqueId());

        profile.setState(ProfileState.FFA); // Set player state to FFA

        Knockback.getKnockbackProfiler().setKnockback(player, "default");

        player.setGameMode(GameMode.SURVIVAL); // Set player gamemode to survival
        player.setFlying(false);
        player.setAllowFlight(false);

        // Give kit loadout
        player.getInventory().setArmorContents(kit.getKitLoadout().getArmor());
        player.getInventory().setContents(kit.getKitLoadout().getContents());

        player.teleport(ffaArena.getSpawnA()); // Teleport player to arena (FFA Arena)

        player.sendMessage(CC.translate("&4&lPractice FFA"));
        player.sendMessage(CC.translate("&7&l✸ &fKit: &4" + getKit().getName()));
        player.sendMessage(CC.translate("&7&l✸ &fPlayers: &4" + getFFAPlayers().size()));
        player.sendMessage(CC.translate(""));
        player.sendMessage(CC.translate("&7&l✸ &4To leave the FFA Arena, do &4/ffa leave."));

        this.broadcastMessage("&4" + player.getName() + " &fhas joined FFA.");

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
        return Kit.getByName(cPractice.get().getMainConfig().getString("FFA.KIT"));
    }

    private void broadcastMessage(String message) {
        for (Profile ffaPlayer : cPractice.get().getFfaManager().getFFAPlayers()) {
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

