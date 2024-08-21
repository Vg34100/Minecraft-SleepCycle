package net.vg.sleepcycle.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.CriterionValidator;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.vg.sleepcycle.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SleepCriterion extends SimpleCriterionTrigger<SleepCriterion.Conditions> {
    public static final ResourceLocation ID = ResourceLocation.parse("tutorialmod.sleep");
    public SleepCriterion() {
    }

    @Override
    public @NotNull Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, instance -> true);
    }

//    public static Criterion<Conditions> create(Conditions conditions) {
//        return ModCriteria.SLEEP.create(new SleepCriterion.Conditions());
//    }

    public static class Conditions implements SimpleCriterionTrigger.SimpleInstance, CriterionTriggerInstance {

        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.unit(() -> Conditions.INSTANCE).fieldOf("instance").forGetter(c -> Conditions.INSTANCE)
        ).apply(instance, Conditions::new));
        public static final Conditions INSTANCE = new Conditions();
        public Conditions() {
        }

        public Conditions(Conditions conditions) {

        }

        @Override
        public void validate(CriterionValidator criterionValidator) {
        }

        @Override
        public @NotNull Optional<ContextAwarePredicate> player() {
            return Optional.empty();
        }
    }
}