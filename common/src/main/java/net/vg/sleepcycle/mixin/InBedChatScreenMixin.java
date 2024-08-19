package net.vg.sleepcycle.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.vg.sleepcycle.config.ModConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InBedChatScreen.class)
public class InBedChatScreenMixin {

    @Shadow private Button leaveBedButton;

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo info) {
        // Adjust the position of the "Leave Bed" button
        this.leaveBedButton.setY(this.leaveBedButton.getY() - ModConfigs.SLEEP_BUTTON_HEIGHT); // Adjust the position
    }
}
