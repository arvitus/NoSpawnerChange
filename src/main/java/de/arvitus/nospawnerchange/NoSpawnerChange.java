package de.arvitus.nospawnerchange;

import de.arvitus.nospawnerchange.config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class NoSpawnerChange implements ModInitializer {
    public static final String MOD_ID = "nospawnerchange";
    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID);
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            LOGGER.info("Reloading {} config", MOD_ID);
            CONFIG = Config.loadOrCreate();
        });

        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(modContainer -> {
            ModMetadata meta = modContainer.getMetadata();
            LOGGER.info(
                "Loaded {} v{} by {}",
                meta.getName(),
                meta.getVersion(),
                meta.getAuthors().stream().findFirst().map(Person::getName).orElse("unknown")
            );
        });
    }    public static Config CONFIG = Config.loadOrCreate();


}