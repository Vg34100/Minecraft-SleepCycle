package net.vg.sleepcycle.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.vg.sleepcycle.config.ModConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow protected abstract void setRotation(float yaw, float pitch);

    @Shadow protected abstract void moveBy(float f, float g, float h);

    @Shadow protected abstract float clipToSpace(float f);

    @Inject(method = "update", at = @At("TAIL"))
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if(ModConfigs.CHANGE_CAMERA_POS) {
            if(focusedEntity instanceof LivingEntity && ((LivingEntity)focusedEntity).isSleeping()) {
                Direction direction = ((LivingEntity)focusedEntity).getSleepingDirection();
                setRotation(direction != null ? direction.asRotation() - 180.0F : 0.0F, 45.0F);
                // ClipToSpace required to prevent clipping
                moveBy(-clipToSpace(4.0f), 0.3f, 0.0f); // Adjust Y value to move the camera up
            }
        }
    }
}
