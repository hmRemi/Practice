package com.hysteria.practice.utilities.playerversion;

import com.hysteria.practice.utilities.playerversion.impl.PlayerVersionDefaultImpl;
import com.hysteria.practice.utilities.playerversion.impl.PlayerVersionProtocolLibImpl;
import com.hysteria.practice.utilities.playerversion.impl.PlayerVersionViaVersionImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class PlayerVersionHandler {

    public static IPlayerVersion version = new PlayerVersionDefaultImpl();

    public static void init() {
        /* Plugin Manager */
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();

        /* Detect plugin */
        if (pluginManager.getPlugin("ViaVersion") != null) {
            version = new PlayerVersionViaVersionImpl();
        }
        else if (pluginManager.getPlugin("ProtocolLib") != null) {
            version = new PlayerVersionProtocolLibImpl();
        }
    }
}
