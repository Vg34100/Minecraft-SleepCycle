package net.vg.sleepcycle.mixin;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vg.sleepcycle.util.TimeProgressionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public abstract class BedBlockMixin {
    @Inject(method = "onUse", at = @At("RETURN"))
    private void onUseReturn(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        // Check if the original method returned SUCCESS
        if (cir.getReturnValue() == ActionResult.SUCCESS && !world.isClient) {
            // Ensure player is sleeping and add custom logic
            if (player instanceof ServerPlayerEntity) {

                // Register the world for time progression
                TimeProgressionHandler.addWorld((ServerWorld) world);
            }
        }
    }
}
