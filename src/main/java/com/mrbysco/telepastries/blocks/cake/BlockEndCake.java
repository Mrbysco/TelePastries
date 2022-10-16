package com.mrbysco.telepastries.blocks.cake;

import com.mrbysco.telepastries.config.TeleConfig;
import com.mrbysco.telepastries.util.CakeTeleporter;
import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;

import java.util.List;

public class BlockEndCake extends BlockCakeBase {
	public BlockEndCake(AbstractBlock.Properties properties) {
		super(properties);
	}

	@Override
	public void teleportToDimension(IWorld worldIn, BlockPos pos, PlayerEntity player) {
		if (player != null && !(player instanceof FakePlayer) && player.isAlive() && !worldIn.isClientSide()) {
			World world = ((IServerWorld) worldIn).getLevel();
			if (!world.isClientSide && !player.isPassenger() && !player.isVehicle() && player.canChangeDimensions()) {
				ServerPlayerEntity playerMP = (ServerPlayerEntity) player;
				MinecraftServer server = player.getServer();
				ServerWorld destinationWorld = server != null ? server.getLevel(getCakeWorld()) : null;
				if (destinationWorld == null)
					return;

				CakeTeleporter teleporter = new CakeTeleporter(destinationWorld);
				teleporter.addDimensionPosition(playerMP, playerMP.getLevel().dimension(), playerMP.blockPosition().offset(0, 1, 0));
				playerMP.changeDimension(destinationWorld, teleporter);
			}
		}
	}

	@Override
	public boolean isRefillItem(ItemStack stack) {
		List<? extends String> items = TeleConfig.SERVER.endCakeRefillItems.get();
		if (items == null || items.isEmpty()) return false;
		ResourceLocation registryLocation = stack.getItem().getRegistryName();
		return registryLocation != null && items.contains(registryLocation.toString());
	}

	@Override
	public RegistryKey<World> getCakeWorld() {
		return RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("the_end"));
	}

	@Override
	public boolean consumeCake() {
		return TeleConfig.SERVER.consumeEndCake.get();
	}
}
