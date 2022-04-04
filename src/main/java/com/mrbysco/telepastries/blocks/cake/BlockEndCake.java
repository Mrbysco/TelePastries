package com.mrbysco.telepastries.blocks.cake;

import com.mrbysco.telepastries.config.TeleConfig;
import com.mrbysco.telepastries.util.CakeTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.util.FakePlayer;

import java.util.List;

public class BlockEndCake extends BlockCakeBase {
	public BlockEndCake(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void teleportToDimension(LevelAccessor worldIn, BlockPos pos, Player player) {
		if (player != null && !(player instanceof FakePlayer) && player.isAlive() && !worldIn.isClientSide()) {
			Level world = ((ServerLevelAccessor) worldIn).getLevel();
			if (!world.isClientSide && !player.isPassenger() && !player.isVehicle() && player.canChangeDimensions()) {
				ServerPlayer playerMP = (ServerPlayer) player;
				MinecraftServer server = player.getServer();
				ServerLevel destinationWorld = server != null ? server.getLevel(getCakeWorld()) : null;
				if (destinationWorld == null)
					return;

				CakeTeleporter teleporter = new CakeTeleporter(destinationWorld);
				CakeTeleporter.addDimensionPosition(playerMP, playerMP.getLevel().dimension(), playerMP.blockPosition().offset(0, 1, 0));
				playerMP.changeDimension(destinationWorld, teleporter);
			}
		}
	}

	@Override
	public boolean isRefillItem(ItemStack stack) {
		List<? extends String> items = TeleConfig.COMMON.endCakeRefillItems.get();
		if (items == null || items.isEmpty()) return false;
		ResourceLocation registryLocation = stack.getItem().getRegistryName();
		return registryLocation != null && items.contains(registryLocation.toString());
	}

	@Override
	public ResourceKey<Level> getCakeWorld() {
		return Level.END;
	}

	@Override
	public boolean consumeCake() {
		return TeleConfig.COMMON.consumeEndCake.get();
	}
}
