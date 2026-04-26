package com.mrbysco.telepastries.item;

import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class CakeBlockItem extends BlockItem {
	public CakeBlockItem(Block block, Properties properties) {
		super(block, properties.useBlockDescriptionPrefix());
	}

	@Override
	protected boolean canPlace(BlockPlaceContext context, BlockState state) {
		Player player = context.getPlayer();
		CollisionContext collisionContext = player == null ? CollisionContext.empty() : CollisionContext.of(player);
		boolean flag = (!this.mustSurvive() || state.canSurvive(context.getLevel(), context.getClickedPos())) && context.getLevel().isUnobstructed(state, context.getClickedPos(), collisionContext);
		if (!flag) {
			BlockCakeBase cakeBlock = (BlockCakeBase) getBlock();
			Identifier cakeLocation = cakeBlock.getCakeWorld().identifier();
			Identifier currentLocation = player.level().dimension().identifier();
			if (cakeLocation.equals(currentLocation)) {
				player.sendOverlayMessage(Component.translatable("telepastries.same_dimension"));
			} else {
				player.sendOverlayMessage(Component.translatable("telepastries.teleport_restricted"));
			}
		}
		return flag;
	}
}
