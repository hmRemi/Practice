package com.hysteria.practice.game.kit;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.game.kit.meta.KitEditRules;
import com.hysteria.practice.game.kit.meta.KitGameRules;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class Kit {

    private final String name;
    private String displayName;

    private ItemStack displayIcon;
    private boolean enabled;
    private int slot;

    private final KitEditRules editRules = new KitEditRules();
    private final KitGameRules gameRules = new KitGameRules();
    private final KitLoadout kitLoadout = new KitLoadout();

    public Kit(String name) {
        this.name = name;
        this.displayIcon = new ItemStack(Material.DIAMOND_SWORD);
    }

    public ItemStack getDisplayIcon() {
        return this.displayIcon.clone();
    }
}