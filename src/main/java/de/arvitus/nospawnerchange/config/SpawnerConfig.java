package de.arvitus.nospawnerchange.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class SpawnerConfig {
    @Comment("Whether to allow changing the mob type.")
    public boolean allowChange = true;
    @Comment("Whether to allow changing the mob type ONLY if the spawner is empty.")
    public boolean onlyIfEmpty = true;
    @Comment("Whether to allow changing the mob type ONLY if the `can_place_on` component of the spawn egg is set.")
    public boolean onlyWithCanPlaceOn = true;
}
