package com.hysteria.practice;

import com.hysteria.practice.game.kit.Kit;
import com.hysteria.practice.game.arena.Arena;
import com.hysteria.practice.game.knockback.Knockback;
import com.hysteria.practice.game.knockback.KnockbackProfiler;

public class HyPracticeAPI {

    public static HyPracticeAPI INSTANCE;

    public static void setKnockbackProfile(KnockbackProfiler profile) {
        Knockback.setKnockbackProfiler(profile);
    }

    public static Kit getKit(String name) {
        return Kit.getByName(name);
    }

    public static Arena getRandomArena(Kit kit) {
        return Arena.getRandomArena(kit);
    }
}
