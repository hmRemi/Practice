package com.hysteria.practice.chunk.reset;

import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.ChunkSection;
import net.minecraft.server.v1_8_R3.NibbleArray;

public interface INekoChunkReset {
    void setSections(Chunk nmsChunk, ChunkSection[] sections);
    ChunkSection[] cloneSections(ChunkSection[] sections);
    ChunkSection cloneSection(ChunkSection chunkSection);

    default NibbleArray cloneNibbleArray(NibbleArray nibbleArray) {
        return new NibbleArray(nibbleArray.a().clone());
    }
}
