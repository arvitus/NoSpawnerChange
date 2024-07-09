package de.arvitus.nospawnerchange.mixin;

import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.item.SpawnEggItem;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin {
    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/SpawnEggItem;getEntityType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/EntityType;", shift = At.Shift.BEFORE, ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void disableInSurvival(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir, World world, ItemStack itemStack, BlockPos blockPos) {
        PlayerEntity player = context.getPlayer();
        if (player == null || player.isCreative() || itemStack.canPlaceOn(new CachedBlockPosition(world, blockPos, false))) {
            return;
        }

        cir.setReturnValue(ActionResult.FAIL);
    }
}
