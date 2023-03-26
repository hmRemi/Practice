package com.hysteria.practice.chunk;

import com.hysteria.practice.HyPractice;
import com.hysteria.practice.utilities.LocationUtil;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkManager {

	private final HyPractice plugin = HyPractice.get();
	private boolean chunksLoaded;

	public ChunkManager() {
		new BukkitRunnable() {
			@Override
			public void run() {
				loadChunks();
			}
		}.runTaskLater(plugin, 2L);
	}

	private void loadChunks() {
		FileConfiguration configuration = HyPractice.get().getArenasConfig().getConfiguration();

		if (configuration.contains("arenas")) {
			for (String arenaName : configuration.getConfigurationSection("arenas").getKeys(false)) {
				String path = "arenas." + arenaName;

				Location location1 = LocationUtil.deserialize(configuration.getString(path + ".cuboid.location1"));
				Location location2 = LocationUtil.deserialize(configuration.getString(path + ".cuboid.location2"));

				if (location1 != null && location2 != null) {
					int spawnMinX = location1.getBlockX() >> 4;
					int spawnMinZ = location2.getBlockZ() >> 4;
					int spawnMaxX = location1.getBlockX() >> 4;
					int spawnMaxZ = location2.getBlockZ() >> 4;

					if (spawnMinX > spawnMaxX) {
						int lastSpawnMinX = spawnMinX;
						spawnMinX = spawnMaxX;
						spawnMaxX = lastSpawnMinX;
					}

					if (spawnMinZ > spawnMaxZ) {
						int lastSpawnMinZ = spawnMinZ;
						spawnMinZ = spawnMaxZ;
						spawnMaxZ = lastSpawnMinZ;
					}

					World spawnWorld = location1.getWorld();
					for (int x = spawnMinX; x <= spawnMaxX; x++) {
						for (int z = spawnMinZ; z <= spawnMaxZ; z++) {
							Chunk chunk = spawnWorld.getChunkAt(x >> 4, z >> 4);
							if (!chunk.isLoaded()) {
								chunk.load();
							}
						}
					}
				} else {
					plugin.getLogger().info(" ");
					plugin.getLogger().info("                ERROR     ERROR    ERROR                ");
					plugin.getLogger().info("		Please make sure you set the Spawn Locations");
					plugin.getLogger().info("                ERROR     ERROR    ERROR                ");
					plugin.getLogger().info(" ");
				}
			}
		}

		plugin.getLogger().info("Finished loading all the chunks!");
		this.chunksLoaded = true;
	}

	public boolean areChunksLoaded() {
		return chunksLoaded;
	}
}
