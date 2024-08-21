package net.vg.sleepcycle.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.vg.sleepcycle.Constants;
import net.vg.sleepcycle.block.SleepingBagBlock;

public class ModItems {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Constants.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Block> SLEEPING_BAG = BLOCKS.register("sleeping_bag",
            () -> new SleepingBagBlock(DyeColor.BLACK, BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .noOcclusion()
                    .dynamicShape()));  // Add dynamicShape() here

    public static final RegistrySupplier<Item> SLEEPING_BAG_ITEM = ITEMS.register("sleeping_bag",
            () -> new SleepingBagItem(SLEEPING_BAG.get(), new Item.Properties().arch$tab(CreativeModeTabs.FUNCTIONAL_BLOCKS).stacksTo(1)));

    public static void register() {
        BLOCKS.register();
        ITEMS.register();
    }
}
