package com.hysteria.practice.api.rank.impl;

import com.hysteria.practice.api.rank.Rank;
import meth.crystal.aspirin.api.player.GlobalPlayer;
import meth.crystal.aspirin.api.player.PlayerData;
import meth.crystal.aspirin.plugin.AspirinAPI;

import java.util.UUID;

/**
 * @author Hysteria Development
 * @project HyPractice
 * @date 4/25/2023
 */

public class Aspirin implements Rank {

    @Override
    public String getName(UUID uuid) {
        PlayerData data = AspirinAPI.INSTANCE.getPlayerData(uuid);
        return data == null ? "No Data" : data.getHighestRank().getName();
    }

    @Override
    public String getPrefix(UUID uuid) {
        PlayerData data = AspirinAPI.INSTANCE.getPlayerData(uuid);
        return data == null ? "No Data" : data.getHighestRank().getPrefix();
    }

    @Override
    public String getSuffix(UUID uuid) {
        PlayerData data = AspirinAPI.INSTANCE.getPlayerData(uuid);
        return data == null ? "No Data" : data.getHighestRank().getSuffix();
    }

    @Override
    public String getColor(UUID uuid) {
        PlayerData data = AspirinAPI.INSTANCE.getPlayerData(uuid);
        return data == null ? "No Data" : data.getHighestRank().getColor() + data.getHighestRank().getName();
    }

    @Override
    public int getWeight(UUID uuid) {
        GlobalPlayer globalPlayer = AspirinAPI.INSTANCE.getGlobalPlayer(uuid);
        return globalPlayer == null ? 0 : globalPlayer.getRankWeight();
    }
}