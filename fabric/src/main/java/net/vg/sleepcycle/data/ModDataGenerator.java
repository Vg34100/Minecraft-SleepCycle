package net.vg.sleepcycle.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.vg.sleepcycle.Constants;
import net.vg.sleepcycle.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModModelGenerator::new);
        pack.addProvider(ModRecipeProvider::new);
    }

    private static class ModModelGenerator extends FabricModelProvider {

        public ModModelGenerator(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
//            blockStateModelGenerator.createBedItem(ModItems.SLEEPING_BAG.get(), Blocks.WHITE_WOOL);
        }

        @Override
        public void generateItemModels(ItemModelGenerators itemModelGenerator) {
            itemModelGenerator.generateFlatItem(ModItems.SLEEPING_BAG_ITEM.get(), ModelTemplates.FLAT_ITEM);

        }
    }

    public static class ModRecipeProvider extends FabricRecipeProvider {

        public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        public void buildRecipes(RecipeOutput exporter) {
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.SLEEPING_BAG_ITEM.get(), 1)
                    .pattern("iii")
                    .define('i', ItemTags.WOOL)
                    .unlockedBy(RecipeProvider.getHasName(Items.WHITE_WOOL), RecipeProvider.has(Items.WHITE_WOOL))
                    .save(exporter, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, RecipeProvider.getSimpleRecipeName(ModItems.SLEEPING_BAG_ITEM.get())));
        }
    }
}