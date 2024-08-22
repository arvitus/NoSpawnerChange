package de.arvitus.nospawnerchange.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class SpawnerConfig {
    @Comment("Whether to allow changing mob type.")
    public boolean change = true;
    @Comment(
        "Whether to allow setting the mob type, if the spawner is empty.\n" +
        "This overrides `change`."
    )
    public boolean changeEmpty = true;
    @Comment(
        "Whether to allow changing the mob type ONLY if the `can_place_on` component of the spawn egg is set.\n" +
        "This will do nothing if `change` and `change-empty` are false."
    )
    public boolean onlyWithCanPlaceOn = true;

    @Override
    public String toString() {
        return "SpawnerConfig{" +
               "change=" + change +
               ", changeEmpty=" + changeEmpty +
               ", canPlaceOn=" + onlyWithCanPlaceOn +
               '}';
    }
}
