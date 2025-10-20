package com.realistic_highlands2;

import com.realistic_highlands2.commands.RLGenCommand;
import com.realistic_highlands2.commands.RLSeedCommand;
import com.realistic_highlands2.commands.RLWorldCommand;
import com.realistic_highlands2.config.PluginConfig;
import com.realistic_highlands2.generator.RealisticWorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.generator.ChunkGenerator;
import java.util.Objects;

public final class RealisticHighlands2 extends JavaPlugin {

    private static RealisticHighlands2 instance;
    private PluginConfig pluginConfig;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        pluginConfig = new PluginConfig(this);
        pluginConfig.loadConfig();

        // Register commands
        Objects.requireNonNull(getCommand("rlgen")).setExecutor(new RLGenCommand(this));
        Objects.requireNonNull(getCommand("rlworld")).setExecutor(new RLWorldCommand(this));
        Objects.requireNonNull(getCommand("rlseed")).setExecutor(new RLSeedCommand(this));

        getLogger().info("RealisticHighlands2 has been enabled!");

        // Optionally create a default world if it doesn't exist
        String defaultWorldName = pluginConfig.getDefaultWorldName();
        if (Bukkit.getWorld(defaultWorldName) == null && pluginConfig.isAutoCreateDefaultWorld()) {
            getLogger().info("Attempting to create default RealisticHighlands2 world: " + defaultWorldName);
            WorldCreator wc = new WorldCreator(defaultWorldName);
            wc.generator(getDefaultChunkGenerator(defaultWorldName));
            wc.seed(pluginConfig.getSeed());
            Bukkit.createWorld(wc);
            getLogger().info("Default RealisticHighlands2 world '" + defaultWorldName + "' created or configured.");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("RealisticHighlands2 has been disabled!");
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new RealisticWorldGenerator(this, pluginConfig.getSeed());
    }
    
    public ChunkGenerator getDefaultChunkGenerator(String worldName) {
        return new RealisticWorldGenerator(this, pluginConfig.getSeed());
    }

    public static RealisticHighlands2 getInstance() {
        return instance;
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }
}
