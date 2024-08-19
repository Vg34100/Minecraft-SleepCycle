package net.vg.sleepcycle.mixin;

import net.minecraft.client.Camera;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.vg.sleepcycle.config.ModConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow @Deprecated protected abstract void setRotation(float f, float g);

    @Shadow protected abstract void move(float f, float g, float h);

    @Shadow protected abstract float getMaxZoom(float g);

    @Inject(method = "setup", at = @At("TAIL"))
    public void setup(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f, CallbackInfo ci) {
        if(ModConfigs.CHANGE_CAMERA_POS) {
            if(entity instanceof LivingEntity && ((LivingEntity)entity).isSleeping()) {
                Direction direction = ((LivingEntity)entity).getBedOrientation();
                setRotation(direction != null ? direction.toYRot() - 180.0F : 0.0F, 45.0F);
                // getMaxZoom required to prevent clipping
                move(-getMaxZoom(4.0f), 0.3f, 0.0f); // Adjust Y value to move the camera up
            }
        }
    }
}
