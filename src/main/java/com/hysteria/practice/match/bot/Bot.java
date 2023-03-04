package com.hysteria.practice.match.bot;

import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.utilities.ItemUtil;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.util.PlayerAnimation;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.game.arena.impl.StandaloneArena;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/22/2023
 */
public class Bot {

    private NPC npc;
    private Kit kit;
    private Arena arena;
    private StandaloneArena standaloneArena;
    private boolean destroyed;
    private Random random;
    private BotDifficulty botDifficulty;
    public BotMechanics botMechanics;

    public Bot() {
        this.random = new Random();
    }

    public boolean isSpawned() {
        return this.npc.isSpawned();
    }

    public Player getBukkitEntity() {
        return (Player)this.npc.getEntity();
    }

    public void swing() {
        if (this.getBukkitEntity() != null) {
            PlayerAnimation.ARM_SWING.play(this.getBukkitEntity());
        }
    }

    public void destroy() {
        if (this.getBukkitEntity() != null) {
            this.getBukkitEntity().setHealth(20.0);
        }
        this.npc.despawn();
        this.npc.destroy();
        this.destroyed = true;
    }

    public void hurt(final boolean burn, final boolean critical, final boolean sharp) {
        this.getBukkitEntity().playEffect(EntityEffect.HURT);
        for (final Entity ent : this.getBukkitEntity().getNearbyEntities(100.0, 100.0, 100.0)) {
            if (ent instanceof Player) {
                this.getBukkitEntity().getWorld().playSound(this.getBukkitEntity().getLocation(), Sound.HURT_FLESH, 0.7f, 1.0f);
            }
        }
        if (burn) {
            this.getBukkitEntity().setFireTicks(20);
        }
        else {
            final Location l = this.getBukkitEntity().getLocation().add(0.0, 1.0, 0.0);
            if (critical) {
                for (int i = 0; i < this.random.nextInt(5) + 10; ++i) {
                    l.getWorld().playEffect(l, Effect.CRIT, 1);
                }
            }
            if (sharp) {
                for (int i = 0; i < this.random.nextInt(5) + 10; ++i) {
                    l.getWorld().playEffect(l, Effect.MAGIC_CRIT, 1);
                }
            }
        }
    }

    public void startMechanics(final List<UUID> players, final BotDifficulty difficulty) {
        this.botMechanics = new BotMechanics(this, players, difficulty);
    }

    public NPC getNpc() {
        return this.npc;
    }

    public Kit getKit() {
        return this.kit;
    }

    public Arena getArena() {
        return this.arena;
    }

    public StandaloneArena getStandaloneArena() {
        return this.standaloneArena;
    }

    public boolean isDestroyed() {
        return this.destroyed;
    }

    public Random getRandom() {
        return this.random;
    }

    public BotDifficulty getBotDifficulty() {
        return this.botDifficulty;
    }

    public BotMechanics getBotMechanics() {
        return this.botMechanics;
    }

    public void setNpc(final NPC npc) {
        this.npc = npc;
    }

    public void setKit(final Kit kit) {
        this.kit = kit;
    }

    public void setArena(final Arena arena) {
        this.arena = arena;
    }

    public void setStandaloneArena(final StandaloneArena standaloneArena) {
        this.standaloneArena = standaloneArena;
    }

    public void setDestroyed(final boolean destroyed) {
        this.destroyed = destroyed;
    }

    public void setRandom(final Random random) {
        this.random = random;
    }

    public void setBotDifficulty(final BotDifficulty botDifficulty) {
        this.botDifficulty = botDifficulty;
    }

    public void setBotMechanics(final BotMechanics botMechanics) {
        this.botMechanics = botMechanics;
    }

    public enum BotDifficulty
    {
        EASY(2.5, ItemUtil.createItem(Material.STAINED_GLASS_PANE, ChatColor.WHITE.toString() + ChatColor.BOLD + "Easy"), 1),
        MEDIUM(2.8, ItemUtil.createItem(Material.STAINED_GLASS_PANE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Medium", 1, (short)4),2 ),
        HARD(3.0, ItemUtil.createItem(Material.STAINED_GLASS_PANE, ChatColor.GOLD.toString() + ChatColor.BOLD + "Hard", 1, (short)1),3),
        EXPERT(3.2, ItemUtil.createItem(Material.STAINED_GLASS_PANE, ChatColor.RED.toString() + ChatColor.BOLD + "Expert", 1, (short)1),4);

        private double reach;
        private ItemStack item;
        private final int slot;

        private BotDifficulty(final double reach, final ItemStack item, int slot) {
            this.item = item;
            this.reach = reach;
            this.slot = slot;
        }

        public ItemStack getItem() {
            return this.item;
        }

        public double getReach() {
            return this.reach;
        }

        public int getSlot() {
            return slot;
        }
    }
}
