package net.vg.sleepcycle.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.*;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.EntityEffectPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.entity.LootContextPredicateValidator;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

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
//    @Override
//    public Codec<Conditions> getConditionsCodec() {
//        return null;
//    }
//
//    @Override
//    public static AdvancementCriterion<Conditions> create(Conditions conditions) {
//        return super.create(conditions);
//    }
//


//
//    @Override
//    public Identifier getId() {
//        return ID;
//    }
//
//    @Override
//    protected Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer deserializer) {
//        return new Conditions(playerPredicate);
//    }
//


//    @Override
//    public static AdvancementCriterion<Conditions> create(Conditions conditions) {
//        return super.create(conditions);
//    }

    //    public static class Conditions implements CriterionConditions, AbstractCriterion.Conditions {
//        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create((instance) -> {
//            return instance.group(
//                    EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
//                    EntityEffectPredicate.CODEC.optionalFieldOf("effects").forGetter(Conditions::effects),
//                    EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("source").forGetter(Conditions::source)
//            ).apply(instance, Conditions::new);
//        });
//
//        private final Optional<LootContextPredicate> player;
//        private final Optional<EntityEffectPredicate> effects;
//        private final Optional<LootContextPredicate> source;
//
//        public Conditions(Optional<LootContextPredicate> player, Optional<EntityEffectPredicate> effects, Optional<LootContextPredicate> source) {
//            this.player = player;
//            this.effects = effects;
//            this.source = source;
//        }
//
//        public static Conditions create() {
//            return new Conditions(Optional.empty(), Optional.empty(), Optional.empty());
//        }
//
////        @Override
////        public Identifier getId() {
////            return ID;
////        }
//
//        public boolean matches(ServerPlayerEntity player, @Nullable LootContext context) {
//            if (this.effects.isPresent() && !this.effects.get().test(player)) {
//                return false;
//            } else {
//                return !this.source.isPresent() || (context != null && this.source.get().test(context));
//            }
//        }
//
//        public Optional<LootContextPredicate> player() {
//            return this.player;
//        }
//
//        public Optional<EntityEffectPredicate> effects() {
//            return this.effects;
//        }
//
//        public Optional<LootContextPredicate> source() {
//            return this.source;
//        }
//
//        @Override
//        public void validate(LootContextPredicateValidator validator) {
//
//        }
//    }
}
