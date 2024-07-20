package net.vg.sleepcycle.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.*;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.entity.LootContextPredicateValidator;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class SleepCriterion extends AbstractCriterion<SleepCriterion.Conditions> {
    public static final Identifier ID = Identifier.of("tutorialmod", "sleep");

    public SleepCriterion() {
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> true);
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return SleepCriterion.Conditions.CODEC;
    }

    public static AdvancementCriterion<Conditions> create() {
        return ModCriteria.SLEEP.create(new SleepCriterion.Conditions());
    }

    public static class Conditions implements CriterionConditions, AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                // Add fields here if needed in the future
                Codec.unit(() -> Conditions.INSTANCE).fieldOf("instance").forGetter(c -> Conditions.INSTANCE)
        ).apply(instance, Conditions::new));
        public static final Conditions INSTANCE = new Conditions();
        public Conditions() {
        }

        public Conditions(Conditions conditions) {

        }

        @Override
        public void validate(LootContextPredicateValidator validator) {

        }

        @Override
        public Optional<LootContextPredicate> player() {
            return Optional.empty();
        }
    }
}
