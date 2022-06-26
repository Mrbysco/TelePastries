package com.mrbysco.telepastries.item;

import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class CakeBlockItem extends BlockItem {
	public CakeBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	protected boolean canPlace(BlockPlaceContext context, BlockState state) {
		Player player = context.getPlayer();
		CollisionContext collisionContext = player == null ? CollisionContext.empty() : CollisionContext.of(player);
		boolean flag = (!this.mustSurvive() || state.canSurvive(context.getLevel(), context.getClickedPos())) && context.getLevel().isUnobstructed(state, context.getClickedPos(), collisionContext);
		if (!flag) {
			BlockCakeBase cakeBlock = (BlockCakeBase) getBlock();
			ResourceLocation cakeLocation = cakeBlock.getCakeWorld().location();
			ResourceLocation currentLocation = player.level.dimension().location();
			if (cakeLocation.equals(currentLocation)) {
				player.displayClientMessage(Component.translatable("telepastries.same_dimension"), true);
			} else {
				player.displayClientMessage(Component.translatable("telepastries.teleport_restricted"), true);
			}
		}
		return flag;
	}
}
