package com.mrbysco.telepastries.blocks.cake;

import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.List;

public class BlockNetherCake extends BlockCakeBase {
    public BlockNetherCake(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public void teleportToDimension(IWorld world, BlockPos pos, PlayerEntity player) {
        super.teleportToDimension(world, pos, player);
    }

    @Override
    public boolean isRefillItem(ItemStack stack) {
        List<? extends String> items = TeleConfig.SERVER.netherCakeRefillItems.get();
        if (items == null || items.isEmpty()) return false;
        ResourceLocation registryLocation = stack.getItem().getRegistryName();
        return registryLocation != null && items.contains(registryLocation.toString());
    }

    @Override
    public RegistryKey<World> getCakeWorld() {
        return World.NETHER;
    }

    @Override
    public boolean consumeCake() {
        return TeleConfig.SERVER.consumeNetherCake.get();
    }
}
