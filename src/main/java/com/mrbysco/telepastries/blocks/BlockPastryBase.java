package com.mrbysco.telepastries.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockPastryBase extends Block {

	public BlockPastryBase(BlockBehaviour.Properties builder) {
		super(builder);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.below()).isSolid();
	}
}
