package com.mrbysco.telepastries.blocks.cake;

import com.mrbysco.telepastries.config.TeleConfig;
import com.mrbysco.telepastries.util.CakeTeleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockEndCake extends BlockCakeBase {
	public BlockEndCake(String registry) {
		super(registry);
	}

	@Override
	public void teleportToDimension(World world, BlockPos pos, EntityPlayer player) {
		if (!player.isDead) {
			if (!world.isRemote && !player.isRiding() && !player.isBeingRidden() && player.isNonBoss()) {
				EntityPlayerMP playerMP = (EntityPlayerMP) player;
				WorldServer worldServer = playerMP.getServer().getWorld(getCakeDimension());
				CakeTeleporter teleporter = new CakeTeleporter(worldServer, worldServer.getSpawnCoordinate());
				teleporter.addDimensionPosition(playerMP, playerMP.dimension, playerMP.getPosition().add(0, 1, 0));
				teleporter.teleportToDimension(playerMP, getCakeDimension(), worldServer.getSpawnCoordinate());
			}
		}
	}

	@Override
	public Item getRefillItem() {
		return Item.REGISTRY.getObject(new ResourceLocation(TeleConfig.pastries.end.endCakeRefillItem));
	}

	@Override
	public int getCakeDimension() {
		return 1;
	}

	@Override
	public boolean consumeCake() {
		return TeleConfig.pastries.end.consumeEndCake;
	}
}
