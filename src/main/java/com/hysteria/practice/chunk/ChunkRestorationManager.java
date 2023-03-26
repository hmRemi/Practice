package com.hysteria.practice.chunk;

import com.hysteria.practice.chunk.data.NekoChunkData;
import com.hysteria.practice.chunk.reset.INekoChunkReset;
import com.hysteria.practice.chunk.reset.impl.VanillaNekoChunkReset;
import com.hysteria.practice.chunk.restoration.IChunkRestoration;
import com.hysteria.practice.chunk.restoration.impl.HyChunkRestoration;
import com.hysteria.practice.game.arena.cuboid.Cuboid;
import lombok.Getter;
import lombok.Setter;
import com.hysteria.practice.game.arena.impl.StandaloneArena;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * ask first, and give me the credits.
 * Arigato! n.n
 */
@Getter
public class ChunkRestorationManager {

    @Setter @Getter private static INekoChunkReset iNekoChunkReset;
    @Setter @Getter private static IChunkRestoration iChunkRestoration;

    private final Map<StandaloneArena, NekoChunkData> chunks = new ConcurrentHashMap<>();
    private final Map<Cuboid, NekoChunkData> eventMapChunks = new ConcurrentHashMap<>();

    public ChunkRestorationManager() {
        if (iNekoChunkReset == null) { // Let the other plugins create an INekoReset before we load ours.
            iNekoChunkReset = new VanillaNekoChunkReset();
        }
        if (iChunkRestoration == null) { // Let the other plugins create an IChunkRestoration before we load ours.
            iChunkRestoration = new HyChunkRestoration(iNekoChunkReset, this);
        }
    }
}