package rip.crystal.practice.chunk;

import lombok.Getter;
import lombok.Setter;
import rip.crystal.practice.chunk.data.NekoChunkData;
import rip.crystal.practice.chunk.reset.INekoChunkReset;
import rip.crystal.practice.chunk.reset.impl.VanillaNekoChunkReset;
import rip.crystal.practice.chunk.restoration.IChunkRestoration;
import rip.crystal.practice.chunk.restoration.impl.FrostChunkRestoration;
import rip.crystal.practice.game.arena.cuboid.Cuboid;
import rip.crystal.practice.game.arena.impl.StandaloneArena;

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
            iChunkRestoration = new FrostChunkRestoration(iNekoChunkReset, this);
        }
    }
}