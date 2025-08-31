package com.mrbysco.telepastries.blocks.cake;

import com.mrbysco.telepastries.config.TeleConfig;
import com.mrbysco.telepastries.util.CakeTeleportHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.portal.TeleportTransition;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.List;

public class BlockEndCake extends BlockCakeBase {
	public BlockEndCake(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void teleportToDimension(LevelAccessor levelAccessor, BlockPos pos, Player player) {
		if (player != null && !(player instanceof FakePlayer) && player.isAlive() && !levelAccessor.isClientSide()) {
			if (levelAccessor instanceof ServerLevel serverLevel && !player.isPassenger() && !player.isVehicle() &&
					player.canTeleport(player.level(), serverLevel)) {
				ServerPlayer serverPlayer = (ServerPlayer) player;
				MinecraftServer server = player.getServer();
				ServerLevel destinationWorld = server != null ? server.getLevel(getCakeWorld()) : null;
				if (destinationWorld == null)
					return;

				CakeTeleportHelper.addDimensionPosition(serverPlayer, serverPlayer.level().dimension(), serverPlayer.blockPosition());
				TeleportTransition transition = CakeTeleportHelper.getCakeTeleportData(destinationWorld, serverPlayer);
				if (transition == null) return;
				serverPlayer.teleport(transition);
			}
		}
	}

	@Override
	public boolean isRefillItem(ItemStack stack) {
		List<? extends String> items = TeleConfig.COMMON.endCakeRefillItems.get();
		if (items.isEmpty()) return false;
		ResourceLocation registryLocation = BuiltInRegistries.ITEM.getKey(stack.getItem());
		return registryLocation != null && items.contains(registryLocation.toString());
	}

	@Override
	public ResourceKey<Level> getCakeWorld() {
		return ResourceKey.create(Registries.DIMENSION, ResourceLocation.withDefaultNamespace("the_end"));
	}

	@Override
	public boolean consumeCake() {
		return TeleConfig.COMMON.consumeEndCake.get();
	}
}
