package rip.crystal.practice.essentials.abilities.impl;

import com.google.common.collect.Sets;
import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.cooldown.LunarClientAPICooldown;
import rip.crystal.practice.essentials.abilities.Ability;
import rip.crystal.practice.essentials.abilities.utils.DurationFormatter;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.chat.CC;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;
import java.util.UUID;

public class GuardianAngel extends Ability {

    private final cPractice plugin = cPractice.get();
    private final Set<UUID> guardians = Sets.newHashSet();

    public GuardianAngel() {
        super("GUARDIAN_ANGEL");
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);

            Player player = event.getPlayer();
            Profile profile = Profile.get(player.getUniqueId());

            if (profile.getGuardianangel().onCooldown(player)) {
                player.sendMessage(CC.translate("&7You are on &4&lGuardian Angel &7cooldown for &4" + DurationFormatter.getRemaining(profile.getGuardianangel().getRemainingMilis(player), true, true)));
                player.updateInventory();
                return;
            }

            if(profile.getPartneritem().onCooldown(player)){
                player.sendMessage(CC.translate("&7You are on &4&lPartner Item &7cooldown &7for &4" + DurationFormatter.getRemaining(profile.getPartneritem().getRemainingMilis(player), true, true)));
                player.updateInventory();
                return;
            }

            PlayerUtil.decrement(player);

            profile.getGuardianangel().applyCooldown(player, 60 * 1000);
            profile.getPartneritem().applyCooldown(player,  10 * 1000);

            if(LunarClientAPI.getInstance().isRunningLunarClient(player)) {
                LunarClientAPICooldown.sendCooldown(player, "GuardianAngel");
            }
            guardians.add(player.getUniqueId());

            plugin.getAbilityManager().playerMessage(player, this.getAbility());
            plugin.getAbilityManager().cooldownExpired(player, this.getName(), this.getAbility());
        }
    }

    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (guardians.contains(event.getEntity().getUniqueId())) {
                Player player = (Player) event.getEntity();
                if (player.getHealth() < 3.0 || player.getHealth() == 3.0) {
                    player.setHealth(player.getMaxHealth());
                    player.playSound(player.getLocation(), Sound.DRINK, 1F, 1F);
                    guardians.remove(player.getUniqueId());
                }
            }
        }
    }
}