package com.hysteria.practice.game.ffa;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.Locale;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.player.profile.Profile;
import com.hysteria.practice.player.profile.ProfileState;
import com.hysteria.practice.utilities.Cooldown;
import com.hysteria.practice.utilities.MessageFormat;
import com.hysteria.practice.utilities.PlayerUtil;
import com.hysteria.practice.utilities.TimeUtil;
import com.hysteria.practice.utilities.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class FFAListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final Profile profile = Profile.get(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA) return;
        profile.setRefillCooldown(new Cooldown(0));

        event.setDeathMessage(null);
        event.setDroppedExp(0);
        event.getDrops().clear();
        player.setHealth(20.0);

        new FFATeleportRunnable(player).runTaskLater(HyPractice.get(), 5L);

        final Player killer = PlayerUtil.getLastAttacker(event.getEntity());
        if (killer == null) return;
        this.broadcastMessage("&b" + player.getName() + " &fwas slain by &b" + killer.getName() + "&f.");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onLow(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Profile profile = Profile.get(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA) return;
        if (player.getLocation().getBlockY() > 30) return;
        new FFATeleportRunnable(player).runTaskLater(HyPractice.get(), 5L);
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        final Profile profile = Profile.get(event.getPlayer().getUniqueId());
        final Player player = event.getPlayer();
        Item getItemDrop = event.getItemDrop();
        if (profile.getState() != ProfileState.FFA) return;
        if (getItemDrop.getItemStack().getType() == Material.DIAMOND_SWORD) {
            player.sendMessage(CC.translate("&cYou cannot drop your sword."));
            event.setCancelled(true);
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                getItemDrop.remove();
            }
        }.runTaskLater(HyPractice.get(), 5000L);
    }

    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
        final Profile profile = Profile.get(event.getPlayer().getUniqueId());
        if (profile.getState() != ProfileState.FFA) return;
        event.setCancelled(false);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        final Player player = event.getPlayer();
        final Profile profile = Profile.get(player.getUniqueId());

        if (profile.getState() != ProfileState.FFA) return;
        if (!event.getItem().getType().equals(Material.POTION)) return;

        int heldSlot = player.getInventory().getHeldItemSlot();
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(HyPractice.get(), () -> {
            ItemStack held = player.getInventory().getItem(heldSlot);
            if (held != null && held.getType() == Material.GLASS_BOTTLE) {
                held.setAmount(0);
            }
        }, 1L);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        final Player player = (Player) event.getDamager();
        final Player victim = (Player) event.getEntity();
        final Profile profile = Profile.get(player.getUniqueId());

        if (profile.getState() != ProfileState.FFA) return;
        if (HyPractice.get().getFfaManager().getFfaSafezone() == null
                || (!HyPractice.get().getFfaManager().getFfaSafezone().contains(victim.getLocation())
                && !HyPractice.get().getFfaManager().getFfaSafezone().contains(player.getLocation()))) return;

        player.sendMessage(CC.translate("&cYou can't attack players in safezone!"));
        event.setCancelled(true);
    }

    @EventHandler
    public void onPearlLand(PlayerTeleportEvent event) {
        final Location to = event.getTo();
        final Profile profile = Profile.get(event.getPlayer().getUniqueId());
        if (profile.getState() != ProfileState.FFA) return;
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;

        to.setX(to.getBlockX() + 0.5);
        to.setZ(to.getBlockZ() + 0.5);
        event.setTo(to);

        final Location pearlLocation = event.getTo();
        final Location playerLocation = event.getFrom();
        if (playerLocation.getBlockY() < pearlLocation.getBlockY()) {
            Block block = pearlLocation.getBlock();
            for (BlockFace face : BlockFace.values()) {
                Material type = block.getRelative(face).getType();
                if (type == Material.GLASS || type == Material.BARRIER) {
                    pearlLocation.setY(pearlLocation.getBlockY() - 1.0);
                    break;
                }
            }
        } else {
            pearlLocation.setY(pearlLocation.getBlockY() + 0.0);
        }
        event.setTo(pearlLocation);
    }

    @EventHandler
    public void onPearl(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Profile profile = Profile.get(player.getUniqueId());

        if (profile.getState() != ProfileState.FFA) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR || !event.hasItem())
            return;

        if (event.getItem().getType() != Material.ENDER_PEARL) return;
        if (profile.getEnderpearlCooldown().hasExpired()) return;

        String time = TimeUtil.millisToSeconds(profile.getEnderpearlCooldown().getRemaining());
        new MessageFormat(Locale.MATCH_ENDERPEARL_COOLDOWN.format(profile.getLocale())).add("{context}", (time.equalsIgnoreCase("1.0") ? "" : "s")).add("{time}", time).send(player);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPearlLaunch(ProjectileLaunchEvent event) {
        final Player player = (Player) event.getEntity().getShooter();
        if (player == null) return;

        final Profile profile = Profile.get(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA) return;
        if (!(event.getEntity().getShooter() instanceof Player) || !(event.getEntity() instanceof EnderPearl) || profile.getState() != ProfileState.FFA)
            return;
        if (!profile.getEnderpearlCooldown().hasExpired()) return;
        profile.setEnderpearlCooldown(new Cooldown(16_000));
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (!e.getLine(0).equalsIgnoreCase("[Refill]")) return;
        e.setLine(0, "§b[Refill]");
        e.setLine(1, "§fRight Click");
        e.setLine(2, "§fTo refill!!");
    }

    public Inventory refillInv = Bukkit.createInventory(null, 36, "§7Refill Potions");

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        final Profile profile = Profile.get(player.getUniqueId());

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!(e.getClickedBlock().getState() instanceof Sign) || !((Sign) (e.getClickedBlock().getState())).getLine(0).equalsIgnoreCase("§b[Refill]")) return;
        if (!profile.getRefillCooldown().hasExpired()) {
            String time = TimeUtil.millisToSeconds(profile.getRefillCooldown().getRemaining());
            new MessageFormat(Locale.FFA_REFILL_COOLDOWN.format(profile.getLocale())).add("{context}", (time.equalsIgnoreCase("1.0") ? "" : "s")).add("{time}", time).send(player);
            e.setCancelled(true);
            return;
        }
        int slot = 0;
        while (slot < this.refillInv.getSize()) {
            this.refillInv.setItem(slot, new ItemStack(Material.POTION, 1, (short) 16421));
            ++slot;
        }
        e.getPlayer().openInventory(this.refillInv);
        if (!profile.getRefillCooldown().hasExpired()) return;
        profile.setRefillCooldown(new Cooldown(30_000));
    }

    private void broadcastMessage(String message) {
        HyPractice.get().getFfaManager().getFFAPlayers().forEach(player -> player.msg(CC.translate(message)));
    }

    private static class FFATeleportRunnable extends BukkitRunnable {
        private final Player player;

        public FFATeleportRunnable(Player player) {
            this.player = player;
        }

        public void run() {
            HyPractice.get().getFfaManager().joinFFA(player, Objects.requireNonNull(Arena.getByName("FFA")));
        }
    }
}
