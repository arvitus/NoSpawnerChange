<div align="center">

# No Spawner Change

[![Static Badge](https://img.shields.io/badge/modrinth-00AF5C?logo=modrinth&logoColor=white)](https://modrinth.com/mod/nospawnerchange)
[![Static Badge](https://img.shields.io/badge/GitHub-010409?style=flat&logo=github)](https://github.com/arvitus/NoSpawnerChange)
[![Static Badge](https://img.shields.io/badge/Discord-5662f6?style=flat&logo=discord&logoColor=white)](https://discord.gg/xzdbetZVrn)

A Minecraft mod that prevents changing the spawner type in survival mode.

</div>

## What does it do?

With this mod installed, by default, changing a spawner in survival mode will behave exactly like it usually does in
adventure mode.
So you won't be able to change the spawner type, unless the `can_place_on` component of the spawn egg allows it.

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

You can reload the config by using `/reload`.