package net.vg.sleepcycle.mixin;

import net.minecraft.server.world.SleepManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SleepManager.class)
public class SleepManagerMixin {
    // Removes the skipping night functionality
    @Inject(method = "canSkipNight", at = @At(value = "HEAD"), cancellable = true)
    public void canSkipNightInject(int percentage, CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(false);
    }
}
