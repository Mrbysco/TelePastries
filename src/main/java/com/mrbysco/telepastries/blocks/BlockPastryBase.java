package com.mrbysco.telepastries.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class BlockPastryBase extends Block {

    public BlockPastryBase(AbstractBlock.Properties builder) {
        super(builder);
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.below()).getMaterial().isSolid();
    }
}
