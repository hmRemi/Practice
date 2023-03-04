package com.hysteria.practice.chunk.restoration;

import com.hysteria.practice.game.arena.cuboid.Cuboid;
import com.hysteria.practice.game.arena.impl.StandaloneArena;

public interface IChunkRestoration {
    void copy(StandaloneArena standaloneArena);
    void reset(StandaloneArena standaloneArena);
    void copy(Cuboid cuboid);
    void reset(Cuboid cuboid);
}