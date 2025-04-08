package de.arvitus.nospawnerchange.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.arvitus.nospawnerchange.config.SpawnerConfig;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.Spawner;
import net.minecraft.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static de.arvitus.nospawnerchange.NoSpawnerChange.CONFIG;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin {
    @Inject(
        method = "useOnBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/SpawnEggItem;getEntityType" +
                     "(Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;Lnet/minecraft/item/ItemStack;)" +
                     "Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        cancellable = true
    )
    private void disableInSurvival(
        ItemUsageContext context,
        CallbackInfoReturnable<ActionResult> cir,
        @Local World world,
        @Local ItemStack itemStack,
        @Local BlockPos blockPos,
        @Local Spawner spawner
    ) {
        PlayerEntity player = context.getPlayer();
        if (player == null || player.isCreative()) return;

        SpawnerConfig spawnerConfig = null;
        Boolean isEmpty = null;

        boolean canPlace = itemStack.canPlaceOn(new CachedBlockPosition(world, blockPos, false));
        if (spawner instanceof MobSpawnerBlockEntity mobSpawner) {
            spawnerConfig = CONFIG.monsterSpawner;
            isEmpty = mobSpawner.getLogic().getRenderedEntity(world, blockPos) == null;
        } else if (spawner instanceof TrialSpawnerBlockEntity trialSpawner) {
            spawnerConfig = CONFIG.trialSpawner;
            isEmpty = !trialSpawner
                .getSpawner()
                .getData()
                .hasSpawnData(trialSpawner.getSpawner(), Random.create());
        }

        if (
            (isEmpty != null && spawnerConfig != null) &&
            spawnerConfig.allowChange &&
            (!spawnerConfig.onlyWithCanPlaceOn || canPlace) &&
            (!spawnerConfig.onlyIfEmpty || isEmpty)
        ) {
            return;
        }

        cir.setReturnValue(ActionResult.FAIL);
    }
}
