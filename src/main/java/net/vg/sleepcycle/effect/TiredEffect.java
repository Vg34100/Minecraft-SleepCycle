package net.vg.sleepcycle.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.vg.sleepcycle.SleepCycle;

import java.util.UUID;

public class TiredEffect extends StatusEffect {

    protected TiredEffect(StatusEffectCategory statusEffectCategory, int i) {
        super(statusEffectCategory, i);

        // Use Identifiers instead of Strings
        Identifier movementSpeedModifierId = Identifier.of(SleepCycle.MOD_ID, "tired_movement_speed");
        Identifier attackSpeedModifierId = Identifier.of(SleepCycle.MOD_ID, "tired_attack_speed");

        // Register attribute modifiers with Identifiers
        this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, movementSpeedModifierId, -0.30, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, attackSpeedModifierId, -0.30, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
