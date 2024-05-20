package com.hysteria.practice.match;

import lombok.Data;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

@Data
public class MatchSnapshot {

	@Getter
	private static Map<UUID, MatchSnapshot> snapshots = new HashMap<>();

	private final String username;
	private final double health;
	private final UUID uuid;

	private int potionsThrown, potionsMissed, longestCombo, totalHits;
	private final int hunger;

	private final Collection<PotionEffect> effects;
	private final ItemStack[] armor, contents;
	private long createdAt;
	private UUID opponent;

	public MatchSnapshot(Player player, boolean dead) {
		this.uuid = player.getUniqueId();
		this.username = player.getName();
		this.health = dead ? 0 : (player.getHealth() == 0 ? 0 : Math.round(player.getHealth() / 2));
		this.hunger = player.getFoodLevel();
		this.armor = player.getInventory().getArmorContents();
		this.contents = player.getInventory().getContents();
		this.effects = player.getActivePotionEffects();
		this.createdAt = System.currentTimeMillis();
	}

	public int getRemainingPotions() {
		return (int) Arrays.stream(contents)
				.filter(itemStack -> itemStack != null && itemStack.getType() == Material.POTION && itemStack.getDurability() == 16421)
				.count();
	}

	public boolean shouldDisplayRemainingPotions() {
		return this.getRemainingPotions() > 0 || potionsThrown > 0 || potionsMissed > 0;
	}

	public double getPotionAccuracy() {
		if (potionsMissed == 0) {
			return 100.0;
		} else if (potionsThrown == potionsMissed) {
			return 50.0;
		}

		return Math.round(100.0D - (((double) potionsMissed / (double) potionsThrown) * 100.0D));
	}

	public static MatchSnapshot getByUuid(UUID uuid) {
		return snapshots.get(uuid);
	}

	public static MatchSnapshot getByName(String name) {
		return snapshots.values().stream().filter(snapshot -> snapshot.getUsername().equals(name)).findFirst().orElse(null);
	}
}
