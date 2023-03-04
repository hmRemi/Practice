package com.hysteria.practice.shop.command;

import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.entity.Player;
import com.hysteria.practice.shop.menu.ShopMenu;

public class ShopCommand extends BaseCommand {

    @Command(name="shop", aliases = {"store", "coinsshop, coinshop"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        new ShopMenu().openMenu(player);
    }
}
