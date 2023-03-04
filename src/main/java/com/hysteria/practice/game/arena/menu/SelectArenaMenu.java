package com.hysteria.practice.game.arena.menu;

import com.hysteria.practice.utilities.menu.Button;
import com.hysteria.practice.utilities.menu.Menu;
import org.bukkit.entity.Player;

import java.util.Map;

public class SelectArenaMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return "&4Select Arena";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		return super.getButtons();
	}

}
