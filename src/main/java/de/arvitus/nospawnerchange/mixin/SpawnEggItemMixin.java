package de.arvitus.nospawnerchange.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.arvitus.nospawnerchange.config.SpawnerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static de.arvitus.nospawnerchange.NoSpawnerChange.CONFIG;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin {
    @Inject(
        method = "useOn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Spawner;setEntityId(Lnet/minecraft/world/entity/EntityType;" +
                     "Lnet/minecraft/util/RandomSource;)V"
        ),
        cancellable = true
    )
    private void disableInSurvival(
        UseOnContext context,
        CallbackInfoReturnable<InteractionResult> cir,
        @Local Level level,
        @Local ItemStack itemStack,
        @Local BlockPos pos,
        @Local Spawner spawnerHolder
    ) {
        Player player = context.getPlayer();
        if (player == null || player.isCreative()) return;

        SpawnerConfig spawnerConfig = null;
        Boolean isEmpty = null;

        boolean canPlace = itemStack.canPlaceOnBlockInAdventureMode(new BlockInWorld(level, pos, false));
        if (spawnerHolder instanceof SpawnerBlockEntity mobSpawner) {
            spawnerConfig = CONFIG.monsterSpawner;
            isEmpty = mobSpawner.getSpawner().getOrCreateDisplayEntity(level, pos) == null;
        } else if (spawnerHolder instanceof TrialSpawnerBlockEntity trialSpawner) {
            spawnerConfig = CONFIG.trialSpawner;
            isEmpty = !trialSpawner
                .getTrialSpawner()
                .getStateData()
                .hasMobToSpawn(trialSpawner.getTrialSpawner(), RandomSource.create());
        }

        if (
            (isEmpty != null && spawnerConfig != null) &&
            spawnerConfig.allowChange &&
            (!spawnerConfig.onlyWithCanPlaceOn || canPlace) &&
            (!spawnerConfig.onlyIfEmpty || isEmpty)
        ) {
            return;
        }

        cir.setReturnValue(InteractionResult.FAIL);
    }
}
