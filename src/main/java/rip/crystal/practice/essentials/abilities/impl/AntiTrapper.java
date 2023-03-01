package rip.crystal.practice.essentials.abilities.impl;

import org.bukkit.event.player.PlayerMoveEvent;
import rip.crystal.practice.essentials.abilities.Ability;
import rip.crystal.practice.essentials.abilities.utils.DurationFormatter;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.Utils;
import rip.crystal.practice.utilities.chat.CC;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public class AntiTrapper extends Ability {

    private final cPractice plugin = cPractice.get();
    public static Map<String, Long> cooldownvic;
    public int count;

    public AntiTrapper() {
        super("ANTI_TRAPPER");
        AntiTrapper.cooldownvic = new HashMap<>();
        this.count = 0;
    }

    static {
        AntiTrapper.cooldownvic = new HashMap<>();
    }

    public static boolean isOnCooldownVic(Player player) {
        return AntiTrapper.cooldownvic.containsKey(player.getName())
                && AntiTrapper.cooldownvic.get(player.getName()) > System.currentTimeMillis();
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            Profile profile = Profile.get(damager.getUniqueId());


            if (!isAbility(damager.getItemInHand())) {
                return;
            }
            if (isAbility(damager.getItemInHand())) {
                if (profile.getAntitrapper().onCooldown(damager)) {
                    damager.sendMessage(CC.translate("&7You are on &c&lAntiTrapper &7cooldown for &4" + DurationFormatter.getRemaining(profile.getAntitrapper().getRemainingMilis(damager), true, true)));
                    damager.updateInventory();
                    return;
                }

                if (profile.getPartneritem().onCooldown(damager)) {
                    damager.sendMessage(CC.translate("&7You are on &c&lPartner Item &7cooldown &7for &4" + DurationFormatter.getRemaining(profile.getPartneritem().getRemainingMilis(damager), true, true)));
                    damager.updateInventory();
                    return;
                }

                count = count + 1;
                if (count >= 3) {
                    count = 0;

                    // Apply cooldown on third hit
                    profile.getAntitrapper().applyCooldown(damager, 60 * 1000);
                    profile.getPartneritem().applyCooldown(damager,  10 * 1000);

                    // Apply cooldown on victim to prevent interaction
                    AntiTrapper.cooldownvic.put(victim.getName(), System.currentTimeMillis() + (15 * 1000));

                    plugin.getAbilityManager().playerMessage(damager, this.getAbility());
                    plugin.getAbilityManager().targetMessage(victim, damager, this.getAbility());

                    //damager.sendMessage(CC.translate(cPractice.get().getAbilityConfig().getString("ANTI_TRAPPER.MESSAGE.PLAYER")).replace("%target%", victim.getName()));
                    //victim.sendMessage(CC.translate(cPractice.get().getAbilityConfig().getString("ANTI_TRAPPER.MESSAGE.TARGET"))
                            //.//replace("%player%", damager.getName()));
                    PlayerUtil.decrement(damager);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Profile profile = Profile.get(player.getUniqueId());
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (!isAbility(player.getItemInHand())) {
                return;
            }
            if (isAbility(player.getItemInHand())) {
                if (this.hasCooldown(player)) {
                    player.sendMessage(CC.translate("&7You are on cooldown for &4" + DurationFormatter.getRemaining(profile.getAntitrapper().getRemainingMilis(player), true)));
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (AntiTrapper.isOnCooldownVic(player)) {
            long millisLeft = AntiTrapper.cooldownvic.get(event.getPlayer().getName()) - System.currentTimeMillis();
            event.setCancelled(true);
            player.sendMessage(CC.translate("&7You can't place blocks for another &4" + Utils.formatLongMin(millisLeft) + " &7seconds"));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (AntiTrapper.isOnCooldownVic(player)) {
            long millisLeft = AntiTrapper.cooldownvic.get(event.getPlayer().getName()) - System.currentTimeMillis();
            event.setCancelled(true);
            player.sendMessage(CC.translate("&7You can't break blocks for another &4" + Utils.formatLongMin(millisLeft) + " &7seconds"));
        }
    }

    @EventHandler
    public void onFenceInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (block.getType().equals(Material.FENCE_GATE) || block.getType().equals(Material.CHEST)) {
                if (AntiTrapper.isOnCooldownVic(player)) {
                    long millisLeft = AntiTrapper.cooldownvic.get(event.getPlayer().getName()) - System.currentTimeMillis();
                    event.setCancelled(true);
                    player.sendMessage(CC.translate("&7You can't interact with blocks for another &4" + Utils.formatLongMin(millisLeft) + " &7seconds"));
                }
            }
        }
    }
}
