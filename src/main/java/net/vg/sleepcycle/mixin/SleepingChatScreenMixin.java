package net.vg.sleepcycle.mixin;

import net.minecraft.client.gui.screen.SleepingChatScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.vg.sleepcycle.config.ModConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SleepingChatScreen.class)
public class SleepingChatScreenMixin {
    @Shadow private ButtonWidget stopSleepingButton;
    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo info) {
        // Adjust the position of the "Leave Bed" button
        this.stopSleepingButton.setY(this.stopSleepingButton.getY() - ModConfigs.SLEEP_BUTTON_HEIGHT); // Adjust the position
    }
}
