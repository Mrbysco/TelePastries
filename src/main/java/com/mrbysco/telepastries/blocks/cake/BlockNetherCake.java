package com.mrbysco.telepastries.blocks.cake;

import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.List;

public class BlockNetherCake extends BlockCakeBase {
	public BlockNetherCake(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void teleportToDimension(LevelAccessor world, BlockPos pos, Player player) {
		super.teleportToDimension(world, pos, player);
	}

	@Override
	public boolean isRefillItem(ItemStack stack) {
		List<? extends String> items = TeleConfig.COMMON.netherCakeRefillItems.get();
		if (items == null || items.isEmpty()) return false;
		ResourceLocation registryLocation = BuiltInRegistries.ITEM.getKey(stack.getItem());
		return registryLocation != null && items.contains(registryLocation.toString());
	}

	@Override
	public ResourceKey<Level> getCakeWorld() {
		return ResourceKey.create(Registries.DIMENSION, new ResourceLocation("the_nether"));
	}

	@Override
	public boolean consumeCake() {
		return TeleConfig.COMMON.consumeNetherCake.get();
	}
}
