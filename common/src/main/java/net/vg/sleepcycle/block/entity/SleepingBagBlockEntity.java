package net.vg.sleepcycle.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vg.sleepcycle.block.SleepingBagBlock;

public class SleepingBagBlockEntity extends BlockEntity {

    public SleepingBagBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SLEEPING_BAG.get(), pos, state);
    }
}