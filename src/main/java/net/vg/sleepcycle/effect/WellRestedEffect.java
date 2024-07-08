package net.vg.sleepcycle.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.vg.sleepcycle.SleepCycle;

public class WellRestedEffect extends StatusEffect {
    protected WellRestedEffect(StatusEffectCategory statusEffectCategory, int i) {
        super(statusEffectCategory, i);

        // Use Identifiers instead of Strings
        Identifier absorptionModifierId = Identifier.of(SleepCycle.MOD_ID, "well_rested_absorption");

        // Register attribute modifiers with Identifiers
        this.addAttributeModifier(EntityAttributes.GENERIC_MAX_ABSORPTION, absorptionModifierId, 4.0, EntityAttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 50 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getHealth() < entity.getMaxHealth()) {
            entity.heal(1.0F); // Regenerate 1 health point
        }
        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        entity.setAbsorptionAmount(Math.max(entity.getAbsorptionAmount(), (float)(4 * (1 + amplifier))));
    }
//    @Override
//    public boolean canApplyUpdateEffect(int duration, int amplifier) {
//        return true;
//        int i = 50 >> amplifier;
//        if (i > 0) {
//            return duration % i == 0;
//        } else {
//            return true;
//        }
//    }
//
//    @Override
//    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
//        if (entity.getHealth() < entity.getMaxHealth()) {
//            entity.heal(1.0F);
//        }
//
//        return entity.getAbsorptionAmount() > 0.0F || entity.getWorld().isClient;
//    }
//
//    @Override
//    public void onApplied(LivingEntity entity, int amplifier) {
//        entity.setAbsorptionAmount(entity.getAbsorptionAmount() + (float)(4 * (amplifier + 1)));
//        super.onApplied(entity, amplifier);
//    }

//    @Override
//    public void onRemoved(AttributeContainer attributes) {
//        entity.setAbsorptionAmount(entity.getAbsorptionAmount() - (float)(4 * (amplifier + 1)));
//        super.onRemoved(attributes);
//    }
}
