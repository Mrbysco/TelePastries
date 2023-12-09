package com.mrbysco.telepastries.blocks.cake.compat;

import com.mrbysco.telepastries.blocks.cake.BlockCakeBase;
import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.fml.ModList;

import java.util.List;

public class BlockLostCityCake extends BlockCakeBase {
	public BlockLostCityCake(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (ModList.get().isLoaded("lostcities")) {
			return super.use(state, level, pos, player, handIn, hit);
		} else {
			if (player.getUsedItemHand() == handIn && !level.isClientSide) {
				player.sendSystemMessage(Component.translatable("telepastries.pastry.support.disabled", "lostcities").withStyle(ChatFormatting.RED));
			}
			return InteractionResult.SUCCESS;
		}
	}

	@Override
	public boolean isRefillItem(ItemStack stack) {
		List<? extends String> items = TeleConfig.COMMON.lostCitiesCakeRefillItem.get();
		if (items == null || items.isEmpty()) return false;
		ResourceLocation registryLocation = BuiltInRegistries.ITEM.getKey(stack.getItem());
		return registryLocation != null && items.contains(registryLocation.toString());
	}

	@Override
	public ResourceKey<Level> getCakeWorld() {
		return ResourceKey.create(Registries.DIMENSION, new ResourceLocation("lostcities", "lostcity"));
	}

	@Override
	public boolean consumeCake() {
		return TeleConfig.COMMON.consumeLostCitiesCake.get();
	}
}
