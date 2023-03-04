package com.hysteria.practice.essentials.command.staff;
/* 
   Made by cpractice Development Team
   Created on 28.11.2021
*/

import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.api.command.BaseCommand;
import com.hysteria.practice.api.command.Command;
import com.hysteria.practice.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TrollCommand extends BaseCommand {

    @Command(name="troll", permission = "cpractice.troll")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();
        if(args.length == 0) {
            player.sendMessage(CC.translate("&4Usage: /troll (player)"));
        }
        if (args.length > 0) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
            if (p.isOnline()) {
                Player player2 = Bukkit.getPlayer(args[0]);
                String path = Bukkit.getServer().getClass().getPackage().getName();
                String version = path.substring(path.lastIndexOf(".") + 1);
                try {
                    Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
                    Class<?> PacketPlayOutGameStateChange = Class.forName("net.minecraft.server." + version + ".PacketPlayOutGameStateChange");
                    Class<?> Packet = Class.forName("net.minecraft.server." + version + ".Packet");
                    Constructor<?> playOutConstructor = PacketPlayOutGameStateChange.getConstructor(Integer.TYPE, Float.TYPE);
                    Object packet = playOutConstructor.newInstance(5, 0);
                    Object craftPlayerObject = craftPlayer.cast(player2);
                    Method getHandleMethod = craftPlayer.getMethod("getHandle");
                    Object handle = getHandleMethod.invoke(craftPlayerObject);
                    Object pc = handle.getClass().getField("playerConnection").get(handle);
                    Method sendPacketMethod = pc.getClass().getMethod("sendPacket", Packet);
                    sendPacketMethod.invoke(pc, packet);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                player.sendMessage(CC.translate("&4" + player2.getName() + " &fgot trolled!"));
            }
        }
    }
}
