package rip.crystal.practice.chunk.data;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.ChunkSection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * ask first, and give me the credits.
 * Arigato! n.n
 */
@Getter @Setter
public class NekoChunkData {

	public Map<NekoChunk, ChunkSection[]> chunks = new ConcurrentHashMap<>();

	public ChunkSection[] getNyaChunk(int x, int z) {
		for (Map.Entry<NekoChunk, ChunkSection[]> chunksFromMap : chunks.entrySet()) {
			if (chunksFromMap.getKey().getX() == x && chunksFromMap.getKey().getZ() == z) {
				return chunksFromMap.getValue();
			}
		}

		return null;
	}
}
