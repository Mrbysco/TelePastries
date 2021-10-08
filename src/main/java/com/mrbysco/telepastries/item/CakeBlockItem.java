package com.mrbysco.telepastries.item;

import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.text.TranslationTextComponent;

public class CakeBlockItem extends BlockItem {
	public CakeBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	protected boolean canPlace(BlockItemUseContext context, BlockState state) {
		PlayerEntity player = context.getPlayer();
		ISelectionContext iselectioncontext = player == null ? ISelectionContext.empty() : ISelectionContext.of(player);
		boolean flag = (!this.mustSurvive() || state.canSurvive(context.getLevel(), context.getClickedPos())) && context.getLevel().isUnobstructed(state, context.getClickedPos(), iselectioncontext);
		if(!flag) {
			BlockCakeBase cakeBlock = (BlockCakeBase)getBlock();
			ResourceLocation cakeLocation = cakeBlock.getCakeWorld().location();
			ResourceLocation currentLocation = player.level.dimension().location();
			if(cakeLocation.equals(currentLocation)) {
				player.displayClientMessage(new TranslationTextComponent("telepastries.same_dimension"), true);
			} else {
				player.displayClientMessage(new TranslationTextComponent("telepastries.teleport_restricted"), true);
			}
		}
		return flag;
	}
}
