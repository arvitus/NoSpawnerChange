package de.arvitus.nospawnerchange.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.nio.file.Path;

import static de.arvitus.nospawnerchange.NoSpawnerChange.*;

@ConfigSerializable
public class Config {
    private static final Path PATH = CONFIG_DIR.resolve("config.conf");
    private static final HoconConfigurationLoader LOADER = HoconConfigurationLoader.builder()
        .path(PATH)
        .prettyPrinting(true)
        .build();

    @Comment("Configuration for normal monster spawners")
    public SpawnerConfig monsterSpawner = new SpawnerConfig();
    @Comment("Configuration for trial spawners")
    public SpawnerConfig trialSpawner = new SpawnerConfig();

    public static Config loadOrCreate() {
        if (!PATH.toFile().isFile()) {
            CommentedConfigurationNode node = LOADER.createNode();
            try {
                node.set(new Config());
                LOADER.save(node);
            } catch (Exception e) {
                LOGGER.warn("Failed to save default config to disk", e);
            }
        }
        else {
            try {
                return LOADER.load().get(Config.class);
            } catch (Exception e) {
                if (CONFIG != null) {
                    LOGGER.warn("Failed to load config, using previous value instead", e);
                    return CONFIG;
                }
                LOGGER.warn("Failed to load config, using default config instead", e);
            }
        }
        return new Config();
    }
}