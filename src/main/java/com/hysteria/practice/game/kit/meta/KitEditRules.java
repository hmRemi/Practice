package com.hysteria.practice.game.kit.meta;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
public class KitEditRules {
	private boolean allowPotionFill;
	private final List<ItemStack> editorItems = Lists.newArrayList();
}
