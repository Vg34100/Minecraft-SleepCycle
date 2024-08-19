package net.vg.sleepcycle.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class BedBlockMixin {
//    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
//    private void onUse(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
//        if (level.isDay()) {
//            // Skip the day check in vanilla code
//            player.startSleepInBed(blockPos).ifLeft((bedSleepingProblem) -> {
//                if (bedSleepingProblem.getMessage() != null) {
//                    player.displayClientMessage(bedSleepingProblem.getMessage(), true);
//                }
//            });
//            cir.setReturnValue(InteractionResult.SUCCESS);
//        }
//    }
}
