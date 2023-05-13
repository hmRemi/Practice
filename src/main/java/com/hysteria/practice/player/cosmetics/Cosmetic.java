package com.hysteria.practice.player.cosmetics;
/* 
   Made by hypractice Development Team
   Created on 30.11.2021
*/

import com.hysteria.practice.player.cosmetics.impl.killeffects.KillEffectType;
import com.hysteria.practice.player.profile.Profile;

import java.util.UUID;

public class Cosmetic {

    private final int deathEffects = KillEffectType.values().length;

    public String getDeathEffect(UUID uuid) {
        Profile profile = Profile.get(uuid);
        return profile.getKillEffectType().getName();
    }

    public void setDeathEffect(UUID uuid, KillEffectType deathEffect) {
        Profile profile = Profile.get(uuid);
        profile.setKillEffectType(deathEffect);
    }

    public int getDeathEffects() {
        return deathEffects;
    }
}
