package net.vg.sleepcycle.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.EffectsChangedCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityEffectPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.vg.sleepcycle.advancement.SleepCriterion;
import net.vg.sleepcycle.effect.ModEffects;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {

    TagKey<Item> bedsTag = ItemTags.BEDS;



    public ModAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
        Advancement root = Advancement.Builder.create().display(
                        Items.BLACK_BED,
                        Text.translatable("advancement.sleepcycle.root.title"),
                        Text.translatable("advancements.sleepcycle.root.description"),
                        Identifier.of("textures/gui/advancements/backgrounds/stone.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("has_bed", InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().tag(bedsTag).build()))
                .build(consumer, "sleepcycle:root").value();

        AdvancementEntry rootEntry = new AdvancementEntry(Identifier.of("sleepcycle:root"), root);

        Advancement wellRested = Advancement.Builder.create()
                .parent(rootEntry)
                .display(
                        Items.POTION, // Replace with an actual item from your mod that represents the well-rested effect
                        Text.translatable("advancements.sleepcycle.well_rested.title"),
                        Text.translatable("advancements.sleepcycle.well_rested.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("well_rested", EffectsChangedCriterion.Conditions.create(EntityEffectPredicate.Builder.create().addEffect(ModEffects.WELL_RESTED)))
                .build(consumer, "sleepcycle:well_rested").value();

        Advancement tired = Advancement.Builder.create()
                .parent(rootEntry)
                .display(
                        Items.POTION, // Replace with an actual item from your mod that represents the tired effect
                        Text.translatable("advancements.sleepcycle.tired.title"),
                        Text.translatable("advancements.sleepcycle.tired.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("tired", EffectsChangedCriterion.Conditions.create(EntityEffectPredicate.Builder.create().addEffect(ModEffects.TIRED)))
                .build(consumer, "sleepcycle:tired").value();

        Advancement sleep5Min = Advancement.Builder.create()
                .parent(rootEntry)
                .display(
                        Items.CLOCK,
                        Text.translatable("advancements.sleepcycle.sleep_5_min.title"),
                        Text.translatable("advancements.sleepcycle.sleep_5_min.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("sleep_5_min", SleepCriterion.create())
                .build(consumer, "sleepcycle:sleep_5_min").value();
    }
}
