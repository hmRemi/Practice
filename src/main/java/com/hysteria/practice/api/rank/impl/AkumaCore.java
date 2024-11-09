package com.hysteria.practice.api.rank.impl;

import cc.insidious.akuma.api.AkumaAPI;
import cc.insidious.akuma.api.rank.Rank;

import java.util.UUID;

public class AkumaCore implements com.hysteria.practice.api.rank.Rank {

    @Override
    public String getName(UUID uuid) {
        Rank data = AkumaAPI.getInstance().getGrantHandler().getActiveRank(uuid);
        return data == null ? "No Data" : data.getName();
    }

    @Override
    public String getPrefix(UUID uuid) {
        Rank data = AkumaAPI.getInstance().getGrantHandler().getActiveRank(uuid);
        return data == null ? "No Data" : data.getPrefix();
    }

    @Override
    public String getSuffix(UUID uuid) {
        Rank data = AkumaAPI.getInstance().getGrantHandler().getActiveRank(uuid);
        return data == null ? "No Data" : data.getSuffix();
    }

    @Override
    public String getColor(UUID uuid) {
        Rank data = AkumaAPI.getInstance().getGrantHandler().getActiveRank(uuid);
        return data == null ? "No Data" : data.getColor().getCode();
    }

    @Override
    public int getWeight(UUID uuid) {
        Rank data = AkumaAPI.getInstance().getGrantHandler().getActiveRank(uuid);
        return data == null ? 0 : data.getWeight();
    }

}
