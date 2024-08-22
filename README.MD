# No Spawner Change

*A Minecraft mod that disables the ability to change the spawner type in survival mode.*

## What it does

With this mod installed, by default, changing a spawner in survival mode will behave exactly like it does in adventure mode.
So you won't be able to change the spawner type, unless the `can_place_on` component of the Spawn Egg allows it.

You can further customize this behavior using the config.

## Config (default)
```hocon
# Configuration for normal monster spawners
monster-spawner {
  # Whether to allow changing mob type.
  change = true
  # Whether to allow setting the mob type, if the spawner is empty.
  # This overrides `change`.
  change-empty = true
  # Whether to allow changing the mob type ONLY if the `can_place_on` component of the spawn egg is set.
  # This will do nothing if `change` and `change-empty` are false.
  only-with-can-place-on = true
}
# Configuration for trial spawners
trial-spawner {
  # Whether to allow changing mob type.
  change = true
  # Whether to allow setting the mob type, if the spawner is empty.
  # This overrides `change`.
  change-empty = true
  # Whether to allow changing the mob type ONLY if the `can_place_on` component of the spawn egg is set.
  # This will do nothing if `change` and `change-empty` are false.
  only-with-can-place-on = true
}
```
