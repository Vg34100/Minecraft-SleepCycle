package net.vg.sleepcycle.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.vg.sleepcycle.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    private void modifyBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player.hasStatusEffect(ModEffects.TIRED)) {
            float originalSpeed = cir.getReturnValue();
            float modifiedSpeed = originalSpeed * 0.3F; // Apply the Tired effect modifier
            cir.setReturnValue(modifiedSpeed);
        }
    }
}
