// TODO(Ravel): Failed to fully resolve file: class com.intellij.psi.impl.source.tree.java.PsiBinaryExpressionImpl
//  cannot be cast to class com.intellij.psi.PsiLiteralExpression (com.intellij.psi.impl.source.tree.java
//  .PsiBinaryExpressionImpl and com.intellij.psi.PsiLiteralExpression are in unnamed module of loader com.intellij
//  .ide.plugins.cl.PluginClassLoader @462dfbe7)
// TODO(Ravel): Failed to fully resolve file: class com.intellij.psi.impl.source.tree.java.PsiBinaryExpressionImpl
//  cannot be cast to class com.intellij.psi.PsiLiteralExpression (com.intellij.psi.impl.source.tree.java
//  .PsiBinaryExpressionImpl and com.intellij.psi.PsiLiteralExpression are in unnamed module of loader com.intellij
//  .ide.plugins.cl.PluginClassLoader @462dfbe7)
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
            target = "Lnet/minecraft/world/item/SpawnEggItem;getType(Lnet/minecraft/world/item/ItemStack;)" +
                     "Lnet/minecraft/world/entity/EntityType;"
        ),
        cancellable = true
    )
    private void disableInSurvival(
        UseOnContext context,
        CallbackInfoReturnable<InteractionResult> cir,
        @Local Level world,
        @Local ItemStack itemStack,
        @Local BlockPos blockPos,
        @Local Spawner spawner
    ) {
        Player player = context.getPlayer();
        if (player == null || player.isCreative()) return;

        SpawnerConfig spawnerConfig = null;
        Boolean isEmpty = null;

        boolean canPlace = itemStack.canPlaceOnBlockInAdventureMode(new BlockInWorld(world, blockPos, false));
        if (spawner instanceof SpawnerBlockEntity mobSpawner) {
            spawnerConfig = CONFIG.monsterSpawner;
            isEmpty = mobSpawner.getSpawner().getOrCreateDisplayEntity(world, blockPos) == null;
        } else if (spawner instanceof TrialSpawnerBlockEntity trialSpawner) {
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
