package com.realistic_highlands2.config;

import com.realistic_highlands2.RealisticHighlands2;
import org.bukkit.configuration.file.FileConfiguration;

public class PluginConfig {

    private final RealisticHighlands2 plugin;
    private FileConfiguration config;

    private String defaultWorldName;
    private boolean autoCreateDefaultWorld;
    private long seed;
    private double mountainHeightMultiplier;
    private double terrainRoughness;
    private double riverDepth;

    public PluginConfig(RealisticHighlands2 plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void loadConfig() {
        config = plugin.getConfig(); // Reload config object

        defaultWorldName = config.getString("settings.default-world-name", "world_realistic");
        autoCreateDefaultWorld = config.getBoolean("settings.auto-create-default-world", true);
        seed = config.getLong("settings.seed", 0L); // 0L means random if not set
        mountainHeightMultiplier = config.getDouble("generation-params.mountain-height-multiplier", 2.5);
        terrainRoughness = config.getDouble("generation-params.terrain-roughness", 0.008); // Smaller for smoother, larger for rougher
        riverDepth = config.getDouble("generation-params.river-depth", 0.7);

        // Save defaults if they don't exist (ensures config.yml has all options after reload)
        config.addDefault("settings.default-world-name", defaultWorldName);
        config.addDefault("settings.auto-create-default-world", autoCreateDefaultWorld);
        config.addDefault("settings.seed", seed);
        config.addDefault("generation-params.mountain-height-multiplier", mountainHeightMultiplier);
        config.addDefault("generation-params.terrain-roughness", terrainRoughness);
        config.addDefault("generation-params.river-depth", riverDepth);
        plugin.saveConfig(); // This saves any default values that were missing
    }

    public void saveConfig() {
        config.set("settings.seed", seed);
        // Other settings like mountainHeightMultiplier could also be saved if they become dynamic
        plugin.saveConfig();
    }

    // Getters
    public String getDefaultWorldName() {
        return defaultWorldName;
    }

    public boolean isAutoCreateDefaultWorld() {
        return autoCreateDefaultWorld;
    }

    public long getSeed() {
        return seed;
    }

    public double getMountainHeightMultiplier() {
        return mountainHeightMultiplier;
    }

    public double getTerrainRoughness() {
        return terrainRoughness;
    }

    public double getRiverDepth() {
        return riverDepth;
    }

    // Setters
    public void setSeed(long seed) {
        this.seed = seed;
    }
}
