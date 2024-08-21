package net.vg.sleepcycle.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.resources.ResourceLocation;
import net.vg.sleepcycle.Constants;

public class TiredEffect extends MobEffect {

    protected TiredEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);

        ResourceLocation movementSpeedModifierId = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "tired_movement_speed");
        ResourceLocation attackSpeedModifierId = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "tired_attack_speed");
        ResourceLocation blockBreakSpeedModifierId = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "tired_block_break_speed");


        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, movementSpeedModifierId, -0.30, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, attackSpeedModifierId, -0.30, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, blockBreakSpeedModifierId, -0.30, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}