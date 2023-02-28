package rip.crystal.practice.match.bot;

import org.bukkit.util.Vector;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.potion.Potion;
import org.bukkit.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.citizensnpcs.util.PlayerAnimation;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.List;
import java.util.Random;
import org.bukkit.scheduler.BukkitRunnable;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.utilities.ItemUtil;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/22/2023
 */

public class BotMechanics extends BukkitRunnable
{
    private Bot Bot;
    private Bot.BotDifficulty difficulty;
    private Random random;
    private List<UUID> players;
    private boolean kit;
    private boolean navigation;
    private boolean selfHealing;
    private double attackRange;
    private double swingRangeModifier;
    private Player target;

    public BotMechanics(final Bot Bot, final List<UUID> players, final Bot.BotDifficulty difficulty) {
        this.random = new Random();
        this.target = null;
        this.setupBot(Bot, players, difficulty);
    }

    private void setupBot(final Bot Bot, final List<UUID> players, final Bot.BotDifficulty difficulty) {
        this.Bot = Bot;
        this.players = players;
        this.difficulty = difficulty;
        this.startupBotMechanics();
    }

    private void startupBotMechanics() {
        int delay = 2;
        this.attackRange = 3.2;
        if (this.difficulty == rip.crystal.practice.match.bot.Bot.BotDifficulty.EASY) {
            this.attackRange *= 0.8;
            this.swingRangeModifier = -0.5;
        }
        else if (this.difficulty == rip.crystal.practice.match.bot.Bot.BotDifficulty.HARD) {
            this.attackRange *= 2.0;
            this.swingRangeModifier = 2.0;
        }
        else if (this.difficulty == rip.crystal.practice.match.bot.Bot.BotDifficulty.EXPERT) {
            this.attackRange *= 2.6;
            this.swingRangeModifier = 3.0;
            delay = 1;
        }
        this.runTaskTimerAsynchronously((Plugin) cPractice.get(), 60L, (long)delay);
    }

    private void giveKit(final Kit kit) {
        Bot.getBukkitEntity().getInventory().setArmorContents(kit.getKitLoadout().getArmor());
        Bot.getBukkitEntity().getInventory().setContents(kit.getKitLoadout().getContents());
        this.kit = true;
    }

    private void attemptToHeal() {
        if (this.selfHealing) {
            return;
        }
        final Damageable d = (Damageable)this.Bot.getBukkitEntity();
        if (d.getHealth() <= 13.0 && this.random.nextBoolean() && !this.splashPotion() && !this.useSoupRefill()) {
            this.useGoldenApple();
        }
    }

