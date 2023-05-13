package com.hysteria.practice.player.cosmetics.impl.killeffects;
/* 
   Made by hypractice Development Team
   Created on 30.11.2021
*/

import com.hysteria.practice.utilities.effect.ParticleEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;

public enum KillEffectType
{
    ANGRY("Angry", 10, Material.RED_ROSE, 500, location -> ParticleEffect.VILLAGER_ANGRY.display(0.5f, 0.5f, 0.5f, 0.01f, 25, location, 20.0)),
    BLOOD("Blood", 11, Material.REDSTONE, 500, location -> {
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.REDSTONE, (byte)0), 0.2f, 0.2f, 0.2f, 0.1f, 5, location, 20.0);
        ParticleEffect.BLOCK_DUST.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte)0), 0.2f, 0.2f, 0.2f, 0.1f, 5, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.REDSTONE, (byte)0), 0.2f, 0.2f, 0.2f, 0.1f, 5, location, 20.0);
        ParticleEffect.BLOCK_DUST.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte)0), 0.2f, 0.2f, 0.2f, 0.1f, 5, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.REDSTONE, (byte)0), 0.2f, 0.2f, 0.2f, 0.1f, 5, location, 20.0);
        ParticleEffect.BLOCK_DUST.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte)0), 0.2f, 0.2f, 0.2f, 0.1f, 5, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.REDSTONE, (byte)0), 0.2f, 0.2f, 0.2f, 0.1f, 5, location, 20.0);
        ParticleEffect.BLOCK_DUST.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte)0), 0.2f, 0.2f, 0.2f, 0.1f, 5, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.REDSTONE, (byte)0), 0.2f, 0.2f, 0.2f, 0.1f, 5, location, 20.0);
        ParticleEffect.BLOCK_DUST.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte)0), 0.2f, 0.2f, 0.2f, 0.1f, 5, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.REDSTONE, (byte)0), 0.2f, 0.2f, 0.2f, 0.1f, 5, location, 20.0);
        ParticleEffect.BLOCK_DUST.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte)0), 0.2f, 0.2f, 0.2f, 0.1f, 5, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.REDSTONE, (byte)0), 0.2f, 0.2f, 0.2f, 0.1f, 5, location, 20.0);
        ParticleEffect.BLOCK_DUST.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte)0), 0.2f, 0.2f, 0.2f, 0.1f, 5, location, 20.0);
        return;
    }),
    CHESS("Chess", 12, Material.BEDROCK, 500, location -> {
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.QUARTZ_BLOCK, (byte)0), 0.3f, 0.3f, 0.3f, 0.1f, 5, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.COAL_BLOCK, (byte)0), 0.3f, 0.3f, 0.3f, 0.1f, 5, location, 20.0);
        return;
    }),
    CLOUD("Cloud", 13, Material.WOOL, 500, location -> ParticleEffect.CLOUD.display(0.0f, 0.0f, 0.0f, 0.1f, 100, location, 20.0)),
    COAL("Coal", 14, Material.COAL, 500, location -> {
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.COAL, (byte)0), 0.3f, 0.3f, 0.3f, 0.1f, 20, location, 20.0);
        ParticleEffect.SMOKE_LARGE.display(0.3f, 0.3f, 0.3f, 0.1f, 3, location, 20.0);
        return;
    }),
    COOKIE("Cookie", 15, Material.COOKIE, 500, location -> ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.COOKIE, (byte)0), 0.7f, 0.7f, 0.7f, 0.1f, 35, location, 20.0)),
    DIAMOND("Diamond", 16, Material.DIAMOND, 500, location -> {
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.DIAMOND, (byte)0), 0.3f, 0.3f, 0.3f, 0.1f, 2, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.DIAMOND, (byte)0), 0.3f, 0.3f, 0.3f, 0.1f, 2, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.DIAMOND, (byte)0), 0.3f, 0.3f, 0.3f, 0.1f, 2, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.DIAMOND, (byte)0), 0.3f, 0.3f, 0.3f, 0.1f, 2, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.DIAMOND, (byte)0), 0.3f, 0.3f, 0.3f, 0.1f, 2, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.DIAMOND, (byte)0), 0.3f, 0.3f, 0.3f, 0.1f, 2, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.DIAMOND, (byte)0), 0.3f, 0.3f, 0.3f, 0.1f, 2, location, 20.0);
        ParticleEffect.FIREWORKS_SPARK.display(0.3f, 0.3f, 0.3f, 0.1f, 3, location, 20.0);
        return;
    }),
    EMERALD("Emerald", 19, Material.EMERALD, 500, location -> {
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.EMERALD, (byte)0), 0.3f, 0.3f, 0.3f, 0.1f, 20, location, 20.0);
        ParticleEffect.FIREWORKS_SPARK.display(0.3f, 0.3f, 0.3f, 0.1f, 3, location, 20.0);
        return;
    }),
    TNT("TNT", 20, Material.TNT, 500, location -> ParticleEffect.EXPLOSION_LARGE.display(0.5f, 0.5f, 0.5f, 1.0f, 12, location, 20.0)),
    FIREWORK("Firework", 21, Material.FIREWORK, 500, location -> {
        location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        return;
    }),
    FLAME("Flame", 22, Material.FLINT_AND_STEEL, 500, location -> ParticleEffect.FLAME.display(0.2f, 0.2f, 0.2f, 0.1f, 10, location, 20.0)),
    GOLD("Gold", 23, Material.GOLD_INGOT, 500, location -> {
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.GOLD_INGOT, (byte)0), 0.3f, 0.3f, 0.3f, 0.1f, 20, location, 20.0);
        ParticleEffect.FIREWORKS_SPARK.display(0.3f, 0.3f, 0.3f, 0.1f, 3, location, 20.0);
        return;
    }),
    HALLOWEEN("Halloween", 24, Material.PUMPKIN, 500, location -> {
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.PUMPKIN, (byte)0), 0.3f, 0.3f, 0.3f, 0.1f, 10, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.DEAD_BUSH, (byte)0), 0.3f, 0.3f, 0.3f, 0.1f, 10, location, 20.0);
        return;
    }),
    HAPPY("Happy", 25, Material.EMERALD_ORE, 500, location -> ParticleEffect.VILLAGER_HAPPY.display(0.5f, 0.5f, 0.5f, 0.01f, 100, location, 20.0)),
    HEART("Heart", 28, Material.REDSTONE_BLOCK, 500, location -> ParticleEffect.HEART.display(0.4f, 0.4f, 0.4f, 0.1f, 10, location, 20.0)),
    IRON("Iron", 29, Material.IRON_INGOT, 500, location -> {
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.IRON_INGOT, (byte)0), 0.3f, 0.3f, 0.3f, 0.1f, 20, location, 20.0);
        ParticleEffect.FIREWORKS_SPARK.display(0.3f, 0.3f, 0.3f, 0.1f, 3, location, 20.0);
        return;
    }),
    LAVA("Lava", 30, Material.LAVA_BUCKET, 500, location -> ParticleEffect.LAVA.display(0.5f, 0.5f, 0.5f, 0.1f, 12, location, 20.0)),
    LIGHTNING("Lightning", 31, Material.STICK, 500, location -> location.getWorld().strikeLightningEffect(location)),
    NOTE("Note", 32, Material.NOTE_BLOCK, 500, location -> ParticleEffect.NOTE.display(0.5f, 0.5f, 0.5f, 1.0f, 12, location, 20.0)),
    THUNDER("Thunder", 33, Material.NETHER_STAR, 500, location -> {
        ParticleEffect.CLOUD.display(0.3f, 0.3f, 0.3f, 0.1f, 20, location, 20.0);
        ParticleEffect.WATER_SPLASH.display(0.3f, 0.4f, 0.3f, 0.1f, 20, location, 20.0);
        return;
    }),
    NONE("None", 34, Material.BARRIER, 0, location -> {

    });


    private final String name;
    private final int price;
    private final int slot;
    private final Material material;
    private final EffectCallable callable;

    public static KillEffectType getByName(final String input) {
        return Arrays.stream(values()).filter(type -> type.name().equalsIgnoreCase(input) || type.getName().equalsIgnoreCase(input)).findFirst().orElse(null);
    }

    public boolean hasPermission(final Player player) {
        return player.hasPermission(this.getPermissionForAll()) || player.hasPermission(this.getPermission());
    }

    public String getPermissionForAll() {
        return "cosmetics.killeffect.*";
    }

    public String getPermission() {
        return "cosmetics.killeffect." + this.name().toLowerCase();
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public int getSlot() {
        return this.slot;
    }


    public Material getMaterial() {
        return material;
    }

    public EffectCallable getCallable() {
        return this.callable;
    }

    KillEffectType(final String name, final int slot, Material material,  int price, final EffectCallable callable) {
        this.name = name;
        this.slot = slot;
        this.material = material;
        this.price = price;
        this.callable = callable;
    }
}

