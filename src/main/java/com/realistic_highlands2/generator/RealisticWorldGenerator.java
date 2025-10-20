package com.realistic_highlands2.generator;

import com.realistic_highlands2.RealisticHighlands2;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.PerlinNoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RealisticWorldGenerator extends ChunkGenerator {

    private final RealisticHighlands2 plugin;
    private final PerlinNoiseGenerator heightNoise;
    private final PerlinNoiseGenerator detailNoise;
    private final SimplexNoiseGenerator riverNoise;
    private final SimplexNoiseGenerator lakeNoise;
    private final long worldSeed;

    public RealisticWorldGenerator(RealisticHighlands2 plugin, long seed) {
        this.plugin = plugin;
        this.worldSeed = (seed == 0L) ? ThreadLocalRandom.current().nextLong() : seed;

        Random rand = new Random(this.worldSeed);
        this.heightNoise = new PerlinNoiseGenerator(rand.nextLong());
        this.detailNoise = new PerlinNoiseGenerator(rand.nextLong());
        this.riverNoise = new SimplexNoiseGenerator(rand.nextLong());
        this.lakeNoise = new SimplexNoiseGenerator(rand.nextLong());
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomes) {
        ChunkData chunk = create  ChunkData(world);

        // Get generation parameters from config (can be dynamic)
        double mountainHeightMultiplier = plugin.getPluginConfig().getMountainHeightMultiplier();
        double terrainRoughness = plugin.getPluginConfig().getTerrainRoughness();
        double riverDepth = plugin.getPluginConfig().getRiverDepth();

        int seaLevel = 63;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;

                // Base terrain height (large features, mountains)
                double height = heightNoise.noise(worldX * terrainRoughness, worldZ * terrainRoughness, 8, 0.5, true) * mountainHeightMultiplier;

                // Add finer detail to the terrain
                height += detailNoise.noise(worldX * terrainRoughness * 2.0, worldZ * terrainRoughness * 2.0, 4, 0.5, true) * mountainHeightMultiplier * 0.2;

                // River generation
                double riverValue = riverNoise.noise(worldX * 0.015, worldZ * 0.015, 2, 0.5, true);
                if (riverValue > 0.4) { // Higher value for narrower, deeper rivers
                    height -= (riverValue - 0.4) * 20 * riverDepth; // Deeper rivers
                }

                // Lake generation
                double lakeValue = lakeNoise.noise(worldX * 0.005, worldZ * 0.005, 3, 0.6, true);
                if (lakeValue > 0.6) { // Large, flat areas for lakes
                    height = Math.min(height, seaLevel - 5); // Ensure lakes are below sea level
                }

                int surfaceHeight = (int) (height + seaLevel);

                // Ensure surface is above bedrock
                if (surfaceHeight < 1) surfaceHeight = 1;

                // Fill blocks from bedrock to surface
                for (int y = 0; y < surfaceHeight; y++) {
                    if (y == 0) {
                        chunk.setBlock(x, y, z, Material.BEDROCK);
                    } else if (y < surfaceHeight - 4) {
                        chunk.setBlock(x, y, z, Material.STONE);
                    } else {
                        chunk.setBlock(x, y, z, Material.DIRT);
                    }
                }

                // Top layer
                if (surfaceHeight >= seaLevel) {
                    chunk.setBlock(x, surfaceHeight, z, Material.GRASS_BLOCK);
                } else { // Below sea level, water or sand
                    chunk.setBlock(x, surfaceHeight, z, Material.SAND);
                    for (int y = surfaceHeight + 1; y <= seaLevel; y++) {
                        chunk.setBlock(x, y, z, Material.WATER);
                    }
                }
            }
        }

        return chunk;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return true; // We manage surface generation
    }

    @Override
    public boolean shouldGenerateBedrock() {
        return false; // We place bedrock manually
    }

    @Override
    public boolean shouldGenerateCaves() {
        return true; // Let Minecraft handle caves typically unless we want custom ones
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true; // Allow mob spawning
    }

    @Override
    public boolean shouldGenerateStructures() {
        return true; // Allow structures like villages, strongholds, etc.
    }
}