    private boolean useGoldenApple() {
        ItemStack gapple = null;
        for (final ItemStack is : this.Bot.getBukkitEntity().getInventory().getContents()) {
            if (is != null && is.getType() == Material.GOLDEN_APPLE) {
                gapple = is.clone();
            }
        }
        if (gapple != null) {
            this.selfHealing = true;
            final ItemStack finalGapple = gapple;
            ItemStack hand = null;
            for (int i = 0; i < 9; ++i) {
                if (this.Bot.getBukkitEntity().getInventory().getItem(i) != null && this.Bot.getBukkitEntity().getInventory().getItem(i).equals((Object)gapple)) {
                    hand = this.Bot.getBukkitEntity().getInventory().getItem(i);
                    this.Bot.getBukkitEntity().getInventory().setHeldItemSlot(i);
                    break;
                }
            }
            if (hand == null) {
                this.Bot.getBukkitEntity().getInventory().setHeldItemSlot(1);
                ItemUtil.removeItems((Inventory)this.Bot.getBukkitEntity().getInventory(), finalGapple, 1);
                for (int i = 9; i < 36; ++i) {
                    if (this.Bot.getBukkitEntity().getInventory().getItem(i) == null || this.Bot.getBukkitEntity().getInventory().getItem(i).getType() == Material.AIR) {
                        this.Bot.getBukkitEntity().getInventory().setItem(i, this.Bot.getBukkitEntity().getItemInHand());
                        break;
                    }
                }
                this.Bot.getBukkitEntity().setItemInHand(gapple);
            }
            new BukkitRunnable() {
                public void run() {
                    BotMechanics.this.Bot.getBukkitEntity().setItemInHand(finalGapple);
                    try {
                        final Class<?> clz = PlayerAnimation.class;
                        clz.getField("START_USE_MAINHAND_ITEM");
                        try {
                            PlayerAnimation.START_USE_MAINHAND_ITEM.play(BotMechanics.this.Bot.getBukkitEntity());
                        }
                        catch (NoSuchFieldError noSuchFieldError) {}
                        clz.getField("EAT_FOOD");
                        try {
                            PlayerAnimation.EAT_FOOD.play(BotMechanics.this.Bot.getBukkitEntity());
                        }
                        catch (NoSuchFieldError noSuchFieldError2) {}
                    }
                    catch (Exception ex) {}
                    new BukkitRunnable() {
                        public void run() {
                            if (BotMechanics.this.Bot.getBukkitEntity() != null) {
                                BotMechanics.this.Bot.getNpc().getNavigator().setPaused(true);
                                BotMechanics.this.Bot.getBukkitEntity().setItemInHand(new ItemStack(Material.AIR));
                                finalGapple.setAmount(1);
                                if (finalGapple.getDurability() == 0) {
                                    BotMechanics.this.Bot.getBukkitEntity().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 1));
                                    BotMechanics.this.Bot.getBukkitEntity().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1));
                                }
                                else {
                                    BotMechanics.this.Bot.getBukkitEntity().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600, 4));
                                    BotMechanics.this.Bot.getBukkitEntity().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1));
                                    BotMechanics.this.Bot.getBukkitEntity().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6000, 0));
                                    BotMechanics.this.Bot.getBukkitEntity().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0));
                                }
                                ItemUtil.removeItems((Inventory)BotMechanics.this.Bot.getBukkitEntity().getInventory(), finalGapple, finalGapple.getAmount());
                                new BukkitRunnable() {
                                    public void run() {
                                        if (BotMechanics.this.Bot.getNpc() != null && BotMechanics.this.Bot.isSpawned() && BotMechanics.this.Bot.getNpc().getNavigator() != null) {
                                            BotMechanics.this.Bot.getNpc().getNavigator().setPaused(false);
                                            BotMechanics.this.Bot.getBukkitEntity().getInventory().setHeldItemSlot(0);
                                            BotMechanics.this.selfHealing = false;
                                        }
                                    }
                                }.runTaskLater((Plugin)cPractice.get(), (long)(BotMechanics.this.random.nextInt(4) + 2));
                            }
                        }
                    }.runTaskLater((Plugin)cPractice.get(), 35L);
                }
            }.runTaskLater((Plugin)cPractice.get(), (long)(this.random.nextInt(2) + 1));
            return true;
        }
        return false;
    }

    private boolean useSoupRefill() {
        ItemStack soup = null;
        for (final ItemStack is : this.Bot.getBukkitEntity().getInventory().getContents()) {
            if (is != null && is.getType() == Material.MUSHROOM_SOUP) {
                soup = is.clone();
            }
        }
        if (soup != null) {
            this.selfHealing = true;
            this.Bot.getNpc().getNavigator().setPaused(true);
            final ItemStack finalSoup = soup;
            ItemStack hand = null;
            for (int i = 0; i < 9; ++i) {
                if (this.Bot.getBukkitEntity().getInventory().getItem(i) != null && this.Bot.getBukkitEntity().getInventory().getItem(i).equals((Object)soup)) {
                    hand = this.Bot.getBukkitEntity().getInventory().getItem(i);
                    this.Bot.getBukkitEntity().getInventory().setHeldItemSlot(i);
                    break;
                }
            }
            if (hand == null) {
                this.Bot.getBukkitEntity().getInventory().setHeldItemSlot(1);
                ItemUtil.removeItems((Inventory)this.Bot.getBukkitEntity().getInventory(), finalSoup, 1);
                for (int i = 9; i < 36; ++i) {
                    if (this.Bot.getBukkitEntity().getInventory().getItem(i) == null || this.Bot.getBukkitEntity().getInventory().getItem(i).getType() == Material.AIR) {
                        this.Bot.getBukkitEntity().getInventory().setItem(i, this.Bot.getBukkitEntity().getItemInHand());
                        break;
                    }
                }
                this.Bot.getBukkitEntity().setItemInHand(soup);
            }
            this.Bot.getBukkitEntity().setItemInHand(finalSoup);
            new BukkitRunnable() {
                public void run() {
                    if (BotMechanics.this.Bot == null || BotMechanics.this.Bot.getBukkitEntity() == null || BotMechanics.this.Bot.getBukkitEntity().isDead() || finalSoup == null) {
                        return;
                    }
                    ItemUtil.removeItems((Inventory)BotMechanics.this.Bot.getBukkitEntity().getInventory(), finalSoup, finalSoup.getAmount());
                    if (!BotMechanics.this.Bot.getBukkitEntity().isDead()) {
                        final Damageable d = (Damageable)BotMechanics.this.Bot.getBukkitEntity();
                        d.setHealth((d.getHealth() < 13.0) ? (d.getHealth() + 7.0) : 20.0);
                        final Class<?> clz = PlayerAnimation.class;
                        try {
                            clz.getField("START_USE_MAINHAND_ITEM");
                            try {
                                PlayerAnimation.START_USE_MAINHAND_ITEM.play(BotMechanics.this.Bot.getBukkitEntity());
                            }
                            catch (NoSuchFieldError noSuchFieldError) {}
                        }
                        catch (Exception ex) {}
                        BotMechanics.this.Bot.getBukkitEntity().setItemInHand(new ItemStack(Material.BOWL));
                        new BukkitRunnable() {
                            public void run() {
                                if (BotMechanics.this.Bot.getNpc() != null && BotMechanics.this.Bot.isSpawned() && BotMechanics.this.Bot.getNpc().getNavigator() != null) {
                                    BotMechanics.this.Bot.getNpc().getNavigator().setPaused(false);
                                    final ItemStack is = BotMechanics.this.Bot.getBukkitEntity().getItemInHand();
                                    if (is != null) {
                                        BotMechanics.this.Bot.getBukkitEntity().setItemInHand(new ItemStack(Material.AIR));
                                        BotMechanics.this.Bot.getBukkitEntity().getInventory().setHeldItemSlot(0);
                                        BotMechanics.this.selfHealing = false;
                                    }
                                }
                            }
                        }.runTaskLater((Plugin)cPractice.get(), (long)(BotMechanics.this.random.nextInt(1) + 1));
                    }
                }
            }.runTaskLater((Plugin)cPractice.get(), (long)(this.random.nextInt(1) + 1));
            return true;
        }
        return false;
    }

    private boolean splashPotion() {
        if (!this.Bot.getBukkitEntity().isOnGround() && this.Bot.getBukkitEntity().getLocation().getY() - this.Bot.getBukkitEntity().getLocation().getBlockY() > 0.35 && this.random.nextInt(3) == 0) {
            return false;
        }
        ItemStack pot = null;
        for (final ItemStack is : this.Bot.getBukkitEntity().getInventory().getContents()) {
            if (is != null && ((is.getType() == Material.POTION && (is.getDurability() == 16421 || is.getDurability() == 16453)) || is.getDurability() == 438)) {
                pot = is.clone();
                break;
            }
        }
        if (pot != null) {
            this.selfHealing = true;
            final ItemStack finalPot = pot;
            ItemStack hand = null;
            for (int i = 0; i < 9; ++i) {
                if (this.Bot.getBukkitEntity().getInventory().getItem(i) != null && this.Bot.getBukkitEntity().getInventory().getItem(i).equals((Object)pot)) {
                    hand = this.Bot.getBukkitEntity().getInventory().getItem(i);
                    this.Bot.getBukkitEntity().getInventory().setHeldItemSlot(i);
                    break;
                }
            }
            if (hand == null) {
                this.Bot.getBukkitEntity().getInventory().setHeldItemSlot(1);
                ItemUtil.removeItems((Inventory)this.Bot.getBukkitEntity().getInventory(), finalPot, 1);
                for (int i = 9; i < 36; ++i) {
                    if (this.Bot.getBukkitEntity().getInventory().getItem(i) == null || this.Bot.getBukkitEntity().getInventory().getItem(i).getType() == Material.AIR) {
                        this.Bot.getBukkitEntity().getInventory().setItem(i, this.Bot.getBukkitEntity().getItemInHand());
                        this.Bot.getBukkitEntity().getInventory().setHeldItemSlot(1);
                        break;
                    }
                }
                this.Bot.getBukkitEntity().setItemInHand(pot);
            }
            this.Bot.getBukkitEntity().getInventory().setHeldItemSlot(this.random.nextInt(8) + 1);
            this.Bot.getBukkitEntity().setItemInHand(finalPot);
            final Location behind = this.Bot.getBukkitEntity().getLocation().add(this.Bot.getBukkitEntity().getLocation().getDirection().normalize().multiply(-5)).subtract(0.0, 10.0, 0.0);
            this.Bot.getNpc().getNavigator().setTarget(behind);
            new BukkitRunnable() {
                int targetCounter = BotMechanics.this.random.nextInt(5) + 5;
                int counter = this.targetCounter;

                public void run() {
                    if (BotMechanics.this.Bot.getNpc() != null && BotMechanics.this.Bot.isSpawned() && BotMechanics.this.Bot.getNpc().getNavigator() != null) {
                        --this.counter;
                        if (this.counter == 0 || Math.abs(BotMechanics.this.Bot.getBukkitEntity().getLocation().getPitch() - 90.0f) < 50.0f) {
                            this.cancel();
                            BotMechanics.this.Bot.swing();
                            final ThrownPotion thrownPotion = BotMechanics.this.getThrownPotion(finalPot);
                            new BukkitRunnable() {
                                public void run() {
                                    if (BotMechanics.this.selfHealing && thrownPotion != null && BotMechanics.this.Bot.getNpc() != null && BotMechanics.this.Bot.getNpc().isSpawned() && !BotMechanics.this.Bot.getBukkitEntity().isDead() && !thrownPotion.isDead()) {
                                        BotMechanics.this.Bot.getNpc().getNavigator().setTarget(thrownPotion.getLocation());
                                    }
                                    else {
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer((Plugin)cPractice.get(), 1L, 1L);
                            BotMechanics.this.Bot.getBukkitEntity().setItemInHand(new ItemStack(Material.AIR));
                            ItemUtil.removeItems((Inventory)BotMechanics.this.Bot.getBukkitEntity().getInventory(), finalPot, 1);
                            final Damageable d = (Damageable)BotMechanics.this.Bot.getBukkitEntity();
                            if (d.getHealth() < 12.0) {
                                ItemStack pot = null;
                                for (final ItemStack is : BotMechanics.this.Bot.getBukkitEntity().getInventory().getContents()) {
                                    if (is != null && is.getType() == Material.POTION && (is.getDurability() == 16421 || is.getDurability() == 16453)) {
                                        pot = is.clone();
                                        break;
                                    }
                                }
                                if (pot != null) {
                                    BotMechanics.this.Bot.swing();
                                    BotMechanics.this.getThrownPotion(pot);
                                    ItemUtil.removeItems((Inventory)BotMechanics.this.Bot.getBukkitEntity().getInventory(), pot, 1);
                                }
                            }
                            new BukkitRunnable() {
                                public void run() {
                                    if (BotMechanics.this.Bot.getNpc() != null && BotMechanics.this.Bot.isSpawned() && BotMechanics.this.Bot.getNpc().getNavigator() != null) {
                                        BotMechanics.this.Bot.getBukkitEntity().getInventory().setHeldItemSlot(0);
                                        BotMechanics.this.selfHealing = false;
                                    }
                                }
                            }.runTaskLater((Plugin)cPractice.get(), (long)(BotMechanics.this.random.nextInt(12) + 8));
                        }
                    }
                    else {
                        this.cancel();
                    }
                }
            }.runTaskTimer((Plugin)cPractice.get(), 1L, 1L);
            return true;
        }
        return this.Bot.isDestroyed();
    }

    private ThrownPotion getThrownPotion(final ItemStack potion) {
        final ThrownPotion thrownPotion = (ThrownPotion)this.Bot.getBukkitEntity().getWorld().spawnEntity(this.Bot.getBukkitEntity().getLocation(), EntityType.SPLASH_POTION);
        thrownPotion.getEffects().addAll(Potion.fromItemStack(potion).getEffects());
        thrownPotion.setShooter((ProjectileSource)this.Bot.getBukkitEntity());
        thrownPotion.setItem(potion);
        final Vector vec = this.Bot.getBukkitEntity().getLocation().getDirection();
        if (vec.getY() == 0.0) {
            vec.setY(-this.random.nextInt(2) + 1 + this.random.nextDouble() / 10.0);
        }
        thrownPotion.setVelocity(vec);
        return thrownPotion;
    }

    public void run() {
        if (this.players == null || this.players.isEmpty() || this.Bot.isDestroyed()) {
            this.cancel();
            return;
        }
        if (this.Bot.isSpawned() && this.Bot.getBukkitEntity() != null) {
            if (!this.kit) {
                this.Bot.getNpc().setProtected(false);
                this.giveKit(this.Bot.getKit());
            }
            /*if (!this.navigation) {
                this.navigation = true;
                if (this.difficulty == rip.crystal.practice.match.bot.Bot.BotDifficulty.HARD) {
                    this.Bot.getNpc().getNavigator().getLocalParameters().speedModifier(1.33f);
                }
                else if (this.difficulty == rip.crystal.practice.match.bot.Bot.BotDifficulty.EXPERT) {
                    this.Bot.getNpc().getNavigator().getLocalParameters().speedModifier(1.66f);
                }
                this.Bot.getNpc().getNavigator().getLocalParameters().attackRange(this.attackRange);
            }
            if (!this.Bot.getBukkitEntity().isDead() && this.Bot.getBukkitEntity().getLocation().getBlockY() < 0) {
                this.Bot.getBukkitEntity().setHealth(0.0);
                return;
            }
            if (this.Bot.getBukkitEntity().getVelocity().getY() < 0.1 && this.Bot.getBukkitEntity().getVelocity().getY() > -0.0784) {
                final Vector v = this.Bot.getNpc().getEntity().getVelocity();
                this.Bot.getNpc().getEntity().setVelocity(v.setY(-0.0784));
            }
            double distance = (this.target != null && this.target.getWorld().getName().equals(this.Bot.getBukkitEntity().getWorld().getName())) ? this.target.getLocation().distanceSquared(this.Bot.getBukkitEntity().getLocation()) : 22500.0;
            if (this.Bot.getNpc().getNavigator().getTargetAsLocation() == null || this.random.nextInt(10) == 0) {
                for (final UUID uuid : this.players) {
                    final Player pl = Bukkit.getPlayer(uuid);
                    if (pl != null && pl.getWorld().getName().equals(this.Bot.getBukkitEntity().getWorld().getName())) {
                        final double dis = this.Bot.getBukkitEntity().getLocation().distanceSquared(pl.getLocation());
                        if (dis >= distance) {
                            continue;
                        }
                        this.target = pl;
                        distance = dis;
                    }
                }
            }
            if (this.target != null && !this.selfHealing) {
                if (distance <= this.attackRange * this.attackRange * 1.5 && this.random.nextDouble() > 0.2) {
                    this.Bot.getNpc().getNavigator().setTarget((Entity)this.target, true);
                }
                else {
                    this.Bot.getNpc().getNavigator().setTarget(this.target.getLocation());
                }
                this.Bot.getNpc().getNavigator().setPaused(false);
            }
            if (this.Bot.getNpc().getNavigator().getTargetAsLocation() != null) {
                this.Bot.getBukkitEntity().setSprinting(true);
            }
            final double x = this.attackRange + this.swingRangeModifier + this.random.nextDouble() * 3.0;
            if (distance < x * x && !this.Bot.getNpc().getNavigator().isPaused() && !this.selfHealing) {
                this.Bot.swing();
            }
            if (!this.Bot.getBukkitEntity().isDead()) {
                this.attemptToHeal();
            }*/
        }
    }
}
