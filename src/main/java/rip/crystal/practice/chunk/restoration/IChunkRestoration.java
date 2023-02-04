package rip.crystal.practice.chunk.restoration;

import rip.crystal.practice.game.arena.cuboid.Cuboid;
import rip.crystal.practice.game.arena.impl.StandaloneArena;

public interface IChunkRestoration {
    void copy(StandaloneArena standaloneArena);
    void reset(StandaloneArena standaloneArena);
    void copy(Cuboid cuboid);
    void reset(Cuboid cuboid);
}